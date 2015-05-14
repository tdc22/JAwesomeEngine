package physicsFriction2;

import game.StandardGame;
import gui.Font;
import integration.VerletIntegration;
import loader.FontLoader;
import manifold.MultiPointManifoldManager;
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

public class FrictionTest extends StandardGame {
	PhysicsSpace space;
	RigidBody3 rb1;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug physicsdebug;

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
				0.01f), new MultiPointManifoldManager());
		space.setGlobalForce(new Vector3f(0, -5, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);

		Box ground = new Box(0, -5, 0, 20, 1, 20);
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		addObject(ground);

		Box slope = new Box(0, -2f, 0, 3, 1, 3);
		slope.rotate(20, 0, 20);
		RigidBody3 rb1 = new RigidBody3(PhysicsShapeCreator.create(slope));
		space.addRigidBody(slope, rb1);
		addObject(slope);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		debugger.end();
		physicsdebug.render3d();
	}

	@Override
	public void render2d() {
		debugger.render2d(fps, objects.size(), objects2d.size());
		render2dScene();
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Box q = new Box(0, 10, 0, 0.5f, 0.5f, 0.5f);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(q));
				rb.setMass(1f);
				rb.setInertia(new Quaternionf(0.1f, 0, 0, 0));
				space.addRigidBody(q, rb);
				addObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Sphere q = new Sphere(0, 10, 0, 0.5f, 36, 36);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(q));
				rb.setMass(1f);
				rb.setInertia(new Quaternionf(0.1f, 0, 0, 0));
				space.addRigidBody(q, rb);
				addObject(q);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		debugger.update();
		physicsdebug.update();
		space.update(delta);
		cam.update(delta);
	}
}
