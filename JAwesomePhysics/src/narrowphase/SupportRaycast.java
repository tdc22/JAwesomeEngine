package narrowphase;

import math.VecMath;
import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class SupportRaycast implements RaycastNarrowphase<Vector3f> {
	private final Vector3f b1 = new Vector3f();
	private final Vector3f b2 = new Vector3f();
	private Vector3f s1, s2;

	private final static float EPSILON = 0.01f;
	private final static float MAX_ITERATIONS = 20;

	@Override
	public boolean isColliding(SupportMap<Vector3f> Sa, Ray<Vector3f> ray) {
		// Calculate base vectors
		/*
		 * float t0 = dotRay(Sa.getSupportCenter(), ray.getPosition(),
		 * ray.getDirection()); Vector3f hitOfPlane =
		 * VecMath.scale(ray.getDirection(), t0);
		 * hitOfPlane.translate(ray.getPosition());
		 * hitOfPlane.translate(VecMath.negate(Sa.getSupportCenter()));
		 * 
		 * /*v = Sa.supportPointRelative(new Vector3f(hitOfPlane));
		 * System.out.println(v + "; " + VecMath.dotproduct(v, hitOfPlane) +
		 * "; " + hitOfPlane.lengthSquared() + "; " + v.lengthSquared()); return
		 * VecMath.dotproduct(v, hitOfPlane)/hitOfPlane.length() >=
		 * hitOfPlane.length();
		 */

		/*
		 * Vector3f point = Sa.supportPointRelative(new Vector3f(hitOfPlane));
		 * Vector3f v = VecMath.subtraction(point, hitOfPlane); float dist =
		 * VecMath.dotproduct(v, ray.getDirection());
		 * point.translate(VecMath.scale(ray.getDirection(), -dist));
		 * 
		 * // BEIDE AUF HITPLANE // TODO: SCHAUEN, WIE x-y-Coords (relative zu
		 * plane) berechnet werden!!! // TODO: Base-vektoren berechnen +
		 * dotproduct
		 * 
		 * return (Math.abs(point.x) >= Math.abs(hitOfPlane.x) &&
		 * Math.abs(point.y) >= Math.abs(hitOfPlane.y));
		 * 
		 * //////////////////////////START//////////////////////////
		 * 
		 * /** TODO: Neuer Ansatz (alter geht nicht, weil länglicher Zylinder +
		 * spitzer Winkel): 2d GJK in ray-ebene (ray-direction als Normale)
		 * basevektoren als startrichtungen
		 */

		// STEP 1: Calculate bases of ray-direction

		/*
		 * if (Math.abs(ray.getDirection().x) >= 0.57735f)
		 * b1.set(ray.getDirection().y, -ray.getDirection().x, 0); else
		 * b1.set(0, ray.getDirection().z, -ray.getDirection().y);
		 * 
		 * if (b1.lengthSquared() > 0) b1.normalize();
		 * b2.set(ray.getDirection().getYf() * b1.getZf() -
		 * ray.getDirection().getZf() * b1.getYf(), ray.getDirection().getZf() *
		 * b1.getXf() - ray.getDirection().getXf() * b1.getZf(),
		 * ray.getDirection().getXf() * b1.getYf() - ray.getDirection().getYf()
		 * * b1.getXf());
		 * 
		 * // STEP 2: Project support center on plane and adjust base
		 * directions/pick start directions float t0 =
		 * dotRay(Sa.getSupportCenter(), ray.getPosition(), ray.getDirection());
		 * Vector3f hitOfPlane = VecMath.scale(ray.getDirection(), t0);
		 * hitOfPlane.translate(ray.getPosition()); //Vector2f centerOnPlane =
		 * projectPointOn2dPlane(hitOfPlane, Sa.getSupportCenter(), b1, b2); //
		 * negated! Vector3f centerOnPlane = projectPointOnPlane(hitOfPlane,
		 * Sa.getSupportCenter(), ray.getDirection());
		 * 
		 * // STEP 3: Calculate Support(centerOnPlane) and Support(centerOnPlane
		 * x normal) Vector3f a = Sa.supportPoint(centerOnPlane); Vector3f dir2
		 * = VecMath.crossproduct(centerOnPlane, ray.getDirection()); Vector3f b
		 * = Sa.supportPoint(centerOnPlane);
		 */

		// STEP 4: 2D-GJK on ray-plane

		if (Math.abs(ray.getDirection().x) >= 0.57735f)
			b1.set(ray.getDirection().y, -ray.getDirection().x, 0);
		else
			b1.set(0, ray.getDirection().z, -ray.getDirection().y);

		if (b1.lengthSquared() > 0)
			b1.normalize();
		b2.set(ray.getDirection().getYf() * b1.getZf() - ray.getDirection().getZf() * b1.getYf(),
				ray.getDirection().getZf() * b1.getXf() - ray.getDirection().getXf() * b1.getZf(),
				ray.getDirection().getXf() * b1.getYf() - ray.getDirection().getYf() * b1.getXf());

		// STEP 2: Project support center on plane and adjust base
		// directions/pick start directions
		float t0 = dotRay(Sa.getSupportCenter(), ray.getPosition(), ray.getDirection());
		Vector3f hitOfPlane = VecMath.scale(ray.getDirection(), t0);
		hitOfPlane.translate(ray.getPosition());
		// Vector2f centerOnPlane = projectPointOn2dPlane(hitOfPlane,
		// Sa.getSupportCenter(), b1, b2); // negated!
		Vector3f centerOnPlane = projectPointOnPlane(hitOfPlane, Sa.getSupportCenter(), ray.getDirection());
		centerOnPlane = VecMath.subtraction(centerOnPlane, Sa.getSupportCenter());

		// STEP 3: Calculate Support(centerOnPlane) and Support(centerOnPlane x
		// normal)
		Vector3f a = Sa.supportPoint(centerOnPlane);
		// TODO: cancel if closer than epsilon
		Vector3f dir2 = VecMath.negate(centerOnPlane);
		Vector3f b = Sa.supportPoint(dir2);
		// TODO: can we choose dir2 a little bit smarter? (See: Cylinders)
		Vector3f c = null;
		Vector2f centerOnPlane2 = projectPointOn2dPlane(hitOfPlane, Sa.getSupportCenter(), b1, b2);

		// STEP 4: Check if triangle contains hitOfPlane, otherwise refine
		// triangle
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			Vector3f AB = VecMath.subtraction(b, a);
			// Vector3f ABn = VecMath.crossproduct(AB, ray.getDirection());
			// Project AB on plane and check on which side the centerOnPlane
			// lies
			Vector2f ABonPlane2 = projectPointOn2dPlane(AB, new Vector3f(), b1, b2);
			Vector2f ABonPlane2N = new Vector2f(-ABonPlane2.y, ABonPlane2.x);
			Vector3f dir3;
			float firstDot = VecMath.dotproduct(ABonPlane2N, centerOnPlane2);
			boolean firstDotG = firstDot > 0;
			if (firstDotG) {
				dir3 = VecMath.crossproduct(ray.getDirection(), AB);
			} else {
				dir3 = VecMath.crossproduct(AB, ray.getDirection());
			}
			c = Sa.supportPoint(dir3);

			Vector2f BConPlane2 = projectPointOn2dPlane(VecMath.subtraction(c, b), new Vector3f(), b1, b2);
			Vector2f BConPlane2N = new Vector2f(-BConPlane2.y, BConPlane2.x);
			Vector2f CAonPlane2 = VecMath.addition(ABonPlane2, BConPlane2);
			Vector2f CAonPlane2N = new Vector2f(-CAonPlane2.y, CAonPlane2.x);

			// TODO: optimize???
			Vector2f centerA = projectPointOn2dPlane(VecMath.subtraction(a, centerOnPlane), new Vector3f(), b1, b2);
			Vector2f centerB = projectPointOn2dPlane(VecMath.subtraction(b, centerOnPlane), new Vector3f(), b1, b2);
			Vector2f centerC = projectPointOn2dPlane(VecMath.subtraction(c, centerOnPlane), new Vector3f(), b1, b2);

			firstDot = VecMath.dotproduct(BConPlane2N, centerA); // centerOnPlane
																	// - a
			float secondDot = VecMath.dotproduct(BConPlane2N, centerB);
			float thirdDot = VecMath.dotproduct(CAonPlane2N, centerC);

			System.out.println(centerA + "; " + centerB + "; " + centerC);
			System.out.println(firstDot + "; " + secondDot + "; " + thirdDot);

			if (firstDotG) {
				if (firstDot >= 0 && secondDot >= 0 && thirdDot >= 0)
					return true;
			} else {
				if (firstDot <= 0 && secondDot <= 0 && thirdDot <= 0)
					return true;
			}
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
