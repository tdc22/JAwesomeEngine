package narrowphase;

import java.util.List;

import math.VecMath;
import vector.Vector2f;
import vector.Vector3f;

public class GJK2Util {
	public static boolean doSimplex(List<Vector2f> simplex, Vector2f direction) {
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
			} else {
				// Region 2
				simplex.remove(1);
				direction.set(AO);
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
				} else {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction.set(edgeDirection(AB, AO));
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction.set(AO);
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
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction.set(AO);
					}
				} else {
					// Center
					return true;
				}
			}
		}
		return false;
	}
	
	private static Vector2f edgeDirection(Vector2f edge, Vector2f origin) {
		Vector2f a = new Vector2f(-edge.y, edge.x);
		if (VecMath.dotproduct(a, origin) > 0)
			return a;
		a.negate();
		return a;
	}
}