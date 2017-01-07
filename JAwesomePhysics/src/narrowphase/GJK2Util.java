package narrowphase;

import java.util.List;

import math.VecMath;
import vector.Vector2f;
import vector.Vector3f;

public class GJK2Util {
	public static boolean doSimplex(List<Vector2f> simplex, Vector2f direction) {
		return doSimplexRegion(simplex, direction) == 0;
	}

	public static int doSimplexRegion(List<Vector2f> simplex, Vector2f direction) {
		int simplexsize = simplex.size();
		// Line
		if (simplexsize == 2) {
			Vector2f A = simplex.get(1);
			Vector2f B = simplex.get(0);
			Vector2f AB = VecMath.subtraction(B, A);
			Vector2f AO = VecMath.negate(A);

			if (VecMath.dotproduct(AB, AO) > 0) {
				// Region 1
				direction.set(edgeDirection(AB, AO));
				return 1;
			} else {
				// Region 2
				simplex.remove(1);
				direction.set(AO);
				return 2;
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
					direction.set(edgeDirection(AC, AO));
					return 3;
				} else {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction.set(edgeDirection(AB, AO));
						return 4;
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction.set(AO);
						return 5;
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
						direction.set(edgeDirection(AB, AO));
						return 4;
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction.set(AO);
						return 5;
					}
				} else {
					// Center
					return 0;
				}
			}
		}
		return 6;
	}

	private final static Vector2f tempEdge = new Vector2f();

	private static Vector2f edgeDirection(Vector2f edge, Vector2f origin) {
		tempEdge.set(-edge.y, edge.x);
		if (VecMath.dotproduct(tempEdge, origin) > 0)
			return tempEdge;
		tempEdge.negate();
		return tempEdge;
	}
}