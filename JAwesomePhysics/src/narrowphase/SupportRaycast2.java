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

	@Override
	public Vector2f computeCollision(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// Take line from before, make triangle, refine iteratively, k?
		Vector2f bound1, bound2;
		bound1 = Sa.supportPointNegative(ray.getDirection());
		bound2 = v2;
		System.out.println(bound1 + "; " + bound2);
		System.out.println(dotRay(ray.getPosition(), bound1, v1));
		System.out.println(dotRay(ray.getPosition(), bound2, v1));
		return new Vector2f();
	}

}
