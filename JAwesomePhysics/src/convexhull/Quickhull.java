package convexhull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.VecMath;
import vector.Vector3f;
import collisionshape.ConvexShape;

public class Quickhull {

	public static class Triangle {
		public Vector3f a, b, c, normal;

		public Triangle(Vector3f a, Vector3f b, Vector3f c) {
			this.a = a;
			this.b = b;
			this.c = c;
			normal = VecMath.computeNormal(a, b, c);
			normal.normalize();
		}
	}

	public static ConvexShape computeConvexHull(List<Vector3f> points) {
		points = new ArrayList<Vector3f>(points);
		List<Triangle> faces = new ArrayList<Triangle>();

		if (points.size() > 0) {
			int a = getFurthestPoint(new Vector3f(-1, 0, 0), points);
			Vector3f A = points.get(a);
			points.remove(a);

			int b = getFurthestPoint(new Vector3f(1, 0, 0), points);
			Vector3f B = points.get(b);
			points.remove(b);

			int c = getFurthestPoint(new Vector3f(0, -1, 0), points);
			Vector3f C = points.get(c);
			points.remove(c);

			Triangle base = new Triangle(A, B, C);
			int furthest = getFurthestPoint(base, points);
			Vector3f D = points.get(furthest);
			points.remove(furthest);

			if (VecMath.dotproduct(base.normal, VecMath.subtraction(A, D)) > 0) {
				faces.add(base);
				faces.add(new Triangle(A, D, B));
				faces.add(new Triangle(A, C, D));
				faces.add(new Triangle(D, C, B));
			} else {
				faces.add(new Triangle(A, C, B));
				faces.add(new Triangle(A, B, D));
				faces.add(new Triangle(A, D, C));
				faces.add(new Triangle(D, B, C));
			}

			// starting tetrahedron created

			int currT = 0;
			while (currT < faces.size()) {
				Triangle t = faces.get(currT);
				furthest = getFurthestPointDirection(t, points);
				if (furthest != -1) {
					Vector3f p = points.get(furthest);
					points.remove(furthest);

					Triangle[] adjacents = findAdjacentTriangles(t, faces);
					if (VecMath.dotproduct(VecMath.subtraction(p, t.a),
							adjacents[0].normal) > 0) {
						Vector3f adjD = findTheD(t.a, t.b, adjacents[0]);
						faces.add(new Triangle(p, t.a, adjD));
						faces.add(new Triangle(t.b, p, adjD));
						faces.remove(adjacents[0]);
					} else {
						faces.add(new Triangle(t.a, t.b, p));
					}

					if (VecMath.dotproduct(VecMath.subtraction(p, t.b),
							adjacents[1].normal) > 0) {
						Vector3f adjD = findTheD(t.b, t.c, adjacents[1]);
						faces.add(new Triangle(p, t.b, adjD));
						faces.add(new Triangle(t.c, p, adjD));
						faces.remove(adjacents[1]);
					} else {
						faces.add(new Triangle(t.b, t.c, p));
					}

					if (VecMath.dotproduct(VecMath.subtraction(p, t.c),
							adjacents[2].normal) > 0) {
						Vector3f adjD = findTheD(t.c, t.a, adjacents[2]);
						faces.add(new Triangle(p, t.c, adjD));
						faces.add(new Triangle(t.a, p, adjD));
						faces.remove(adjacents[2]);
					} else {
						faces.add(new Triangle(t.c, t.a, p));
					}

					faces.remove(t);

					// check convex
					for (int i = 0; i < faces.size(); i++) {
						Triangle f = faces.get(i);
						Triangle[] adjs = findAdjacentTriangles(f, faces);
						if (VecMath.dotproduct(VecMath.subtraction(f.c, f.a),
								adjs[0].normal) > 0) {
							Vector3f adjD = findTheD(f.a, f.b, adjs[0]);
							faces.add(new Triangle(f.c, f.a, adjD));
							faces.add(new Triangle(f.b, f.c, adjD));
							faces.remove(i);
							faces.remove(adjs[0]);
							i--;
						} else if (VecMath.dotproduct(
								VecMath.subtraction(f.a, f.b), adjs[1].normal) > 0) {
							Vector3f adjD = findTheD(f.b, f.c, adjs[1]);
							faces.add(new Triangle(f.a, f.b, adjD));
							faces.add(new Triangle(f.c, f.a, adjD));
							faces.remove(i);
							faces.remove(adjs[1]);
							i--;
						} else if (VecMath.dotproduct(
								VecMath.subtraction(f.b, f.c), adjs[2].normal) > 0) {
							Vector3f adjD = findTheD(f.c, f.a, adjs[2]);
							faces.add(new Triangle(f.b, f.c, adjD));
							faces.add(new Triangle(f.a, f.b, adjD));
							faces.remove(i);
							faces.remove(adjs[2]);
							i--;
						}
					}
				} else {
					currT++;
				}
			}

			System.out.println("Num faces: " + faces.size());
		}

		List<Vector3f> vertices = new ArrayList<Vector3f>();
		HashMap<Integer, Integer[]> adjacentsMap = new HashMap<Integer, Integer[]>();
		for (Triangle f : faces) {
			if (!vertices.contains(f.a))
				vertices.add(f.a);
			if (!vertices.contains(f.b))
				vertices.add(f.b);
			if (!vertices.contains(f.c))
				vertices.add(f.c);
		}
		List<Integer> adjs = new ArrayList<Integer>();
		for (int i = 0; i < vertices.size(); i++) {
			Vector3f v = vertices.get(i);
			for (Triangle f : faces) {
				if (f.a.equals(v)) {
					int b = vertices.indexOf(f.b);
					if (!adjs.contains(b))
						adjs.add(b);
					int c = vertices.indexOf(f.c);
					if (!adjs.contains(c))
						adjs.add(c);
				} else if (f.b.equals(v)) {
					int a = vertices.indexOf(f.a);
					if (!adjs.contains(a))
						adjs.add(a);
					int c = vertices.indexOf(f.c);
					if (!adjs.contains(c))
						adjs.add(c);
				} else if (f.c.equals(v)) {
					int a = vertices.indexOf(f.a);
					if (!adjs.contains(a))
						adjs.add(a);
					int b = vertices.indexOf(f.b);
					if (!adjs.contains(b))
						adjs.add(b);
				}
			}
			Integer[] adjacents = new Integer[adjs.size()];
			adjs.toArray(adjacents);
			adjacentsMap.put(i, adjacents);
			adjs.clear();
		}

		ConvexShape shape = new ConvexShape(0, 0, 0, vertices, adjacentsMap);
		return shape;
	}

