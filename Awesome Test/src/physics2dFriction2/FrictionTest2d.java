package physics2dFriction2;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.Font;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import integration.EulerIntegration;
import loader.FontLoader;
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
import shape2d.Circle;
import shape2d.Quad;
import vector.Vector2f;
import broadphase.SAP2;

public class FrictionTest2d extends StandardGame {
	PhysicsSpace2 space;
	RigidBody2 rb1;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug2 physicsdebug;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(
				new EPA2()), new ImpulseResolution(), new ProjectionCorrection(
				1), new MultiPointManifoldManager2());
		space.setGlobalForce(new Vector2f(0, 100));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug2(inputs, font, space);

		Quad ground = new Quad(400, 550, 300, 20);
		RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		add2dObject(ground);

		Quad slope = new Quad(400, 300, 100, 20);
		slope.rotate(40);
		RigidBody2 rb2 = new RigidBody2(PhysicsShapeCreator.create(slope));
		space.addRigidBody(slope, rb2);
		add2dObject(slope);
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		debugger.render2d(fps, objects.size(), objects2d.size());
		debugger.begin();
		render2dScene();
		debugger.end();
		physicsdebug.render2d();
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
				add2dObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Circle c = new Circle(400, 80, 20, 10);
				RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(c));
				rb.setMass(1f);
				rb.setInertia(new Matrix1f(1));
				space.addRigidBody(c, rb);
				add2dObject(c);
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
