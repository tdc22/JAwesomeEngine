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
	private final static float MAX_ITERATIONS_HIT = 20;
	private final static float EPSILON = 0.001f;
	private final Vector3f zero = new Vector3f();
	private final List<Vector2f> simplex = new ArrayList<Vector2f>();
	private final List<Vector3f> simplex3 = new ArrayList<Vector3f>();
	private final Vector2f centerOnPlane2 = new Vector2f();

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

		// STEP 2: Project support center on plane and adjust base
		// directions/pick start directions
		float t0 = dotRay(Sa.getSupportCenter(), ray.getPosition(), ray.getDirection());
		Vector3f hitOfPlane = VecMath.scale(ray.getDirection(), t0);
		hitOfPlane.translate(ray.getPosition());

		Vector3f centerOnPlane = projectPointOnPlane(hitOfPlane, Sa.getSupportCenter(), ray.getDirection());
		centerOnPlane = VecMath.subtraction(centerOnPlane, Sa.getSupportCenter());

		// STEP 3: Calculate Support(centerOnPlane) and Support(centerOnPlane x
		// normal)
		Vector2f direction;

		centerOnPlane2.set(projectPointOn2dPlane(hitOfPlane, Sa.getSupportCenter(), b1, b2));
		centerOnPlane2.negate();
		simplex.clear();
		simplex3.clear();

		Vector3f dir = centerOnPlane;
		Vector3f point = Sa.supportPoint(dir);
		Vector2f start = projectPointOn2dPlane(point, Sa.getSupportCenter(), b1, b2);
		start.translate(centerOnPlane2);
		simplex.add(start);
		simplex3.add(point);
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
			simplex3.add(point);
			int region = GJK2Util.doSimplexRegion(simplex, direction);
			if (region == 0) {
				return true;
			} else {
				switch (region) {
				case 5:
					simplex3.remove(2);
				case 2:
				case 3:
					simplex3.remove(1);
					break;
				case 4:
					simplex3.remove(0);
					break;
				}
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
		Vector3f a = simplex3.get(0);
		Vector3f b = simplex3.get(1);
		Vector3f c = simplex3.get(2);

		Vector2f projA = projectPointOn2dPlane(a, Sa.getSupportCenter(), b1, b2);
		Vector2f projB = projectPointOn2dPlane(b, Sa.getSupportCenter(), b1, b2);
		Vector2f projC = projectPointOn2dPlane(c, Sa.getSupportCenter(), b1, b2);

		projA.negate();
		projB.negate();
		projC.negate();

		Vector3f n = null;

		for (int i = 0; i < MAX_ITERATIONS_HIT; i++) {
			n = VecMath.computeNormal(a, b, c);
			if (VecMath.dotproduct(n, ray.getDirection()) > 0) {
				Vector3f tempC = b;
				b = c;
				c = tempC;
				Vector2f tempProjC = projB;
				projB = projC;
				projC = tempProjC;
				n.negate();
			}
			Vector3f p = Sa.supportPoint(n);

			if (VecMath.dotproduct(p, n) - VecMath.dotproduct(n, a) < EPSILON) {
				break;
			}

			Vector2f q = projectPointOn2dPlane(p, Sa.getSupportCenter(), b1, b2);
			q.negate();

			Vector2f AB = VecMath.subtraction(projB, projA);
			Vector2f BC = VecMath.subtraction(projC, projB);
			Vector2f CA = VecMath.subtraction(projA, projC);
			Vector2f PA = VecMath.subtraction(projA, q);
			Vector2f PB = VecMath.subtraction(projB, q);
			Vector2f PC = VecMath.subtraction(projC, q); // TODO:
															// optimize,
															// put in
															// else
			Vector2f PAn = new Vector2f(-PA.y, PA.x);
			Vector2f PBn = new Vector2f(-PB.y, PB.x);
			Vector2f PCn = new Vector2f(-PC.y, PC.x);
			boolean outsideA = VecMath.dotproduct(AB, PBn) > 0;
			boolean outsideB = VecMath.dotproduct(BC, PCn) > 0;
			boolean outsideC = VecMath.dotproduct(CA, PAn) > 0;

			if (outsideA) {
				if (outsideB) {
					// Region 1
					b = p;
					projB = q;
				} else {
					if (outsideC) {
						// Region 3
						a = p;
						projA = q;
					} else {
						// Region 4
						Vector2f Cp = VecMath.subtraction(q, projC);
						Vector2f rp = new Vector2f(-(q.y - centerOnPlane2.y), q.x - centerOnPlane2.x);
						if (VecMath.dotproduct(Cp, rp) > 0) {
							b = p;
							projB = q;
						} else {
							a = p;
							projA = q;
						}
					}
				}
			} else {
				if (outsideB) {
					if (outsideC) {
						// Region 2
						c = p;
						projC = q;
					} else {
						// Region 5
						Vector2f Ap = VecMath.subtraction(q, projA);
						Vector2f rp = new Vector2f(-(q.y - centerOnPlane2.y), q.x - centerOnPlane2.x);
						if (VecMath.dotproduct(Ap, rp) > 0) {
							c = p;
							projC = q;
						} else {
							b = p;
							projB = q;
						}
					}
				} else {
					if (outsideC) {
						// Region 6
						Vector2f Bp = VecMath.subtraction(q, projB);
						Vector2f rp = new Vector2f(-(q.y - centerOnPlane2.y), q.x - centerOnPlane2.x);
						if (VecMath.dotproduct(Bp, rp) > 0) {
							a = p;
							projA = q;
						} else {
							c = p;
							projC = q;
						}
					} else {
						// Region 7
						// TODO: check for optimizations

						Vector2f Ap = VecMath.subtraction(q, projA);
						Vector2f Bp = VecMath.subtraction(q, projB);
						Vector2f rp = new Vector2f(-(q.y - centerOnPlane2.y), q.x - centerOnPlane2.x);
						if (VecMath.dotproduct(Ap, rp) > 0 && VecMath.dotproduct(Bp, rp) <= 0) {
							c = p;
							projC = q;
						} else {
							Vector2f Cp = VecMath.subtraction(q, projC);
							if (VecMath.dotproduct(Bp, rp) > 0 && VecMath.dotproduct(Cp, rp) <= 0) {
								a = p;
								projA = q;
							} else {
								if (VecMath.dotproduct(Cp, rp) > 0 && VecMath.dotproduct(Ap, rp) <= 0) {
									b = p;
									projB = q;
								} else {
									System.out.println("ERROR: no region");
								}
							}
						}
					}
				}
			}
		}

		// Last step: Calculate hitpoint between Ray and Triangle abc
		// TODO: Potentially wrong normal if iteration limit above is exceeded!

		return VecMath.dotproduct(VecMath.subtraction(a, ray.getPosition()), n)
				/ VecMath.dotproduct(ray.getDirection(), n);
	}

	@Override
	public Vector3f computeCollision(SupportMap<Vector3f> Sa, Ray<Vector3f> ray) {
		float d = computeCollisionOnRay(Sa, ray);
		return new Vector3f(ray.getPosition().x + ray.getDirection().x * d,
				ray.getPosition().y + ray.getDirection().y * d, ray.getPosition().z + ray.getDirection().z * d);
	}
}