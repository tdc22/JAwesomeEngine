package physicsFriction2;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.Font;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import integration.EulerIntegration;
import integration.VerletIntegration;
import loader.FontLoader;
import manifold.MultiPointManifoldManager;
import manifold.MultiPointManifoldManager2;
import matrix.Matrix1f;
import narrowphase.EPA;
import narrowphase.EPA2;
import narrowphase.GJK;
import narrowphase.GJK2;
import objects.RigidBody;
import objects.RigidBody2;
import objects.RigidBody3;
import physics.PhysicsDebug;
import physics.PhysicsDebug2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import physics.PhysicsSpace2;
import positionalcorrection.ProjectionCorrection;
import quaternion.Quaternionf;
import resolution.ImpulseResolution;
import shape.Box;
import shape.Sphere;
import shape2d.Circle;
import shape2d.Quad;
import vector.Vector2f;
import vector.Vector3f;
import broadphase.SAP;
import broadphase.SAP2;

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
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		setRendering2d(true);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(
				new EPA()), new ImpulseResolution(), new ProjectionCorrection(0.01f), new MultiPointManifoldManager());
		space.setGlobalForce(new Vector3f(0, -5, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugmanager = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);

		Box ground = new Box(0, -5, 0, 20, 1, 20);
		RigidBody3 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		addObject(ground);

		Box slope = new Box(0, -2f, 0, 3, 1, 3);
		slope.rotate(20, 0, 20);
		RigidBody3 rb1 = PhysicsShapeCreator.create(slope);
		space.addRigidBody(slope, rb1);
		addObject(slope);
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
		render2dScene();
		physicsdebug.render2d();
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				 Box q = new Box(0, 10, 0, 0.5f, 0.5f, 0.5f);
				 RigidBody3 rb = PhysicsShapeCreator.create(q);
				 rb.setMass(1f);
				 rb.setInertia(new Quaternionf());
				 space.addRigidBody(q, rb);
				 addObject(q);
				 tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Sphere q = new Sphere(0, 10, 0, 0.5f, 36, 36);
				 RigidBody3 rb = PhysicsShapeCreator.create(q);
				 rb.setMass(1f);
				 rb.setInertia(new Quaternionf());
				 space.addRigidBody(q, rb);
				 addObject(q);
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
