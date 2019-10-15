package physics2dCollisionDetection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import broadphase.DynamicAABBTree2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import integration.EulerIntegration;
import loader.InputLoader;
import loader.ShaderLoader;
import manifold.CollisionManifold;
import manifold.SimpleManifoldManager2;
import matrix.Matrix1f;
import narrowphase.EPA2;
import narrowphase.GJK2;
import narrowphase.SupportRaycast2;
import objects.CompoundObject2;
import objects.RigidBody;
import objects.RigidBody2;
import objects.ShapedObject2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.NullCorrection;
import quaternion.Complexf;
import resolution.NullResolution;
import shader.Shader;
import shape2d.Circle;
import shape2d.Ellipse;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import utils.Pair;
import vector.Vector2f;
import vector.Vector4f;

public class CollisionDetectionTest2d extends StandardGame {
	PhysicsSpace2 space;
	Quad q1, q2, q3;
	Circle c1;
	Ellipse e1;
	Shader defaultshader, s1, s2, s3, s4, s5, s6;
	RigidBody2 rb1, rb2, rb3, rb4, rb5;
	CompoundObject2 rb6;
	List<ManifoldVisualization> manifolds;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		int shaderprogram = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert",
				"res/shaders/colorshader.frag");
		s1 = new Shader(shaderprogram);
		s2 = new Shader(shaderprogram);
		s3 = new Shader(shaderprogram);
		s4 = new Shader(shaderprogram);
		s5 = new Shader(shaderprogram);
		s6 = new Shader(shaderprogram);

		s1.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s2.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s3.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s4.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s5.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s6.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));

		addShader2d(s1);
		addShader2d(s2);
		addShader2d(s3);
		addShader2d(s4);
		addShader2d(s5);
		addShader2d(s6);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		manifolds = new ArrayList<ManifoldVisualization>();

		space = new PhysicsSpace2(new EulerIntegration(), new DynamicAABBTree2(), new GJK2(new EPA2()),
				new SupportRaycast2(), new NullResolution(), new NullCorrection(), new SimpleManifoldManager2());
		space.setCullStaticOverlaps(false);

		q1 = new Quad(400, 200, 25, 25);
		rb1 = new RigidBody2(PhysicsShapeCreator.create(q1));
		space.addRigidBody(q1, rb1);
		s1.addObject(q1);

		q2 = new Quad(500, 200, 35, 35);
		rb2 = new RigidBody2(PhysicsShapeCreator.create(q2));
		space.addRigidBody(q2, rb2);
		s2.addObject(q2);

		q3 = new Quad(100, 500, 25, 25);
		rb3 = new RigidBody2(PhysicsShapeCreator.create(q3));
		space.addRigidBody(q3, rb3);
		s3.addObject(q3);

		c1 = new Circle(80, 80, 25, 40);
		rb4 = new RigidBody2(PhysicsShapeCreator.create(c1));
		space.addRigidBody(c1, rb4);
		s4.addObject(c1);

		e1 = new Ellipse(500, 50, 50, 25, 40);
		rb5 = new RigidBody2(PhysicsShapeCreator.create(e1));
		space.addRigidBody(e1, rb5);
		s5.addObject(e1);

		Quad q = new Quad(500, 500, 20, 20);
		Circle c = new Circle(500, 500, 20, 10);
		rb6 = new CompoundObject2();
		rb6.addCollisionShape(PhysicsShapeCreator.create(q));
		rb6.addCollisionShape(PhysicsShapeCreator.create(c));
		rb6.setMass(1f);
		rb6.setInertia(new Matrix1f(1));
		space.addCompoundObject(rb6, new ShapedObject2[] { q, c });
		s6.addObject(q);
		s6.addObject(c);

		inputs = InputLoader.load(inputs, "res/inputs.txt");
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		render2dLayer();
		for (ManifoldVisualization mv : manifolds) {
			defaultshader.removeObject(mv);
			mv.delete();
		}
		manifolds.clear();
	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		// q1.rotate(delta / 10f);

		if (inputs.isEventActive("Translate1")) {
			q1.translate(0, -delta / 4f);
		}
		if (inputs.isEventActive("Translate2")) {
			q1.translate(0, delta / 4f);
		}
		if (inputs.isEventActive("Translate3")) {
			q1.translate(-delta / 4f, 0);
		}
		if (inputs.isEventActive("Translate4")) {
			q1.translate(delta / 4f, 0);
		}

		if (inputs.isEventActive("Rotate2")) {
			q1.rotate(delta / 10f);
		}
		if (inputs.isEventActive("Rotate3")) {
			q1.rotate(-delta / 10f);
		}

		space.update(delta);

		s1.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s2.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s3.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s4.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s5.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s6.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));

		Set<Pair<RigidBody<Vector2f, ?, Complexf, ?>, RigidBody<Vector2f, ?, Complexf, ?>>> overlaps = space
				.getOverlaps();
		for (Pair<RigidBody<Vector2f, ?, Complexf, ?>, RigidBody<Vector2f, ?, Complexf, ?>> o : overlaps) {
			if (o.contains(rb1))
				s1.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.contains(rb2))
				s2.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.contains(rb3))
				s3.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.contains(rb4))
				s4.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.contains(rb5))
				s5.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.contains(rb6))
				s6.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
		}

		for (CollisionManifold<Vector2f, Complexf> cm : space.getCollisionManifolds()) {
			ManifoldVisualization mv = new ManifoldVisualization(cm);
			defaultshader.addObject(mv);
			manifolds.add(mv);
			Pair<RigidBody<Vector2f, ?, Complexf, ?>, RigidBody<Vector2f, ?, Complexf, ?>> o = cm.getObjects();
			if (o.contains(rb1))
				s1.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.contains(rb2))
				s2.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.contains(rb3))
				s3.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.contains(rb4))
				s4.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.contains(rb5))
				s5.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.contains(rb6))
				s6.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
		}

		// System.out.println(rb6.getCompoundBroadphase().getObjects().size() +
		// "; "
		// + rb6.getCompoundBroadphase().getOverlaps().size());

		cam.update(delta);
	}
}
