package physicsBasicTest;

import game.StandardGame;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.VerletIntegration;

import java.awt.Color;

import loader.FontLoader;
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
import shape.Box;
import shape.Sphere;
import utils.Debugger;
import vector.Vector3f;
import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class BasicTest extends StandardGame {
	PhysicsSpace space;
	RigidBody3 rb1;
	int tempdelta = 0;
	boolean impulseapplied = false;
	Debugger debugger;
	PhysicsDebug physicsdebug;
	InputEvent run, step;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(
				new EPA()), new ImpulseResolution(), new ProjectionCorrection(
				0.01f), new SimpleManifoldManager<Vector3f>()); // new
																// MultiPointManifoldManager();
		space.setGlobalForce(new Vector3f(0, -5, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		addObject(ground);

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

		// Box q = new Box(0, 10, 0, 0.5f, 0.5f, 0.5f);
		// rb1 = PhysicsShapeCreator.create(q);
		// rb1.setMass(1f);
		// rb1.setInertia(new Quaternionf());
		// space.addRigidBody(q, rb1);
		// addObject(q);

		// Sphere c = new Sphere(0, 10, 0, 0.5f, 36, 36);
		// c.setColor(Color.RED);
		// rb1 = new RigidBody3(PhysicsShapeCreator.create(c));
		// rb1.setMass(1f);
		// rb1.setInertia(new Quaternionf());
		// space.addRigidBody(c, rb1);
		// addObject(c);

		step = new InputEvent("Step", new Input(Input.KEYBOARD_EVENT, " ",
				KeyInput.KEY_PRESSED));
		run = new InputEvent("Run", new Input(Input.KEYBOARD_EVENT, "X",
				KeyInput.KEY_DOWN));
		inputs.addEvent(step);
		inputs.addEvent(run);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		physicsdebug.render3d();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Box q = new Box(0, 10, 0, 0.5f, 0.5f, 0.5f);
				q.setColor(Color.BLUE);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(q));
				rb.setMass(1f);
				rb.setInertia(new Quaternionf());
				space.addRigidBody(q, rb);
				addObject(q);
				tempdelta = 0;
				System.out.println("Box added.");
				// Box q = new Box(cam.getTranslation(), 0.5f, 0.5f, 0.5f);
				// RigidBody3 rb = PhysicsShapeCreator.create(q);
				// rb.setMass(1f);
				// rb.setInertia(new Quaternionf());
				// rb.applyCentralImpulse(VecMath.scale(cam.getDirection(), 5));
				// space.addRigidBody(q, rb);
				// addObject(q);
				// tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Sphere c = new Sphere(0, 10, 0, 0.5f, 36, 36);
				c.setColor(Color.RED);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(c));
				rb.setMass(1f);
				rb.setInertia(new Quaternionf());
				space.addRigidBody(c, rb);
				addObject(c);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		debugger.update();
		// if (run.isActive() || step.isActive())
		space.update(delta);
		physicsdebug.update();
		cam.update(delta);
	}
}
