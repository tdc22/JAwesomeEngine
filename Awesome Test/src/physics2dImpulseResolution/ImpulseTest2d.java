package physics2dImpulseResolution;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import integration.EulerIntegration;
import manifold.SimpleManifoldManager;
import narrowphase.EPA2;
import narrowphase.GJK2;
import objects.RigidBody2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.NullCorrection;
import resolution.LinearImpulseResolution;
import shape2d.Quad;
import vector.Vector2f;
import broadphase.SAP2;

public class ImpulseTest2d extends StandardGame {
	PhysicsSpace2 space;
	RigidBody2 rb1, rb3, rb5, rb7, rb9;
	int tempdelta = 0;
	boolean impulseapplied = false;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		// mouse.setGrabbed(false);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(
				new EPA2()), new LinearImpulseResolution(),
				new NullCorrection(), new SimpleManifoldManager<Vector2f>());

		// Column 1
		Quad q1 = new Quad(250, 100, 25, 25);
		rb1 = new RigidBody2(PhysicsShapeCreator.create(q1));
		rb1.setMass(1);
		space.addRigidBody(q1, rb1);
		add2dObject(q1);

		Quad q2 = new Quad(400, 100, 25, 25);
		RigidBody2 rb2 = new RigidBody2(PhysicsShapeCreator.create(q2));
		rb2.setMass(1);
		space.addRigidBody(q2, rb2);
		add2dObject(q2);

		// Column 2
		Quad q3 = new Quad(250, 200, 25, 25);
		rb3 = new RigidBody2(PhysicsShapeCreator.create(q3));
		rb3.setMass(3);
		space.addRigidBody(q3, rb3);
		add2dObject(q3);

		Quad q4 = new Quad(400, 200, 25, 25);
		RigidBody2 rb4 = new RigidBody2(PhysicsShapeCreator.create(q4));
		rb4.setMass(1);
		space.addRigidBody(q4, rb4);
		add2dObject(q4);

		// Column 3
		Quad q5 = new Quad(250, 300, 25, 25);
		rb5 = new RigidBody2(PhysicsShapeCreator.create(q5));
		rb5.setMass(1);
		space.addRigidBody(q5, rb5);
		add2dObject(q5);

		Quad q6 = new Quad(400, 300, 25, 25);
		RigidBody2 rb6 = new RigidBody2(PhysicsShapeCreator.create(q6));
		rb6.setMass(3);
		space.addRigidBody(q6, rb6);
		add2dObject(q6);

		// Column 4
		Quad q7 = new Quad(250, 400, 25, 25);
		rb7 = new RigidBody2(PhysicsShapeCreator.create(q7));
		rb7.setMass(1);
		space.addRigidBody(q7, rb7);
		add2dObject(q7);

		Quad q8 = new Quad(400, 400, 25, 25);
		RigidBody2 rb8 = new RigidBody2(PhysicsShapeCreator.create(q8));
		rb8.setMass(0);
		space.addRigidBody(q8, rb8);
		add2dObject(q8);

		// Column 5
		Quad q9 = new Quad(250, 500, 25, 25);
		rb9 = new RigidBody2(PhysicsShapeCreator.create(q9));
		rb9.setMass(1);
		space.addRigidBody(q9, rb9);
		add2dObject(q9);

		Quad q10 = new Quad(400, 460, 25, 25);
		RigidBody2 rb10 = new RigidBody2(PhysicsShapeCreator.create(q10));
		rb10.setMass(1);
		space.addRigidBody(q10, rb10);
		add2dObject(q10);
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
	}

	@Override
	public void update(int delta) {
		if (tempdelta <= 2000)
			tempdelta += delta;
		else if (!impulseapplied) {
			System.out.println("Impulse!");
			// rb.applyImpulse(new Vector2f(0.2f, 0), new Vector2f(0, 1));
			rb1.applyCentralImpulse(new Vector2f(100f, 0));
			rb3.applyCentralImpulse(new Vector2f(100f, 0));
			rb5.applyCentralImpulse(new Vector2f(100f, 0));
			rb7.applyCentralImpulse(new Vector2f(100f, 0));
			rb9.applyCentralImpulse(new Vector2f(100f, 0));
			impulseapplied = true;
		}
		space.update(delta);
		cam.update(delta);
	}
}
