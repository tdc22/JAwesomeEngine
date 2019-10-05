package physics2dRaycast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import broadphase.DynamicAABBTree2;
import broadphase.DynamicAABBTree2Generic;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Color;
import integration.EulerIntegration;
import loader.InputLoader;
import loader.ShaderLoader;
import manifold.RaycastResult;
import manifold.SimpleManifoldManager;
import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import narrowphase.EPA2;
import narrowphase.GJK2;
import narrowphase.SupportRaycast2;
import objects.CollisionShape;
import objects.CompoundObject2;
import objects.Ray2;
import objects.RigidBody;
import objects.RigidBody2;
import objects.ShapedObject2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shader.Shader;
import shape2d.Circle;
import shape2d.Ellipse;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector4f;

public class RaycastTest2d extends StandardGame {
	PhysicsSpace2 space;
	Quad q2, q3, q4, q5;
	Circle c, c1;
	Ellipse e1;
	Shader defaultshader, s1, s2, s3, s4, s5, s6, s7, s8;
	RigidBody2 rb1, rb2, rb3, rb4, rb5, rb6, rb7;
	CompoundObject2 rb8;
	Ray2 ray;
	RayVisualization rayVis;
	List<Circle> hitmarkers;
	List<Line> hitnormals;

	final Vector2f up = new Vector2f(0, -1);

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());

		int shaderprogram = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert",
				"res/shaders/colorshader.frag");
		s1 = new Shader(shaderprogram);
		s2 = new Shader(shaderprogram);
		s3 = new Shader(shaderprogram);
		s4 = new Shader(shaderprogram);
		s5 = new Shader(shaderprogram);
		s6 = new Shader(shaderprogram);
		s7 = new Shader(shaderprogram);
		s8 = new Shader(shaderprogram);
		Shader hitmarkershader = new Shader(shaderprogram);
		Shader hitnormalshader = new Shader(shaderprogram);

		s1.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s2.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s3.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s4.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s5.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s6.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s7.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s8.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		hitmarkershader.addArgument("u_color", new Vector4f(0f, 1f, 0f, 1f));
		hitnormalshader.addArgument("u_color", new Vector4f(0f, 0f, 1f, 1f));

		addShader2d(s1);
		addShader2d(s2);
		addShader2d(s3);
		addShader2d(s4);
		addShader2d(s5);
		addShader2d(s6);
		addShader2d(s7);
		addShader2d(s8);
		addShader2d(hitmarkershader);
		addShader2d(hitnormalshader);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		space = new PhysicsSpace2(new EulerIntegration(), new DynamicAABBTree2(), new GJK2(new EPA2()),
				new SupportRaycast2(), new NullResolution(), new NullCorrection(),
				new SimpleManifoldManager<Vector2f>());
		space.setCullStaticOverlaps(false);

		c = new Circle(400, 200, 10, 36);
		s1.addObject(c);
		ray = new Ray2(c.getTranslation(), up);
		rayVis = new RayVisualization(ray);
		s1.addObject(rayVis);

		q2 = new Quad(500, 200, 35, 35);
		rb2 = new RigidBody2(PhysicsShapeCreator.create(q2));
		space.addRigidBody(q2, rb2);
		s2.addObject(q2);

		q3 = new Quad(100, 500, 25, 25);
		rb3 = new RigidBody2(PhysicsShapeCreator.create(q3));
		space.addRigidBody(q3, rb3);
		s3.addObject(q3);

		q4 = new Quad(300, 500, 25, 25);
		q4.rotate(45);
		rb4 = new RigidBody2(PhysicsShapeCreator.create(q4));
		space.addRigidBody(q4, rb4);
		s4.addObject(q4);

		q5 = new Quad(600, 400, 150, 10);
		q5.rotate(20);
		rb5 = new RigidBody2(PhysicsShapeCreator.create(q5));
		space.addRigidBody(q5, rb5);
		s5.addObject(q5);

		c1 = new Circle(80, 80, 25, 40);
		rb6 = new RigidBody2(PhysicsShapeCreator.create(c1));
		space.addRigidBody(c1, rb6);
		s6.addObject(c1);

		e1 = new Ellipse(500, 50, 50, 25, 40);
		rb7 = new RigidBody2(PhysicsShapeCreator.create(e1));
		space.addRigidBody(e1, rb7);
		s7.addObject(e1);

		Quad q = new Quad(500, 500, 20, 20);
		Circle c = new Circle(500, 500, 20, 10);
		rb8 = new CompoundObject2(new DynamicAABBTree2Generic<CollisionShape<Vector2f, ?, ?>>());
		rb8.addCollisionShape(PhysicsShapeCreator.create(q));
		rb8.addCollisionShape(PhysicsShapeCreator.create(c));
		rb8.setMass(1f);
		rb8.setInertia(new Matrix1f(1));
		space.addCompoundObject(rb8, new ShapedObject2[] { q, c });
		s8.addObject(q);
		s8.addObject(c);

		hitmarkers = new ArrayList<Circle>();
		for (int i = 0; i < 6; i++) {
			Circle hitmarker = new Circle(-100, -100, 3, 36);
			hitmarkershader.addObject(hitmarker);
			hitmarkers.add(hitmarker);
		}

		hitnormals = new ArrayList<Line>();
		for (int i = 0; i < 6; i++) {
			Line hitnormal = new Line();
			hitnormalshader.addObject(hitnormal);
			hitnormals.add(hitnormal);
		}

		inputs = InputLoader.load(inputs, "res/inputs.txt");
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		render2dLayer();
	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("Translate1")) {
			c.translate(0, -delta / 4f);
		}
		if (inputs.isEventActive("Translate2")) {
			c.translate(0, delta / 4f);
		}
		if (inputs.isEventActive("Translate3")) {
			c.translate(-delta / 4f, 0);
		}
		if (inputs.isEventActive("Translate4")) {
			c.translate(delta / 4f, 0);
		}

		if (inputs.isEventActive("Rotate2")) {
			c.rotate(delta / 10f);
		}
		if (inputs.isEventActive("Rotate3")) {
			c.rotate(-delta / 10f);
		}

		space.update(delta);

		s1.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s2.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s3.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s4.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s5.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s6.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s7.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s8.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));

		ray.setPosition(c.getTranslation());
		ray.setDirection(ComplexMath.transform(c.getRotation(), up));
		rayVis.updateVisualization();

		Set<RigidBody<Vector2f, ?, ?, ?>> broadphaseHits = space.raycastAllBroadphase(ray);
		for (RigidBody<Vector2f, ?, ?, ?> o : broadphaseHits) {
			if (o.equals(rb1))
				s1.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb2))
				s2.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb3))
				s3.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb4))
				s4.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb5))
				s5.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb6))
				s6.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb7))
				s7.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb8))
				s8.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
		}

		Set<RaycastResult<Vector2f>> hits = space.raycastAll(ray);
		for (Circle c : hitmarkers)
			c.setRendered(false);
		for (Line l : hitnormals)
			l.setRendered(false);
		int c = 0;
		for (RaycastResult<Vector2f> hit : hits) {
			RigidBody<Vector2f, ?, ?, ?> o = hit.getHitObject();
			if (o.equals(rb1))
				s1.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb2))
				s2.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb3))
				s3.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb4))
				s4.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb5))
				s5.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb6))
				s6.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb7))
				s7.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb8))
				s8.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));

			Circle hitmarker = hitmarkers.get(c);
			hitmarker.translateTo(hit.getHitPosition());
			hitmarker.setRendered(true);

			Line hitnormal = hitnormals.get(c);
			hitnormal.update(hit.getHitPosition(),
					VecMath.addition(hit.getHitPosition(), VecMath.scale(hit.getHitNormal(), 30)));
			hitnormal.setRendered(true);

			c++;
		}

		cam.update(delta);
	}

	private class Line extends ShapedObject2 {
		Color c;

		public Line() {
			setRenderMode(GLConstants.LINES);
			c = Color.CYAN;
		}

		public void update(Vector2f start, Vector2f end) {
			delete();
			addVertex(start, c);
			addVertex(end, c);
			addIndices(0, 1);
			prerender();
		}
	}
}
