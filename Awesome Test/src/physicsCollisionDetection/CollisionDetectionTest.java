package physicsCollisionDetection;

import game.Debugger;
import game.StandardGame;
import input.Input;
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
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import utils.Pair;
import utils.Shader;
import vector.Vector3f;
import vector.Vector4f;
import broadphase.SAP;

public class CollisionDetectionTest extends StandardGame {
	PhysicsSpace space;
	Box b1, b2, b3;
	Sphere sp1;
	Cylinder c1;
	Shader s1, s2, s3, s4, s5;
	RigidBody3 rb1, rb2, rb3, rb4, rb5;
	Debugger debugmanager;
	List<ManifoldVisualization> manifolds;

	@Override
	public void init() {
		initDisplay(false, 800, 600, true);
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		// mouse.setGrabbed(false);

		int shaderprogram = ShaderLoader.loadShaderPair(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag");
		s1 = new Shader(shaderprogram);
		s2 = new Shader(shaderprogram);
		s3 = new Shader(shaderprogram);
		s4 = new Shader(shaderprogram);
		s5 = new Shader(shaderprogram);

		s1.addArgument("color", new Vector4f(1f, 1f, 1f, 1f));
		s2.addArgument("color", new Vector4f(1f, 1f, 1f, 1f));
		s3.addArgument("color", new Vector4f(1f, 1f, 1f, 1f));
		s4.addArgument("color", new Vector4f(1f, 1f, 1f, 1f));
		s5.addArgument("color", new Vector4f(1f, 1f, 1f, 1f));

		manifolds = new ArrayList<ManifoldVisualization>();

		space = new PhysicsSpace(new EulerIntegration(), new SAP(), new GJK(
				new EPA()), new NullResolution(), new NullCorrection(),
				new SimpleManifoldManager<Vector3f>());

		b1 = new Box(-1, 0, 0, 1, 1, 1);
		b1.setShader(s1);
		rb1 = PhysicsShapeCreator.create(b1);
		space.addRigidBody(b1, rb1);
		addObject(b1);

		b2 = new Box(4, 0, 0, 1.5f, 1.5f, 1.5f);
		b2.setShader(s2);
		rb2 = PhysicsShapeCreator.create(b2);
		space.addRigidBody(b2, rb2);
		addObject(b2);

		b3 = new Box(-10, -10, 0, 1.5f, 1.5f, 1.5f);
		b3.setShader(s3);
		rb3 = PhysicsShapeCreator.create(b3);
		space.addRigidBody(b3, rb3);
		addObject(b3);

		sp1 = new Sphere(-10, 10, 0, 1, 36, 36);
		sp1.setShader(s4);
		rb4 = PhysicsShapeCreator.create(sp1);
		space.addRigidBody(sp1, rb4);
		addObject(sp1);

		c1 = new Cylinder(10, 10, 0, 1, 1, 36);
		c1.setShader(s5);
		rb5 = PhysicsShapeCreator.create(c1);
		space.addRigidBody(c1, rb5);
		addObject(c1);

		inputs = InputLoader.load("res/inputs.txt");
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		inputs.createInputEvent("toggle Mouse grab").addEventTrigger(
				new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_E,
						KeyEvent.Key_Pressed));
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
		for (ManifoldVisualization mv : manifolds) {
			mv.render();
			mv.delete();
		}
		manifolds.clear();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
	}

	@Override
	public void update(int delta) {
		if (inputs.isInputEventActive("toggle Mouse grab")) {
			mouse.setGrabbed(!mouse.isGrabbed());
		}

		if (inputs.isInputEventActive("Translate1")) {
			b1.translate(0, delta / 100f, 0);
		}
		if (inputs.isInputEventActive("Translate2")) {
			b1.translate(0, -delta / 100f, 0);
		}
		if (inputs.isInputEventActive("Translate3")) {
			b1.translate(-delta / 100f, 0, 0);
		}
		if (inputs.isInputEventActive("Translate4")) {
			b1.translate(delta / 100f, 0, 0);
		}

		if (inputs.isInputEventActive("Rotate1")) {
			b1.rotate(delta / 10f, 0, 0);
		}
		if (inputs.isInputEventActive("Rotate2")) {
			b1.rotate(-delta / 10f, 0, 0);
		}
		if (inputs.isInputEventActive("Rotate3")) {
			b1.rotate(0, -delta / 10f, 0);
		}
		if (inputs.isInputEventActive("Rotate4")) {
			b1.rotate(0, delta / 10f, 0);
		}

		space.update(delta);

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
				s1.setArgument(0, new Vector4f(1f, 0f, 0f, 0.5f));
			if (o.contains(rb2))
				s2.setArgument(0, new Vector4f(1f, 0f, 0f, 0.5f));
			if (o.contains(rb3))
				s3.setArgument(0, new Vector4f(1f, 0f, 0f, 0.5f));
			if (o.contains(rb4))
				s4.setArgument(0, new Vector4f(1f, 0f, 0f, 0.5f));
			if (o.contains(rb5))
				s5.setArgument(0, new Vector4f(1f, 0f, 0f, 0.5f));
			GL11.glDisable(GL_DEPTH_TEST);
		}

		debugmanager.update();
		cam.update(delta);
	}
}
