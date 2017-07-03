package debug_ConvexHull4;

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
import shape.Sphere;
import sound.NullSoundEnvironment;
import utils.Debugger;
import utils.GLConstants;
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
		listsOfFacePoints = new ArrayList<List<Vector3f>>();
		for (int i = 0; i < faces.size(); i++) {
			listsOfFacePoints.add(getLightPoints(faces.get(i), points));
		}
		/*System.out.println("Points: ");
		for (int i = 0; i < listsOfFacePoints.size(); i++) {
			System.out.println(i + " : " + listsOfFacePoints.get(i).size());
		}*/
		// 3. Push the 4 faces on the stack (done above)
	}
	
	List<Triangle> faces;

	List<Vector3f> points, vertices;

	HashMap<Integer, Integer[]> adjacentsMap;

	ArrayList<List<Vector3f>> listsOfFacePoints;
	
	Vector3f furthestPoint;
	
	int furthestPointID;
	
	HashSet<Integer> lightFaceVertices;
	
	HashMap<Integer, List<Triangle>> lightFaceVerticesToTriangles;
	
	List<Vector3f> facepoints;
	
	HashSet<Integer> vertsOnEdge;

	List<Integer> edge;
	
	List<Triangle> newLightFaces;
	
	Integer[] furthestPointNeighbours;
	
	public void step1(Triangle t) {
		System.out.println("Step 1 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		// 2. Get most distant point of the face's point set
		furthestPoint = null;
		furthestPointID = -1;
		Vector3f A = vertices.get(t.a);
		facepoints = listsOfFacePoints.get(faceIndex);
		float distance = 0;
		for(int i = 0; i < facepoints.size(); i++) {
			Vector3f P = facepoints.get(i);
			Vector3f PA = VecMath.subtraction(P, A);
			float dist = VecMath.dotproduct(PA, t.normal);
			if (dist >= distance) {
				distance = dist;
				furthestPoint = P;
				furthestPointID = i;
			}
		}
		
		// DEBUG
		if(furthestPoint != null)
			furthestPointSphere.translateTo(furthestPoint);
		// DEBUG
	}

	public void step2(Triangle t) {
		System.out.println("Step 2 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		facepoints.remove(furthestPointID);
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
		lightFaceVertices = new HashSet<Integer>(); // TODO: remove?
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
			if (VecMath.dotproduct(tri.normal, triP) > 0) {
				faces.remove(i);
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
		// lightFaceVerticesToTriangles: maps removed vertices to their corresponding triangles
		// facepoints: facepoints of removed triangles to be assigned to new triangles or discarded if inside volume
	}
	
	// 4. Extract horizon edges of light-faces and extrude to Point
	
	public void step3(Triangle t) {
		System.out.println("Step 3 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		// 4.0 Remove all vertices that are only connected to lightFaceVertices
		Iterator<Integer> iter = lightFaceVertices.iterator();
		List<Integer> toRemove = new ArrayList<Integer>();
		for (int i = 0; i < lightFaceVertices.size(); i++) {
			int vert = iter.next(); // TODO: check
			if (lightFaceVerticesToTriangles.get(vert).size() == adjacentsMap.get(vert).length) {
				toRemove.add(vert);
			}
		}
		int s = 0; // DEBUG
		for (Integer i : toRemove) {
			for (Integer adj : adjacentsMap.get(i)) {
				adjacentsMap.put(adj, removeArrayEntry(adjacentsMap.get(adj), i));
			}
			lightFaceVertices.remove(i);
			// DEBUG
			removeUnconnectedVerticesSpheres.get(s).translateTo(vertices.get(i));
			s++;
			// DEBUG
			vertices.remove(i);
		}
	}
	
	public void step4(Triangle t) {
		System.out.println("Step 4 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		// 4.1 Get vertices on border between lit and unlit triangles
		vertsOnEdge = new HashSet<Integer>();
		int s = 0; // DEBUG
		for (Integer vert : lightFaceVertices) {
			// if (lightFaceVerticesToTriangles.get(vert).size() !=
			// adjacentsMap.get(vert).length) { (Wird oben schon entfernt)
			vertsOnEdge.add(vert);
			// }
			//DEBUG
			vertsOnEdgeSpheres.get(s).translateTo(vertices.get(vert));
			s++;
			//DEBUG
		}
	}
	
	public void step5(Triangle t) {
		System.out.println("Step 5 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		// 4.2 Get edges on border
		int currentVert = vertsOnEdge.iterator().next();
		edge = new ArrayList<Integer>(); // TODO: make HashSet (no! has to be ordered list!)
		for (int i = 0; i < vertsOnEdge.size(); i++) {
			edge.add(currentVert);
			Integer[] adjs = adjacentsMap.get(currentVert);

			List<Triangle> vertexLightTriangles = lightFaceVerticesToTriangles.get(currentVert);
			for (int j = 0; j < adjs.length; j++) {
				Integer currAdj = adjs[j];
				if (vertsOnEdge.contains(currAdj) && !edge.contains(currAdj)) {
					int tricount = 0;
					for (int k = 0; k < vertexLightTriangles.size() && tricount < 2; k++) {
						Triangle kTri = vertexLightTriangles.get(k);
						if (kTri.a == currAdj || kTri.b == currAdj || kTri.c == currAdj) {
							tricount++;
						}
					}
					if (tricount == 1) {
						currentVert = adjs[j];
						break;
					}
				}
			}
		}
		
		// DEBUG
		for(int i = 0; i < edge.size(); i++) {
			Integer e = edge.get(i);
			if(i < edge.size() - 1) {
				edgeSpheres.get(i).translateTo(VecMath.scale(VecMath.addition(vertices.get(e), vertices.get(edge.get(i+1))), 0.5f));
			}
			else {
				edgeSpheres.get(i).translateTo(VecMath.scale(VecMath.addition(vertices.get(e), vertices.get(edge.get(0))), 0.5f));
			}
		}
		// DEBUG
	}
	
	public void step6(Triangle t) {
		System.out.println("Step 6 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		// 4.2.1 remove old adjacents (crossing triangle hole)
		int edgesize = edge.size();
		int edgesizeMinusOne = edgesize - 1;
		for (int i = 0; i < edgesize; i++) {
			int currentVert = edge.get(i);
			List<Integer> removeAdj = new ArrayList<Integer>();
			for (Integer adj : adjacentsMap.get(currentVert)) {
				if (edge.contains(adj)) {
					int adjIndexOnEdge = edge.indexOf(adj);
					if (Math.abs(i - adjIndexOnEdge) > 1 && !(i == 0 && adjIndexOnEdge == edgesizeMinusOne)
							&& !(i == edgesizeMinusOne && adjIndexOnEdge == 0)) {
						//System.out.println("Diff: " + i + "; " + edge.indexOf(adj) + "; " + (i - edge.indexOf(adj)));
						removeAdj.add(adj);
					}
				}
			}
			for (Integer removAdjacent : removeAdj) { // TODO: make faster
				//System.out.println("Remove adj " + removAdjacent + "; from vert " + currentVert);
				adjacentsMap.put(currentVert, removeArrayEntry(adjacentsMap.get(currentVert), removAdjacent));
			}
		}
	}
	
	public void step7(Triangle t) {
		System.out.println("Step 7 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		// 4.3 Stitch holes using edge
		newLightFaces = new ArrayList<Triangle>();
		furthestPointNeighbours = new Integer[edge.size()];
		for (int i = 0; i < edge.size(); i++) {
			int vertIDa, vertIDb;
			if (i < edge.size() - 1) {
				vertIDa = edge.get(i);
				vertIDb = edge.get(i + 1);
			} else {
				vertIDa = edge.get(i);
				vertIDb = edge.get(0);
			}

			//System.out.println("Stitch!! " + i + "; " + vertIDa + "; " + vertIDb);
			if (vertIDa != vertIDb) {
				// TODO: correct?
				Vector3f vA = vertices.get(vertIDa);
				Vector3f vB = vertices.get(vertIDb);
				Vector3f norm = VecMath.computeNormal(vA, vB, furthestPoint);
				Triangle stichTriangle;
				//System.out.println(VecMath.dotproduct(t.normal, norm));
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
	}
	
	public void step8(Triangle t) {
		System.out.println("Step 8 " + t.a + "; " + t.b + "; " + t.c + "; " + faceIndex);
		// 5. Assign all points of all light-faces to the new created faces
		adjacentsMap.put(furthestPointID, furthestPointNeighbours);

		// 5. Assign all points of all light-faces to the new created faces
		//System.out.println(facepoints.size());
		for (Triangle tri : newLightFaces) {
			listsOfFacePoints.add(0, getLightPoints(tri, facepoints));
		}

		// 6. Push new created faces on the stack and start at (1))

		// ///////// CHECK ADJS
		//System.out.println("CHECK ADJS");
		for (int i = 0; i < vertices.size(); i++) {
			//System.out.print(i + "(" + adjacentsMap.get(i).length + "): ");
			for (Integer a : adjacentsMap.get(i)) {
				//System.out.print(" " + a);
				if (a == null)
					System.exit(0);
			}
			//System.out.println();
		}
		
		// DEBUG
		for(Sphere s : removeUnconnectedVerticesSpheres)
			s.translateTo(10000, 0, 0);
		for(Sphere s : vertsOnEdgeSpheres)
			s.translateTo(10000, 0, 0);
		for(Sphere s : edgeSpheres)
			s.translateTo(10000, 0, 0);
		// DEBUG
	}
	
	private Vector3f[] getExtremePoints(List<Vector3f> points) {
		Vector3f[] result = new Vector3f[6];
		
		Vector3f initial = points.get(0);
		result[0] = initial;
		result[1] = initial;
		result[2] = initial;
		result[3] = initial;
		result[4] = initial;
		result[5] = initial;
		
		for(Vector3f p : points) {
			if(p.x < result[0].x) {
				result[0] = p;
			}
			if(p.y < result[1].y) {
				result[1] = p;
			}
			if(p.z < result[2].z) {
				result[2] = p;
			}
			if(p.x > result[3].x) {
				result[3] = p;
			}
			if(p.y > result[4].y) {
				result[4] = p;
			}
			if(p.z > result[5].z) {
				result[5] = p;
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
		
		int colorShaderHandle = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag");
		
		Shader colorshader = new Shader(colorShaderHandle);
		colorshader.addArgument("u_color", new Vector4f(1f, 0f, 0f, 1f));
		addShader(colorshader);
		
		furthestPointSphere = new Sphere(10000, 0, 0, 0.1f, 36, 36);
		colorshader.addObject(furthestPointSphere);
		
		Shader colorshader2 = new Shader(colorShaderHandle);
		colorshader2.addArgument("u_color", new Vector4f(0f, 1f, 0f, 1f));
		addShader(colorshader2);
		
		removeUnconnectedVerticesSpheres = new ArrayList<Sphere>();
		for(int i = 0; i < 100; i++) {
			Sphere s = new Sphere(10000, 0, 0, 0.1f, 36, 36);
			colorshader2.addObject(s);
			removeUnconnectedVerticesSpheres.add(s);
		}
		
		Shader colorshader3 = new Shader(colorShaderHandle);
		colorshader3.addArgument("u_color", new Vector4f(0f, 0f, 1f, 1f));
		addShader(colorshader3);
		
		vertsOnEdgeSpheres = new ArrayList<Sphere>();
		for(int i = 0; i < 100; i++) {
			Sphere s = new Sphere(10000, 0, 0, 0.1f, 36, 36);
			colorshader3.addObject(s);
			vertsOnEdgeSpheres.add(s);
		}
		
		Shader colorshader4 = new Shader(colorShaderHandle);
		colorshader4.addArgument("u_color", new Vector4f(1f, 1f, 0f, 1f));
		addShader(colorshader4);
		
		edgeSpheres = new ArrayList<Sphere>();
		for(int i = 0; i < 100; i++) {
			Sphere s = new Sphere(10000, 0, 0, 0.1f, 36, 36);
			colorshader4.addObject(s);
			edgeSpheres.add(s);
		}

		System.out.println(points.size());
		pointcloud = new PointCloud(points);
		hullInit();

		facesDone = 1;
		for (int i = 0; i < 0; i++) { // 112 /// Aktuelles Problem Iteration:
										// 64 -> 65 Adj zwischen 2 und 58 geht
										// verloren
			faceIndex = faces.size() - facesDone;
			Triangle t = faces.get(faceIndex);
			step1(t);
			if(furthestPointID != -1) {
				step2(t);
				step3(t);
				step4(t);
				step5(t);
				step6(t);
				step7(t);
				step8(t);
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
				switch(currentstep) {
				case 0:
					mT = faces.get(faceIndex);
					step1(mT);
					if(furthestPointID != -1)
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
