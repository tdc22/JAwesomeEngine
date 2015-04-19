package narrowphase;

import java.util.ArrayList;
import java.util.List;

import manifold.ContactManifold;
import math.VecMath;
import objects.SupportMap;
import vector.Vector3f;

public class EPA implements ManifoldGenerator<Vector3f> {
	private class Triangle {
		Vector3f a, b, c, normal;
		float distance;

		public Triangle(Vector3f a, Vector3f b, Vector3f c) {
			this.a = a;
			this.b = b;
			this.c = c;
			normal = VecMath.normalize(VecMath.computeNormal(a, b, c));
		}
	}

	// Source 1:
	// http://www.bulletphysics.org/Bullet/phpBB3/viewtopic.php?p=&f=4&t=2931
	// Source 2:
	// http://allenchou.net/2013/12/game-physics-contact-generation-epa/

	private final float TOLERANCE = 0.001f;
	private final int MAX_ITERATIONS = 50;

	@Override
	public ContactManifold<Vector3f> computeCollision(SupportMap<Vector3f> Sa,
			SupportMap<Vector3f> Sb, List<Vector3f> simplex) {
		List<Triangle> faces = new ArrayList<Triangle>();

		Vector3f A = simplex.get(3);
		Vector3f B = simplex.get(2);
		Vector3f C = simplex.get(1);
		Vector3f D = simplex.get(0);
		faces.add(new Triangle(A, B, C));
		faces.add(new Triangle(A, D, B));
		faces.add(new Triangle(A, C, D));
		faces.add(new Triangle(D, C, B));

		// System.out.println("------------------------------------");
		// System.out.println(A + "; " + B + "; " + C + "; " + D);

		Vector3f normal = new Vector3f();
		float depth = 0;
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			Triangle t = findClosestTriangle(faces);
			// System.out.println(faces.size() + "; " + t.normal + "; "
			// + VecMath.dotproduct(t.normal, VecMath.negate(t.a)));
			// System.out.println(t.normal);

			if (isOriginInsideTriangleArea(t)) {
				Vector3f p = support(Sa, Sb, t.normal);
				// System.out.println(t.normal);
				double d = VecMath.dotproduct(p, t.normal);
				// System.out.println(d - t.distance + "; " + p);
				if (d - t.distance < TOLERANCE) {
					normal = t.normal;
					depth = (float) d;
					// System.out.println("res: " + normal + "; " + depth + "; "
					// + t.a + "; " + t.b + "; " + t.c);
					break;
				} else {
					// Check if convex!!!
					Triangle[] adjacents = findAdjacentTriangles(t, faces);

					if (adjacents[0] != null) {
						if (VecMath.dotproduct(VecMath.subtraction(p, t.a),
								adjacents[0].normal) > 0) {
							Vector3f adjD = findTheD(t.a, t.b, adjacents[0]);
							faces.add(new Triangle(p, t.a, adjD));
							faces.add(new Triangle(t.b, p, adjD));
							faces.remove(adjacents[0]);
						} else {
							faces.add(new Triangle(t.a, t.b, p));
						}
					}

					if (adjacents[1] != null) {
						if (VecMath.dotproduct(VecMath.subtraction(p, t.b),
								adjacents[1].normal) > 0) {
							Vector3f adjD = findTheD(t.b, t.c, adjacents[1]);
							faces.add(new Triangle(p, t.b, adjD));
							faces.add(new Triangle(t.c, p, adjD));
							faces.remove(adjacents[1]);
						} else {
							faces.add(new Triangle(t.b, t.c, p));
						}
					}

					if (adjacents[2] != null) {
						if (VecMath.dotproduct(VecMath.subtraction(p, t.c),
								adjacents[2].normal) > 0) {
							Vector3f adjD = findTheD(t.c, t.a, adjacents[2]);
							faces.add(new Triangle(p, t.c, adjD));
							faces.add(new Triangle(t.a, p, adjD));
							faces.remove(adjacents[2]);
						} else {
							faces.add(new Triangle(t.c, t.a, p));
						}
					}
				}
			}
			faces.remove(t);

			if (faces.size() == 0) {
				System.out.println("ERROR");
				break;
			}
		}
		// System.out.println(normal);

