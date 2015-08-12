package physicsBasicTest;

import java.awt.Color;

import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import narrowphase.EPA;
import narrowphase.GJK;
import objects.RigidBody3;
import physics.PhysicsDebug;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import quaternion.Quaternionf;
import resolution.ImpulseResolution;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import space.PhysicsProfiler;
import space.SimplePhysicsProfiler;
import utils.Debugger;
import utils.GameProfiler;
import utils.Profiler;
import utils.SimpleGameProfiler;
import vector.Vector3f;

public class BasicTest extends StandardGame {
	PhysicsSpace space;
	int tempdelta = 0;
	boolean impulseapplied = false;
	Debugger debugger;
	PhysicsDebug physicsdebug;
//	Profiler profiler;
	InputEvent run, step;
	Shader defaultshader;

	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "TEST", false), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		
		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(new EPA()), new ImpulseResolution(),
				new ProjectionCorrection(0.01f), new SimpleManifoldManager<Vector3f>());// new
																						// MultiPointManifoldManager());
		space.setGlobalGravitation(new Vector3f(0, -8f, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshader2, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);
//		GameProfiler gp = new SimpleGameProfiler();
//		setProfiler(gp);
//		PhysicsProfiler pp = new SimplePhysicsProfiler();
//		space.setProfiler(pp);
//		profiler = new Profiler(inputs, font, gp, pp);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(ground));

		space.addRigidBody(ground, rb);
		defaultshader.addObject(ground);

		// Some walls

		/*
		 * Box w1 = new Box(10, -1f, 0, 1, 5, 10); space.addRigidBody(w1, new
		 * RigidBody3(PhysicsShapeCreator.create(w1))); addObject(w1);
		 * 
		 * Box w2 = new Box(-10, -1f, 0, 1, 5, 10); space.addRigidBody(w2, new
		 * RigidBody3(PhysicsShapeCreator.create(w2))); addObject(w2);
		 * 
		 * Box w3 = new Box(0, -1f, 10, 10, 5, 1); space.addRigidBody(w3, new
		 * RigidBody3(PhysicsShapeCreator.create(w3))); addObject(w3);
		 * 
		 * Box w4 = new Box(0, -1f, -10, 10, 5, 1); space.addRigidBody(w4, new
		 * RigidBody3(PhysicsShapeCreator.create(w4))); addObject(w4);
		 */

		// End walls

		step = new InputEvent("Step", new Input(Input.KEYBOARD_EVENT, " ", KeyInput.KEY_PRESSED));
		run = new InputEvent("Run", new Input(Input.KEYBOARD_EVENT, "X", KeyInput.KEY_DOWN));
		inputs.addEvent(step);
		inputs.addEvent(run);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.begin();
		renderScene();
		physicsdebug.render3d();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
//		profiler.render2d();
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 100) {
			if (inputs.isMouseButtonDown("0")) {
				Box q = new Box(0, 10, 0, 0.5f, 0.5f, 0.5f);
				q.setColor(Color.BLUE);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(q));
				rb.setMass(0.1f);
				rb.setInertia(new Quaternionf(0.1f, 0, 0, 0));
				space.addRigidBody(q, rb);
				defaultshader.addObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Sphere c = new Sphere(0, 10, 0, 0.5f, 36, 36);
				c.setColor(Color.RED);
				RigidBody3 rb1 = new RigidBody3(PhysicsShapeCreator.create(c));
				rb1.setMass(0.1f);
				rb1.setInertia(new Quaternionf(0.03f, 0, 0, 0));
				space.addRigidBody(c, rb1);
				defaultshader.addObject(c);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}

		debugger.update(fps, 0, 0);
//		profiler.update(delta);
		space.update(delta);
		physicsdebug.update();

		if (display.isMouseBound())
			cam.update(delta);
	}

}
