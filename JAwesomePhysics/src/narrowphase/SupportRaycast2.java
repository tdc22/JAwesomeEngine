package narrowphase;

import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;

public class SupportRaycast2 implements RaycastNarrowphase<Vector2f> {
	private final Vector2f temp = new Vector2f();

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		temp.set(-ray.getDirection().y, ray.getDirection().x);
		float dot = dotRay(ray.getPosition(), Sa.getSupportCenter(), temp);
		if (dot > 0) {
			dot = dotRay(ray.getPosition(), Sa.supportPoint(temp), temp);
			return dot <= 0;
		} else if (dot < 0) {
			dot = dotRay(ray.getPosition(), Sa.supportPointNegative(temp), temp);
			return dot >= 0;
		}

		return true;
	}

	private float dotRay(Vector2f vecA, Vector2f vecB, Vector2f vecCheck) {
		return vecCheck.x * (vecA.x - vecB.x) + vecCheck.y * (vecA.y - vecB.y);
	}

	@Override
	public float computeCollisionOnRay(SupportMap<Vector2f> Sa,
			Ray<Vector2f> ray) {
		// TODO Auto-generated method stub

		// Take line from before, make triangle, refine iteratively, k?

		return 0;
	}

	@Override
	public Vector2f computeCollision(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// TODO Auto-generated method stub
		return new Vector2f();
	}

}
