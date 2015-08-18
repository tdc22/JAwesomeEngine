package debug_EPA2d;

import java.util.ArrayList;
import java.util.List;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
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
import shape2d.Circle;
import shape2d.Quad;
import utils.Debugger;
import vector.Vector2f;

public class EPA2dDebugger extends StandardGame {
	public class Edge {
		Vector2f a, b, normal;
		float distance;

		public Edge(Vector2f a, Vector2f b) {
			this.a = a;
			this.b = b;
			Vector2f e = VecMath.subtraction(b, a);
			normal = edgeDirection(e, a);
			if (normal.lengthSquared() > 0)
				normal.normalize();
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
	List<Edge> edges;
	boolean done = false;

	Vector2f normal = new Vector2f();

	float depth = 0;

	SupportMap<Vector2f> Sa;
	SupportMap<Vector2f> Sb;
	private final float TOLERANCE = 0.05f;
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
		if (isOriginInsideEdgeArea(e)) {
			Vector2f p = support(Sa, Sb, e.normal);
			double d = VecMath.dotproduct(p, e.normal);
			if (d - e.distance < TOLERANCE) {
				normal = e.normal;
				depth = (float) d;
				// END
				System.out.println("End. (" + iter + ") " + normal);
				done = true;
				return;
			} else {
				edges.add(new Edge(e.a, p));
				edges.add(new Edge(p, e.b));
			}
		}
		edges.remove(e);

		iter++;
	}

	private Vector2f edgeDirection(Vector2f edge, Vector2f origin) {
		Vector2f a = new Vector2f(-edge.y, edge.x);
		if (VecMath.dotproduct(a, origin) > 0)
			return a;
		a.negate();
		return a;
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
		add2dShader(defaultshader);

		debugger = new Debugger(inputs, defaultshader3, defaultshader, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"),
				cam);
		cam.setFlyCam(true);
		cam.translateTo(0, 1, 3);
		cam.setFlySpeed(0.01f);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		Circle c = new Circle(200, 200, 0.2f, 32);
		defaultshader.addObject(c);
		c.scale(10f);

		// Test 5
		Quad s1 = new Quad(120, 50, 20, 20);
		s1.rotate(40);
		rb1 = new RigidBody2(PhysicsShapeCreator.create(s1));

		Quad s2 = new Quad(95.16675f, 47.023335f, 5, 6);
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
		support1.scale(10f);
		defaultshader.addObject(support1);

		// Compute simplex as starting point for EPA
		GJK2 gjk = new GJK2(new EmptyManifoldGenerator2());
		boolean b = gjk.isColliding(rb1, rb2);

		System.out.println(gjk.getSimplex().size() + "; " + b + "; " + s2.getTranslation2() + "; " + s2.getRotation());

		// init EPA
		epaInit(gjk.getSimplex());

		// Input to step EPA
		InputEvent stepEPA = new InputEvent("Step EPA", new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
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
}
