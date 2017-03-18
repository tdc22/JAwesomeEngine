package debug_ConvexHull3_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
import sound.NullSoundEnvironment;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

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

	List<Triangle> faces;

	List<Vector3f> points, vertices;

	HashMap<Integer, Integer[]> adjacentsMap;

	LinkedList<List<Vector3f>> listsOfFacePoints;

	int faceIndex, facesDone;

	Font font;

	List<Text> texts;

	boolean step2ready = false;

	public void hullInit() {
		// Initial Phase
		// 1. Create initial simplex
		Vector3f[] EPs = getExtremePoints(points);

		// 1.1 Get two most distant points as baseline of base triangle
		int a = -1, b = -1;
		float distance = -1;
		for (int i = 0; i < EPs.length - 1; i++) {
			for (int j = i; j < EPs.length; j++) {
				float dist = (float) VecMath.subtraction(EPs[i], EPs[j]).lengthSquared();
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
				float dist = (float) VecMath.crossproduct(AB, IA).lengthSquared();
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
		faces = new ArrayList<Triangle>();
		vertices = new ArrayList<Vector3f>();
		adjacentsMap = new HashMap<Integer, Integer[]>();
		vertices.add(A);
		vertices.add(B);
		vertices.add(C);
		vertices.add(D);
		points.remove(A);
		points.remove(B);
		points.remove(C);
		points.remove(D);
		if (keepOrientation) {
			System.out.println("!! 0");
			faces.add(new Triangle(0, 1, 2, VecMath.computeNormal(A, B, C)));
			faces.add(new Triangle(0, 3, 1, VecMath.computeNormal(A, D, B)));
			faces.add(new Triangle(3, 2, 1, VecMath.computeNormal(D, C, B)));
			faces.add(new Triangle(0, 2, 3, VecMath.computeNormal(A, C, D)));
			adjacentsMap.put(0, new Integer[] { 1, 2, 3 });
			adjacentsMap.put(1, new Integer[] { 2, 0, 3 });
			adjacentsMap.put(2, new Integer[] { 0, 1, 3 });
			adjacentsMap.put(3, new Integer[] { 1, 0, 2 });
		} else {
			System.out.println("!! 1");
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
		listsOfFacePoints = new LinkedList<List<Vector3f>>();
		for (int i = 0; i < faces.size(); i++) {
			listsOfFacePoints.add(getLightPoints(faces.get(i), points));
		}
		System.out.println("Points: ");
		for (int i = 0; i < listsOfFacePoints.size(); i++) {
			System.out.println(i + " : " + listsOfFacePoints.get(i).size());
		}
		// 3. Push the 4 faces on the stack (done above)
	}

	private HashSet<Integer> lightFaceVertices;
	private HashMap<Integer, List<Triangle>> lightFaceVerticesToTriangles;
	private Vector3f furthestPoint;
	private int furthestPointID;
	private List<Vector3f> facepoints;

	public void hullStep(Triangle t) {
		System.out.println("a " + t.a + "; " + t.b + "; " + t.c + "; " + t.normal + "; " + faceIndex);
		// 2. Get most distant point of the face's point set
		furthestPoint = null;
		furthestPointID = -1;
		Vector3f A = vertices.get(t.a);
		facepoints = listsOfFacePoints.get(faceIndex);
		float distance = 0;
		for (int i = 0; i < facepoints.size(); i++) {
			Vector3f P = facepoints.get(i);
			Vector3f PA = VecMath.subtraction(P, A);
			float dist = VecMath.dotproduct(PA, t.normal);
			if (dist >= distance) {
				distance = dist;
				furthestPoint = P;
				furthestPointID = i;
			}
		}
		System.out.println(furthestPointID + "; " + furthestPoint + "; " + facepoints.size() + "; " + faceIndex);
		if (furthestPointID != -1) {
			facepoints.remove(furthestPointID);
			System.out.println("FP: " + facepoints.size());
			System.out.println("contains vertex: " + vertices.contains(furthestPoint));
			if (vertices.contains(furthestPoint)) {
				// TODO: HashSet?
				System.out.println("SHIT WENT DOWN MATE!");
				return;
			} else {
				vertices.add(furthestPoint);
				furthestPointID = vertices.size() - 1;
			}
			faces.remove(faceIndex);
			listsOfFacePoints.remove(faceIndex);

			// 3. Find all faces that can be seen from this point
			lightFaceVertices = new HashSet<Integer>(); // TODO:
														// remove?
			lightFaceVerticesToTriangles = new HashMap<Integer, List<Triangle>>();
			lightFaceVertices.add(t.a);
			lightFaceVertices.add(t.b);
			lightFaceVertices.add(t.c);
			List<Triangle> vertsA = new ArrayList<Triangle>();
			List<Triangle> vertsB = new ArrayList<Triangle>();
			List<Triangle> vertsC = new ArrayList<Triangle>();
			vertsA.add(t);
			vertsB.add(t);
			vertsC.add(t);
			lightFaceVerticesToTriangles.put(t.a, vertsA);
			lightFaceVerticesToTriangles.put(t.b, vertsB);
			lightFaceVerticesToTriangles.put(t.c, vertsC);
			for (int i = faces.size() - 1; i >= 0; i--) {
				Triangle tri = faces.get(i);
				Vector3f triP = VecMath.subtraction(furthestPoint, vertices.get(tri.a));
				System.out.println(
						"Y " + tri.a + "; " + tri.b + "; " + tri.c + "; " + VecMath.dotproduct(tri.normal, triP));
				if (VecMath.dotproduct(tri.normal, triP) > 0) {
					faces.remove(i);
					System.out.println("Light face vertices added");
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
					System.out.println("ADD LIGHT FACES POINTS ");
					facepoints.addAll(listsOfFacePoints.remove(i));
				}
			}

			step2ready = true;

		} else {
			facesDone++;
		}
	}

	private void step2(Triangle t) {
		// 4. Extract horizon edges of light-faces and extrude to Point
		// TODO: LÖSUNG IM CHAT MIT FLO
		// 4.0 Remove all vertices that are only connected to
		// lightFaceVertices
		Iterator<Integer> iter = lightFaceVertices.iterator();
		List<Integer> toRemove = new ArrayList<Integer>();
		for (int i = 0; i < lightFaceVertices.size(); i++) {
			int vert = iter.next(); // TODO: check
			System.out.println("vert + " + i + "; " + vert + "; " + lightFaceVerticesToTriangles.get(vert).size() + "; "
					+ adjacentsMap.get(vert).length);
			if (lightFaceVerticesToTriangles.get(vert).size() == adjacentsMap.get(vert).length) {
				toRemove.add(vert);
			}
		}
		for (Integer i : toRemove) {
			System.out.println("remove LFV " + i);
			for (Integer adj : adjacentsMap.get(i)) {
				adjacentsMap.put(adj, removeArrayEntry(adjacentsMap.get(adj), i));
			}
			lightFaceVertices.remove(i);
			vertices.remove(i);
		}
		// 4.1 Get vertices on border between lighted and unlighted
		// triangles
		HashSet<Integer> vertsOnEdge = new HashSet<Integer>();
		System.out.println("Light Face Vertices: " + lightFaceVertices.size());
		for (Integer vert : lightFaceVertices) {
			System.out.println("LFV: " + vert + "; " + lightFaceVerticesToTriangles.get(vert).size() + "; "
					+ adjacentsMap.get(vert).length);
			// if (lightFaceVerticesToTriangles.get(vert).size() !=
			// adjacentsMap.get(vert).length) { (Wird oben schon entfernt)
			vertsOnEdge.add(vert);
			// }
		}
		System.out.println("Edges!");
		for (Integer i : vertsOnEdge) {
			System.out.println("Edge: " + i);
		}
		// 4.2 Get edges on border
		int currentVert = vertsOnEdge.iterator().next();
		List<Integer> edge = new ArrayList<Integer>(); // TODO: make HashSet
		for (int i = 0; i < vertsOnEdge.size(); i++) {
			edge.add(currentVert);
			Integer[] adjs = adjacentsMap.get(currentVert);

			for (int j = 0; j < adjs.length; j++) {
				Integer currAdj = adjs[j];
				System.out.println(currentVert + "; " + currAdj + "; " + vertsOnEdge.contains(currAdj) + "; "
						+ (!edge.contains(currAdj)));
				if (vertsOnEdge.contains(currAdj) && !edge.contains(currAdj)) {
					int tricount = 0;
					List<Triangle> vertexLightTriangles = lightFaceVerticesToTriangles.get(currentVert);
					for (int k = 0; k < vertexLightTriangles.size() && tricount < 2; k++) {
						Triangle kTri = vertexLightTriangles.get(k);
						if (kTri.a == currAdj || kTri.b == currAdj || kTri.c == currAdj) {
							tricount++;
						}
					}
					System.out.println(tricount);
					if (tricount == 1) {
						currentVert = adjs[j];
						break;
					}
				}
			}
		}

		// 4.2.1 Notes and proof
		// 17 keeps 4 as ADJ even though they aren't adjacent to each
		// other after stiching!
		// TODO: remove all adjacency-relations that get lost through
		// stitching.
		// Proposition 1: Iterate over all vertices on the stitching edge
		// and remove
		// all other vertices on the edge from the adjacency-list except
		// their
		// direct neighbours.
		// Proof: A-----D
		// | / |
		// | / | (B and D are neighbours)
		// | / |
		// B-----C
		// Becomes:
		// A-----D
		// |\ /|
		// | E | (B and D are not neighbours anymore)
		// |/ \|
		// B-----C
		int edgesize = edge.size();
		int edgesizeMinusOne = edgesize - 1;
		for (int i = 0; i < edgesize; i++) {
			currentVert = edge.get(i);
			List<Integer> removeAdj = new ArrayList<Integer>();
			for (Integer adj : adjacentsMap.get(currentVert)) {
				if (edge.contains(adj)) {
					int adjIndexOnEdge = edge.indexOf(adj);
					if (Math.abs(i - adjIndexOnEdge) > 1 && !(i == 0 && adjIndexOnEdge == edgesizeMinusOne)
							&& !(i == edgesizeMinusOne && adjIndexOnEdge == 0)) {
						System.out.println("Diff: " + i + "; " + edge.indexOf(adj) + "; " + (i - edge.indexOf(adj)));
						removeAdj.add(adj);
					}
				}
			}
			for (Integer removAdjacent : removeAdj) { // TODO: make faster
				System.out.println("Remove adj " + removAdjacent + "; from vert " + currentVert);
				adjacentsMap.put(currentVert, removeArrayEntry(adjacentsMap.get(currentVert), removAdjacent));
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

			System.out.println("Stitch!! " + i + "; " + vertIDa + "; " + vertIDb);
			if (vertIDa != vertIDb) {
				// TODO: correct?
				Vector3f vA = vertices.get(vertIDa);
				Vector3f vB = vertices.get(vertIDb);
				Vector3f norm = VecMath.computeNormal(vA, vB, furthestPoint);
				Triangle stichTriangle;
				System.out.println(VecMath.dotproduct(t.normal, norm));
				if (VecMath.dotproduct(t.normal, norm) >= 0) {
					stichTriangle = new Triangle(vertIDa, vertIDb, furthestPointID, norm);
				} else {
					stichTriangle = new Triangle(vertIDa, furthestPointID, vertIDb,
							VecMath.computeNormal(vA, furthestPoint, vB));
				}

				faces.add(0, stichTriangle);
				newLightFaces.add(stichTriangle);

				// Upade adjacents map
				adjacentsMap.put(vertIDa, addArrayEntry(adjacentsMap.get(vertIDa), furthestPointID));
			} else {
				System.out.println("Stitchingerror!"); // TODO: investigate?
			}
			furthestPointNeighbours[i] = vertIDa; // TODO: maybe put back in
													// if above?? (DANGER!)
		}

		// Update adjacents map for new point
		System.out.println("new furthest point: " + furthestPointID);
		adjacentsMap.put(furthestPointID, furthestPointNeighbours);

		// 5. Assign all points of all light-faces to the new created faces
		System.out.println(facepoints.size());
		for (Triangle tri : newLightFaces) {
			listsOfFacePoints.add(0, getLightPoints(tri, facepoints));
		}
		System.out.println("Points: ");
		for (int i = 0; i < listsOfFacePoints.size(); i++) {
			System.out.println(i + " : " + listsOfFacePoints.get(i).size());
		}

		// 6. Push new created faces on the stack and start at (1))

		// ///////// CHECK ADJS
		System.out.println("CHECK ADJS");
		for (int i = 0; i < vertices.size(); i++) {
			System.out.print(i + "(" + adjacentsMap.get(i).length + "): ");
			for (Integer a : adjacentsMap.get(i)) {
				System.out.print(" " + a);
				if (a == null)
					System.exit(0);
			}
			System.out.println();
		}

		step2ready = false;
	}

	private static Integer[] removeArrayEntry(Integer[] array, int remove) {
		Integer[] result = new Integer[array.length - 1];
		int a = 0;
		for (Integer i : array) {
			System.out.println(i + "; " + remove);
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

	private List<Vector3f> getLightPoints(Triangle triangle, List<Vector3f> points) {
		Vector3f A = vertices.get(triangle.a);
		LinkedList<Vector3f> result = new LinkedList<Vector3f>();
		for (int i = points.size() - 1; i >= 0; i--) {
			Vector3f p = points.get(i);
			if (VecMath.dotproduct(triangle.normal, VecMath.subtraction(p, A)) > 0) {
				result.add(p);
				points.remove(i);
			}
		}
		return result;
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
		for (Vector3f v : obj.getVertices())
			points.add(v);
		// points.add(new Vector3f(11.404785, 10.446343, -2.761249));
		// points.add(new Vector3f(5.6260934, 5.6621804, 11.700975));
		// points.add(new Vector3f(6.812222, -1.6368495, 9.790112));
		// points.add(new Vector3f(2.2193725, -1.7034389, -4.2405186));
		// points.add(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		vertices = new ArrayList<Vector3f>();

		System.out.println(points.size());
		pointcloud = new PointCloud(points);
		hullInit();

		facesDone = 1;
		for (int i = 0; i < 60; i++) { // 112 /// Aktuelles Problem Iteration:
										// 64 -> 65 Adj zwischen 2 und 58 geht
										// verloren
			faceIndex = faces.size() - facesDone;
			Triangle t = faces.get(faceIndex);
			hullStep(t);
			if (step2ready) {
				step2(t);
			}
		}
		faceIndex = faces.size() - facesDone;

		simplex = new Simplex(vertices, faces, faces.get(faceIndex));
		defaultshader.addObject(simplex);
		System.out.println(points.size());
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
				if (!step2ready) {
					mT = faces.get(faceIndex);
					hullStep(mT);
				} else {
					step2(mT);
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
			Text t = new Text(i + "", 0, 0, font, 0.3f);
			t.scale(1, -1);
			Matrix4f mat = t.getMatrix();
			mat.translateTo(vertices.get(i));
			mat.store(t.getMatrixBuffer());
			t.getMatrixBuffer().flip();
			t.prerender();
			defaultshader.addObject(t);
			texts.add(t);
		}
		// Duplicationcheck
		for (int i = 0; i < vertices.size(); i++) {
			Vector3f v = vertices.get(i);
			for (int j = i + 1; j < vertices.size(); j++) {
				Vector3f v2 = vertices.get(j);
				if (v.equals(v2)) {
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
