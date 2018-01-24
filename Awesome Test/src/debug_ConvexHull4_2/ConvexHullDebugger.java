package debug_ConvexHull4_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Color;
import gui.Font;
import gui.Text;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import math.VecMath;
import matrix.Matrix4f;
import objects.ShapedObject3;
import shader.Shader;
import shape.Sphere;
import sound.NullSoundEnvironment;
import utils.Debugger;
import utils.GLConstants;
import utils.Pair;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class ConvexHullDebugger extends StandardGame {
	public static class Triangle {
		int a, b, c;
		Vector3f normal;

		public Triangle(int a, int b, int c, Vector3f normal) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.normal = normal;
		}
	}

	Shader defaultshader;

	Debugger debugger;

	InputEvent toggleMouseBind;

	Simplex simplex;

	PointCloud pointcloud;

	int faceIndex, facesDone;

	Font font;

	List<Text> texts;

	int currentstep = 0;

	Sphere furthestPointSphere;

	List<Sphere> removeUnconnectedVerticesSpheres;

	List<Sphere> vertsOnEdgeSpheres;

	List<Sphere> edgeSpheres;

	public void hullInit() {
		/*
		 * List<Vector3f> testpointsforDups = new ArrayList<Vector3f>(); for (Vector3f p
		 * : points) { if (testpointsforDups.contains(p)) {
		 * System.out.println("FOUND DUPLICATE " + p); } testpointsforDups.add(p); }
		 */

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
				float dist = (float) VecMath.crossproduct(AB, IA, tmpvec).lengthSquared();
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

		System.out.println("Pointcount: " + points.size());
		System.out.println("Init: " + A + "; " + B + "; " + C + "; " + D);

		// 2. Assign points to faces
		// 2.1 Create faces and add them to queue
		faces = new ArrayList<Triangle>();
		vertices = new ArrayList<Vector3f>();
		adjacentsMap = new HashMap<Integer, ArrayList<Integer>>();
		edgesToTriangles = new HashMap<Pair<Integer, Integer>, Pair<Triangle, Triangle>>();
		lastremovedTriangles = new ArrayList<Triangle>();
		lightFaceVerticesToTriangles = new HashMap<Integer, List<Triangle>>();
		freeVertexPositions = new ArrayList<Integer>();
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
		/*
		 * System.out.println("Points: "); for (int i = 0; i < listsOfFacePoints.size();
		 * i++) { System.out.println(i + " : " + listsOfFacePoints.get(i).size()); }
		 */
		// 3. Push the 4 faces on the stack (done above)
	}

	List<Triangle> faces;

	List<Vector3f> points, vertices;

	HashMap<Integer, ArrayList<Integer>> adjacentsMap;

	ArrayList<List<Vector3f>> listsOfFacePoints;

	Vector3f furthestPoint;

	int furthestPointID;

	HashSet<Integer> lightFaceVertices;

	HashMap<Integer, List<Triangle>> lightFaceVerticesToTriangles;

	List<Vector3f> facepoints;

	HashSet<Integer> vertsOnEdge;

	List<Integer> edge;

	List<Triangle> newLightFaces;

	ArrayList<Integer> furthestPointNeighbours;

	HashMap<Pair<Integer, Integer>, Pair<Triangle, Triangle>> edgesToTriangles;

	List<Triangle> lastremovedTriangles;

	Vector3f tmpvec = new Vector3f();

	List<Integer> freeVertexPositions;

	public void step1(Triangle t) {
		// System.out.println("Step 1 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		// 2. Get most distant point of the face's point set
		furthestPoint = null;
		furthestPointID = -1;
		Vector3f A = vertices.get(t.a);
		facepoints = listsOfFacePoints.get(faceIndex);
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
		System.out.println("furthest: " + furthestPointID + "; " + furthestPoint);

		// DEBUG
		if (furthestPoint != null)
			furthestPointSphere.translateTo(furthestPoint);
		// DEBUG
	}

	public void step2(Triangle t) {
		// System.out.println("Step 2 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		facepoints.remove(furthestPointID);
		/*
		 * if (vertices.contains(furthestPoint)) { // TODO: Remove!
		 * System.out.println("SHIT WENT DOWN MATE!"); return; } else {
		 */
		System.out.println("Added vert: " + furthestPoint);
		vertices.add(furthestPoint);
		furthestPointID = vertices.size() - 1;
		// }
		lastremovedTriangles.clear();
		lastremovedTriangles.add(faces.remove(faceIndex));
		listsOfFacePoints.remove(faceIndex);
		// 3. Find all faces that can be seen from this point
		lightFaceVertices = new HashSet<Integer>(); // HAS TO BE REINITIALIZED... don't ask why.
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
		// lightFaceVertices: all vertices of the removed faces
		// lightFaceVerticesToTriangles: maps removed vertices to their
		// corresponding triangles
		// facepoints: facepoints of removed triangles to be assigned to new
		// triangles or discarded if inside volume
	}

	// 4. Extract horizon edges of light-faces and extrude to Point
	Pair<Integer, Integer> tmppair = new Pair<Integer, Integer>(null, null);
	List<Integer> toRemove = new ArrayList<Integer>();

	public void step3(Triangle t) {
		// System.out.println("Step 3 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		// 4.0 Remove all vertices that are only connected to lightFaceVertices
		Iterator<Integer> iter = lightFaceVertices.iterator();
		toRemove.clear();
		for (int i = 0; i < lightFaceVertices.size(); i++) {
			int vert = iter.next(); // TODO: check
			if (lightFaceVerticesToTriangles.get(vert).size() == adjacentsMap.get(vert).size()) {
				toRemove.add(vert);
			}
		}
		int s = 0; // DEBUG
		for (Integer i : toRemove) {
			for (Integer adj : adjacentsMap.get(i)) {
				adjacentsMap.get(adj).remove(i);
				tmppair.set(i, adj);
				edgesToTriangles.remove(tmppair);
			}
			lightFaceVertices.remove((int) i);
			// DEBUG
			removeUnconnectedVerticesSpheres.get(s).translateTo(vertices.get(i));
			s++;
			// DEBUG
			vertices.set((int) i, null);
			freeVertexPositions.add(i);
		}
	}

	public void step4(Triangle t) {
		// System.out.println("Step 4 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		// 4.1 Get vertices on border between lit and unlit triangles
		vertsOnEdge = new HashSet<Integer>(); // HAS TO BE REINITIALIZED
		int s = 0; // DEBUG
		for (Integer vert : lightFaceVertices) {
			// if (lightFaceVerticesToTriangles.get(vert).size() !=
			// adjacentsMap.get(vert).length) { (Wird oben schon entfernt)
			vertsOnEdge.add(vert);
			// }
			// DEBUG
			vertsOnEdgeSpheres.get(s).translateTo(vertices.get(vert));
			s++;
			// DEBUG
		}
	}

	public void step5(Triangle t) {
		// System.out.println("Step 5 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		// 4.2 Get edges on border
		int currentVert = vertsOnEdge.iterator().next();
		edge.clear(); // TODO: make HashSet (no! has to be ordered list!)
		for (int i = 0; i < vertsOnEdge.size(); i++) {
			edge.add(currentVert);
			ArrayList<Integer> adjs = adjacentsMap.get(currentVert);

			List<Triangle> vertexLightTriangles = lightFaceVerticesToTriangles.get(currentVert);
			for (int j = 0; j < adjs.size(); j++) {
				Integer currAdj = adjs.get(j);
				if (vertsOnEdge.contains(currAdj) && !edge.contains(currAdj)) {
					int tricount = 0;
					for (int k = 0; k < vertexLightTriangles.size() && tricount < 2; k++) {
						Triangle kTri = vertexLightTriangles.get(k);
						if (kTri.a == currAdj || kTri.b == currAdj || kTri.c == currAdj) {
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

		// DEBUG
		for (int i = 0; i < edge.size(); i++) {
			Integer e = edge.get(i);
			if (i < edge.size() - 1) {
				edgeSpheres.get(i).translateTo(
						VecMath.scale(VecMath.addition(vertices.get(e), vertices.get(edge.get(i + 1))), 0.5f));
			} else {
				edgeSpheres.get(i)
						.translateTo(VecMath.scale(VecMath.addition(vertices.get(e), vertices.get(edge.get(0))), 0.5f));
			}
		}
		// DEBUG
	}

	List<Integer> removeAdj = new ArrayList<Integer>();

	public void step6(Triangle t) {
		// System.out.println("Step 6 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		// 4.2.1 remove old adjacents (crossing triangle hole)
		int edgesize = edge.size();
		int edgesizeMinusOne = edgesize - 1;
		for (int i = 0; i < edgesize; i++) {
			int currentVert = edge.get(i);
			removeAdj.clear();
			for (Integer adj : adjacentsMap.get(currentVert)) {
				if (edge.contains(adj)) {
					int adjIndexOnEdge = edge.indexOf(adj);
					if (Math.abs(i - adjIndexOnEdge) > 1 && !(i == 0 && adjIndexOnEdge == edgesizeMinusOne)
							&& !(i == edgesizeMinusOne && adjIndexOnEdge == 0)) {
						tmppair.set(currentVert, adj);
						Pair<Triangle, Triangle> edgeTriangles = edgesToTriangles.get(tmppair);
						// TODO: performance
						if (lastremovedTriangles.contains(edgeTriangles.getFirst())
								&& lastremovedTriangles.contains(edgeTriangles.getSecond())) {
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
	}

	public void step7(Triangle t) {
		// System.out.println("Step 7 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		// 4.3 Stitch holes using edge
		newLightFaces.clear();
		furthestPointNeighbours = new ArrayList<Integer>(edge.size());

		Vector3f A = vertices.get(edge.get(0));
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

			// if (vertIDa != vertIDb) {
			// TODO: correct?
			Vector3f vA = vertices.get(vertIDa);
			Vector3f vB = vertices.get(vertIDb);
			Vector3f norm = VecMath.computeNormal(vA, vB, furthestPoint);
			Triangle stitchTriangle;
			if (correctOrientation) {
				stitchTriangle = new Triangle(vertIDa, vertIDb, furthestPointID, norm);
			} else {
				norm.negate();
				stitchTriangle = new Triangle(vertIDa, furthestPointID, vertIDb, norm);
			}

			faces.add(0, stitchTriangle);
			newLightFaces.add(stitchTriangle);

			// Update adjacents map
			adjacentsMap.get(vertIDa).add(furthestPointID);
			tmppair.set(vertIDa, vertIDb);
			Pair<Triangle, Triangle> oldEdgeInfo = edgesToTriangles.get(tmppair);
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
				edgesToTriangles.put(new Pair<Integer, Integer>(vertIDa, furthestPointID),
						new Pair<Triangle, Triangle>(null, stitchTriangle));
			}
			tmppair.set(vertIDb, furthestPointID);
			oldEdgeInfo = edgesToTriangles.get(tmppair);
			if (oldEdgeInfo != null) {
				// TODO: just relevant for last iteration
				oldEdgeInfo.setFirst(stitchTriangle);
			} else {
				edgesToTriangles.put(new Pair<Integer, Integer>(vertIDb, furthestPointID),
						new Pair<Triangle, Triangle>(stitchTriangle, null));
			}
			/*
			 * } else { System.out.println("Stitchingerror!"); // TODO: investigate? }
			 */
			furthestPointNeighbours.add(vertIDa);
		}
	}

	public void step8(Triangle t) {
		// System.out.println("Step 8 " + t.a + "; " + t.b + "; " + t.c + "; "
		// + faceIndex);
		// 5. Assign all points of all light-faces to the new created faces
		adjacentsMap.put(furthestPointID, furthestPointNeighbours);

		for (Triangle tri : newLightFaces) {
			listsOfFacePoints.add(0, getLightPoints(tri, facepoints));
		}

		// 6. Push new created faces on the stack and start at (1))

		// ///////// CHECK ADJS
		// System.out.println("CHECK ADJS");
		/*
		 * for (int i = 0; i < vertices.size(); i++) { // System.out.print(i + "(" +
		 * adjacentsMap.get(i).length + "): "); for (Integer a : adjacentsMap.get(i)) {
		 * // System.out.print(" " + a); if (a == null) System.exit(0); } //
		 * System.out.println(); }
		 */

		// DEBUG
		for (Sphere s : removeUnconnectedVerticesSpheres)
			s.translateTo(10000, 0, 0);
		for (Sphere s : vertsOnEdgeSpheres)
			s.translateTo(10000, 0, 0);
		for (Sphere s : edgeSpheres)
			s.translateTo(10000, 0, 0);
		// DEBUG
	}

	private Vector3f[] getExtremePoints(List<Vector3f> points) {
		Vector3f[] result = new Vector3f[6];

		Vector3f initial = points.get(0);
		for (int i = 0; i < 6; i++)
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

	private List<Vector3f> getLightPoints(Triangle triangle, List<Vector3f> points) {
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

	private static ArrayList<Integer> createArrayList(Integer... initialValues) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (Integer a : initialValues) {
			result.add(a);
		}
		return result;
	}

	private boolean linearIndependent(Vector3f a, Vector3f b, Vector3f c) {
		// ((x1*y2 - x2*y1) != 0 || (x1*z2 - x2*z1) != 0 || (y1*z2 - y2*z1) != 0
		// x1 = ab, x2 = ac
		float x1 = b.x - a.x;
		float y1 = b.y - a.y;
		float z1 = b.z - a.z;
		float x2 = c.x - a.x;
		float y2 = c.y - a.y;
		float z2 = c.z - a.z;
		return ((x1 * y2 - x2 * y1) != 0 || (x1 * z2 - x2 * z1) != 0 || (y1 * z2 - y2 * z1) != 0);
	}

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings(),
				new NullSoundEnvironment());

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		display.bindMouse();
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		cam.setFlyCam(true);
		cam.translateTo(0, 1, 3);
		cam.setFlySpeed(0.004f);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		// Input to step EPA
		InputEvent stepEPA = new InputEvent("Step Hull", new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(stepEPA);

		points = new ArrayList<Vector3f>();
		// for (int i = 0; i < 1000; i++) {
		// points.add(new Vector3f(Math.random() * 10 - 5,
		// Math.random() * 10 - 5, Math.random() * 10 - 5));
		// }
		ShapedObject3 obj = ModelLoader.load("res/models/bunny_lowpoly.mobj");
		System.out.println("LoadedVertexCount: " + obj.getVertices().size());
		for (Vector3f v : obj.getVertices())
			points.add(v);
		ArrayList<Vector3f> originalPoints = new ArrayList<Vector3f>(points);
		// points.add(new Vector3f(11.404785, 10.446343, -2.761249));
		// points.add(new Vector3f(5.6260934, 5.6621804, 11.700975));
		// points.add(new Vector3f(6.812222, -1.6368495, 9.790112));
		// points.add(new Vector3f(2.2193725, -1.7034389, -4.2405186));
		// points.add(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		vertices = new ArrayList<Vector3f>();
		edge = new ArrayList<Integer>();
		newLightFaces = new ArrayList<Triangle>();

		int colorShaderHandle = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert",
				"res/shaders/colorshader.frag");

		Shader colorshader = new Shader(colorShaderHandle);
		colorshader.addArgument("u_color", new Vector4f(1f, 0f, 0f, 1f));
		addShader(colorshader);

		furthestPointSphere = new Sphere(10000, 0, 0, 0.1f, 36, 36);
		colorshader.addObject(furthestPointSphere);

		Shader colorshader2 = new Shader(colorShaderHandle);
		colorshader2.addArgument("u_color", new Vector4f(0f, 1f, 0f, 1f));
		addShader(colorshader2);

		removeUnconnectedVerticesSpheres = new ArrayList<Sphere>();
		for (int i = 0; i < 100; i++) {
			Sphere s = new Sphere(10000, 0, 0, 0.1f, 36, 36);
			colorshader2.addObject(s);
			removeUnconnectedVerticesSpheres.add(s);
		}

		Shader colorshader3 = new Shader(colorShaderHandle);
		colorshader3.addArgument("u_color", new Vector4f(0f, 0f, 1f, 1f));
		addShader(colorshader3);

		vertsOnEdgeSpheres = new ArrayList<Sphere>();
		for (int i = 0; i < 100; i++) {
			Sphere s = new Sphere(10000, 0, 0, 0.1f, 36, 36);
			colorshader3.addObject(s);
			vertsOnEdgeSpheres.add(s);
		}

		Shader colorshader4 = new Shader(colorShaderHandle);
		colorshader4.addArgument("u_color", new Vector4f(1f, 1f, 0f, 1f));
		addShader(colorshader4);

		edgeSpheres = new ArrayList<Sphere>();
		for (int i = 0; i < 100; i++) {
			Sphere s = new Sphere(10000, 0, 0, 0.1f, 36, 36);
			colorshader4.addObject(s);
			edgeSpheres.add(s);
		}

		pointcloud = new PointCloud(points);

		long allIterations = 0;
		int iterations = 1;
		for (int s = 0; s < iterations; s++) {
			points = new ArrayList<Vector3f>(originalPoints);
			long timeStart = System.nanoTime();
			hullInit();

			facesDone = 1;
			for (int i = 0; i < 10000; i++) {
				faceIndex = faces.size() - facesDone;
				if (faceIndex < 0) {
					break;
				}
				Triangle t = faces.get(faceIndex);
				System.out.println(faces.size() + "; " + facesDone);
				System.out.println(faceIndex);
				step1(t);
				if (furthestPointID != -1 && furthestPoint != null && !vertices.contains(furthestPoint)) {// TODO:
					// HashSet?
					step2(t);
					step3(t);
					step4(t);
					step5(t);
					step6(t);
					step7(t);
					step8(t);
				} else {
					facesDone++;
				}
			}

			long resulttime = System.nanoTime() - timeStart;
			System.out.println(
					"TIME: " + s + ": " + resulttime + "; " + vertices.size() + "; " + this.adjacentsMap.size());
			allIterations += resulttime;
		}
		System.out.println("Result: " + (allIterations / iterations));

		// TODO: wrong if you want to step...
		// Compress vertices list (remove nulls)
		for (int i = vertices.size() - 1; i >= 0 && freeVertexPositions.size() > 0; i--) {
			int newPos = freeVertexPositions.remove(0);
			if (newPos < i) {
				Vector3f v = vertices.set(i, null);
				System.out.println("nulled " + i + "; " + freeVertexPositions.size() + "; " + newPos);
				if (v != null) {
					for (Integer adj : adjacentsMap.get(i)) {
						ArrayList<Integer> adjAdjs = adjacentsMap.get((int) adj);
						for (int j = 0; j < adjAdjs.size(); j++) {
							if (adjAdjs.get(j) == i) {
								adjAdjs.set(j, newPos);
								break;
							}
						}
					}
					vertices.set(newPos, v);
					adjacentsMap.put(newPos, adjacentsMap.remove(i));
					// DEBUG
					for (Triangle f : faces) {
						if (f.a == i)
							f.a = newPos;
						if (f.b == i)
							f.b = newPos;
						if (f.c == i)
							f.c = newPos;
					}
					// DEBUG
				} else {
					freeVertexPositions.add(0, newPos);
				}
			} else {
				i++;
			}
		}
		for (int i = vertices.size() - 1; i >= 0; i--) {
			Vector3f v = vertices.remove(i);
			if (v != null) {
				vertices.add(v);
				break;
			}
		}
		// TODO: remove trailing nulls in vertices
		// for(Vector3f v : vertices)
		// System.out.println("v: " + v);
		for (int i = 0; i < vertices.size(); i++) {
			System.out.println("v " + i + ": " + vertices.get(i));
		}

		faceIndex = faces.size() - facesDone;
		if (faceIndex < 0)
			faceIndex = 0;

		simplex = new Simplex(vertices, faces, faces.get(faceIndex));
		defaultshader.addObject(simplex);
		defaultshader.addObject(pointcloud);

		font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		texts = new ArrayList<Text>();
		updateTexts();
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
	}

	@Override
	public void render2d() {
		render2dLayer();
		debugger.end();
	}

	private Triangle mT;

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("Step Hull") || inputs.isKeyDown("R")) {
			simplex.delete();
			defaultshader.removeObject(simplex);
			// if (points.size() > 1) {
			// pointcloud.delete();
			// defaultshader.removeObject(pointcloud);
			// }

			if (facesDone <= faces.size()) {
				faceIndex = faces.size() - facesDone;
				switch (currentstep) {
				case 0:
					mT = faces.get(faceIndex);
					step1(mT);
					if (furthestPointID != -1 && !vertices.contains(furthestPoint))
						currentstep++;
					else
						facesDone++;
					break;
				case 1:
					step2(mT);
					currentstep++;
					break;
				case 2:
					step3(mT);
					currentstep++;
					break;
				case 3:
					step4(mT);
					currentstep++;
					break;
				case 4:
					step5(mT);
					currentstep++;
					break;
				case 5:
					step6(mT);
					currentstep++;
					break;
				case 6:
					step7(mT);
					currentstep++;
					break;
				case 7:
					step8(mT);
					currentstep = 0;
					break;
				}
			} else {
				System.out.println("DONE!!! !");
			}
			faceIndex = faces.size() - facesDone;

			if (faceIndex >= 0)
				simplex = new Simplex(vertices, faces, faces.get(faceIndex));
			else
				simplex = new Simplex(vertices, faces, null);
			defaultshader.addObject(simplex);
			// if (points.size() > 0) {
			// pointcloud = new PointCloud(points);
			// defaultshader.addObject(pointcloud);
			// }
			System.out.println("NUM FACES : " + faces.size() + " (done: " + facesDone + ")");
			updateTexts();

			// DEBUG
			if (this.adjacentsMap.containsKey(17)) {
				System.out.print("ADJS 17: ");
				for (Integer a : adjacentsMap.get(17)) {
					System.out.print(a + " ");
				}
				System.out.println();
			}
			// DEBUG
		}
		if (inputs.isKeyDown("P")) {
			facesDone = 1;
			System.out.println("RESET");
		}
		debugger.update(fps, 0, 0);

		if (display.isMouseBound())
			cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}

	private void updateTexts() {
		for (Text t : texts) {
			defaultshader.removeObject(t);
			t.delete();
		}
		texts.clear();
		for (int i = 0; i < vertices.size(); i++) {
			Vector3f v = vertices.get(i);
			if (v != null) {
				Text t = new Text(i + "", 0, 0, font, 0.3f);
				t.scale(1, -1);
				Matrix4f mat = t.getMatrix();
				mat.translateTo(v);
				mat.store(t.getMatrixBuffer());
				t.getMatrixBuffer().flip();
				t.prerender();
				defaultshader.addObject(t);
				texts.add(t);
			}
		}
		// Duplicationcheck
		for (int i = 0; i < vertices.size(); i++) {
			Vector3f v = vertices.get(i);
			for (int j = i + 1; j < vertices.size(); j++) {
				Vector3f v2 = vertices.get(j);
				if (v != null && v.equals(v2)) {
					System.out.println("Duplicated vertices: " + i + "; " + j + "; " + v + "; " + v2);
				}
			}
		}
	}

	private class PointCloud extends ShapedObject3 {
		public PointCloud(List<Vector3f> points) {
			setRenderMode(GLConstants.POINTS);
			for (int i = 0; i < points.size(); i++) {
				addVertex(points.get(i), Color.GRAY, new Vector2f(0, 0), new Vector3f(0, 1, 0));
				addIndex(i);
			}
			prerender();
		}
	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
	}
}
