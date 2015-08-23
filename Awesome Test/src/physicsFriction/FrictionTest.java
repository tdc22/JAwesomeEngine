package physicsFriction;

import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
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
import shader.Shader;
import shape.Box;
import utils.Debugger;
import vector.Vector3f;

public class FrictionTest extends StandardGame {
	PhysicsSpace space;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug physicsdebug;
	Shader defaultshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 20);
		cam.rotateTo(0, 0);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(new EPA()), new LinearImpulseResolution(),
				new ProjectionCorrection(0.01f), new SimpleManifoldManager<Vector3f>());
		space.setGlobalGravitation(new Vector3f(0, -8f, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshader2, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		defaultshader.addObject(ground);

		Box q = new Box(-12, 0, 0, 1, 1, 1);
		RigidBody3 rb1 = new RigidBody3(PhysicsShapeCreator.create(q));
		rb1.setMass(0.1f);
		rb1.setInertia(new Quaternionf());
		rb1.applyCentralImpulse(new Vector3f(0.5f, 0, 0));
		space.addRigidBody(q, rb1);
		defaultshader.addObject(q);
		tempdelta = 0;
	}

	@Override
	public void render() {
		debugger.begin();
		renderScene();
		debugger.end();
		physicsdebug.render3d();
	}

	@Override
	public void render2d() {
		render2dScene();
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Box q = new Box(-12, 0, 0, 1, 1, 1);
				RigidBody3 rb1 = new RigidBody3(PhysicsShapeCreator.create(q));
				rb1.setMass(0.1f);
				rb1.setInertia(new Quaternionf());
				rb1.applyCentralImpulse(new Vector3f(0.5f, 0, 0));
				space.addRigidBody(q, rb1);
				defaultshader.addObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Box q = new Box(12, 0, 0, 1, 1, 1);
				RigidBody3 rb1 = new RigidBody3(PhysicsShapeCreator.create(q));
				rb1.setMass(0.1f);
				rb1.setInertia(new Quaternionf());
				rb1.applyCentralImpulse(new Vector3f(-0.5f, 0, 0));
				space.addRigidBody(q, rb1);
				defaultshader.addObject(q);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		debugger.update(fps, 0, 0);
		space.update(delta);
		physicsdebug.update();
		cam.update(delta);
	}
}
