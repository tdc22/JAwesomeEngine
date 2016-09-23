package convexhull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import collisionshape.ConvexShape;
import math.VecMath;
import utils.VectorConstants;
import vector.Vector3f;

public class Quickhull {

	public static class Triangle {
		public int a, b, c;

		public Triangle(int a, int b, int c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}

	public static ConvexShape computeConvexHull(List<Vector3f> points, int iterations) {
		List<Triangle> faces = new ArrayList<Triangle>();
		HashMap<Integer, Integer[]> adjacentsMap = new HashMap<Integer, Integer[]>();
		List<Vector3f> vertices = computeConvexHullVertices(points, faces, adjacentsMap, iterations);
//		List<Integer> adjs = new ArrayList<Integer>();
//		for (int i = 0; i < vertices.size(); i++) {
//			Vector3f v = vertices.get(i);
//			for (Triangle f : faces) {
//				if (f.a.equals(v)) {
//					int b = vertices.indexOf(f.b);
//					if (!adjs.contains(b))
//						adjs.add(b);
//					int c = vertices.indexOf(f.c);
//					if (!adjs.contains(c))
//						adjs.add(c);
//				} else if (f.b.equals(v)) {
//					int a = vertices.indexOf(f.a);
//					if (!adjs.contains(a))
//						adjs.add(a);
//					int c = vertices.indexOf(f.c);
//					if (!adjs.contains(c))
//						adjs.add(c);
//				} else if (f.c.equals(v)) {
//					int a = vertices.indexOf(f.a);
//					if (!adjs.contains(a))
//						adjs.add(a);
//					int b = vertices.indexOf(f.b);
//					if (!adjs.contains(b))
//						adjs.add(b);
//				}
//			}
//			Integer[] adjacents = new Integer[adjs.size()];
//			adjs.toArray(adjacents);
//			adjacentsMap.put(i, adjacents);
//			adjs.clear();
//		}

//		System.out.println("Stats: " + faces.size() + "; " + vertices.size() + "; " + adjacentsMap.size());
		ConvexShape shape = new ConvexShape(0, 0, 0, vertices, adjacentsMap);
		return shape;
	}

	public static List<Vector3f> computeConvexHullVertices(List<Vector3f> points, int iterations) {
		return computeConvexHullVertices(points, new ArrayList<Triangle>(), new HashMap<Integer, Integer[]>(), iterations);
	}