		// source:
		// http://allenchou.net/2013/12/game-physics-contact-generation-epa/
		Vector3f tangentA, tangentB;
		if (Math.abs(normal.x) >= 0.57735f)
			tangentA = new Vector3f(normal.y, -normal.x, 0);
		else
			tangentA = new Vector3f(0, normal.z, -normal.y);

		if (tangentA.length() > 0)
			tangentA.normalize();
		tangentB = VecMath.crossproduct(normal, tangentA);

		Vector3f negnormal = VecMath.negate(normal);
		// System.out.println(depth + "; " + normal);
		return new ContactManifold<Vector3f>(depth, normal,
				Sa.supportPoint(normal), Sb.supportPoint(negnormal),
				Sa.supportPointRelative(normal),
				Sb.supportPointRelative(normal), Sa.supportPointLocal(normal),
				Sb.supportPointLocal(negnormal), tangentA, tangentB);
	}

	/**
	 * Finds the closest feature to the origin on the Minkowski Difference.
	 */
	private Triangle findClosestTriangle(List<Triangle> faces) {
		Triangle closest = null;
		float distance = Float.MAX_VALUE;
		for (Triangle f : faces) {
			float dist = VecMath.dotproduct(f.normal, f.a);
			if (dist < distance) {
				closest = f;
				distance = dist;
				f.distance = distance;
			}
		}
		return closest;
	}

	/**
	 * Finds up to three triangles adjacent to t.
	 * 
	 * @param t
	 * @param faces
	 * @return
	 */
	private Triangle[] findAdjacentTriangles(Triangle t, List<Triangle> faces) {
		Triangle[] result = new Triangle[3];
		for (Triangle f : faces) {
			if (!f.equals(t)) {
				if (f.a.equals(t.b) && f.b.equals(t.a) || f.b.equals(t.b)
						&& f.c.equals(t.a) || f.c.equals(t.b)
						&& f.a.equals(t.a))
					result[0] = f;
				if (f.a.equals(t.c) && f.b.equals(t.b) || f.b.equals(t.c)
						&& f.c.equals(t.b) || f.c.equals(t.c)
						&& f.a.equals(t.b))
					result[1] = f;
				if (f.a.equals(t.a) && f.b.equals(t.c) || f.b.equals(t.a)
						&& f.c.equals(t.c) || f.c.equals(t.a)
						&& f.a.equals(t.c))
					result[2] = f;
			}
		}
		return result;
	}

	/**
	 * Finds the vertex in b that is not ax or ay.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private Vector3f findTheD(Vector3f ax, Vector3f ay, Triangle b) {
		if (!b.a.equals(ax) && !b.a.equals(ay))
			return b.a;
		if (!b.b.equals(ax) && !b.b.equals(ay))
			return b.b;
		return b.c;
	}

	private boolean isOriginInsideTriangleArea(Triangle t) {
		if (VecMath.dotproduct(
				VecMath.crossproduct(VecMath.subtraction(t.b, t.a), t.normal),
				VecMath.negate(t.a)) <= 0) {
			if (VecMath.dotproduct(VecMath.crossproduct(
					VecMath.subtraction(t.c, t.b), t.normal), VecMath
					.negate(t.b)) <= 0) {
				if (VecMath.dotproduct(VecMath.crossproduct(
						VecMath.subtraction(t.a, t.c), t.normal), VecMath
						.negate(t.c)) <= 0) {
					return true;
				}
			}
		}
		return false;
	}

	// private List<Triangle> removeSeenTriangles(List<Triangle> faces, Vector3f
	// p) {
	// List<Triangle> result = new ArrayList<Triangle>();
	// for(Triangle t : faces) {
	// if(VecMath.dotproduct(t.normal, p) > 0) {
	// result.add(new Triangle(t.a, t.b, p));
	// result.add(new Triangle(t.b, t.c, p));
	// result.add(new Triangle(t.c, t.a, p));
	// // faces.remove(f);
	// System.out.println("removed!");
	// } else {
	// result.add(t);
	// }
	// }
	// return result;
	// }

	private Vector3f support(SupportMap<Vector3f> Sa, SupportMap<Vector3f> Sb,
			Vector3f dir) {
		return VecMath.subtraction(Sa.supportPoint(dir),
				Sb.supportPoint(VecMath.negate(dir)));
	}
}
