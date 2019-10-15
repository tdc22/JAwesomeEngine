package physicsConvexHull;

import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Color;
import gui.Font;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager3;
import math.QuatMath;
import math.VecMath;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.RigidBody3;
import objects.ShapedObject3;
import physics.PhysicsDebug;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import physicsSupportFunction.SupportObject;
import positionalcorrection.ProjectionCorrection;
import quaternion.Quaternionf;
import resolution.ImpulseResolution;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import sound.NullSoundEnvironment;
import utils.Debugger;
import vector.Vector3f;

public class ConvexTest extends StandardGame {
	PhysicsSpace space;
	int tempdelta = 0;
	boolean impulseapplied = false;
	Debugger debugger;
	PhysicsDebug physicsdebug;
	Shader defaultshader;

	RigidBody3 bunnyBody, bunnyBody2;
	Cylinder directionpointer;
	Sphere supportposition;

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

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(new EPA()), new SupportRaycast(),
				new ImpulseResolution(), new ProjectionCorrection(0.01f), new SimpleManifoldManager3());
		space.setGlobalGravitation(new Vector3f(0, -8f, 0));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space, defaultshader);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(ground));

		space.addRigidBody(ground, rb);
		defaultshader.addObject(ground);

		ShapedObject3 bunny = ModelLoader.load("res/models/bunny.mobj");
		ShapedObject3 bunny2 = ModelLoader.load("res/models/bunny_lowpoly.mobj");
		bunny2.translateTo(20, 0, 0);
		bunnyBody = new RigidBody3(PhysicsShapeCreator.createHull(bunny));
		bunnyBody2 = new RigidBody3(PhysicsShapeCreator.createHull(bunny2));
		bunnyBody.setMass(0);
		bunnyBody2.setMass(0);

		space.addRigidBody(bunny, bunnyBody);
		space.addRigidBody(bunny2, bunnyBody2);
		defaultshader.addObject(bunny);
		defaultshader.addObject(bunny2);

		SupportObject so1 = new SupportObject(bunny, bunnyBody);
		defaultshader.addObject(so1);

		directionpointer = new Cylinder(0, 0, 0, 0.1f, 1, 36);
		defaultshader.addObject(directionpointer);

		supportposition = new Sphere(0, 0, 0, 0.2f, 36, 36);
		defaultshader.addObject(supportposition);
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

	private Vector3f up = new Vector3f(0, 1, 0);

	@Override
	public void update(int delta) {
		// System.out.println(rb1.getLinearVelocity());
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Box q = new Box(cam.getTranslation(), 0.5f, 0.5f, 0.5f);
				q.setColor(Color.BLUE);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(q));
				rb.setMass(0.1f);
				rb.setInertia(new Quaternionf(0.1f, 0, 0, 0));
				space.addRigidBody(q, rb);
				rb.applyCentralImpulse(VecMath.scale(cam.getDirection(), 3));
				defaultshader.addObject(q);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Sphere c = new Sphere(cam.getTranslation(), 0.5f, 36, 36);
				c.setColor(Color.RED);
				RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(c));
				rb.setMass(0.1f);
				rb.setInertia(new Quaternionf(0.03f, 0, 0, 0));
				space.addRigidBody(c, rb);
				rb.applyCentralImpulse(VecMath.scale(cam.getDirection(), 3));
				defaultshader.addObject(c);
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		float rotspeed = delta * 0.01f;
		bunnyBody2.rotate(rotspeed, rotspeed, rotspeed);

		debugger.update(fps, 0, 0);
		space.update(delta);
		physicsdebug.update();

		if (inputs.isKeyDown("I")) {
			directionpointer.rotate(0, 0, 0.1f * delta);
		}
		if (inputs.isKeyDown("K")) {
			directionpointer.rotate(0, 0, -0.1f * delta);
		}
		if (inputs.isKeyDown("J")) {
			directionpointer.rotate(0.1f * delta, 0, 0);
		}
		if (inputs.isKeyDown("L")) {
			directionpointer.rotate(-0.1f * delta, 0, 0);
		}
		Vector3f dir = QuatMath.transform(directionpointer.getRotation(), up);
		Vector3f sup = bunnyBody.supportPoint(dir);
		this.supportposition.translateTo(sup);

		if (display.isMouseBound())
			cam.update(delta);
	}
}