	private static int getFurthestPoint(Vector3f dir, List<Vector3f> points) {
		float distance = 0;
		int pointID = -1;
		for (int i = 0; i < points.size(); i++) {
			float dist = Math.abs(VecMath.dotproduct(dir, points.get(i)));
			if (dist > distance) {
				distance = dist;
				pointID = i;
			}
		}
		return pointID;
	}

	private static int getFurthestPoint(Triangle t, List<Vector3f> points) {
		float distance = 0;
		int pointID = -1;
		for (int i = 0; i < points.size(); i++) {
			float dist = Math.abs(VecMath.dotproduct(t.normal,
					VecMath.subtraction(points.get(i), t.a)));
			if (dist > distance) {
				distance = dist;
				pointID = i;
			}
		}
		return pointID;
	}

	private static int getFurthestPointDirection(Triangle t,
			List<Vector3f> points) {
		float distance = 0;
		int pointID = -1;
		for (int i = 0; i < points.size(); i++) {
			float dist = VecMath.dotproduct(t.normal,
					VecMath.subtraction(points.get(i), t.a));
			if (dist > distance) {
				distance = dist;
				pointID = i;
			}
		}
		return pointID;
	}

	/**
	 * Finds up to three triangles adjacent to t.
	 * 
	 * @param t
	 * @param faces
	 * @return
	 */
	private static Triangle[] findAdjacentTriangles(Triangle t,
			List<Triangle> faces) {
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
	private static Vector3f findTheD(Vector3f ax, Vector3f ay, Triangle b) {
		if (!b.a.equals(ax) && !b.a.equals(ay))
			return b.a;
		if (!b.b.equals(ax) && !b.b.equals(ay))
			return b.b;
		return b.c;
	}
}
