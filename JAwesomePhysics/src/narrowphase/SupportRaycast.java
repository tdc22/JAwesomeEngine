package narrowphase;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class SupportRaycast implements RaycastNarrowphase<Vector3f> {
	private final Vector3f b1 = new Vector3f();
	private final Vector3f b2 = new Vector3f();

	private final static float MAX_ITERATIONS = 20;
	private final Vector3f zero = new Vector3f();

	@Override
	public boolean isColliding(SupportMap<Vector3f> Sa, Ray<Vector3f> ray) {
		if (Math.abs(ray.getDirection().x) >= 0.57735f)
			b1.set(ray.getDirection().y, -ray.getDirection().x, 0);
		else
			b1.set(0, ray.getDirection().z, -ray.getDirection().y);

		if (b1.lengthSquared() > 0)
			b1.normalize();
		b2.set(ray.getDirection().getYf() * b1.getZf() - ray.getDirection().getZf() * b1.getYf(),
				ray.getDirection().getZf() * b1.getXf() - ray.getDirection().getXf() * b1.getZf(),
				ray.getDirection().getXf() * b1.getYf() - ray.getDirection().getYf() * b1.getXf());

		// STEP 2: Project support center on plane and adjust base directions/pick start directions
		float t0 = dotRay(Sa.getSupportCenter(), ray.getPosition(), ray.getDirection());
		Vector3f hitOfPlane = VecMath.scale(ray.getDirection(), t0);
		hitOfPlane.translate(ray.getPosition());

		Vector3f centerOnPlane = projectPointOnPlane(hitOfPlane, Sa.getSupportCenter(), ray.getDirection());
		centerOnPlane = VecMath.subtraction(centerOnPlane, Sa.getSupportCenter());

		// STEP 3: Calculate Support(centerOnPlane) and Support(centerOnPlane x normal)
		List<Vector2f> simplex = new ArrayList<Vector2f>();
		Vector2f direction;

		Vector2f centerOnPlane2 = projectPointOn2dPlane(hitOfPlane, Sa.getSupportCenter(), b1, b2);
		simplex.clear();

		Vector3f dir = centerOnPlane;
		Vector3f point = Sa.supportPoint(dir);
		Vector2f start = projectPointOn2dPlane(point, Sa.getSupportCenter(), b1, b2);
		start.translate(centerOnPlane2);
		simplex.add(start);
		dir = VecMath.negate(dir);

		for (int i = 0; i < MAX_ITERATIONS; i++) {
			point = Sa.supportPoint(dir);
			Vector2f a = projectPointOn2dPlane(point, Sa.getSupportCenter(), b1, b2);
			a.translate(centerOnPlane2);
			direction = projectPointOn2dPlane(dir, zero, b1, b2);
			if (VecMath.dotproduct(a, direction) < 0) {
				return false;
			}
			simplex.add(a);
			if (GJK2Util.doSimplex(simplex, direction)) {
				return true;
			}
			dir.set(direction.x * b1.x + direction.y * b2.x, direction.x * b1.y + direction.y * b2.y,
					direction.x * b1.z + direction.y * b2.z);
		}

		return false;
	}

	private float dotRay(Vector3f vecA, Vector3f vecB, Vector3f vecCheck) {
		return vecCheck.x * (vecA.x - vecB.x) + vecCheck.y * (vecA.y - vecB.y) + vecCheck.z * (vecA.z - vecB.z);
	}

	private Vector3f projectPointOnPlane(Vector3f point, Vector3f origin, Vector3f normal) {
		float dist = dotRay(point, origin, normal);
		return new Vector3f(point.x - dist * normal.x, point.y - dist * normal.y, point.z - dist * normal.z);
	}

	private Vector2f projectPointOn2dPlane(Vector3f point, Vector3f origin, Vector3f base1, Vector3f base2) {
		return new Vector2f(dotRay(point, origin, base1), dotRay(point, origin, base2));
	}

	@Override
	public float computeCollisionOnRay(SupportMap<Vector3f> Sa, Ray<Vector3f> ray) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector3f computeCollision(SupportMap<Vector3f> Sa, Ray<Vector3f> ray) {
		// TODO Auto-generated method stub
		return new Vector3f();
	}

}
