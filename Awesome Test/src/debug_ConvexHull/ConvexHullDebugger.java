package debug_ConvexHull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import collisionshape.ConvexShape;
import convexhull.Quickhull;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import math.VecMath;
import objects.ShapedObject;
import objects.ShapedObject3;
import shader.Shader;
import sound.NullSoundEnvironment;
import utils.Debugger;
import utils.GLConstants;
import utils.VectorConstants;
import vector.Vector2f;
import vector.Vector3f;

public class ConvexHullDebugger extends StandardGame {
	public class Triangle {
		Vector3f a, b, c, normal;
		float distance;

		public Triangle(Vector3f a, Vector3f b, Vector3f c) {
			this.a = a;
			this.b = b;
			this.c = c;
			normal = VecMath.computeNormal(a, b, c);
			if(normal.lengthSquared() > 0)
				normal.normalize();
		}
	}

	Shader defaultshader;

	Debugger debugger;

	InputEvent toggleMouseBind;

	Simplex simplex;

	PointCloud pointcloud;

	List<Triangle> faces;
	int currT = 0;

	List<Vector3f> points, vertices;

	public void hullInit() {
		int a = getFurthestPoint(VectorConstants.AXIS_X, points);
		Vector3f A = points.remove(a);

		int b = getFurthestPoint(VectorConstants.AXIS_Y, points);
		Vector3f B = points.remove(b);

		int c = getFurthestPoint(VectorConstants.AXIS_Z, points);
		Vector3f C = points.remove(c);

		Triangle base = new Triangle(A, B, C);
		int furthest = getFurthestPoint(base, points);
		Vector3f D = points.remove(furthest);

		faces = new ArrayList<Triangle>();
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

		int iters = 106; // 170 (Successful: 50, 105, ???: 106, Failed: 170)
		for(int i = 0; i < iters; i++)//170
			hullStep(i == iters-1);
		finishStep();
//		System.out.println("Stats: " + faces.size() + "; " + vertices.size() + "; " + adjacentsMap.size());
		
		//CHECK with algo
		ConvexShape shape = Quickhull.computeConvexHull(points, iters);
		System.out.println("-------------START COMPARISON----------------");
		List<Vector3f> algoVerts = shape.getVertices();
		HashMap<Integer, Integer[]> algoAdjacents = shape.getAdjacentsMap();
		for(int i = 0; i < vertices.size(); i++) {
			if(!vertices.get(i).equals(algoVerts.get(i))) {
				System.out.println("Vert " + i + ": " + vertices.get(i) + "; " + algoVerts.get(i));
			}
		}
		for(int i = 0; i < adjacentsMap.size(); i++) {
			Integer[] adjs = adjacentsMap.get(i);
			Integer[] algoAdjs = algoAdjacents.get(i);
			if(adjs.length == algoAdjs.length) {
				for(int j = 0; j < adjs.length; j++) {
					if(adjs[j] != algoAdjs[j]) {
						System.out.println("Adj " + i + ", " + j + ": " + adjs[j] + "; " + algoAdjs[j]);
					}
				}
			}
			else {
				System.out.println("Adjs array length mismatch: " + i + "; " + adjs.length + "; " + algoAdjs.length);
			}
		}
		if(algoVerts.size() != vertices.size()) System.out.println("Vertexcount mismatch.");
		if(algoAdjacents.size() != adjacentsMap.size()) System.out.println("Adjacentsmapcount mismatch.");
		System.out.println("END COMPARISON ---");
		
		simplex = new Simplex(faces, faces.get(0));
		defaultshader.addObject(simplex);
		// removePoints(faces, points);
	}

	HashMap<Integer, Integer[]> adjacentsMap;
	
