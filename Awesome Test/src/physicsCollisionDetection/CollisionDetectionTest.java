package physicsCollisionDetection;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.EulerIntegration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import loader.FontLoader;
import loader.InputLoader;
import loader.ShaderLoader;
import manifold.CollisionManifold;
import manifold.SimpleManifoldManager;
import narrowphase.EPA;
import narrowphase.GJK;
import objects.RigidBody;
import objects.RigidBody3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import utils.Debugger;
import utils.Pair;
import vector.Vector3f;
import vector.Vector4f;
import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class CollisionDetectionTest extends StandardGame {
	PhysicsSpace space;
	Box b1, b2, b3;
	Sphere sp1;
	Cylinder c1;
	Shader s1, s2, s3, s4, s5;
	RigidBody3 rb1, rb2, rb3, rb4, rb5;
	Debugger debugger;
	List<ManifoldVisualization> manifolds;
	InputEvent toggleMouseBind, giveMeData;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		// mouse.setGrabbed(false);

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		int shaderprogram = ShaderLoader.loadShaderFromFile(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag");
		s1 = new Shader(shaderprogram);
		s2 = new Shader(shaderprogram);
		s3 = new Shader(shaderprogram);
		s4 = new Shader(shaderprogram);
		s5 = new Shader(shaderprogram);

		s1.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s2.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s3.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s4.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s5.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));

		addShader(s1);
		addShader(s2);
		addShader(s3);
		addShader(s4);
		addShader(s5);

		manifolds = new ArrayList<ManifoldVisualization>();

		space = new PhysicsSpace(new EulerIntegration(), new SAP(), new GJK(
				new EPA()), new NullResolution(), new NullCorrection(),
				new SimpleManifoldManager<Vector3f>());
		space.setCullStaticOverlaps(false);

		b1 = new Box(-1, 0, 0, 1, 1, 1);
		rb1 = new RigidBody3(PhysicsShapeCreator.create(b1));
		space.addRigidBody(b1, rb1);
		s1.addObject(b1);

		b2 = new Box(4, 0, 0, 1.5f, 1.5f, 1.5f);
		rb2 = new RigidBody3(PhysicsShapeCreator.create(b2));
		space.addRigidBody(b2, rb2);
		s2.addObject(b2);

		b3 = new Box(-10, -10, 0, 1.5f, 1.5f, 1.5f);
		rb3 = new RigidBody3(PhysicsShapeCreator.create(b3));
		space.addRigidBody(b3, rb3);
		s3.addObject(b3);

		sp1 = new Sphere(-10, 10, 0, 1, 36, 36);
		rb4 = new RigidBody3(PhysicsShapeCreator.create(sp1));
		space.addRigidBody(sp1, rb4);
		s4.addObject(sp1);

		c1 = new Cylinder(10, 10, 0, 1, 1, 36);
		rb5 = new RigidBody3(PhysicsShapeCreator.create(c1));
		space.addRigidBody(c1, rb5);
		s5.addObject(c1);

		inputs = InputLoader.load(inputs, "res/inputs.txt");
		debugger = new Debugger(inputs, defaultshader, defaultshader2,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "R", KeyInput.KEY_PRESSED));
		giveMeData = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "P", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
		inputs.addEvent(giveMeData);
	}

	@Override
	public void render() {
		debugger.begin();
		renderScene();
		for (ManifoldVisualization mv : manifolds) {
			mv.render();
			mv.delete();
		}
		manifolds.clear();
	}

	@Override
	public void render2d() {
		debugger.end();
	}

	@Override
	public void update(int delta) {
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}

		if (inputs.isEventActive("Translate1")) {
			b1.translate(0, delta / 100f, 0);
		}
		if (inputs.isEventActive("Translate2")) {
			b1.translate(0, -delta / 100f, 0);
		}
		if (inputs.isEventActive("Translate3")) {
			b1.translate(-delta / 100f, 0, 0);
		}
		if (inputs.isEventActive("Translate4")) {
			b1.translate(delta / 100f, 0, 0);
		}

		if (inputs.isEventActive("Rotate1")) {
			b1.rotate(delta / 10f, 0, 0);
		}
		if (inputs.isEventActive("Rotate2")) {
			b1.rotate(-delta / 10f, 0, 0);
		}
		if (inputs.isEventActive("Rotate3")) {
			b1.rotate(0, -delta / 10f, 0);
		}
		if (inputs.isEventActive("Rotate4")) {
			b1.rotate(0, delta / 10f, 0);
		}

		space.update(delta);
		if (giveMeData.isActive())
			System.out.println(b1.getTranslation() + "; " + b1.getRotation());

		s1.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s2.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s3.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s4.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s5.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));

		Set<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>> overlaps = space
				.getBroadphase().getOverlaps();
		for (Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>> o : overlaps) {
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
		}

		for (CollisionManifold<Vector3f> cm : space.getCollisionManifolds()) {
			manifolds.add(new ManifoldVisualization(cm));
			Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>> o = cm
					.getObjects();
			if (o.contains(rb1))
				s1.setArgument(0, new Vector4f(1f, 0f, 0f, 0.7f));
			if (o.contains(rb2))
				s2.setArgument(0, new Vector4f(1f, 0f, 0f, 0.7f));
			if (o.contains(rb3))
				s3.setArgument(0, new Vector4f(1f, 0f, 0f, 0.7f));
			if (o.contains(rb4))
				s4.setArgument(0, new Vector4f(1f, 0f, 0f, 0.7f));
			if (o.contains(rb5))
				s5.setArgument(0, new Vector4f(1f, 0f, 0f, 0.7f));
		}
		System.out.println(rb1.getTranslation() + "; " + rb1.getRotation()
				+ "; ");

		debugger.update(fps, 0, 0);
		cam.update(delta);
	}
}
