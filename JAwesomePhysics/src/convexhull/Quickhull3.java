package convexhull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import math.VecMath;
import vector.Vector3f;
import collisionshape.ConvexShape;

public class Quickhull3 {
	public static ConvexShape computeConvexHull(List<Vector3f> points,
			int iterations) {
		// source: http://thomasdiewald.com/blog/?p=1888

		// TODO: COMPLETELY REWRITE / COPY FROM CONVEXHULLDEBUGGER DELETE THIS
		// SHIT HERE

		// Initial Phase
		// 1. Create initial simplex
		Vector3f[] EPs = getExtremePoints(points);

		// 1.1 Get two most distant points as baseline of base triangle
		int a = -1, b = -1;
		float distance = -1;
		for (int i = 0; i < EPs.length - 1; i++) {
			for (int j = i; j < EPs.length; j++) {
				float dist = (float) VecMath.subtraction(EPs[i], EPs[j])
						.lengthSquared();
				if (dist > distance) {
					distance = dist;
					a = i;
					b = j;
				}
			}
		}
		Vector3f A = EPs[a];
		Vector3f B = EPs[b];

		// 1.2 Get furthest point from base line to create base triangle
		Vector3f AB = VecMath.subtraction(B, A);
		AB.normalize();
		int c = -1;
		distance = -1;
		for (int i = 0; i < EPs.length; i++) {
			if (i != a && i != b) {
				Vector3f IA = VecMath.subtraction(EPs[i], A);
				float dist = (float) VecMath.crossproduct(AB, IA)
						.lengthSquared();
				if (dist > distance) {
					distance = dist;
					c = i;
				}
			}
		}
		Vector3f C = EPs[c];

		// 1.3 Get furthest point from base triangle
		Vector3f ABCnormal = VecMath.computeNormal(A, B, C);
		boolean keepOrientation = true;
		int d = -1;
		distance = 0;
		for (int i = 0; i < EPs.length; i++) {
			if (i != a && i != b && i != c) {
				Vector3f IA = VecMath.subtraction(EPs[i], A);
				float dist = VecMath.dotproduct(IA, ABCnormal);
				if (Math.abs(dist) > distance) {
					keepOrientation = (dist < 0); // TODO: check
					distance = Math.abs(dist);
					d = i;
				}
			}
		}
		Vector3f D = EPs[d];

		// 2. Assign points to faces
		// 2.1 Create faces and add them to queue
		LinkedList<Triangle> faces = new LinkedList<Triangle>();
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		HashMap<Integer, Integer[]> adjacentsMap = new HashMap<Integer, Integer[]>();
		vertices.add(A);
		vertices.add(B);
		vertices.add(C);
		vertices.add(D);
		if (keepOrientation) {
			faces.add(new Triangle(0, 1, 2, VecMath.computeNormal(A, B, C)));
			faces.add(new Triangle(0, 3, 1, VecMath.computeNormal(A, D, B)));
			faces.add(new Triangle(3, 2, 1, VecMath.computeNormal(D, C, B)));
			faces.add(new Triangle(0, 2, 3, VecMath.computeNormal(A, C, D)));
			adjacentsMap.put(0, new Integer[] { 1, 2, 3 });
			adjacentsMap.put(1, new Integer[] { 2, 0, 3 });
			adjacentsMap.put(2, new Integer[] { 0, 1, 3 });
			adjacentsMap.put(3, new Integer[] { 1, 0, 2 });
		} else {
			faces.add(new Triangle(0, 2, 1, VecMath.computeNormal(A, C, B)));
			faces.add(new Triangle(0, 1, 3, VecMath.computeNormal(A, B, D)));
			faces.add(new Triangle(0, 3, 2, VecMath.computeNormal(A, D, C)));
			faces.add(new Triangle(3, 1, 2, VecMath.computeNormal(D, B, C)));
			adjacentsMap.put(0, new Integer[] { 2, 1, 3 });
			adjacentsMap.put(1, new Integer[] { 0, 2, 3 });
			adjacentsMap.put(2, new Integer[] { 1, 0, 3 });
			adjacentsMap.put(3, new Integer[] { 0, 1, 2 });
		}
		// 2.2 Find lighting points for initial triangles
		System.out.println("Pointcount of quickhull: " + points.size());
		LinkedList<List<Vector3f>> listsOfFacePoints = new LinkedList<List<Vector3f>>();
		for (int i = 0; i < faces.size(); i++) {
			listsOfFacePoints.add(getLightPoints(faces.get(i), points));
		}
		// 3. Push the 4 faces on the stack (done above)

		// Iteration Phase

		// 1. If stack not empty Pop Face from Stack
		Triangle t;
		while ((t = faces.poll()) != null) {
			System.out.println("a " + t.a + "; " + t.b + "; " + t.c + "; "
					+ t.normal);
			// 2. Get most distant point of the face's point set
			Vector3f furthestPoint = null;
			int furthestPointID = -1;
			A = vertices.get(t.a);
			List<Vector3f> facepoints = listsOfFacePoints.poll();
			distance = -1;
			for (int i = 0; i < facepoints.size(); i++) {
				Vector3f P = facepoints.get(i);
				Vector3f PA = VecMath.subtraction(A, P);
				float dist = VecMath.dotproduct(PA, t.normal);
				if (Math.abs(dist) > distance) {
					distance = dist;
					furthestPoint = P;
					furthestPointID = i;
				}
			}
			System.out.println(furthestPointID);
			if (furthestPointID != -1) {
				facepoints.remove(furthestPointID);
				vertices.add(furthestPoint);
				furthestPointID = vertices.size() - 1;

				// 3. Find all faces that can be seen from this point
				HashSet<Integer> lightFaceVertices = new HashSet<Integer>();
				lightFaceVertices.add(t.a);
				lightFaceVertices.add(t.b);
				lightFaceVertices.add(t.c);
				List<Vector3f> lightFacesPoints = new ArrayList<Vector3f>();
				for (int i = faces.size() - 1; i >= 0; i--) {
					Triangle tri = faces.get(i);
					Vector3f triP = VecMath.subtraction(furthestPoint,
							vertices.get(tri.a));
					if (VecMath.dotproduct(tri.normal, triP) >= 0) {
						faces.remove(i);
						System.out.println("Light face vertices added");
						lightFaceVertices.add(tri.a);
						lightFaceVertices.add(tri.b);
						lightFaceVertices.add(tri.c);
						lightFacesPoints.addAll(listsOfFacePoints.remove(i));
					}
				}

				// 4. Extract horizon edges of light-faces and extrude to Point
				// TODO: LÖSUNG IM CHAT MIT FLO
				// 4.0 Remove all vertices that are only connected to
				// lightFaceVertices
				Iterator<Integer> iter = lightFaceVertices.iterator();
				List<Integer> toRemove = new ArrayList<Integer>();
				for (int i = 0; i < lightFaceVertices.size(); i++) {
					int vert = iter.next(); // TODO: check
					System.out.println("vert + " + i + "; " + vert + "; "
							+ adjacentsMap);
					boolean foundNonLight = false;
					for (Integer adjacent : adjacentsMap.get(vert)) {
						if (!lightFaceVertices.contains(adjacent)) {
							foundNonLight = true;
						}
					}
					if (!foundNonLight) {
						toRemove.add(vert);
						for (Integer adj : adjacentsMap.get(vert)) {
							adjacentsMap.put(
									adj,
									removeArrayEntry(adjacentsMap.get(adj),
											vert));
						}
					}
				}
				for (Integer i : toRemove) {
					lightFaceVertices.remove(i);
					vertices.remove(i);
				}
				// 4.1 Get vertices on border between lighted and unlighted
				// triangles
				HashSet<Integer> vertsOnEdge = new HashSet<Integer>();
				System.out.println("Light Face Vertices: ");
				for (Integer vert : lightFaceVertices) {
					System.out.println("LFV: " + vert);
					for (Integer adjacent : adjacentsMap.get(vert)) {
						if (!lightFaceVertices.contains(adjacent)) {
							// vert is on edge
							vertsOnEdge.add(vert);
							break;
						}
					}
				}
				System.out.println("Edges!");
				for (Integer i : vertsOnEdge) {
					System.out.println("Edge: " + i);
				}
				// 4.2 Get edges on border
				int lastVert = -1;
				int currentVert = vertsOnEdge.iterator().next();
				List<Integer> edge = new ArrayList<Integer>();
				for (int i = 0; i < vertsOnEdge.size(); i++) {
					edge.add(currentVert);
					Integer[] adjs = adjacentsMap.get(currentVert);
					for (int j = 0; j < adjs.length; j++) {
						if (vertsOnEdge.contains(adjs[j])
								&& adjs[j] != lastVert) {
							lastVert = currentVert;
							currentVert = adjs[j];
							break;
						}
					}
				}

				// 4.3 Stitch holes using edge
				// TODO: fix adjacents map, fix triangle list
				List<Triangle> newLightFaces = new ArrayList<Triangle>();
				Integer[] furthestPointNeighbours = new Integer[edge.size()];
				for (int i = 0; i < edge.size(); i++) {
					int vertIDa, vertIDb;
					if (i < edge.size() - 1) {
						vertIDa = edge.get(i);
						vertIDb = edge.get(i + 1);
					} else {
						vertIDa = edge.get(i);
						vertIDb = edge.get(0);
					}
					Triangle stichTriangle = new Triangle(vertIDa, vertIDb,
							furthestPointID, VecMath.computeNormal(
									vertices.get(vertIDa),
									vertices.get(vertIDb), furthestPoint));
					faces.add(stichTriangle);
					newLightFaces.add(stichTriangle);

					// Upade adjacents map
					adjacentsMap
							.put(vertIDa,
									addArrayEntry(adjacentsMap.get(i),
											furthestPointID));
					furthestPointNeighbours[i] = vertIDa;
				}
				// Update adjacents map for new point
				adjacentsMap.put(furthestPointID, furthestPointNeighbours);

				// 5. Assign all points of all light-faces to the new created
				// faces
				for (Triangle tri : newLightFaces) {
					listsOfFacePoints
							.add(getLightPoints(tri, lightFacesPoints));
				}

				// 6. Push new created faces on the stack and start at (1))
			}
		}

		ConvexShape shape = new ConvexShape(0, 0, 0, vertices, adjacentsMap);
		return shape;
	}

