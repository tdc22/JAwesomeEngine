package physics2dBasicTest;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.Font;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import integration.VerletIntegration;
import loader.FontLoader;
import manifold.PersistentManifoldManager2;
import matrix.Matrix1f;
import narrowphase.EPA2;
import narrowphase.GJK2;
import objects.RigidBody2;
import physics.PhysicsDebug2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.ProjectionCorrection;
import resolution.ImpulseResolution;
import shape2d.Circle;
import shape2d.Quad;
import vector.Vector2f;
import broadphase.SAP2;

public class BasicTest2d extends StandardGame {
	PhysicsSpace2 space;
	RigidBody2 rb1;
	int tempdelta = 0;
	Debugger debugmanager;
	PhysicsDebug2 physicsdebug;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		// display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		space = new PhysicsSpace2(new VerletIntegration(), new SAP2(),
				new GJK2(new EPA2()), new ImpulseResolution(),
				new ProjectionCorrection(1), new PersistentManifoldManager2()); // SimpleManifoldManager<Vector2f>());
		space.setGlobalForce(new Vector2f(0, 100));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugmanager = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug2(inputs, font, space);

		Quad ground = new Quad(400, 550, 300, 20);
		RigidBody2 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		add2dObject(ground);

		// Quad q = new Quad(400, 80, 20, 20);
		// rb1 = PhysicsShapeCreator.create(q);
		// rb1.setMass(1f);
		// rb1.setInertia(new Matrix1f(1));
		// space.addRigidBody(q, rb1);
		// addOverlayElement(q);
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
		render2dScene();
		physicsdebug.render2d();
	}

	@Override
	public void update(int delta) {
		// if (rb1 != null)
		// System.out.println(rb1.getMatrix());
		// System.out.println(rb1.getTranslation2() + ";      "
		// + rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Quad q = new Quad(400, 80, 20, 20);
				RigidBody2 rb = PhysicsShapeCreator.create(q);
				rb.setMass(1f);
				rb.setInertia(new Matrix1f(1));
				space.addRigidBody(q, rb);
				add2dObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Circle c = new Circle(400, 80, 20, 10);
				RigidBody2 rb = PhysicsShapeCreator.create(c);
				rb.setMass(1f);
				rb.setInertia(new Matrix1f(1));
				space.addRigidBody(c, rb);
				add2dObject(c);
				tempdelta = 0;

				rb1 = rb;
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