	public static List<Vector3f> computeConvexHullVertices(List<Vector3f> points, List<Triangle> faces, HashMap<Integer, Integer[]> adjacentsMap,
			int iterations) {
		points = new ArrayList<Vector3f>(points);
		Set<Vector3f> verticesSet = new HashSet<Vector3f>();
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector3f> facenormals = new ArrayList<Vector3f>();
		if (points.size() > 0) {
			int a = getFurthestPoint(VectorConstants.AXIS_X, points);
			Vector3f A = points.remove(a);

			int b = getFurthestPoint(VectorConstants.AXIS_Y, points);
			Vector3f B = points.remove(b);

			int c = getFurthestPoint(VectorConstants.AXIS_Z, points);
			Vector3f C = points.remove(c);

			int pos = vertices.size();
			a = insertVertex(A, vertices, verticesSet, pos);
			b = insertVertex(B, vertices, verticesSet, pos + 1);
			c = insertVertex(C, vertices, verticesSet, pos + 2);
			
			Triangle base = new Triangle(a, b, c);
			Vector3f basenormal = VecMath.computeNormal(A, B, C);
			if(basenormal.lengthSquared() > 0)
				basenormal.normalize();
			
			int d = getFurthestPoint(base, basenormal, points, vertices);
			Vector3f D = points.remove(d);
			d = insertVertex(D, vertices, verticesSet, vertices.size());

			if (VecMath.dotproduct(basenormal, VecMath.subtraction(A, D)) > 0) {
				faces.add(base);
				faces.add(new Triangle(a, d, b));
				faces.add(new Triangle(d, c, b));
				faces.add(new Triangle(a, c, d));
				facenormals.add(basenormal);
				facenormals.add(computeNormal(A, D, B));
				facenormals.add(computeNormal(A, C, D));
				facenormals.add(computeNormal(D, C, B));
				adjacentsMap.put(0, new Integer[]{1, 2, 3});
				adjacentsMap.put(1, new Integer[]{3, 2, 0});
				adjacentsMap.put(2, new Integer[]{3, 0, 1});
				adjacentsMap.put(3, new Integer[]{0, 2, 1});
			} else {
				faces.add(new Triangle(a, c, b));
				faces.add(new Triangle(a, b, d));
				faces.add(new Triangle(a, d, c));
				faces.add(new Triangle(d, b, c));
				facenormals.add(computeNormal(A, C, B));
				facenormals.add(computeNormal(A, B, D));
				facenormals.add(computeNormal(A, D, C));
				facenormals.add(computeNormal(D, B, C));
				adjacentsMap.put(0, new Integer[]{2, 3, 1});
				adjacentsMap.put(1, new Integer[]{0, 3, 2});
				adjacentsMap.put(2, new Integer[]{1, 3, 1});
				adjacentsMap.put(3, new Integer[]{1, 0, 2});
			}

			// starting tetrahedron created

			int currT = 0;
			int p;
			while (currT < faces.size() && iterations > 0) {
				Triangle t = faces.get(currT);
				Vector3f tnormal = facenormals.get(currT);
				p = getFurthestPointDirection(t, tnormal, points, vertices);
				if (p != -1) {
					System.out.println("CurrT " + currT);
					Integer[] tAdjs = adjacentsMap.get(currT);
//					System.out.println(tAdjs);
					Vector3f P = points.remove(p);
					p = insertVertex(P, vertices, verticesSet, vertices.size());
					
					faces.add(new Triangle(t.a, t.b, p));
					faces.add(new Triangle(t.b, t.c, p));
					faces.add(new Triangle(t.c, t.a, p));
					facenormals.add(computeNormal(vertices.get(t.a), vertices.get(t.b), vertices.get(p)));
					facenormals.add(computeNormal(vertices.get(t.b), vertices.get(t.c), vertices.get(p)));
					facenormals.add(computeNormal(vertices.get(t.c), vertices.get(t.a), vertices.get(p)));
					
					// TODO: (DONE) add after remove? No! Check convex later, so special case not to remove double adjs?
					Integer[] adjsA = adjacentsMap.get(t.a);
					Integer[] adjsB = adjacentsMap.get(t.b);
					Integer[] adjsC = adjacentsMap.get(t.c);
					adjsA[adjsA.length-1] = p;
					adjsB[adjsB.length-1] = p;
					adjsC[adjsC.length-1] = p;
					adjacentsMap.put(t.a, adjsA);
					adjacentsMap.put(t.b, adjsB);
					adjacentsMap.put(t.c, adjsC);
					adjacentsMap.put(p, new Integer[]{a, b, c});
					
					faces.remove(currT);
					facenormals.remove(currT);
					
					System.out.println("----------------OUTPUT EVERYTHING----------------");
					System.out.println(t.a + "; " + t.b + "; " + t.c + "; " + p);
					System.out.println("-----------------END EVERYTHING------------------");
					
					
					// check convex
					for (int i = 0; i < faces.size(); i++) {
						Triangle f = faces.get(i);
						Vector3f fa = vertices.get(f.a);
						Vector3f fb = vertices.get(f.b);
						Vector3f fc = vertices.get(f.c);
//						Integer[] adjs = adjacentsMap.get(i); FALSCH, vertex, nicht facebasiert
						int adjA = findTriangle(f.a, f.b, f, adjacentsMap);
						int adjB = findTriangle(f.b, f.c, f, adjacentsMap);
						int adjC = findTriangle(f.c, f.a, f, adjacentsMap);
						Vector3f adjANormal = null;
						if(adjA != -1)
							adjANormal = computeNormal(fa, fb, vertices.get(adjA));
						Vector3f adjBNormal = null;
						if(adjB != -1)
							adjBNormal = computeNormal(fb, fc, vertices.get(adjB));
						Vector3f adjCNormal = null;
						if(adjC != -1)
							adjCNormal = computeNormal(fc, fa, vertices.get(adjC));
//						if(iterations == 1)
//							System.out.println(faces.size() + "; " + adjs[1] + "; " + f.a + "; " + f.b + "; " + f.c + "; " + adjs[1].a + "; " + adjs[1].b + "; " + adjs[1].c + "; " + adjs[1].normal);
//						System.out.println("RAWR " + fa + "; " + fb + "; " + fc + "; " + adjs);
						if (adjA != -1 && VecMath.dotproduct(VecMath.subtraction(fc, fa), adjANormal) > 0) {
							if (iterations == 1)
								System.out.println("DA");
							int adjD = findTheD(f.a, f.b, faces.get(adjA));
							faces.add(new Triangle(f.c, f.a, adjD));
							faces.add(new Triangle(f.b, f.c, adjD));
							facenormals.add(computeNormal(fc, fa, vertices.get(adjD)));
							facenormals.add(computeNormal(fb, fc, vertices.get(adjD)));
							faces.remove(i);
							faces.remove(adjA);
							boolean triangleAJustChanged = checkTriangles(f.c, f.a, adjD, t.a, p, t.c, t.b); // TODO: move inside of r-loop (performance!)
							boolean triangleBJustChanged = checkTriangles(f.b, f.c, adjD, t.a, t.b, p, t.c);
							System.out.println(f.b + "; " + f.c + "; " + adjD + "; " + t.a + "; " + t.b + "; " + t.c + "; " + p);
							System.out.println("changed a " + triangleAJustChanged); // TODO: ERROR IN CHECKTRIANGLE
							System.out.println("changed b " + triangleBJustChanged);
							Integer[] adjsOfA = adjacentsMap.get(f.a);
							Integer[] adjsOfB = adjacentsMap.get(f.b);
							Integer[] adjsOfC = adjacentsMap.get(f.c);
							Integer[] adjsOfD = adjacentsMap.get(adjD);
							Integer[] newAdjsA = new Integer[adjsOfA.length - 1];
							Integer[] newAdjsB = new Integer[adjsOfB.length - 1];
							Integer[] newAdjsC = Arrays.copyOf(adjsOfC, adjsOfC.length + 1);
							Integer[] newAdjsD = Arrays.copyOf(adjsOfD, adjsOfD.length + 1);
							int x = 0;
							for(int r = 0; r < adjsOfA.length; r++) {
								if(adjsOfA[r] != f.b || (triangleAJustChanged && r == adjsOfA.length - 1)) {
									newAdjsA[x] = adjsOfA[r];
									x++;
								}
							}
							x = 0;
							for(int r = 0; r < adjsOfB.length; r++) {
								System.out.println(r + ".. " + adjsOfB.length + ".. " + newAdjsB.length + ".. " + triangleBJustChanged);
								if(adjsOfB[r] != f.a || (triangleBJustChanged && r == adjsOfB.length - 1)) {
									newAdjsB[x] = adjsOfB[r];
									x++;
								}
							}
							newAdjsC[newAdjsC.length-1] = adjD;
							newAdjsD[newAdjsD.length-1] = f.c;
							System.out.println("L1 " + f.a + "; " + f.b + "; " + f.c + "; " + adjD);
							System.out.println("A " + newAdjsA);
							outputArray(adjsOfA);
							outputArray(newAdjsA);
							System.out.println("B " + newAdjsB);
							outputArray(adjsOfB);
							outputArray(newAdjsB);
							System.out.println("C " + newAdjsC);
							outputArray(adjsOfC);
							outputArray(newAdjsC);
							System.out.println("D " + newAdjsD);
							outputArray(adjsOfD);
							outputArray(newAdjsD);
							adjacentsMap.put(f.a, newAdjsA);
							adjacentsMap.put(f.b, newAdjsB);
							adjacentsMap.put(f.c, newAdjsC);
							adjacentsMap.put(adjD, newAdjsD);
							i--;
						} else if (adjB != -1
								&& VecMath.dotproduct(VecMath.subtraction(fa, fb), adjBNormal) > 0) {
							if (iterations == 1)
								System.out.println("DB");
							int adjD = findTheD(f.b, f.c, faces.get(adjB));
							faces.add(new Triangle(f.a, f.b, adjD));
							faces.add(new Triangle(f.c, f.a, adjD));
							facenormals.add(computeNormal(fa, fb, vertices.get(adjD)));
							facenormals.add(computeNormal(fc, fa, vertices.get(adjD)));
							faces.remove(i);
							faces.remove(adjB);
							boolean triangleBJustChanged = checkTriangles(f.a, f.b, adjD, t.a, t.b, t.c, p);
							boolean triangleCJustChanged = checkTriangles(f.c, f.a, adjD, t.a, t.b, t.c, p);
							Integer[] adjsOfA = adjacentsMap.get(f.a);
							Integer[] adjsOfB = adjacentsMap.get(f.b);
							Integer[] adjsOfC = adjacentsMap.get(f.c);
							Integer[] adjsOfD = adjacentsMap.get(adjD);
							Integer[] newAdjsA = Arrays.copyOf(adjsOfA, adjsOfA.length + 1);
							Integer[] newAdjsB = new Integer[adjsOfB.length - 1];
							Integer[] newAdjsC = new Integer[adjsOfC.length - 1];
							Integer[] newAdjsD = Arrays.copyOf(adjsOfD, adjsOfD.length + 1);
							newAdjsA[newAdjsA.length-1] = adjD;
							int x = 0;
							for(int r = 0; r < adjsOfB.length; r++) {
								if(adjsOfB[r] != f.c || (triangleBJustChanged && r == adjsOfB.length - 1)) {
									newAdjsB[x] = adjsOfB[r];
									x++;
								}
							}
							x = 0;
							for(int r = 0; r < adjsOfC.length; r++) {
								if(adjsOfC[r] != f.b || (triangleCJustChanged && r == adjsOfC.length - 1)) {
									newAdjsC[x] = adjsOfC[r];
									x++;
								}
							}
							newAdjsD[newAdjsD.length-1] = f.a;
							System.out.println("L2 " + f.a + "; " + f.b + "; " + f.c + "; " + adjD);
							System.out.println("A " + newAdjsA);
							outputArray(newAdjsA);
							System.out.println("B " + newAdjsB);
							outputArray(newAdjsB);
							System.out.println("C " + newAdjsC);
							outputArray(newAdjsC);
							System.out.println("D " + newAdjsD);
							outputArray(newAdjsD);
							adjacentsMap.put(f.a, newAdjsA);
							adjacentsMap.put(f.b, newAdjsB);
							adjacentsMap.put(f.c, newAdjsC);
							adjacentsMap.put(adjD, newAdjsD);
							i--;
						} else if (adjC != -1
								&& VecMath.dotproduct(VecMath.subtraction(fb, fc), adjCNormal) > 0) {
							if (iterations == 1)
								System.out.println("DC");
							int adjD = findTheD(f.c, f.a, faces.get(adjC));
							faces.add(new Triangle(f.b, f.c, adjD));
							faces.add(new Triangle(f.a, f.b, adjD));
							facenormals.add(computeNormal(fb, fc, vertices.get(adjD)));
							facenormals.add(computeNormal(fa, fb, vertices.get(adjD)));
							faces.remove(i);
							faces.remove(adjC);
							System.out.println(f.a+ "; " + f.b+ "; " + adjD+ "; " + t.a+ "; " + t.b+ "; " + t.c+ "; " + p);
							boolean triangleCJustChanged = checkTriangles(f.b, f.c, adjD, t.a, t.b, t.c, p);
							boolean triangleAJustChanged = checkTriangles(f.a, f.b, adjD, t.a, t.b, t.c, p);
							System.out.println("changed: " + triangleAJustChanged);
							Integer[] adjsOfA = adjacentsMap.get(f.a);
							Integer[] adjsOfB = adjacentsMap.get(f.b);
							Integer[] adjsOfC = adjacentsMap.get(f.c);
							Integer[] adjsOfD = adjacentsMap.get(adjD);
							Integer[] newAdjsA = new Integer[adjsOfA.length - 1];
							Integer[] newAdjsB = Arrays.copyOf(adjsOfB, adjsOfB.length + 1);
							Integer[] newAdjsC = new Integer[adjsOfC.length - 1];
							Integer[] newAdjsD = Arrays.copyOf(adjsOfD, adjsOfD.length + 1);
							int x = 0;
							for(int r = 0; r < adjsOfA.length; r++) {
								if(adjsOfA[r] != f.c || (triangleAJustChanged && r == adjsOfA.length - 1)) {
									newAdjsA[x] = adjsOfA[r];
									x++;
								}
							}
							newAdjsB[newAdjsB.length-1] = adjD;
							x = 0;
							for(int r = 0; r < adjsOfC.length; r++) {
								if(adjsOfC[r] != f.a || (triangleCJustChanged && r == adjsOfC.length - 1)) {
									newAdjsC[x] = adjsOfC[r];
									x++;
								}
							}
							newAdjsD[newAdjsD.length-1] = f.b;
							System.out.println("L3 " + f.a + "; " + f.b + "; " + f.c + "; " + adjD);
							System.out.println("A " + newAdjsA);
							outputArray(adjsOfA);
							outputArray(newAdjsA);
							System.out.println("B " + newAdjsB);
							outputArray(adjsOfB);
							outputArray(newAdjsB);
							System.out.println("C " + newAdjsC);
							outputArray(adjsOfC);
							outputArray(newAdjsC);
							System.out.println("D " + newAdjsD);
							outputArray(adjsOfD);
							outputArray(newAdjsD);
							adjacentsMap.put(f.a, newAdjsA);
							adjacentsMap.put(f.b, newAdjsB);
							adjacentsMap.put(f.c, newAdjsC);
							adjacentsMap.put(adjD, newAdjsD);
							i--;
						}
					}
				} else {
					currT++;
				}
				iterations--;
			}

//			System.out.println("Num faces: " + faces.size());
		}

		return vertices;
	}
	
