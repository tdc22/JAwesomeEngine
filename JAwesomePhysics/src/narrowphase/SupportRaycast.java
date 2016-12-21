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

		/*if (Math.abs(ray.getDirection().x) >= 0.57735f)
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

		List<Vector2f> simplex = new ArrayList<Vector2f>();
		Vector3f dir;

		// STEP 4: Check if triangle contains hitOfPlane, otherwise refine
		// triangle
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			Vector3f AB = VecMath.subtraction(b, a);
			Vector2f ABonPlane2 = projectPointOn2dPlane(AB, new Vector3f(), b1, b2);
			Vector2f ABonPlane2N = new Vector2f(-ABonPlane2.y, ABonPlane2.x);
			Vector3f dir3;
			if (VecMath.dotproduct(ABonPlane2N, centerOnPlane2) > 0) {
				dir3 = VecMath.crossproduct(ray.getDirection(), AB);
				ABonPlane2N.negate();
			} else {
				dir3 = VecMath.crossproduct(AB, ray.getDirection());
			}
			c = Sa.supportPoint(dir3);

			Vector2f aProj = projectPointOn2dPlane(a, Sa.getSupportCenter(), b1, b2);
			Vector2f bProj = projectPointOn2dPlane(b, Sa.getSupportCenter(), b1, b2);
			Vector2f cProj = projectPointOn2dPlane(c, Sa.getSupportCenter(), b1, b2);
			
			Vector2f BConPlane2 = projectPointOn2dPlane(c, b, b1, b2);
			Vector2f BConPlane2N = new Vector2f(-BConPlane2.y, BConPlane2.x);
			Vector2f CAonPlane2 = projectPointOn2dPlane(a, c, b1, b2);
			Vector2f CAonPlane2N = new Vector2f(-CAonPlane2.y, CAonPlane2.x);

			if (VecMath.dotproduct(BConPlane2N, centerOnPlane2) > 0) {
				BConPlane2N.negate();
			}
			if (VecMath.dotproduct(CAonPlane2N, centerOnPlane2) < 0) {
				CAonPlane2N.negate();
			}

			float aDot = VecMath.dotproduct(VecMath.subtraction(aProj, centerOnPlane2), ABonPlane2N);
			float bDot = VecMath.dotproduct(VecMath.subtraction(bProj, centerOnPlane2), BConPlane2N);
			float cDot = VecMath.dotproduct(VecMath.subtraction(cProj, centerOnPlane2), CAonPlane2N);
			
			boolean aDotGZ = aDot > 0;
			boolean bDotGZ = bDot > 0;
			boolean cDotGZ = cDot > 0;
			
			if(aDotGZ && bDotGZ && cDotGZ) {
				return true;
			}
			if(!aDotGZ && !bDotGZ && !cDotGZ) {
				return true;
			}
			
			// Start iteration
			simplex.clear();
			simplex.add(aProj);
			simplex.add(bProj);
			simplex.add(cProj);
			Vector2f direction = new Vector2f();
			if (GJK2Util.doSimplex(simplex, direction)) {
				return true;
			}
			
			dir = new Vector3f(direction.x * b1.x + direction.y * b2.x, direction.x * b1.y + direction.y * b2.y, direction.x * b1.z + direction.y * b2.z);
		}*/
		
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
		List<Vector3f> simplex3 = new ArrayList<Vector3f>();
		List<Vector2f> simplex = new ArrayList<Vector2f>();
		Vector2f direction;
		
		Vector2f centerOnPlane2 = projectPointOn2dPlane(hitOfPlane, Sa.getSupportCenter(), b1, b2);
		simplex.clear();
		
		Vector3f dir = centerOnPlane;
		Vector3f point = Sa.supportPoint(dir);
		simplex3.add(point);
		Vector2f start = projectPointOn2dPlane(point, Sa.getSupportCenter(), b1, b2);
		start.translate(centerOnPlane2);
		simplex.add(start);
		dir = VecMath.negate(dir);
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			point = Sa.supportPoint(dir);
			Vector2f a = projectPointOn2dPlane(point, Sa.getSupportCenter(), b1, b2);
			a.translate(centerOnPlane2);
			direction = projectPointOn2dPlane(dir, new Vector3f(), b1, b2);
			if (VecMath.dotproduct(a, direction) < 0) {
				return false;
			}
			simplex3.add(point);
			simplex.add(a);
			if (GJK2Util.doSimplex(simplex, direction)) {
				return true;
			}
			dir.set(direction.x * b1.x + direction.y * b2.x, direction.x * b1.y + direction.y * b2.y, direction.x * b1.z + direction.y * b2.z);
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
