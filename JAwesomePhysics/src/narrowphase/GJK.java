package narrowphase;

import math.VecMath;
import objects.SupportMap;
import vector.Vector3f;

public class GJK extends GilbertJohnsonKeerthi<Vector3f> {
	private final int MAX_ITERATIONS = 80;

	public GJK(ManifoldGenerator<Vector3f> manifoldgeneration) {
		super(manifoldgeneration, 4);
	}

	private boolean doSimplex() {
		int simplexsize = simplex.size();
		// Line
		if (simplexsize == 2) {
			// System.out.print("line ");
			Vector3f A = simplex.get(1);
			Vector3f B = simplex.get(0);
			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AO = VecMath.negate(A);
			if (VecMath.dotproduct(AB, AO) > 0) {
				// Region 1
				direction = edgeDirection(AB, AO);
				// System.out.print(AB.toString() + "; " + AO.toString());
				// System.out.print("line region 1");
			} else {
				// Region 2
				simplex.remove(1);
				direction = AO;
				// System.out.print("line region 2");
			}
			// System.out.println(" " + A + "; " + B + "; " + direction);
		}
		// Triangle
		if (simplexsize == 3) {
			// System.out.print("triangle ");
			Vector3f A = simplex.get(2);
			Vector3f B = simplex.get(1);
			Vector3f C = simplex.get(0);
			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AC = VecMath.subtraction(C, A);
			Vector3f ABC = VecMath.crossproduct(AB, AC);
			Vector3f AO = VecMath.negate(A);

			if (VecMath.dotproduct(VecMath.crossproduct(ABC, AC), AO) > 0) {
				if (VecMath.dotproduct(AC, AO) > 0) {
					// Region 1
					simplex.remove(1);
					direction = edgeDirection(AC, AO);
					// System.out.print("r 1");
				} else {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
						// System.out.print("r 4");
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction = AO;
						// System.out.print("r 5");
					}
				}
			} else {
				if (VecMath.dotproduct(VecMath.crossproduct(AB, ABC), AO) > 0) {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
						// System.out.print("r 4(2)");
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction = AO;
						// System.out.print("r 5(2)");
					}
				} else {
					if (VecMath.dotproduct(ABC, AO) >= 0) {
						// Region 2
						direction = ABC;
						// System.out.print("r 2");
					} else {
						// Region 3
						Vector3f temp = simplex.get(0);
						simplex.set(0, simplex.get(1));
						simplex.set(1, temp);
						direction = VecMath.negate(ABC);
						// System.out.print("r 3");
					}
				}
			}
			// System.out.println(" " + A + "; " + B + "; " + C + "; " +
			// direction);
		}
		// Tetrahedron
		if (simplexsize == 4) {
			// //System.out.print("tetrahedron ");
			Vector3f A = simplex.get(3);
			Vector3f B = simplex.get(2);
			Vector3f C = simplex.get(1);
			Vector3f D = simplex.get(0);

			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AC = VecMath.subtraction(C, A);
			Vector3f AD = VecMath.subtraction(D, A);

			Vector3f ABC = VecMath.crossproduct(AB, AC);
			Vector3f ACD = VecMath.crossproduct(AC, AD);
			Vector3f ADB = VecMath.crossproduct(AD, AB);

			Vector3f AO = VecMath.negate(A);

			// TEST
			// Vector3f BA = VecMath.substraction(A, B);
			// Vector3f BC = VecMath.substraction(C, B);
			// Vector3f BD = VecMath.substraction(D, B);
			// Vector3f BDC = VecMath.crossproduct(BD, BC);
			//
			// if ((VecMath.dotproduct(BDC, BA) > 0)
			// || (VecMath.dotproduct(ACD, AB) > 0)
			// || (VecMath.dotproduct(ADB, AC) > 0)
			// || (VecMath.dotproduct(ABC, AD) > 0)) {
			// System.out.print("Correct Orientation: "
			// + !(VecMath.dotproduct(BDC, BA) > 0) + "; "
			// + !(VecMath.dotproduct(ACD, AB) > 0) + "; "
			// + !(VecMath.dotproduct(ADB, AC) > 0) + "; "
			// + !(VecMath.dotproduct(ABC, AD) > 0));
			// System.out.print("Orientation: "
			// + (VecMath.dotproduct(BDC, BA) == 0) + "; "
			// + (VecMath.dotproduct(ACD, AB) == 0) + "; "
			// + (VecMath.dotproduct(ADB, AC) == 0) + "; "
			// + (VecMath.dotproduct(ABC, AD) == 0));
			// System.out.println(A.toString() + "; " + B.toString() + "; "
			// + C.toString() + "; " + D.toString());
			// }
			// TEST END

			// //System.out.print(ABC + "; " + ADB + "; " + ACD + "; ");
			if (VecMath.dotproduct(ABC, AO) > 0) {
				if (VecMath.dotproduct(ADB, AO) > 0) {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Top
						simplex.remove(2);
						simplex.remove(1);
						simplex.remove(0);
						direction = AO;
						// System.out.print("top");
					} else {
						// Edge 1
						simplex.remove(1);
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
						// System.out.print("edge 1");
					}
				} else {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Edge 2
						simplex.remove(2);
						simplex.remove(0);
						direction = edgeDirection(AC, AO);
						// System.out.print("edge 2");
					} else {
						// Face 1
						simplex.remove(0);
						direction = ABC;
						// System.out.print("face 1");
					}
				}
			} else {
				if (VecMath.dotproduct(ADB, AO) > 0) {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Edge 3
						simplex.remove(2);
						simplex.remove(1);
						direction = edgeDirection(AD, AO);
						// System.out.print("edge 3");
					} else {
						// Face 2
						simplex.remove(1); // CHANGE ORIENTATION?????
						Vector3f temp = simplex.get(0);
						simplex.set(0, simplex.get(1));
						simplex.set(1, temp);
						direction = ADB;
						// System.out.print("face 2");
					}
				} else {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Face 3
						simplex.remove(2);
						direction = ACD;
						// System.out.print("face 3");
					} else {
						// Center
						// System.out.print("center");
						// System.out.println(" " + A + "; " + B + "; " + C +
						// "; " + D);
						return true;
					}
				}
			}
			// System.out.println(" " + A + "; " + B + "; " + C + "; " + D +
			// "; " + direction);
		}
		return false;
	}

	private Vector3f edgeDirection(Vector3f edge, Vector3f origin) {
		// return VecMath.crossproduct(VecMath.crossproduct(edge, origin),
		// edge);
		float eXoYeYoX = edge.x * origin.y - edge.y * origin.x;
		float eYoZeZoY = edge.y * origin.z - edge.z * origin.y;
		float eZoXeXeZ = edge.z * origin.x - edge.x * origin.z;
		return new Vector3f((eZoXeXeZ) * edge.z - (eXoYeYoX) * edge.y,
				(eXoYeYoX) * edge.x - (eYoZeZoY) * edge.z, (eYoZeZoY) * edge.y
						- (eZoXeXeZ) * edge.x);
	}

	final Vector3f startdirection = new Vector3f(1, 1, 1);

	@Override
	public boolean isColliding(SupportMap<Vector3f> Sa, SupportMap<Vector3f> Sb) {
		// System.out.println("---------- New Loop: ----------");
		simplex.clear();
		// S = Support(?)
		direction = support(Sa, Sb, startdirection);
		// [] = S
		simplex.add(direction);
		// D = -S
		direction = VecMath.negate(direction);
		// Loop:
		// System.out.println("startpoint: " + simplex.get(0) + "; " +
		// direction);
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			// A = Support(D)
			Vector3f a = support(Sa, Sb, direction);
			// System.out.println("New Point: " + a);
			// if AtD < 0 No Intersection
			if (VecMath.dotproduct(a, direction) < 0)
				return false;
			// [] += A
			simplex.add(a);
			// if DoSimplex([], D) Intersection
			if (doSimplex()) {
				return true;
			}
		}
		// System.out.println("MAX ITERATIONS!!");
		return false;
	}

	private Vector3f support(SupportMap<Vector3f> Sa, SupportMap<Vector3f> Sb,
			Vector3f dir) {
		// System.out.println("sup: " +
		// VecMath.substraction(Sa.supportPoint(dir),
		// Sb.supportPoint(VecMath.negate(dir))) + ": " + Sa.supportPoint(dir) +
		// "; " + Sb.supportPoint(VecMath.negate(dir)) + "; " + dir);
		Vector3f suppA = Sa.supportPoint(dir);
		Vector3f suppB = Sb.supportPointNegative(dir);
		suppB.negate();
		suppA.translate(suppB);
		return suppA;
	}
}