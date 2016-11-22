package physics2dFriction3;

import broadphase.SAP2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import matrix.Matrix1f;
import narrowphase.EPA2;
import narrowphase.GJK2;
import narrowphase.SupportRaycast2;
import objects.RigidBody2;
import physics.PhysicsDebug2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.ProjectionCorrection;
import resolution.ImpulseResolution;
import shader.Shader;
import shape2d.Circle;
import sound.NullSoundEnvironment;
import utils.Debugger;
import vector.Vector2f;

public class FrictionTest2d extends StandardGame {
	PhysicsSpace2 space;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug2 physicsdebug;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		// mouse.setGrabbed(false);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshaderInterface);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(new EPA2()), new SupportRaycast2(),
				new ImpulseResolution(), new ProjectionCorrection(1), new SimpleManifoldManager<Vector2f>());
		space.setGlobalGravitation(new Vector2f(0, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);
		physicsdebug = new PhysicsDebug2(inputs, defaultshader2, font, space);

		Circle c1 = new Circle(100, 200, 20, 6);
		RigidBody2 rb1 = new RigidBody2(PhysicsShapeCreator.create(c1));
		rb1.setAngularDamping(0);
		rb1.setMass(1);
		rb1.setInertia(new Matrix1f(1));
		space.addRigidBody(c1, rb1);
		defaultshader2.addObject(c1);

		Circle c2 = new Circle(400, 200, 20, 6);
		RigidBody2 rb2 = new RigidBody2(PhysicsShapeCreator.create(c2));
		rb2.setAngularDamping(0);
		rb2.setMass(1);
		rb2.setInertia(new Matrix1f(1));
		space.addRigidBody(c2, rb2);
		defaultshader2.addObject(c2);

		rb1.applyImpulse(new Vector2f(100, 0), new Vector2f(0, -10));
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		debugger.begin();
		render2dLayer();
	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getAngularVelocity() + "; "
		// + rb2.getAngularVelocity());
		debugger.update(fps, 0, 0);
		physicsdebug.update();
		space.update(delta);
		cam.update(delta);
	}
}
