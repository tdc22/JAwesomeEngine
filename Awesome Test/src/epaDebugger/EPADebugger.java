package epaDebugger;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.util.ArrayList;
import java.util.List;

import loader.FontLoader;
import math.VecMath;
import narrowphase.EmptyManifoldGenerator;
import narrowphase.GJK;
import objects.RigidBody3;
import objects.SupportMap;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import physicsSupportFunction.SupportDifferenceObject;
import quaternion.Quaternionf;
import shape.Box;
import utils.Debugger;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class EPADebugger extends StandardGame {
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

	PhysicsSpace space;
	Debugger debugger;
	RigidBody3 rb1, rb2;
	Simplex simplex;

	SupportDifferenceObject support1;

	InputEvent toggleMouseBind;

	// ------------------- EPA ---------------------
	List<Triangle> faces;

	Vector3f normal = new Vector3f();

	float depth = 0;

	SupportMap<Vector3f> Sa;
	SupportMap<Vector3f> Sb;
	private final float TOLERANCE = 0.001f;
	private final int MAX_ITERATIONS = 50;

	public void epaInit(List<Vector3f> gjksimplex) {
		Sa = rb1;
		Sb = rb2;

		faces = new ArrayList<Triangle>();

		System.out.println(gjksimplex.get(0) + "; " + gjksimplex.get(1) + "; "
				+ gjksimplex.get(2));

		Vector3f A = gjksimplex.get(3);
		Vector3f B = gjksimplex.get(2);
		Vector3f C = gjksimplex.get(1);
		Vector3f D = gjksimplex.get(0);
		faces.add(new Triangle(A, B, C));
		faces.add(new Triangle(A, D, B));
		faces.add(new Triangle(A, C, D));
		faces.add(new Triangle(D, C, B));

		simplex = new Simplex(faces, findClosestTriangle(faces));
	}

	public void epaStep() {
		Triangle t = findClosestTriangle(faces);
		// System.out.println(faces.size() + "; " + t.normal + "; "
		// + VecMath.dotproduct(t.normal, VecMath.negate(t.a)));
		// System.out.println(t.normal);

		if (isOriginInsideTriangleArea(t)) {
			Vector3f p = support(Sa, Sb, t.normal);
			// System.out.println(t.normal);
			double d = VecMath.dotproduct(p, t.normal);
			System.out.println(d - t.distance + "; " + p);
			if (d - t.distance < TOLERANCE) {
				normal = t.normal;
				depth = (float) d;
				// System.out.println("res: " + normal + "; " + depth + "; "
				// + t.a + "; " + t.b + "; " + t.c);
				return;
			} else {
				// Check if convex!!!
				Triangle[] adjacents = findAdjacentTriangles(t, faces);
				for (Triangle a : adjacents) {
					if (a != null)
						System.out.println("NOTNULL " + a.a + "; " + a.b + "; "
								+ a.c);
				}
				if (adjacents[0] != null
						&& VecMath.dotproduct(VecMath.subtraction(p, t.a),
								adjacents[0].normal) > 0) {
					Vector3f adjD = findTheD(t.a, t.b, adjacents[0]);
					faces.add(new Triangle(p, t.a, adjD));
					faces.add(new Triangle(t.b, p, adjD));
					faces.remove(adjacents[0]);
				} else {
					faces.add(new Triangle(t.a, t.b, p));
				}

				if (adjacents[1] != null
						&& VecMath.dotproduct(VecMath.subtraction(p, t.b),
								adjacents[1].normal) > 0) {
					Vector3f adjD = findTheD(t.b, t.c, adjacents[1]);
					faces.add(new Triangle(p, t.b, adjD));
					faces.add(new Triangle(t.c, p, adjD));
					faces.remove(adjacents[1]);
				} else {
					faces.add(new Triangle(t.b, t.c, p));
				}

				if (adjacents[2] != null
						&& VecMath.dotproduct(VecMath.subtraction(p, t.c),
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
		faces.remove(t);
		if (faces.size() == 0) {
			System.out.println("ERROR");
			return; // break replaced with return
		}
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

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());

		display.bindMouse();
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		cam.setFlyCam(true);
		cam.translateTo(0, 1, 3);
		cam.setFlySpeed(0.01f);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		// Box b1 = new Box(-8.960001f, 8.190001f, 0.0f, 1f, 1f, 1f);
		// b1.setRotation(new Quaternionf(0.87097573f, -0.41262922f,
		// 0.26483604f,
		// 0.03165175f));
		// rb1 = new RigidBody3(PhysicsShapeCreator.create(b1));
		//
		// Sphere s1 = new Sphere(-10, 10, 0, 1, 36, 36);
		// rb2 = new RigidBody3(PhysicsShapeCreator.create(s1));

		// Test 2:
		// Box b1 = new Box(4.1700006f, 2.1599996f, 0.0f, 1f, 1f, 1f);
		// b1.setRotation(new Quaternionf(0.25023422f, -0.09507953f,
		// -0.8314483f,
		// -0.48689112f));
		// rb1 = new RigidBody3(PhysicsShapeCreator.create(b1));
		//
		// Box s1 = new Box(4, 0, 0, 1.5f, 1.5f, 1.5f);
		// rb2 = new RigidBody3(PhysicsShapeCreator.create(s1));
		//
		// // Fix transformation (usually done in PhysicsSpace-class of Engine
		// rb1.setRotation(b1.getRotation());
		// rb1.setTranslation(b1.getTranslation());
		//
		// rb2.setRotation(s1.getRotation());
		// rb2.setTranslation(s1.getTranslation());

		// Test 3
		// Sphere s1 = new Sphere(-0.033776358f, -2.2662005f, -0.03187143f,
		// 0.5f,
		// 36, 36);
		// s1.setRotation(new Quaternionf(1.0, -9.505826E-5, 1.3472783E-6,
		// 2.0184624E-4));
		// rb1 = new RigidBody3(PhysicsShapeCreator.create(s1));
		//
		// Sphere s2 = new Sphere(0.11460103f, -3.118881f, -0.0043758536f, 0.5f,
		// 36, 36);
		// s2.setRotation(new Quaternionf(0.99999946, 9.70781E-5, 2.2611355E-5,
		// -0.0011489653));
		// rb2 = new RigidBody3(PhysicsShapeCreator.create(s2));

		/*
		 * output: 4; Vector3f[0.5908022, 1.0959291, -0.57619476];
		 * Vector3f[-0.03338337, -1.4683976, -0.78323746]; Vector3f[-1.0176206,
		 * 0.1312356, 0.26089716]; Vector3f[1.1122894, -0.14214492,
		 * -0.13434744];|; Vector3f[8.088608, -3.203665, -7.5166597];
		 * Quaternionf[0.8605771, 0.37082738, 0.0139486715, -0.34885654];
		 * Vector3f[8.280044, -3.325496, -6.407919]; Quaternionf[0.9584382,
		 * 0.028309878, 0.061130494, -0.27723733] ERROR
		 */

		// Test 4
		/*
		 * Box s1 = new Box(8.088608f, -3.203665f, -7.5166597f, 0.5f, 0.5f,
		 * 0.5f); s1.setRotation(new Quaternionf(0.8605771, 0.37082738,
		 * 0.0139486715, -0.34885654)); rb1 = new
		 * RigidBody3(PhysicsShapeCreator.create(s1));
		 * 
		 * Box s2 = new Box(8.280044f, -3.325496f, -6.407919f, 0.5f, 0.5f,
		 * 0.5f); s2.setRotation(new Quaternionf(0.9584382, 0.028309878,
		 * 0.061130494, -0.27723733)); rb2 = new
		 * RigidBody3(PhysicsShapeCreator.create(s2));
		 */

		// Test 5
		Box s1 = new Box(0.0f, -3.6325386f, 0.0f, 0.5f, 0.5f, 0.5f);
		s1.setRotation(new Quaternionf(1.0, 0.0, 0.0, 0.0));
		rb1 = new RigidBody3(PhysicsShapeCreator.create(s1));

		Box s2 = new Box(0, -5, 0, 10, 1, 10);
		s2.setRotation(new Quaternionf(1.0, 0.0, 0.0, 0.0));
		rb2 = new RigidBody3(PhysicsShapeCreator.create(s2));

		// Fix transformation (usually done in PhysicsSpace-class of Engine
		rb1.setRotation(s1.getRotation());
		rb1.setTranslation(s1.getTranslation());

		rb2.setRotation(s2.getRotation());
		rb2.setTranslation(s2.getTranslation());

		// Visualize the support functions
		support1 = new SupportDifferenceObject(s1, rb1, s2, rb2);

		// Compute simplex as starting point for EPA
		GJK gjk = new GJK(new EmptyManifoldGenerator());
		gjk.isColliding(rb1, rb2);

		// init EPA
		epaInit(gjk.getSimplex());

		// Input to step EPA
		InputEvent stepEPA = new InputEvent("Step EPA", new Input(
				Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(stepEPA);
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

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		simplex.render();
		support1.render();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	private Vector3f support(SupportMap<Vector3f> Sa, SupportMap<Vector3f> Sb,
			Vector3f dir) {
		return VecMath.subtraction(Sa.supportPoint(dir),
				Sb.supportPoint(VecMath.negate(dir)));
	}

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("Step EPA")) {
			epaStep();
			simplex.delete();
			simplex = new Simplex(faces, findClosestTriangle(faces));
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
}
