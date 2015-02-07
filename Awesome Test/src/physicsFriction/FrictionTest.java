package physicsFriction;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.Font;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import integration.VerletIntegration;
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
import resolution.LinearImpulseResolution;
import shape.Box;
import vector.Vector3f;
import broadphase.SAP;

public class FrictionTest extends StandardGame {
	PhysicsSpace space;
	RigidBody3 rb1;
	int tempdelta = 0;
	Debugger debugmanager;
	PhysicsDebug physicsdebug;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 20);
		cam.rotateTo(0, 0);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(
				new EPA()), new LinearImpulseResolution(),
				new ProjectionCorrection(0.01f),
				new SimpleManifoldManager<Vector3f>());
		space.setGlobalForce(new Vector3f(0, -5, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugmanager = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		addObject(ground);

		Box q = new Box(-12, 0, 0, 1, 1, 1);
		RigidBody3 rb1 = PhysicsShapeCreator.create(q);
		rb1.setMass(1f);
		rb1.setInertia(new Quaternionf());
		rb1.applyCentralImpulse(new Vector3f(5, 0, 0));
		space.addRigidBody(q, rb1);
		addObject(q);
		tempdelta = 0;
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
		physicsdebug.render3d();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
		render2dScene();
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Box q = new Box(-12, 0, 0, 1, 1, 1);
				RigidBody3 rb1 = PhysicsShapeCreator.create(q);
				rb1.setMass(1f);
				rb1.setInertia(new Quaternionf());
				rb1.applyCentralImpulse(new Vector3f(5, 0, 0));
				space.addRigidBody(q, rb1);
				addObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Box q = new Box(12, 0, 0, 1, 1, 1);
				RigidBody3 rb1 = PhysicsShapeCreator.create(q);
				rb1.setMass(1f);
				rb1.setInertia(new Quaternionf());
				rb1.applyCentralImpulse(new Vector3f(-5, 0, 0));
				space.addRigidBody(q, rb1);
				addObject(q);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		debugmanager.update();
		space.update(delta);
		physicsdebug.update();
		cam.update(delta);
	}
}
