package rotationCenter2d;

import broadphase.DynamicAABBTree2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.MultiPointManifoldManager2;
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
import shape2d.Quad;
import sound.NullSoundEnvironment;
import vector.Vector2f;

public class RotationCenterTest extends StandardGame {
	Shader defaultshader;
	Quad rotquad1, rotquad2, rotquad3, rotquad4, rotquad5, rotquad6;
	PhysicsSpace2 space;
	PhysicsDebug2 physicsdebug;
	int spawndelta = 0;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		rotquad1 = new Quad(100, 150, 25, 25);
		rotquad2 = new Quad(250, 150, 25, 25);
		rotquad3 = new Quad(400, 150, 25, 25);
		rotquad4 = new Quad(550, 150, 25, 25);
		rotquad5 = new Quad(700, 150, 25, 25);
		rotquad2.setRotationCenter(new Vector2f(25, 0));
		rotquad3.setRotationCenter(new Vector2f(0, 25));
		rotquad4.setRotationCenter(new Vector2f(25, 25));
		rotquad5.setRotationCenter(new Vector2f(-50, -50));
		defaultshader.addObject(rotquad1);
		defaultshader.addObject(rotquad2);
		defaultshader.addObject(rotquad3);
		defaultshader.addObject(rotquad4);
		defaultshader.addObject(rotquad5);

		space = new PhysicsSpace2(new VerletIntegration(), new DynamicAABBTree2(), new GJK2(new EPA2()),
				new SupportRaycast2(), new ImpulseResolution(), new ProjectionCorrection(1),
				new MultiPointManifoldManager2());
		space.setGlobalGravitation(new Vector2f(0, 120));
		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		physicsdebug = new PhysicsDebug2(inputs, defaultshader, font, space);

		rotquad6 = new Quad(400, 500, 25, 25);
		rotquad6.setRotationCenter(new Vector2f(50, 0));
		RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(rotquad6));
		space.addRigidBody(rotquad6, rb);
		defaultshader.addObject(rotquad6);
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
		float rotdelta = delta * 0.1f;
		rotquad1.rotate(rotdelta);
		rotquad2.rotate(rotdelta);
		rotquad3.rotate(rotdelta);
		rotquad4.rotate(rotdelta);
		rotquad5.rotate(rotdelta);
		rotquad6.rotate(rotdelta);

		spawndelta += delta;
		if (spawndelta > 1000) {
			Circle c = new Circle(400, 250, 20, 10);
			RigidBody2 rb1 = new RigidBody2(PhysicsShapeCreator.create(c));
			rb1.setMass(1f);
			rb1.setInertia(new Matrix1f(1));
			space.addRigidBody(c, rb1);
			defaultshader.addObject(c);
			spawndelta = 0;
		}
		space.update(delta);
		physicsdebug.update();
	}

}
