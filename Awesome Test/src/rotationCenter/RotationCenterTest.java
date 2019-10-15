package rotationCenter;

import broadphase.DynamicAABBTree3;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.MultiPointManifoldManager;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.RigidBody3;
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
import vector.Vector3f;

public class RotationCenterTest extends StandardGame {
	Shader defaultshader;
	Box rotquad1, rotquad2, rotquad3, rotquad4, rotquad5, rotquad6;
	PhysicsSpace space;
	PhysicsDebug physicsdebug;
	int spawndelta = 0;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		defaultshader = new Shader(ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		rotquad1 = new Box(0, 0, 0, 1, 1, 1);
		rotquad2 = new Box(4, 0, 0, 1, 1, 1);
		rotquad3 = new Box(8, 0, 0, 1, 1, 1);
		rotquad4 = new Box(12, 0, 0, 1, 1, 1);
		rotquad5 = new Box(16, 0, 0, 1, 1, 1);
		rotquad2.setRotationCenter(new Vector3f(1, 0, 0));
		rotquad3.setRotationCenter(new Vector3f(0, 1, 0));
		rotquad4.setRotationCenter(new Vector3f(0, 0, 1));
		rotquad5.setRotationCenter(new Vector3f(1, 1, 1));
		defaultshader.addObject(rotquad1);
		defaultshader.addObject(rotquad2);
		defaultshader.addObject(rotquad3);
		defaultshader.addObject(rotquad4);
		defaultshader.addObject(rotquad5);
		
		space = new PhysicsSpace(new VerletIntegration(), new DynamicAABBTree3(), new GJK(new EPA()),
				new SupportRaycast(), new ImpulseResolution(), new ProjectionCorrection(1),
				new MultiPointManifoldManager());
		space.setGlobalGravitation(new Vector3f(0, -1, 0));
		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		physicsdebug = new PhysicsDebug(inputs, defaultshader, font, space);

		rotquad6 = new Box(0, -10, 0, 1, 1, 1);
		rotquad6.setRotationCenter(new Vector3f(2, 0, 0));
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(rotquad6));
		space.addRigidBody(rotquad6, rb);
		defaultshader.addObject(rotquad6);
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);

		float rotdelta = delta * 0.1f;
		rotquad1.rotate(0, 0, rotdelta);
		rotquad2.rotate(0, 0, rotdelta);
		rotquad3.rotate(0, 0, rotdelta);
		rotquad4.rotate(0, 0, rotdelta);
		rotquad5.rotate(0, 0, rotdelta);
		rotquad6.rotate(0, 0, rotdelta);
		
		spawndelta += delta;
		if (spawndelta > 1000) {
			Sphere c = new Sphere(0, -4, 0, 1, 32, 32);
			RigidBody3 rb1 = new RigidBody3(PhysicsShapeCreator.create(c));
			rb1.setMass(0.1f);
			rb1.setInertia(new Quaternionf(0.03f, 0, 0, 0));
			space.addRigidBody(c, rb1);
			defaultshader.addObject(c);
			spawndelta = 0;
		}
		space.update(delta);
		physicsdebug.update();
	}

}
