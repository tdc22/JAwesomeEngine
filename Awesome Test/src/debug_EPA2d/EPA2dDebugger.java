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
import physics2dSupportFunction.SupportDifferenceObject2;
import physics2dSupportFunction.SupportObject2;
import quaternion.Complexf;
import shader.Shader;
import shape2d.Circle;
import shape2d.Ellipse;
import shape2d.Quad;
import utils.Debugger;
import vector.Vector2f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class EPA2dDebugger extends StandardGame {
	public class Edge {
		Vector2f a, b, normal;
		float distance;

		public Edge(Vector2f a, Vector2f b) {
			this.a = a;
			this.b = b;
			
			normal = new Vector2f(a.y - b.y, b.x - a.x);
			if (VecMath.dotproduct(normal, a) < 0)
				normal.negate();
			
			if (normal.lengthSquared() > 0)
				normal.normalize();
		}
	}

	PhysicsSpace2 space;
	Debugger debugger;
	RigidBody2 rb1, rb2;
	Simplex simplex;
	int iter = 0;

	SupportDifferenceObject2 support1;
	Shader defaultshader;

	InputEvent toggleMouseBind;

	// ------------------- EPA2d ---------------------
	List<Edge> edges;
	boolean done = false;

	Vector2f normal = new Vector2f();

	float depth = 0;

	SupportMap<Vector2f> Sa;
	SupportMap<Vector2f> Sb;
	private final float TOLERANCE = 0.01f;
	private final float EPSILON = 0.00f;

	public void epaInit(List<Vector2f> gjksimplex) {
		Sa = rb1;
		Sb = rb2;

		edges = new ArrayList<Edge>();

		Vector2f A = gjksimplex.get(0);
		Vector2f B = gjksimplex.get(1);
		Vector2f C = gjksimplex.get(2);

		edges.add(new Edge(A, B));
		edges.add(new Edge(B, C));
		edges.add(new Edge(C, A));

		simplex = new Simplex(edges, findClosestEdge(edges));
		defaultshader.addObject(simplex);
	}

	public void epaStep() {
		Edge e = findClosestEdge(edges);
		if(isOriginInsideEdgeArea(e)) {
			Vector2f p = support(Sa, Sb, e.normal);
			double d = VecMath.dotproduct(p, e.normal);
			if (d - e.distance < TOLERANCE) {
				normal = e.normal;
				depth = (float) d;
				System.out.println("END Iter: " + iter + "; " + normal + "; " + depth);
				return;
			} else {
				edges.add(new Edge(e.a, p));
				edges.add(new Edge(p, e.b));
			}
		}
		edges.remove(e);

		iter++;
	}

	private boolean isOriginInsideEdgeArea(Edge e) {
		return (checkEdge(e.a, e.normal) && checkEdge(e.b, e.normal));
	}

	private boolean checkEdge(Vector2f a, Vector2f normal) {
		return (-a.x * normal.x + -a.y * normal.y <= EPSILON);
	}

	/**
	 * Finds the closest feature to the origin on the Minkowski Difference.
	 */
	private Edge findClosestEdge(List<Edge> edges) {
		Edge closest = null;
		float distance = Float.MAX_VALUE;
		for (Edge e : edges) {
			float dist = VecMath.dotproduct(e.normal, e.a);
			if (dist < distance) {
				closest = e;
				distance = dist;
				e.distance = distance;
			}
		}
		return closest;
	}

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb, Vector2f dir) {
		return VecMath.subtraction(Sa.supportPoint(dir), Sb.supportPointNegative(dir));
	}

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();

		Shader defaultshader3 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader3);
		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		debugger = new Debugger(inputs, defaultshader3, defaultshader, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"),
				cam);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		defaultshader.addObject(new Circle(0, 0, 1f, 36));

		// Test 5
		Quad s1 = new Quad(120, 50, 20, 20);
		s1.setRotation(new Complexf(1.0, 0.0));
		rb1 = new RigidBody2(PhysicsShapeCreator.create(s1));

		Ellipse s2 = new Ellipse(97.00985f, 64.54515f, 3, 3, 120);
		s2.setRotation(new Complexf(1.0, 0.0));
		rb2 = new RigidBody2(PhysicsShapeCreator.create(s2));

		// Fix transformation (usually done in PhysicsSpace-class of Engine
		rb1.setRotation(s1.getRotation());
		rb1.setTranslation(s1.getTranslation());

		rb2.setRotation(s2.getRotation());
		rb2.setTranslation(s2.getTranslation());

		rb1.updateInverseRotation();
		rb2.updateInverseRotation();

		// Visualize the support functions
		support1 = new SupportDifferenceObject2(s1, rb1, s2, rb2);
		defaultshader.addObject(support1);
		defaultshader.addObject(new SupportObject2(s1, rb1));
		defaultshader.addObject(new SupportObject2(s2, rb2));

		// Compute simplex as starting point for EPA
		GJK2 gjk = new GJK2(new EmptyManifoldGenerator2());
		boolean b = gjk.isColliding(rb1, rb2);

		System.out.println(gjk.getSimplex().size() + "; " + b + "; " + s2.getTranslation() + "; " + s2.getRotation());

		// init EPA
		epaInit(gjk.getSimplex());

		// Input to step EPA
		InputEvent stepEPA = new InputEvent("Step EPA", new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(stepEPA);
		cam2d.scale(new Vector2f(0.2f, 0.2f));
		cam2d.translateTo(-50, -50);
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
		if (inputs.isEventActive("Step EPA") && !done) {
			epaStep();
			defaultshader.removeObject(simplex);
			simplex.delete();
			simplex = new Simplex(edges, findClosestEdge(edges));
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

	@Override
	public void renderInterface() {

	}
}
