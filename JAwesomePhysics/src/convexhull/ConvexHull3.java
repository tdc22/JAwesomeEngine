package convexhull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import math.VecMath;
import utils.Pair;
import vector.Vector3f;
import collisionshape.ConvexShape;

public class ConvexHull3 {
	public static List<Vector3f> computeConvexHullVertices(List<Vector3f> points) {
		return computeConvexHull(points).getVertices();
	}
	
	// TODO: allow for just a limited number of iterations!!!
	
	public static ConvexShape computeConvexHull(List<Vector3f> points) {
		initHull(points);
		
		int facesDone = 1;
		int faceIndex = faces.size() - facesDone;
		while(faceIndex >= 0) {
			if(step(faceIndex)) {
				facesDone++;
			}
			faceIndex = faces.size() - facesDone;
		}
		
		for(int i = vertices.size() - 1; i >= 0 && freeVertexPositions.size() > 0; i--) {
			int newPos = freeVertexPositions.remove(0);
			if(newPos < i) {
				Vector3f v = vertices.set(i, null);
				if(v != null) {
					for(Integer adj : adjacentsMap.get(i)) {
						ArrayList<Integer> adjAdjs = adjacentsMap.get((int) adj);
						for(int j = 0; j < adjAdjs.size(); j++) {
							if(adjAdjs.get(j) == i) {
								adjAdjs.set(j, newPos);
								break;
							}
						}
					}
					vertices.set(newPos, v);
					adjacentsMap.put(newPos, adjacentsMap.remove(i));
				}
				else {
					freeVertexPositions.add(0, newPos);
				}
			}
			else {
				i++;
			}
		}
		for(int i = vertices.size() - 1; i >= 0; i--) {
			Vector3f v = vertices.remove(i);
			if(v != null) {
				vertices.add(v);
				break;
			}
		}
		
		HashMap<Integer, Integer[]> resultAdjacentsMap = new HashMap<Integer, Integer[]>();
		for(Integer k : adjacentsMap.keySet()) {
			ArrayList<Integer> adjList = adjacentsMap.get(k);
			Integer[] adjArray = new Integer[adjList.size()];
			adjacentsMap.get(k).toArray(adjArray);
			resultAdjacentsMap.put(k, adjArray);
		}
		
		return new ConvexShape(0, 0, 0, vertices, resultAdjacentsMap);
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
	
	private static List<Triangle> faces;

	private static List<Vector3f> vertices;

	private static HashMap<Integer, ArrayList<Integer>> adjacentsMap;

	private static ArrayList<List<Vector3f>> listsOfFacePoints;

	private static HashMap<Pair<Integer, Integer>, Pair<Triangle, Triangle>> edgesToTriangles;
	
	private static Vector3f tmpvec = new Vector3f();
	
	private static void initHull(List<Vector3f> points) {
		// Initial Phase
		// 1. Create initial simplex
		Vector3f[] EPs = getExtremePoints(points);

		// 1.1 Get two most distant points as baseline of base triangle
		int a = -1, b = -1;
		float distance = -1;
		for (int i = 0; i < EPs.length - 1; i++) {
			for (int j = i; j < EPs.length; j++) {
				float dist = (float) VecMath.subtraction(EPs[i], EPs[j], tmpvec).lengthSquared();
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
		Vector3f IA = new Vector3f();
		for (int i = 0; i < EPs.length; i++) {
			if (i != a && i != b) {
				Vector3f ep = EPs[i];
				IA.set(ep.x - A.x, ep.y - A.y, ep.z - A.z);
				float dist = (float) VecMath.crossproduct(AB, IA, tmpvec)
						.lengthSquared();
				if (dist > distance) {
					distance = dist;
					c = i;
				}
			}
		}
		Vector3f C = EPs[c];

		// 1.3 Get furthest point from base triangle
		Vector3f ABCnormal = VecMath.computeNormal(A, B, C, tmpvec);
		boolean keepOrientation = true;
		int d = -1;
		distance = 0;
		for (int i = 0; i < EPs.length; i++) {
			if (i != a && i != b && i != c) {
				Vector3f ep = EPs[i];
				IA.set(ep.x - A.x, ep.y - A.y, ep.z - A.z);
				float dist = VecMath.dotproduct(IA, ABCnormal);
				if (Math.abs(dist) > distance) {
					keepOrientation = (dist < 0);
					distance = Math.abs(dist);
					d = i;
				}
			}
		}
		Vector3f D = EPs[d];

		// 2. Assign points to faces
		// 2.1 Create faces and add them to queue
		faces = new ArrayList<Triangle>();
		vertices = new ArrayList<Vector3f>();
		adjacentsMap = new HashMap<Integer, ArrayList<Integer>>();
		edgesToTriangles = new HashMap<Pair<Integer, Integer>, Pair<Triangle, Triangle>>();
		lastremovedTriangles = new ArrayList<Triangle>();
		lightFaceVerticesToTriangles = new HashMap<Integer, List<Triangle>>();
		vertices.add(A);
		vertices.add(B);
		vertices.add(C);
		vertices.add(D);
		points.remove(A);
		points.remove(B);
		points.remove(C);
		points.remove(D);
		if (keepOrientation) {
			faces.add(new Triangle(0, 1, 2, VecMath.computeNormal(A, B, C)));
			faces.add(new Triangle(0, 3, 1, VecMath.computeNormal(A, D, B)));
			faces.add(new Triangle(3, 2, 1, VecMath.computeNormal(D, C, B)));
			faces.add(new Triangle(0, 2, 3, VecMath.computeNormal(A, C, D)));
			adjacentsMap.put(0, createArrayList(1, 2, 3));
			adjacentsMap.put(1, createArrayList(2, 0, 3));
			adjacentsMap.put(2, createArrayList(0, 1, 3));
			adjacentsMap.put(3, createArrayList(1, 0, 2));
			edgesToTriangles.put(new Pair<Integer, Integer>(0, 1),
					new Pair<Triangle, Triangle>(faces.get(0), faces.get(1)));
			edgesToTriangles.put(new Pair<Integer, Integer>(0, 2),
					new Pair<Triangle, Triangle>(faces.get(0), faces.get(3)));
			edgesToTriangles.put(new Pair<Integer, Integer>(0, 3),
					new Pair<Triangle, Triangle>(faces.get(1), faces.get(3)));
			edgesToTriangles.put(new Pair<Integer, Integer>(1, 2),
					new Pair<Triangle, Triangle>(faces.get(0), faces.get(2)));
			edgesToTriangles.put(new Pair<Integer, Integer>(1, 3),
					new Pair<Triangle, Triangle>(faces.get(1), faces.get(2)));
			edgesToTriangles.put(new Pair<Integer, Integer>(2, 3),
					new Pair<Triangle, Triangle>(faces.get(2), faces.get(3)));
		} else {
			faces.add(new Triangle(0, 2, 1, VecMath.computeNormal(A, C, B)));
			faces.add(new Triangle(0, 1, 3, VecMath.computeNormal(A, B, D)));
			faces.add(new Triangle(0, 3, 2, VecMath.computeNormal(A, D, C)));
			faces.add(new Triangle(3, 1, 2, VecMath.computeNormal(D, B, C)));
			adjacentsMap.put(0, createArrayList(2, 1, 3));
			adjacentsMap.put(1, createArrayList(0, 2, 3));
			adjacentsMap.put(2, createArrayList(1, 0, 3));
			adjacentsMap.put(3, createArrayList(0, 1, 2));
			edgesToTriangles.put(new Pair<Integer, Integer>(0, 1),
					new Pair<Triangle, Triangle>(faces.get(2), faces.get(3)));
			edgesToTriangles.put(new Pair<Integer, Integer>(0, 2),
					new Pair<Triangle, Triangle>(faces.get(1), faces.get(3)));
			edgesToTriangles.put(new Pair<Integer, Integer>(0, 3),
					new Pair<Triangle, Triangle>(faces.get(1), faces.get(2)));
			edgesToTriangles.put(new Pair<Integer, Integer>(1, 2),
					new Pair<Triangle, Triangle>(faces.get(0), faces.get(3)));
			edgesToTriangles.put(new Pair<Integer, Integer>(1, 3),
					new Pair<Triangle, Triangle>(faces.get(0), faces.get(2)));
			edgesToTriangles.put(new Pair<Integer, Integer>(2, 3),
					new Pair<Triangle, Triangle>(faces.get(0), faces.get(1)));
		}
		// 2.2 Find lighting points for initial triangle
		listsOfFacePoints = new ArrayList<List<Vector3f>>();
		for (int i = 0; i < faces.size(); i++) {
			listsOfFacePoints.add(getLightPoints(faces.get(i), points));
		}
	}
	
	private static List<Triangle> lastremovedTriangles;
	private static HashMap<Integer, List<Triangle>> lightFaceVerticesToTriangles;
	private static Pair<Integer, Integer> tmppair = new Pair<Integer, Integer>(null, null);
	private static List<Integer> toRemove = new ArrayList<Integer>();
	private static List<Integer> edge = new ArrayList<Integer>();
	private static List<Integer> removeAdj = new ArrayList<Integer>();
	private static List<Triangle> newLightFaces = new ArrayList<Triangle>();
	private static List<Integer> freeVertexPositions = new ArrayList<Integer>();
	
	// WELCOME TO MADNESS
	
	private static boolean step(int faceIndex) {
		Triangle t = faces.get(faceIndex);
		// 2. Get most distant point of the face's point set
		Vector3f furthestPoint = null;
		int furthestPointID = -1;
		Vector3f A = vertices.get(t.a);
		List<Vector3f> facepoints = listsOfFacePoints.get(faceIndex);
		float distance = 0;
		for (int i = 0; i < facepoints.size(); i++) {
			Vector3f P = facepoints.get(i);
			float dist = VecMath.dotproduct(VecMath.subtraction(P, A, tmpvec), t.normal);
			if (dist >= distance) {
				distance = dist;
				furthestPoint = P;
				furthestPointID = i;
			}
		}
		if (furthestPointID == -1 || vertices.contains(furthestPoint)) { // TODO: check
			return true;
		}
		
		
		facepoints.remove(furthestPointID);
		vertices.add(furthestPoint);
		furthestPointID = vertices.size() - 1;
		lastremovedTriangles.clear();
		lastremovedTriangles.add(faces.remove(faceIndex));
		listsOfFacePoints.remove(faceIndex);
		// 3. Find all faces that can be seen from this point
		HashSet<Integer> lightFaceVertices = new HashSet<Integer>(); // HAS TO BE REINITIALIZED... don't ask why.
		lightFaceVertices.add(t.a);
		lightFaceVertices.add(t.b);
		lightFaceVertices.add(t.c);
		List<Triangle> vertsA = new ArrayList<Triangle>();
		List<Triangle> vertsB = new ArrayList<Triangle>();
		List<Triangle> vertsC = new ArrayList<Triangle>();
		vertsA.add(t);
		vertsB.add(t);
		vertsC.add(t);
		lightFaceVerticesToTriangles.clear();
		lightFaceVerticesToTriangles.put(t.a, vertsA);
		lightFaceVerticesToTriangles.put(t.b, vertsB);
		lightFaceVerticesToTriangles.put(t.c, vertsC);
		for (int i = faces.size() - 1; i >= 0; i--) {
			Triangle tri = faces.get(i);
			Vector3f triA = vertices.get(tri.a);
			if (VecMath.dotproduct(tri.normal, VecMath.subtraction(furthestPoint, triA, tmpvec)) > 0) {
				lastremovedTriangles.add(faces.remove(i));
				lightFaceVertices.add(tri.a);
				lightFaceVertices.add(tri.b);
				lightFaceVertices.add(tri.c);
				if ((vertsA = lightFaceVerticesToTriangles.get(tri.a)) != null) {
					vertsA.add(tri);
				} else {
					vertsA = new ArrayList<Triangle>();
					vertsA.add(tri);
					lightFaceVerticesToTriangles.put(tri.a, vertsA);
				}
				if ((vertsB = lightFaceVerticesToTriangles.get(tri.b)) != null) {
					vertsB.add(tri);
				} else {
					vertsB = new ArrayList<Triangle>();
					vertsB.add(tri);
					lightFaceVerticesToTriangles.put(tri.b, vertsB);
				}
				if ((vertsC = lightFaceVerticesToTriangles.get(tri.c)) != null) {
					vertsC.add(tri);
				} else {
					vertsC = new ArrayList<Triangle>();
					vertsC.add(tri);
					lightFaceVerticesToTriangles.put(tri.c, vertsC);
				}
				facepoints.addAll(listsOfFacePoints.remove(i));
			}
		}
		
		
		// 4.0 Remove all vertices that are only connected to lightFaceVertices
		Iterator<Integer> iter = lightFaceVertices.iterator();
		toRemove.clear();
		for (int i = 0; i < lightFaceVertices.size(); i++) {
			int vert = iter.next(); // TODO: check
			if (lightFaceVerticesToTriangles.get(vert).size() == adjacentsMap
					.get(vert).size()) {
				toRemove.add(vert);
			}
		}
		for (Integer i : toRemove) {
			for (Integer adj : adjacentsMap.get(i)) {
				adjacentsMap.get(adj).remove(i);
				tmppair.set(i, adj);
				edgesToTriangles.remove(tmppair);
			}
			lightFaceVertices.remove((int) i);
			vertices.set((int) i, null);
			freeVertexPositions.add(i);
		}
		
		
		// 4.1 Get vertices on border between lit and unlit triangles
		HashSet<Integer> vertsOnEdge = new HashSet<Integer>(); // HAS TO BE REINITIALIZED
		for (Integer vert : lightFaceVertices) {
			vertsOnEdge.add(vert);
		}
		
		
		// 4.2 Get edges on border
		int currentVert = vertsOnEdge.iterator().next();
		edge.clear(); // TODO: make HashSet (no! has to be ordered list!)
		for (int i = 0; i < vertsOnEdge.size(); i++) {
			edge.add(currentVert);
			ArrayList<Integer> adjs = adjacentsMap.get(currentVert);

			List<Triangle> vertexLightTriangles = lightFaceVerticesToTriangles
					.get(currentVert);
			for (int j = 0; j < adjs.size(); j++) {
				Integer currAdj = adjs.get(j);
				if (vertsOnEdge.contains(currAdj) && !edge.contains(currAdj)) {
					int tricount = 0;
					for (int k = 0; k < vertexLightTriangles.size()
							&& tricount < 2; k++) {
						Triangle kTri = vertexLightTriangles.get(k);
						if (kTri.a == currAdj || kTri.b == currAdj
								|| kTri.c == currAdj) {
							tricount++;
						}
					}
					if (tricount == 1) {
						currentVert = currAdj;
						break;
					}
				}
			}
		}
		
		
		// 4.2.1 remove old adjacents (crossing triangle hole)
		int edgesize = edge.size();
		int edgesizeMinusOne = edgesize - 1;
		for (int i = 0; i < edgesize; i++) {
			currentVert = edge.get(i);
			removeAdj.clear();
			for (Integer adj : adjacentsMap.get(currentVert)) {
				if (edge.contains(adj)) {
					int adjIndexOnEdge = edge.indexOf(adj);
					if (Math.abs(i - adjIndexOnEdge) > 1
							&& !(i == 0 && adjIndexOnEdge == edgesizeMinusOne)
							&& !(i == edgesizeMinusOne && adjIndexOnEdge == 0)) {
						tmppair.set(currentVert, adj);
						Pair<Triangle, Triangle> edgeTriangles = edgesToTriangles
								.get(tmppair);
						// TODO: performance
						if (lastremovedTriangles.contains(edgeTriangles
								.getFirst())
								&& lastremovedTriangles.contains(edgeTriangles
										.getSecond())) {
							removeAdj.add(adj);
							edgesToTriangles.remove(edgeTriangles);
						}
					}
				}
			}
			for (Integer removAdjacent : removeAdj) { // TODO: make faster
				adjacentsMap.get(currentVert).remove(removAdjacent);
			}
		}
		
		
		// 4.3 Stitch holes using edge
		newLightFaces.clear();
		ArrayList<Integer> furthestPointNeighbours = new ArrayList<Integer>(edge.size());

		A = vertices.get(edge.get(0));
		Vector3f B = vertices.get(edge.get(1));
		Vector3f C = vertices.get(edge.get(2));
		for (int i = 3; i < edge.size() && !linearIndependent(A, B, C); i++) {
			C = vertices.get(edge.get(i));
		}
		Vector3f normal = VecMath.computeNormal(A, B, C);
		boolean correctOrientation = VecMath.dotproduct(normal, VecMath.subtraction(A, furthestPoint, tmpvec)) < 0;

		int vertIDb = edge.get(0);
		for (int i = 0; i < edge.size(); i++) {
			int vertIDa = vertIDb;
			if (i < edge.size() - 1) {
				vertIDb = edge.get(i + 1);
			} else {
				vertIDb = edge.get(0);
			}

			Vector3f vA = vertices.get(vertIDa);
			Vector3f vB = vertices.get(vertIDb);
			Vector3f norm = VecMath.computeNormal(vA, vB, furthestPoint);
			Triangle stitchTriangle;
			if (correctOrientation) {
				stitchTriangle = new Triangle(vertIDa, vertIDb,
						furthestPointID, norm);
			} else {
				norm.negate();
				stitchTriangle = new Triangle(vertIDa, furthestPointID,
						vertIDb, norm);
			}
			faces.add(0, stitchTriangle);
			newLightFaces.add(stitchTriangle);

			// Update adjacents map
			adjacentsMap.get(vertIDa).add(furthestPointID);
			tmppair.set(vertIDa, vertIDb);
			Pair<Triangle, Triangle> oldEdgeInfo = edgesToTriangles
					.get(tmppair);
			// find out which triangle got deleted
			if (lastremovedTriangles.contains(oldEdgeInfo.getFirst())) {
				oldEdgeInfo.setFirst(stitchTriangle);
			} else {
				oldEdgeInfo.setSecond(stitchTriangle);
			}
			tmppair.set(vertIDa, furthestPointID);
			oldEdgeInfo = edgesToTriangles.get(tmppair);
			if (oldEdgeInfo != null) {
				oldEdgeInfo.setSecond(stitchTriangle);
			} else {
				// TODO: just relevant for first iteration, move before loop
				edgesToTriangles.put(new Pair<Integer, Integer>(vertIDa,
						furthestPointID), new Pair<Triangle, Triangle>(
						null, stitchTriangle));
			}
			tmppair.set(vertIDb, furthestPointID);
			oldEdgeInfo = edgesToTriangles.get(tmppair);
			if (oldEdgeInfo != null) {
				// TODO: just relevant for last iteration
				oldEdgeInfo.setFirst(stitchTriangle);
			} else {
				edgesToTriangles.put(new Pair<Integer, Integer>(vertIDb,
						furthestPointID), new Pair<Triangle, Triangle>(
						stitchTriangle, null));
			}
			furthestPointNeighbours.add(vertIDa);
		}
		
		
		// 5. Assign all points of all light-faces to the new created faces
		adjacentsMap.put(furthestPointID, furthestPointNeighbours);

		for (Triangle tri : newLightFaces) {
			listsOfFacePoints.add(0, getLightPoints(tri, facepoints));
		}

		// 6. Push new created faces on the stack and start at (1))
		return false;
	}
	
	private static Vector3f[] getExtremePoints(List<Vector3f> points) {
		Vector3f[] result = new Vector3f[6];

		Vector3f initial = points.get(0);
		for(int i = 0; i < 6; i++)
			result[i] = initial;

		for (Vector3f p : points) {
			if (p.x < result[0].x) {
				result[0] = p;
			}
			if (p.y < result[1].y) {
				result[1] = p;
			}
			if (p.z < result[2].z) {
				result[2] = p;
			}
			if (p.x > result[3].x) {
				result[3] = p;
			}
			if (p.y > result[4].y) {
				result[4] = p;
			}
			if (p.z > result[5].z) {
				result[5] = p;
			}
		}

		return result;
	}
	
	private static ArrayList<Integer> createArrayList(Integer... initialValues) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Integer a : initialValues) {
			result.add(a);
		}
		return result;
	}
	
	private static List<Vector3f> getLightPoints(Triangle triangle,
			List<Vector3f> points) {
		Vector3f A = vertices.get(triangle.a);
		ArrayList<Vector3f> result = new ArrayList<Vector3f>();
		for (int i = points.size() - 1; i >= 0; i--) {
			Vector3f p = points.get(i);
			if (VecMath.dotproduct(triangle.normal, VecMath.subtraction(p, A, tmpvec)) > 0) {
				result.add(p);
				points.remove(i);
			}
		}
		return result;
	}
	
	private static boolean linearIndependent(Vector3f a, Vector3f b, Vector3f c) {
		// ((x1*y2 - x2*y1) != 0 || (x1*z2 - x2*z1) != 0 || (y1*z2 - y2*z1) != 0
		// x1 = ab, x2 = ac
		float x1 = b.x - a.x;
		float y1 = b.y - a.y;
		float z1 = b.z - a.z;
		float x2 = c.x - a.x;
		float y2 = c.y - a.y;
		float z2 = c.z - a.z;
		return ((x1 * y2 - x2 * y1) != 0 || (x1 * z2 - x2 * z1) != 0 || (y1
				* z2 - y2 * z1) != 0);
	}
}