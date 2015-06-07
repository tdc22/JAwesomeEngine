package debug_ConvexHull;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import loader.FontLoader;
import math.VecMath;
import objects.ShapedObject;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ConvexHullDebugger extends StandardGame {
	public class Triangle {
		Vector3f a, b, c, normal;
		float distance;

		public Triangle(Vector3f a, Vector3f b, Vector3f c) {
			this.a = a;
			this.b = b;
			this.c = c;
			normal = VecMath.normalize(VecMath.computeNormal(a, b, c));
		}
	}

	Debugger debugger;

	InputEvent toggleMouseBind;

	Simplex simplex;

	PointCloud pointcloud;

	// ------------------- EPA ---------------------
	List<Triangle> faces;
	int currT = 0;

	List<Vector3f> points, vertices;

	public void hullInit() {
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

		simplex = new Simplex(faces, faces.get(0));
		// removePoints(faces, points);
	}

	public void hullStep() {
		// removePoints(faces, points);
		if (currT == faces.size()) {
			System.out.println("DONE");
			System.exit(0);
		}
		Triangle t = faces.get(currT);
		int furthest = getFurthestPointDirection(t, points);
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
				} else if (VecMath.dotproduct(VecMath.subtraction(f.a, f.b),
						adjs[1].normal) > 0) {
					Vector3f adjD = findTheD(f.b, f.c, adjs[1]);
					faces.add(new Triangle(f.a, f.b, adjD));
					faces.add(new Triangle(f.c, f.a, adjD));
					faces.remove(i);
					faces.remove(adjs[1]);
					i--;
				} else if (VecMath.dotproduct(VecMath.subtraction(f.b, f.c),
						adjs[2].normal) > 0) {
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
			System.out.println("distance: " + dist);
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
			System.out.println("a");
			if (!f.equals(t)) {
				System.out.println("b");
				System.out.println(f.a.equals(t.b) && f.b.equals(t.a));
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
		System.out.println(result[0] + "; " + result[1] + "; " + result[2]);
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
		initDisplay(new GLDisplay(), new DisplayMode(),
				new PixelFormat().withSamples(0), new VideoSettings());

		display.bindMouse();
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		cam.setFlyCam(true);
		cam.translateTo(0, 1, 3);
		cam.setFlySpeed(0.01f);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		// Input to step EPA
		InputEvent stepEPA = new InputEvent("Step Hull", new Input(
				Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(stepEPA);

		points = new ArrayList<Vector3f>();
		for (int i = 0; i < 100; i++) {
			points.add(new Vector3f(Math.random() * 10 - 5,
					Math.random() * 10 - 5, Math.random() * 10 - 5));
		}
		vertices = new ArrayList<Vector3f>();

		hullInit();
		pointcloud = new PointCloud(points);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		simplex.render();
		pointcloud.render();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("Step Hull")) {
			hullStep();
			simplex.delete();
			pointcloud.delete();
			if (currT < faces.size())
				simplex = new Simplex(faces, faces.get(currT));
			else
				simplex = new Simplex(faces, null);
			pointcloud = new PointCloud(points);
			System.out.println("NUM FACES : " + faces.size());
		}
		debugger.update();

		if (display.isMouseBound())
			cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}

	private class PointCloud extends ShapedObject {
		public PointCloud(List<Vector3f> points) {
			setRenderMode(GLConstants.POINTS);
			for (int i = 0; i < points.size(); i++) {
				addVertex(points.get(i), Color.GRAY, new Vector2f(0, 0),
						new Vector3f(0, 1, 0));
				addIndex(i);
			}
			prerender();
		}
	}
}