package physics2dFriction2;

import broadphase.SAP2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.MultiPointManifoldManager2;
import matrix.Matrix1f;
import narrowphase.EPA2;
import narrowphase.GJK2;
import objects.RigidBody2;
import physics.PhysicsDebug2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.ProjectionCorrection;
import resolution.ImpulseResolution;
import shader.Shader;
import shape2d.Circle;
import shape2d.Quad;
import utils.Debugger;
import vector.Vector2f;

public class FrictionTest2d extends StandardGame {
	PhysicsSpace2 space;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug2 physicsdebug;
	Shader defaultshader2;
	InputEvent step;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(new EPA2()), new ImpulseResolution(),
				new ProjectionCorrection(1), new MultiPointManifoldManager2());
		space.setGlobalGravitation(new Vector2f(0, 120));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);
		physicsdebug = new PhysicsDebug2(inputs, defaultshader2, font, space);

		Quad ground = new Quad(400, 550, 300, 20);
		RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		defaultshader2.addObject(ground);

		Quad slope = new Quad(400, 300, 100, 20);
		slope.rotate(40);
		RigidBody2 rb2 = new RigidBody2(PhysicsShapeCreator.create(slope));
		space.addRigidBody(slope, rb2);
		defaultshader2.addObject(slope);

		step = new InputEvent("Step", new Input(Input.KEYBOARD_EVENT, "R", KeyInput.KEY_PRESSED));
		inputs.addEvent(step);
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		debugger.begin();
		render2dLayer();
	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Quad q = new Quad(400, 80, 20, 20);
				RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(q));
				rb.setMass(1f);
				rb.setInertia(new Matrix1f(1));
				space.addRigidBody(q, rb);
				defaultshader2.addObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Circle c = new Circle(400, 80, 20, 10);
				RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(c));
				rb.setMass(1f);
				rb.setInertia(new Matrix1f(1));
				space.addRigidBody(c, rb);
				defaultshader2.addObject(c);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		debugger.update(fps, 0, 0);
		if (inputs.isKeyDown("E") || step.isActive()) {
			RigidBody2 obj = (RigidBody2) space.getObjects().get(space.getObjects().size() - 1);
			System.out.println(obj.getTranslation2() + "; " + obj.getRotation() + "; " + space.hasCollision(obj));
			space.update(delta);
		}
		physicsdebug.update();
		cam.update(delta);
	}
}
