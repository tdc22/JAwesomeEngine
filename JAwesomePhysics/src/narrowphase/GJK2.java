package narrowphase;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class GJK2 extends GilbertJohnsonKeerthi<Vector2f> {
	private final int MAX_ITERATIONS = 50;
	private final int MAX_ITERATIONS_RAYCAST = 50;

	public GJK2(ManifoldGenerator<Vector2f> manifoldgeneration) {
		super(manifoldgeneration, 3);
	}

	private boolean doSimplex() {
		int simplexsize = simplex.size();
		// Line
		if (simplexsize == 2) {

			Vector2f A = simplex.get(1);
			Vector2f B = simplex.get(0);
			Vector2f AB = VecMath.subtraction(B, A);
			Vector2f AO = VecMath.negate(A);

			if (VecMath.dotproduct(AB, AO) > 0) {
				// Region 1
				direction = edgeDirection(AB, AO);
			} else {
				// Region 2
				simplex.remove(1);
				direction = AO;
			}
		}
		// Triangle
		if (simplexsize == 3) {
			Vector2f A = simplex.get(2);
			Vector2f B = simplex.get(1);
			Vector2f C = simplex.get(0);
			Vector2f AB = VecMath.subtraction(B, A);
			Vector2f AC = VecMath.subtraction(C, A);
			Vector3f ABC = VecMath.crossproduct(AB.x, AB.y, 0, AC.x, AC.y, 0);
			Vector2f AO = VecMath.negate(A);

			// if (VecMath.dotproduct(VecMath.crossproduct(ABC, new
			// Vector3f(AC)),
			// new Vector3f(AO)) > 0) {
			if (VecMath.dotproduct(-ABC.z * AC.y, ABC.z * AC.x, AO.x, AO.y) > 0) {
				if (VecMath.dotproduct(AC, AO) > 0) {
					// Region 1
					simplex.remove(1);
					direction = edgeDirection(AC, AO);
				} else {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction = AO;
					}
				}
			} else {
				if (VecMath.dotproduct(AB.y * ABC.z, -AB.x * ABC.z, AO.x, AO.y) > 0) {
					// if (VecMath.dotproduct(
					// VecMath.crossproduct(new Vector3f(AB), ABC),
					// new Vector3f(AO)) > 0) {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction = AO;
					}
				} else {
					// Center
					return true;
				}
			}
		}
		return false;
	}

	private Vector2f edgeDirection(Vector2f edge, Vector2f origin) {
		Vector2f a = new Vector2f(-edge.y, edge.x);
		if (VecMath.dotproduct(a, origin) > 0)
			return a;
		a.negate();
		return a;
	}

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb) {
		simplex.clear();
		// S = Support(?)
		direction = support(Sa, Sb, new Vector2f(1, 1));
		// [] = S
		simplex.add(direction);
		// D = -S
		direction = VecMath.negate(direction);
		// Loop:
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			// A = Support(D)
			Vector2f a = support(Sa, Sb, direction);
			// if AtD < 0 No Intersection
			if (VecMath.dotproduct(a, direction) < 0)
				return false;
			// [] += A
			simplex.add(a);
			// if DoSimplex([], D) Intersection
			if (doSimplex())
				return true;
		}
		return false;
	}

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb, Vector2f dir) {
		Vector2f suppA = Sa.supportPoint(dir);
		Vector2f suppB = Sb.supportPointNegative(dir);
		suppB.negate();
		suppA.translate(suppB);
		return suppA;
	}

	final float EPSILON = 0.001f;
	final float EPSILON_SQR = EPSILON * EPSILON;

	// source:
	// http://www.bulletphysics.com/ftp/pub/test/physics/papers/jgt04raycast.pdf
	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		float lambda = 0;
		Vector2f x = new Vector2f(ray.getPosition());
		Vector2f n = new Vector2f();
		Vector2f v = VecMath.subtraction(x, Sa.supportPoint(ray.getDirection()));
		List<Vector2f> P = new ArrayList<Vector2f>();

		for (int i = 0; i < MAX_ITERATIONS_RAYCAST; i++) {
			if (v.lengthSquared() > EPSILON_SQR) {
				return true;
			}

			Vector2f p = Sa.supportPoint(v);
			Vector2f w = VecMath.subtraction(x, p);
			if (VecMath.dotproduct(v, w) > 0) {
				if (VecMath.dotproduct(v, ray.getDirection()) >= 0) {
					return false;
				}
				lambda -= VecMath.dotproduct(v, w) / VecMath.dotproduct(v, ray.getDirection());
				x = ray.getPosition();
				x.translate(VecMath.scale(ray.getDirection(), lambda));
				n = v;
			}

			// Y <- P u {p}
			P.add(p);
			// v <- v(conv({x} - Y))

			// P <- "smalles X <= Y such that v e conv({x] - X)
		}

		return false;
	}

	@Override
	public Vector2f computeCollision(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// TODO Auto-generated method stub
		return null;
	}
}