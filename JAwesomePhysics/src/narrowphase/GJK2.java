package narrowphase;

import math.VecMath;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class GJK2 extends GilbertJohnsonKeerthi<Vector2f> {
	private final int MAX_ITERATIONS = 50;

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

	final Vector2f startdirection = new Vector2f(1, 1);

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb) {
		simplex.clear();
		// S = Support(?)
		direction = support(Sa, Sb, startdirection);
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

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb,
			Vector2f dir) {
		Vector2f suppA = Sa.supportPoint(dir);
		Vector2f suppB = Sb.supportPointNegative(dir);
		suppB.negate();
		suppA.translate(suppB);
		return suppA;
	}
}