	private static Integer[] removeArrayEntry(Integer[] array, int remove) {
		Integer[] result = new Integer[array.length - 1];
		int a = 0;
		for (Integer i : array) {
			if (i != remove) {
				result[a] = i;
				a++;
			}
		}
		return result;
	}

	private static Integer[] addArrayEntry(Integer[] array, int add) {
		Integer[] result = new Integer[array.length + 1];
		for (int a = 0; a < array.length; a++) {
			result[a] = array[a];
		}
		result[array.length] = add;
		return result;
	}

	private static Vector3f[] getExtremePoints(List<Vector3f> points) {
		Vector3f[] result = new Vector3f[6];

		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float minZ = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		float maxZ = Float.MIN_VALUE;

		for (Vector3f p : points) {
			if (p.x < minX) {
				result[0] = p;
				minX = p.x;
			}
			if (p.y < minY) {
				result[1] = p;
				minY = p.y;
			}
			if (p.z < minZ) {
				result[2] = p;
				minZ = p.z;
			}
			if (p.x > maxX) {
				result[3] = p;
				maxX = p.x;
			}
			if (p.y > maxY) {
				result[4] = p;
				maxY = p.y;
			}
			if (p.z > maxZ) {
				result[5] = p;
				maxZ = p.z;
			}
		}

		return result;
	}

	private static List<Vector3f> getLightPoints(Triangle triangle,
			List<Vector3f> points) {
		LinkedList<Vector3f> result = new LinkedList<Vector3f>();
		for (int i = points.size() - 1; i >= 0; i--) {
			Vector3f p = points.get(i);
			if (VecMath.dotproduct(triangle.normal, p) > 0) {
				result.add(p);
				points.remove(i);
			}
		}
		return result;
	}

	private static class Triangle {
		int a, b, c;
		Vector3f normal;

		public Triangle(int a, int b, int c, Vector3f normal) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.normal = normal;
		}
	}
}