package physicsBasicTest;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.Font;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import integration.VerletIntegration;
import loader.FontLoader;
import manifold.PersistentManifoldManager;
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
import shape.Sphere;
import vector.Vector3f;
import broadphase.SAP;

public class BasicTest extends StandardGame {
	PhysicsSpace space;
	RigidBody3 rb1;
	int tempdelta = 0;
	boolean impulseapplied = false;
	Debugger debugmanager;
	PhysicsDebug physicsdebug;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		setRendering2d(true);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(
				new EPA()), new LinearImpulseResolution(),
				new ProjectionCorrection(0.02f, 0.0f),
				new PersistentManifoldManager());// new
													// SimpleManifoldManager<Vector3f>());
		space.setGlobalForce(new Vector3f(0, -5, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugmanager = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		addObject(ground);

		Box q = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		rb1 = PhysicsShapeCreator.create(q);
		rb1.setMass(1f);
		rb1.setInertia(new Quaternionf());
		space.addRigidBody(q, rb1);
		addObject(q);
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
				Box q = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
				RigidBody3 rb = PhysicsShapeCreator.create(q);
				rb.setMass(1f);
				rb.setInertia(new Quaternionf());
				space.addRigidBody(q, rb);
				addObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Sphere c = new Sphere(0, 0, 0, 0.5f, 36, 36);
				RigidBody3 rb = PhysicsShapeCreator.create(c);
				rb.setMass(1f);
				rb.setInertia(new Quaternionf());
				space.addRigidBody(c, rb);
				addObject(c);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		debugmanager.update();
		physicsdebug.update();
		space.update(delta);
		cam.update(delta);
	}
}
