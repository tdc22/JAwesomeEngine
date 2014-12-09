package physics2dFriction3;

import game.Debugger;
import game.StandardGame;
import gui.Font;
import integration.EulerIntegration;
import loader.FontLoader;
import manifold.SimpleManifoldManager;
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
import vector.Vector2f;
import broadphase.SAP2;

public class FrictionTest2d extends StandardGame {
	PhysicsSpace2 space;
	RigidBody2 rb1, rb2;
	int tempdelta = 0;
	Debugger debugmanager;
	PhysicsDebug2 physicsdebug;

	@Override
	public void init() {
		initDisplay(false, 800, 600, true);
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		mouse.setGrabbed(false);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(
				new EPA2()), new ImpulseResolution(), new ProjectionCorrection(
				1), new SimpleManifoldManager<Vector2f>());
		space.setGlobalForce(new Vector2f(0, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugmanager = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug2(inputs, font, space);

		Circle c1 = new Circle(100, 200, 20, 6);
		rb1 = PhysicsShapeCreator.create(c1);
		rb1.setAngularDamping(0);
		rb1.setMass(1);
		rb1.setInertia(new Matrix1f(1));
		space.addRigidBody(c1, rb1);
		add2dObject(c1);

		Circle c2 = new Circle(400, 200, 20, 6);
		rb2 = PhysicsShapeCreator.create(c2);
		rb2.setAngularDamping(0);
		rb2.setMass(1);
		rb2.setInertia(new Matrix1f(1));
		space.addRigidBody(c2, rb2);
		add2dObject(c2);

		rb1.applyImpulse(new Vector2f(100, 0), new Vector2f(0, -10));
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
		System.out.println(rb1.getAngularVelocity() + "; "
				+ rb2.getAngularVelocity());
		debugmanager.update();
		physicsdebug.update();
		space.update(delta);
		cam.update(delta);
	}
}