	public void hullStep(boolean last) {
		// removePoints(faces, points);
		if (currT == faces.size()) {
			System.out.println("DONE");
			System.out.println("-------------------");
			finishStep();
			for (Vector3f v : vertices)
				System.out.println(v);
			for (int i = 0; i < vertices.size(); i++) {
				for (Integer a : adjacentsMap.get(i))
					System.out.print(a + "; ");
				System.out.println();
			}
			System.out.println("---");
			for (Triangle t : faces) {
				System.out
						.println(vertices.indexOf(t.a) + " | " + vertices.indexOf(t.b) + " | " + vertices.indexOf(t.c));
			}

			System.exit(0);
		}
		Triangle t = faces.get(currT);
		int furthest = getFurthestPointDirection(t, points);
		if (furthest != -1) {
			Vector3f p = points.remove(furthest);

			faces.add(new Triangle(t.a, t.b, p));
			faces.add(new Triangle(t.b, t.c, p));
			faces.add(new Triangle(t.c, t.a, p));

			faces.remove(t);

			// check convex
			for (int i = 0; i < faces.size(); i++) {
				Triangle f = faces.get(i);
				Triangle[] adjs = findAdjacentTriangles(f, faces, last);
				if(last)
					System.out.println(faces.size() + "; " + adjs.length + "; " + adjs[1] + "; " + f.a + "; " + f.b + "; " + f.c + "; " + adjs[1].a + "; " + adjs[1].b + "; " + adjs[1].c + "; " + adjs[1].normal);
				if (adjs[0] != null && VecMath.dotproduct(VecMath.subtraction(f.c, f.a), adjs[0].normal) > 0) {
					if(last)
						System.out.println("DEB1");
					Vector3f adjD = findTheD(f.a, f.b, adjs[0]);
					faces.add(new Triangle(f.c, f.a, adjD));
					faces.add(new Triangle(f.b, f.c, adjD));
					faces.remove(i);
					faces.remove(adjs[0]);
					i--;
				} else if (adjs[1] != null && VecMath.dotproduct(VecMath.subtraction(f.a, f.b), adjs[1].normal) > 0) {
					if(last)
						System.out.println("DEB2");
					Vector3f adjD = findTheD(f.b, f.c, adjs[1]);
					faces.add(new Triangle(f.a, f.b, adjD));
					faces.add(new Triangle(f.c, f.a, adjD));
					faces.remove(i);
					faces.remove(adjs[1]);
					i--;
				} else if (adjs[2] != null && VecMath.dotproduct(VecMath.subtraction(f.b, f.c), adjs[2].normal) > 0) {
					if(last)
						System.out.println("DEB3");
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
	
	private void finishStep() {
		adjacentsMap = new HashMap<Integer, Integer[]>();
		List<Integer> adjs = new ArrayList<Integer>();
		for (Triangle f : faces) {
			if (!vertices.contains(f.a))
				vertices.add(f.a);
			if (!vertices.contains(f.b))
				vertices.add(f.b);
			if (!vertices.contains(f.c))
				vertices.add(f.c);
		}
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
	}

	// private static void removePoints(List<Triangle> faces, List<Vector3f>
	// points) {
	// List<Vector3f> removePoints = new ArrayList<Vector3f>();
	// for(Vector3f p : points) {
	// boolean outside = false;
	// for(Triangle t : faces) {
	// if(VecMath.dotproduct(t.normal, VecMath.subtraction(t.a, p)) > 0) {
	// outside = true;
	// break;
	// }
	// }
	// if(!outside) removePoints.add(p);
	// }
	// points.removeAll(removePoints);
	// }

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
			float dist = Math.abs(VecMath.dotproduct(t.normal, VecMath.subtraction(points.get(i), t.a)));
			if (dist > distance) {
				distance = dist;
				pointID = i;
			}
		}
		return pointID;
	}

	private static int getFurthestPointDirection(Triangle t, List<Vector3f> points) {
		float distance = 0;
		int pointID = -1;
		for (int i = 0; i < points.size(); i++) {
			float dist = VecMath.dotproduct(t.normal, VecMath.subtraction(points.get(i), t.a));
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
	private static Triangle[] findAdjacentTriangles(Triangle t, List<Triangle> faces, boolean last) {
		Triangle[] result = new Triangle[3];
		int i = 0;
		for (Triangle f : faces) {
//			System.out.println("a");
			if (!f.equals(t)) {
//				System.out.println("b");
//				System.out.println(f.a.equals(t.b) && f.b.equals(t.a));
				if (f.a.equals(t.b) && f.b.equals(t.a) || f.b.equals(t.b) && f.c.equals(t.a)
						|| f.c.equals(t.b) && f.a.equals(t.a)){
					result[0] = f;if(last)System.out.println("A" + i + " " + f.a + "; " + f.b + "; " + f.c + "; " + t.a + "; " + t.b + "; " + t.c);}
				if (f.a.equals(t.c) && f.b.equals(t.b) || f.b.equals(t.c) && f.c.equals(t.b)
						|| f.c.equals(t.c) && f.a.equals(t.b)){
					result[1] = f;if(last)System.out.println("B" + i + " " + f.a + "; " + f.b + "; " + f.c + "; " + t.a + "; " + t.b + "; " + t.c);}
				if (f.a.equals(t.a) && f.b.equals(t.c) || f.b.equals(t.a) && f.c.equals(t.c)
						|| f.c.equals(t.a) && f.a.equals(t.c)){
					result[2] = f;if(last)System.out.println("C" + i + " " + f.a + "; " + f.b + "; " + f.c + "; " + t.a + "; " + t.b + "; " + t.c);}
			}
			i++;
		}
//		System.out.println(result[0] + "; " + result[1] + "; " + result[2]);
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
		cam.setFlySpeed(0.01f);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		// Input to step EPA
		InputEvent stepEPA = new InputEvent("Step Hull", new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(stepEPA);

		points = new ArrayList<Vector3f>();
//		 for (int i = 0; i < 1000; i++) {
//		 points.add(new Vector3f(Math.random() * 10 - 5,
//		 Math.random() * 10 - 5, Math.random() * 10 - 5));
//		 }
		ShapedObject3 obj = ModelLoader.load("res/models/bunny_lowpoly.mobj");
		for(Vector3f v : obj.getVertices())
			points.add(v);
//		points.add(new Vector3f(11.404785, 10.446343, -2.761249));
//		points.add(new Vector3f(5.6260934, 5.6621804, 11.700975));
//		points.add(new Vector3f(6.812222, -1.6368495, 9.790112));
//		points.add(new Vector3f(2.2193725, -1.7034389, -4.2405186));
//		points.add(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		vertices = new ArrayList<Vector3f>();

		hullInit();
		pointcloud = new PointCloud(points);
		defaultshader.addObject(pointcloud);
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

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("Step Hull")) {
			simplex.delete();
			defaultshader.removeObject(simplex);
			if(points.size() > 1){
				pointcloud.delete();
				defaultshader.removeObject(pointcloud);
			}
			
			hullStep(false);
			
			if (currT < faces.size())
				simplex = new Simplex(faces, faces.get(currT));
			else
				simplex = new Simplex(faces, null);
			defaultshader.addObject(simplex);
			if(points.size() > 0) {
				pointcloud = new PointCloud(points);
				defaultshader.addObject(pointcloud);
			}
			System.out.println("NUM FACES : " + faces.size());
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
