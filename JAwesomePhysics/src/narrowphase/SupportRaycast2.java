package narrowphase;

import manifold.RaycastHitResult;
import math.VecMath;
import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;

public class SupportRaycast2 implements RaycastNarrowphase<Vector2f> {
	private final Vector2f b = new Vector2f();
	private Vector2f v = new Vector2f();

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		b.set(-ray.getDirection().y, ray.getDirection().x);

		if (dotRay(ray.getPosition(), Sa.getSupportCenter(), b) < 0) {
			b.negate();
		}

		v = Sa.supportPoint(b);
		float diffX = ray.getPosition().x - v.x;
		float diffY = ray.getPosition().y - v.y;
		return (diffX * b.x + diffY * b.y <= 0 && diffX * ray.getDirection().x + diffY * ray.getDirection().y <= 0);
	}

	private float dotRay(Vector2f vecA, Vector2f vecB, Vector2f vecCheck) {
		return vecCheck.x * (vecA.x - vecB.x) + vecCheck.y * (vecA.y - vecB.y);
	}

	private float computeCollisionOnRay(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		return calculateScaleFactorForRay(ray, computeCollisionHit(Sa, ray));
	}

	private final static float EPSILON = 0.01f;
	private final static float MAX_ITERATIONS = 20;

	private Vector2f dir1 = new Vector2f();
	private Vector2f dir2 = new Vector2f();

	private final Vector2f normal = new Vector2f();

	public Vector2f computeCollisionHit(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		dir1.set(ray.getDirection());
		dir1.negate();
		Vector2f bound1 = Sa.supportPoint(dir1);
		float dot1 = dotRay(ray.getPosition(), bound1, b);
		if (Math.abs(dot1) < EPSILON) {
			normal.set(bound1);
			normal.normalize();
			return bound1;
		}

		dir2.set(b);
		Vector2f bound2;

		if (dot1 < 0) {
			dir2.negate();
			bound2 = bound1;
			bound1 = Sa.supportPoint(dir2);
			Vector2f tmp = dir1;
			dir1 = dir2;
			dir2 = tmp;
		} else {
			bound2 = v;
		}
		float dot2 = dotRay(ray.getPosition(), bound2, b);
		if (Math.abs(dot2) < EPSILON) {
			normal.set(bound2);
			normal.normalize();
			return bound2;
		}

		for (int i = 0; i < MAX_ITERATIONS; i++) {
			Vector2f dir3 = getMiddleVector(dir1, dir2, ray.getDirection());
			Vector2f bound3 = Sa.supportPoint(dir3);
			float dot3 = dotRay(ray.getPosition(), bound3, b);

			if (Math.abs(dot3) < EPSILON) {
				normal.set(bound3);
				normal.normalize();
				return bound3;
			}

			if (dot3 > 0) {
				float dx = bound1.x - bound2.x;
				float dy = bound1.y - bound2.y;
				float dL = (float) Math.sqrt(dx * dx + dy * dy);
				float dxn = -dy / dL;
				float dyn = dx / dL;
				if (Math.abs(dxn * (bound3.x - bound2.x) + dyn * (bound3.y - bound2.y)) < EPSILON) {
					normal.set(dxn, dyn);
					if (VecMath.dotproduct(ray.getDirection(), normal) > 0)
						normal.negate();
					return rayLineIntersection(ray, bound3, dx, dy);
				}
				dir1 = dir3;
				bound1 = bound3;
				dot1 = dot3;
			} else {
				float dx = bound1.x - bound2.x;
				float dy = bound1.y - bound2.y;
				float dL = (float) Math.sqrt(dx * dx + dy * dy);
				float dxn = -dy / dL;
				float dyn = dx / dL;
				if (Math.abs(dxn * (bound3.x - bound2.x) + dyn * (bound3.y - bound2.y)) < EPSILON) {
					normal.set(dxn, dyn);
					if (VecMath.dotproduct(ray.getDirection(), normal) > 0)
						normal.negate();
					return rayLineIntersection(ray, bound1, dx, dy);
				}
				dir2 = dir3;
				bound2 = bound3;
				dot2 = dot3;
			}
		}

		Vector2f result = (Math.abs(dot1) < Math.abs(dot2)) ? bound1 : bound2;
		normal.set(result);
		normal.normalize();
		return result;
	}

	private Vector2f getMiddleVector(Vector2f a, Vector2f b, Vector2f negdir) {
		Vector2f c = new Vector2f(a);
		c.translate(b);

		if (c.lengthSquared() > 0) {
			c.normalize();
			if (VecMath.dotproduct(c, negdir) >= 0) {
				c.negate();
			}
		} else {
			c.set(negdir);
			c.negate();
		}

		return c;
	}

	private Vector2f rayLineIntersection(Ray<Vector2f> r, Vector2f la, float ldx, float ldy) {
		float v1x = r.getPosition().x - la.x;
		float v1y = r.getPosition().y - la.y;
		float t2 = (v1x * b.x + v1y * b.y) / (ldx * b.x + ldy * b.y);
		return new Vector2f(la.x + ldx * t2, la.y + ldy * t2);
	}

	private float calculateScaleFactorForRay(Ray<Vector2f> r, Vector2f p) {
		float rPosPx = p.x - r.getPosition().x;
		float rPosPy = p.y - r.getPosition().y;
		return (float) (Math.sqrt(rPosPx * rPosPx + rPosPy * rPosPy) / r.getDirection().length());
	}

	@Override
	public RaycastHitResult<Vector2f> computeCollision(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		float d = computeCollisionOnRay(Sa, ray);
		return new RaycastHitResult<Vector2f>(d, ray.pointOnRay(d), new Vector2f(normal));
	}
}
