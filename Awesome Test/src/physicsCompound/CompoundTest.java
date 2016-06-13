package physicsCompound;

import java.awt.Color;

import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import narrowphase.EPA;
import narrowphase.GJK;
import objects.CompoundObject3;
import objects.RigidBody3;
import objects.ShapedObject3;
import physics.PhysicsDebug;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import quaternion.Quaternionf;
import resolution.ImpulseResolution;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import sound.NullSoundEnvironment;
import utils.Debugger;
import vector.Vector3f;

public class CompoundTest extends StandardGame {
	PhysicsSpace space;
	int tempdelta = 0;
	boolean impulseapplied = false;
	Debugger debugger;
	PhysicsDebug physicsdebug;
	Shader defaultshader;

	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(new EPA()), new ImpulseResolution(),
				new ProjectionCorrection(0.01f), new SimpleManifoldManager<Vector3f>());// new
																						// MultiPointManifoldManager());
		space.setGlobalGravitation(new Vector3f(0, -8f, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space, defaultshader);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(ground));

		space.addRigidBody(ground, rb);
		defaultshader.addObject(ground);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
		physicsdebug.render3d();
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
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Box b = new Box(0, 10, 0, 0.5f, 0.5f, 0.5f);
				Sphere s = new Sphere(0, 10, 0, 0.7f, 36, 36);
				b.setColor(Color.BLUE);
				s.setColor(Color.RED);
				CompoundObject3 rb = new CompoundObject3();
				rb.addCollisionShape(PhysicsShapeCreator.create(b));
				rb.addCollisionShape(PhysicsShapeCreator.create(s));
				rb.setMass(0.1f);
				rb.setInertia(new Quaternionf(0.1f, 0, 0, 0));
				space.addCompoundObject(rb, new ShapedObject3[] { b, s });
				defaultshader.addObject(b);
				defaultshader.addObject(s);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Sphere c = new Sphere(0, 10, 0, 0.5f, 36, 36);
				c.setColor(Color.RED);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(c));
				rb.setMass(0.1f);
				rb.setInertia(new Quaternionf(0.03f, 0, 0, 0));
				space.addRigidBody(c, rb);
				defaultshader.addObject(c);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("2")) {
				Sphere s1 = new Sphere(-0.5f, 10, 0, 0.5f, 36, 36);
				Sphere s2 = new Sphere(0.5f, 10, 0, 0.5f, 36, 36);
				s1.setColor(Color.RED);
				s2.setColor(Color.RED);
				CompoundObject3 rb = new CompoundObject3();
				rb.addCollisionShape(PhysicsShapeCreator.create(s1));
				rb.addCollisionShape(PhysicsShapeCreator.create(s2));
				rb.setMass(0.1f);
				rb.setInertia(new Quaternionf(0.1f, 0, 0, 0));
				space.addCompoundObject(rb, new ShapedObject3[] { s1, s2 });
				defaultshader.addObject(s1);
				defaultshader.addObject(s2);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}

		debugger.update(fps, 0, 0);
		// if (run.isActive() || step.isActive())
		space.update(delta);
		physicsdebug.update();

		if (display.isMouseBound())
			cam.update(delta);
	}
}