	private static void outputArray(Integer[] array) {
		for(Integer i : array)
			System.out.print(i + ", ");
		System.out.println(";");
	}
	
	private static Vector3f computeNormal(Vector3f a, Vector3f b, Vector3f c) {
		Vector3f result = VecMath.computeNormal(a, b, c);
		if(result.lengthSquared() > 0) {
			result.normalize();
		}
		return result;
	}
	
	private static boolean checkTriangles(float aa, float ab, float ac, float qa, float qb, float qc, float qd) {
		return (checkTriangle(aa, ab, ac, qa, qb, qd) || 
				checkTriangle(aa, ab, ac, qb, qc, qd) || 
				checkTriangle(aa, ab, ac, qc, qa, qd));
	}
	
	private static boolean checkTriangle(float aa, float ab, float ac, float ba, float bb, float bc) {
		return ((aa == ba && ab == bb && ac == bc) || (aa == bb && ab == bc && ac == ba) || (aa == bc && ab == ba && ac == bb));
	}
	
	private static int insertVertex(Vector3f v, List<Vector3f> vertices, Set<Vector3f> verticesSet, int pos) {
		if(!verticesSet.contains(v)) {
			vertices.add(v);
			verticesSet.add(v);
			return pos;
		}
		else {
			return vertices.indexOf(v);
		}
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

	private static int getFurthestPoint(Triangle t, Vector3f normal, List<Vector3f> points, List<Vector3f> vertices) {
		float distance = 0;
		int pointID = -1;
		Vector3f A = vertices.get(t.a);
		for (int i = 0; i < points.size(); i++) {
			float dist = Math.abs(VecMath.dotproduct(normal, VecMath.subtraction(points.get(i), A)));
			if (dist > distance) {
				distance = dist;
				pointID = i;
			}
		}
		return pointID;
	}

	private static int getFurthestPointDirection(Triangle t, Vector3f normal, List<Vector3f> points, List<Vector3f> vertices) {
		float distance = 0;
		int pointID = -1;
		Vector3f A = vertices.get(t.a);
		for (int i = 0; i < points.size(); i++) {
			float dist = VecMath.dotproduct(normal, VecMath.subtraction(points.get(i), A));
			if (dist > distance) {
				distance = dist;
				pointID = i;
			}
		}
		return pointID;
	}

//	/**
//	 * Finds up to three triangles adjacent to t.
//	 * 
//	 * @param t
//	 * @param faces
//	 * @return
//	 */
//	private static Triangle[] findAdjacentTriangles(Triangle t, List<Triangle> faces, boolean last) {
//		Triangle[] result = new Triangle[3];
//		int i = 0;
//		for (Triangle f : faces) {
//			if (!f.equals(t)) {
//				if (f.a.equals(t.b) && f.b.equals(t.a) || f.b.equals(t.b) && f.c.equals(t.a)
//						|| f.c.equals(t.b) && f.a.equals(t.a)){
//					result[0] = f;if(last)System.out.println("A" + i + " " + f.a + "; " + f.b + "; " + f.c + "; " + t.a + "; " + t.b + "; " + t.c);}
//				if (f.a.equals(t.c) && f.b.equals(t.b) || f.b.equals(t.c) && f.c.equals(t.b)
//						|| f.c.equals(t.c) && f.a.equals(t.b)){
//					result[1] = f;if(last)System.out.println("B" + i + " " + f.a + "; " + f.b + "; " + f.c + "; " + t.a + "; " + t.b + "; " + t.c);}
//				if (f.a.equals(t.a) && f.b.equals(t.c) || f.b.equals(t.a) && f.c.equals(t.c)
//						|| f.c.equals(t.a) && f.a.equals(t.c)){
//					result[2] = f;if(last)System.out.println("C" + i + " " + f.a + "; " + f.b + "; " + f.c + "; " + t.a + "; " + t.b + "; " + t.c);}
//			}
//			i++;
//		}
//		return result;
//	}

	/**
	 * Finds the vertex in b that is not ax or ay.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static int findTheD(int ax, int ay, Triangle b) {
		if (b.a != ax && b.a != ay)
			return b.a;
		if (b.b != ax && b.b != ay)
			return b.b;
		return b.c;
	}
	
	private static int findTriangle(int ax, int ay, Triangle triangle, HashMap<Integer, Integer[]> adjacentMap) {
		for(int i = 0; i < 3; i++) {
			int adj = 0;
			switch(i) {
			case 0: adj = triangle.a; break;
			case 1: adj = triangle.b; break;
			case 2: adj = triangle.c; break;
			}
			
			if(adj == ax || adj == ay) {
				int tofind = 0;
				if(adj == ax) tofind = ay;
				else tofind = ax;
				
				Integer[] adjAdj = adjacentMap.get(adj);
 				for(int j = 0; j < adjAdj.length; j++) {
					if(adjAdj[j] == tofind)
						return i;
				}
			}
		}
		return -1;
	}
}