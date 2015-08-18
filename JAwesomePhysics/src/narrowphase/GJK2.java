package narrowphase;

import java.util.ArrayList;

import math.VecMath;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class GJK2 extends GilbertJohnsonKeerthi<Vector2f> {

	public GJK2(ManifoldGenerator<Vector2f> manifoldgeneration) {
		super(manifoldgeneration);
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
			Vector3f ABC = VecMath.crossproduct(new Vector3f(AB), new Vector3f(
					AC));
			Vector2f AO = VecMath.negate(A);

			if (VecMath.dotproduct(VecMath.crossproduct(ABC, new Vector3f(AC)),
					new Vector3f(AO)) > 0) {
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
				if (VecMath.dotproduct(
						VecMath.crossproduct(new Vector3f(AB), ABC),
						new Vector3f(AO)) > 0) {
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
		simplex = new ArrayList<Vector2f>();
		// S = Support(?)
		direction = support(Sa, Sb, new Vector2f(1, 1));
		// [] = S
		simplex.add(direction);
		// D = -S
		direction = VecMath.negate(direction);
		// Loop:
		for (int i = 0; i < 50; i++) {
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
		return VecMath.subtraction(Sa.supportPoint(dir),
				Sb.supportPointNegative(dir));
	}
}