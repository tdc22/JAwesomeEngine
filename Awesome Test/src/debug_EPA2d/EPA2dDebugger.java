package debug_EPA2d;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.util.ArrayList;
import java.util.List;

import loader.FontLoader;
import loader.ShaderLoader;
import math.VecMath;
import narrowphase.EmptyManifoldGenerator2;
import narrowphase.GJK2;
import objects.RigidBody2;
import objects.SupportMap;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import physics2dSupportFunction.SupportDifferenceObject;
import quaternion.Quaternionf;
import shader.Shader;
import shape2d.Quad;
import utils.Debugger;
import vector.Vector2f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class EPA2dDebugger extends StandardGame {
	public class Edge {
		Vector2f normal, a, b;
		float distance;
		int index;

		public Edge() {
			normal = new Vector2f();
		}
	}

	PhysicsSpace2 space;
	Debugger debugger;
	RigidBody2 rb1, rb2;
	Simplex simplex;
	int iter = 0;

	SupportDifferenceObject support1;
	Shader defaultshader;

	InputEvent toggleMouseBind;

	// ------------------- EPA2d ---------------------
	List<Vector2f> edges;

	Vector2f normal = new Vector2f();

	float depth = 0;

	SupportMap<Vector2f> Sa;
	SupportMap<Vector2f> Sb;
	private final float TOLERANCE = 0.05f;

	public void epaInit(List<Vector2f> gjksimplex) {
		Sa = rb1;
		Sb = rb2;

		edges = new ArrayList<Vector2f>(gjksimplex);

		// System.out.println("------------------------------------");
		// System.out.println(A + "; " + B + "; " + C + "; " + D);

		simplex = new Simplex(edges, findClosestEdge(edges));
	}

	public void epaStep() {
		Edge e = findClosestEdge(edges);
		Vector2f p = support(Sa, Sb, e.normal);
		double d = VecMath.dotproduct(p, e.normal);
		if (d - e.distance < TOLERANCE) {
			normal = e.normal;
			depth = (float) d;
			// END
			System.out.println("End. (" + iter + ") " + normal);
		} else {
			edges.add(e.index, p);
		}
		iter++;
	}

	private Vector2f edgeDirection(Vector2f edge, Vector2f origin) {
		Vector2f a = new Vector2f(-edge.y, edge.x);
		if (VecMath.dotproduct(a, origin) > 0)
			return a;
		a.negate();
		return a;
	}

	/**
	 * Finds the closest feature to the origin on the Minkowski Difference.
	 */
	private Edge findClosestEdge(List<Vector2f> simplex) {
		Edge closest = new Edge();
		closest.distance = Float.MAX_VALUE;
		for (int i = 0; i < simplex.size(); i++) {
			int j = i + 1 == simplex.size() ? 0 : i + 1;
			Vector2f a = simplex.get(i);
			Vector2f b = simplex.get(j);
			Vector2f e = VecMath.subtraction(b, a);
			Vector2f n = edgeDirection(e, a);
			n.normalize();
			double d = VecMath.dotproduct(n, a);
			if (d < closest.distance) {
				closest.distance = (float) d;
				closest.normal = n;
				closest.index = j;
				closest.a = a;
				closest.b = b;
			}
		}
		return closest;
	}

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb,
			Vector2f dir) {
		return VecMath.subtraction(Sa.supportPoint(dir),
				Sb.supportPointNegative(dir));
	}

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();

		Shader defaultshader3 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader(defaultshader3);
		defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		add2dShader(defaultshader);

		debugger = new Debugger(inputs, defaultshader3, defaultshader,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		cam.setFlyCam(true);
		cam.translateTo(0, 1, 3);
		cam.setFlySpeed(0.01f);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		// Test 5
		Quad s1 = new Quad(120, 50, 20, 20);
		s1.setRotation(new Quaternionf(1.0, 0.0, 0.0, 0.0));
		rb1 = new RigidBody2(PhysicsShapeCreator.create(s1));

		Quad s2 = new Quad(95.99999f, 43.0043f, 5, 6);
		s2.setRotation(new Quaternionf(1.0, 0.0, 0.0, 0.0));
		rb2 = new RigidBody2(PhysicsShapeCreator.create(s2));

		// Fix transformation (usually done in PhysicsSpace-class of Engine
		rb1.setRotation(s1.getRotation());
		rb1.setTranslation(s1.getTranslation());

		rb2.setRotation(s2.getRotation());
		rb2.setTranslation(s2.getTranslation());

		rb1.updateInverseRotation();
		rb2.updateInverseRotation();

		// Visualize the support functions
		support1 = new SupportDifferenceObject(s1, rb1, s2, rb2);
		support1.translate(200, 200);
		defaultshader.addObject(support1);

		// Compute simplex as starting point for EPA
		GJK2 gjk = new GJK2(new EmptyManifoldGenerator2());
		gjk.isColliding(rb1, rb2);

		// init EPA
		epaInit(gjk.getSimplex());

		// Input to step EPA
		InputEvent stepEPA = new InputEvent("Step EPA", new Input(
				Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(stepEPA);
	}

	@Override
	public void render() {
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("Step EPA")) {
			epaStep();
			simplex.delete();
			simplex = new Simplex(edges, findClosestEdge(edges));
			if (!defaultshader.getObjects().contains(simplex))
				defaultshader.addObject(simplex);
		}
		debugger.update(fps, 0, 0);
		cam.update(delta);

		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}
}
