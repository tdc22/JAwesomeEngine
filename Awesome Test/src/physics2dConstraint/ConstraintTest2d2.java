package physics2dConstraint;

import java.util.ArrayList;
import java.util.List;

import broadphase.SAP2;
import constraints.DistanceConstraint2;
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
import objects.Constraint2;
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

public class ConstraintTest2d2 extends StandardGame {
	PhysicsSpace2 space;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug2 physicsdebug;
	List<RigidBody2> bodies;
	int steps = 0;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		// display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		space = new PhysicsSpace2(new VerletIntegration(), new SAP2(), new GJK2(new EPA2()), new ImpulseResolution(),
				new ProjectionCorrection(1), new MultiPointManifoldManager2()); // SimpleManifoldManager<Vector2f>());
		space.setGlobalGravitation(new Vector2f(0, 0));

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);
		physicsdebug = new PhysicsDebug2(inputs, defaultshader2, font, space);

		bodies = new ArrayList<RigidBody2>();

		// Distance constraint
		Circle leftCircle = new Circle(-10, 4, 1, 10);
		RigidBody2 rbL = new RigidBody2(PhysicsShapeCreator.create(leftCircle));
		rbL.setMass(1f);
		rbL.setInertia(new Matrix1f(1));
		bodies.add(rbL);
		space.addRigidBody(leftCircle, rbL);
		defaultshader2.addObject(leftCircle);

		Circle rightCircle = new Circle(10, 4, 1, 10);
		RigidBody2 rbR = new RigidBody2(PhysicsShapeCreator.create(rightCircle));
		rbR.setMass(1f);
		rbR.setInertia(new Matrix1f(1));
		bodies.add(rbR);
		space.addRigidBody(rightCircle, rbR);
		defaultshader2.addObject(rightCircle);

		Constraint2 constraint = new DistanceConstraint2(rbL, rbR, new Vector2f(0, 0), new Vector2f(0, 0), 3);
		space.addConstraint(constraint);
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
		if (inputs.isKeyDown("1"))
			bodies.get(0).applyCentralImpulse(new Vector2f(0, -20));
		if (inputs.isKeyDown("2"))
			bodies.get(1).applyCentralImpulse(new Vector2f(0, -20));

		delta = (int) ((1f / 30f) * 1000f);
		debugger.update(fps, 0, 0);
		space.update(delta);
		physicsdebug.update();
		cam.update(delta);

		RigidBody2 bodyA = bodies.get(0);
		RigidBody2 bodyB = bodies.get(1);
		System.out.println(bodyA.getTranslation().x + "; " + bodyA.getTranslation().y + "; "
				+ bodyA.getRotation().angle() + "; " + bodyB.getTranslation().x + "; " + bodyB.getTranslation().y + "; "
				+ bodyB.getRotation().angle());
		steps++;
		if (steps == 60)
			System.exit(0);
	}

	public static void main(String[] args) {
		ConstraintTest2d2 ct = new ConstraintTest2d2();
		ct.start();
	}
}
