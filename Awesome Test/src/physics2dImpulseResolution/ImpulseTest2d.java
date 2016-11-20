package physics2dImpulseResolution;

import game.StandardGame;
import integration.EulerIntegration;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import narrowphase.EPA2;
import narrowphase.GJK2;
import narrowphase.SupportRaycast2;
import objects.RigidBody2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.NullCorrection;
import resolution.LinearImpulseResolution;
import shader.Shader;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import vector.Vector2f;
import broadphase.SAP2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ImpulseTest2d extends StandardGame {
	PhysicsSpace2 space;
	RigidBody2 rb1, rb3, rb5, rb7, rb9;
	int tempdelta = 0;
	boolean impulseapplied = false;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		// mouse.setGrabbed(false);

		Shader defaultshader2 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(
				new EPA2()), new SupportRaycast2(),
				new LinearImpulseResolution(), new NullCorrection(),
				new SimpleManifoldManager<Vector2f>());

		// Row 1
		Quad q1 = new Quad(250, 100, 25, 25);
		rb1 = new RigidBody2(PhysicsShapeCreator.create(q1));
		rb1.setMass(1);
		space.addRigidBody(q1, rb1);
		defaultshader2.addObject(q1);

		Quad q2 = new Quad(400, 100, 25, 25);
		RigidBody2 rb2 = new RigidBody2(PhysicsShapeCreator.create(q2));
		rb2.setMass(1);
		space.addRigidBody(q2, rb2);
		defaultshader2.addObject(q2);

		// Row 2
		Quad q3 = new Quad(250, 200, 25, 25);
		rb3 = new RigidBody2(PhysicsShapeCreator.create(q3));
		rb3.setMass(3);
		space.addRigidBody(q3, rb3);
		defaultshader2.addObject(q3);

		Quad q4 = new Quad(400, 200, 25, 25);
		RigidBody2 rb4 = new RigidBody2(PhysicsShapeCreator.create(q4));
		rb4.setMass(1);
		space.addRigidBody(q4, rb4);
		defaultshader2.addObject(q4);

		// Row 3
		Quad q5 = new Quad(250, 300, 25, 25);
		rb5 = new RigidBody2(PhysicsShapeCreator.create(q5));
		rb5.setMass(1);
		space.addRigidBody(q5, rb5);
		defaultshader2.addObject(q5);

		Quad q6 = new Quad(400, 300, 25, 25);
		RigidBody2 rb6 = new RigidBody2(PhysicsShapeCreator.create(q6));
		rb6.setMass(3);
		space.addRigidBody(q6, rb6);
		defaultshader2.addObject(q6);

		// Row 4
		Quad q7 = new Quad(250, 400, 25, 25);
		rb7 = new RigidBody2(PhysicsShapeCreator.create(q7));
		rb7.setMass(1);
		space.addRigidBody(q7, rb7);
		defaultshader2.addObject(q7);

		Quad q8 = new Quad(400, 400, 25, 25);
		RigidBody2 rb8 = new RigidBody2(PhysicsShapeCreator.create(q8));
		rb8.setMass(0);
		space.addRigidBody(q8, rb8);
		defaultshader2.addObject(q8);

		// Row 5
		Quad q9 = new Quad(250, 500, 25, 25);
		rb9 = new RigidBody2(PhysicsShapeCreator.create(q9));
		rb9.setMass(1);
		space.addRigidBody(q9, rb9);
		defaultshader2.addObject(q9);

		Quad q10 = new Quad(400, 460, 25, 25);
		RigidBody2 rb10 = new RigidBody2(PhysicsShapeCreator.create(q10));
		rb10.setMass(1);
		space.addRigidBody(q10, rb10);
		defaultshader2.addObject(q10);
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		render2dLayer();
	}

	@Override
	public void renderInterface() {

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
