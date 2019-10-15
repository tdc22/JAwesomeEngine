package physicsImpulse;

import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager3;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.RigidBody3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.NullCorrection;
import quaternion.Quaternionf;
import resolution.NullResolution;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import utils.Debugger;
import vector.Vector3f;

public class ImpulseTest extends StandardGame {
	PhysicsSpace space;
	Box b;
	RigidBody3 rb;
	Debugger debugger;
	int tempdelta = 0;
	boolean impulseapplied = false;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		display.bindMouse();

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		space = new PhysicsSpace(new EulerIntegration(), new SAP(), new GJK(new EPA()), new SupportRaycast(),
				new NullResolution(), new NullCorrection(), new SimpleManifoldManager3());

		b = new Box(0, 0, 0, 1, 1, 1);
		rb = new RigidBody3(PhysicsShapeCreator.create(b));
		rb.setMass(1);
		rb.setInertia(new Quaternionf());
		space.addRigidBody(b, rb);
		defaultshader.addObject(b);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		if (tempdelta <= 2000)
			tempdelta += delta;
		else if (!impulseapplied) {
			System.out.println("Impulse!");
			rb.applyImpulse(new Vector3f(10f, 0, 0), new Vector3f(0, 1, 0));
			// rb.applyCentralImpulse(new Vector3f(0.01f, 0, 0));
			impulseapplied = true;
		}

		debugger.update(fps, 0, 0);
		space.update(delta);
		cam.update(delta);
	}
}
