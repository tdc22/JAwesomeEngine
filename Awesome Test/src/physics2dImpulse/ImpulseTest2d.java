package physics2dImpulse;

import broadphase.SAP2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import integration.EulerIntegration;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import matrix.Matrix1f;
import narrowphase.EPA2;
import narrowphase.GJK2;
import objects.RigidBody2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shader.Shader;
import shape2d.Quad;
import vector.Vector2f;

public class ImpulseTest2d extends StandardGame {
	PhysicsSpace2 space;
	Quad q;
	RigidBody2 rb;
	int tempdelta = 0;
	boolean impulseapplied = false;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		// mouse.setGrabbed(false);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);
		
		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(new EPA2()), new NullResolution(),
				new NullCorrection(), new SimpleManifoldManager<Vector2f>());

		q = new Quad(400, 200, 25, 25);
		rb = new RigidBody2(PhysicsShapeCreator.create(q));
		rb.setMass(1);
		rb.setInertia(new Matrix1f(1));
		space.addRigidBody(q, rb);
		defaultshader2.addObject(q);
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
			rb.applyImpulse(new Vector2f(10f, 0), new Vector2f(0, -1));
			// rb.applyCentralImpulse(new Vector2f(0.2f,0));
			impulseapplied = true;
		}
		space.update(delta);
		cam.update(delta);
	}
}
