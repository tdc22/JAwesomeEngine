package narrowphase;

import math.VecMath;
import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;

public class SupportRaycast2 implements RaycastNarrowphase<Vector2f> {
	private final Vector2f v1 = new Vector2f();
	private Vector2f v2 = new Vector2f();

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		v1.set(-ray.getDirection().y, ray.getDirection().x);
		float dot = dotRay(ray.getPosition(), Sa.getSupportCenter(), v1);
		if (dot > 0) {
			v2 = Sa.supportPoint(v1);
			dot = dotRay(ray.getPosition(), v2, v1);
			return dot <= 0;
		} else if (dot < 0) {
			v1.negate();
			v2 = Sa.supportPoint(v1);
			dot = dotRay(ray.getPosition(), v2, v1);
			return dot <= 0;
		}

		return true;
	}

	private float dotRay(Vector2f vecA, Vector2f vecB, Vector2f vecCheck) {
		return vecCheck.x * (vecA.x - vecB.x) + vecCheck.y * (vecA.y - vecB.y);
	}

	@Override
	public float computeCollisionOnRay(SupportMap<Vector2f> Sa,
			Ray<Vector2f> ray) {
		// TODO
		return 0;
	}
	
	private final static float EPSILON = 0.01f;
	private final static float MAX_ITERATIONS = 20;

	@Override
	public Vector2f computeCollision(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// Take line from before, make triangle, refine iteratively, k?
		Vector2f dir1 = VecMath.negate(ray.getDirection());
		Vector2f bound1 = Sa.supportPoint(dir1);
		float dot1 = dotRay(ray.getPosition(), bound1, v1);
		if(Math.abs(dot1) < EPSILON) return bound1;

		Vector2f dir2 = new Vector2f(v1);
		Vector2f bound2;
		
		if(dot1 < 0) {
			dir2.negate();
			bound2 = Sa.supportPoint(dir2);
		}
		else
		{
			bound2 = v2;
		}
		float dot2 = dotRay(ray.getPosition(), bound2, v1);
		if(Math.abs(dot2) < EPSILON) return bound2;
		
		System.out.println("Start");
		
		Vector2f negativeRayDirection = VecMath.negate(ray.getDirection());
		Vector2f dir3;
		for(int i = 0; i < MAX_ITERATIONS; i++) {
			dir3 = getMiddleVector(dir1, dir2, negativeRayDirection);
			Vector2f bound3 = Sa.supportPoint(dir3);
			float dot3 = dotRay(ray.getPosition(), bound3, v1);
			
			if(Math.abs(dot3) < EPSILON) {
				System.out.println(bound3 + "; success!");
				return bound3;
			}
			System.out.println(dir1 + "; " + dir2 + "; " + dir3 + "; " + dot3);
			if(dot3 > 0) {
				dir1 = dir3;
				bound1 = bound3;
				dot1 = dot3;
			}
			else {
				dir2 = dir3;
				bound2 = bound3;
				dot2 = dot3;
			}
		}
		
		System.out.println("fail");
		return (Math.abs(dot1) < Math.abs(dot2)) ? bound1 : bound2;
	}
	
	private Vector2f getMiddleVector(Vector2f a, Vector2f b, Vector2f dir) {
		Vector2f c = new Vector2f(a);
		c.translate(b);
		
		if(c.lengthSquared() > 0) {
			c.normalize();
			if(VecMath.dotproduct(c, dir) < 0) {
				c.negate();
			}
		}
		else
			c = dir;
		
		return c;
	}
}
