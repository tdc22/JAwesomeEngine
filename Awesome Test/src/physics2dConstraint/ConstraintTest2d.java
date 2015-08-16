package physics2dConstraint;

import game.StandardGame;
import gui.Font;
import integration.VerletIntegration;

import java.util.ArrayList;
import java.util.List;

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
import shape2d.Quad;
import utils.Debugger;
import vector.Vector2f;
import broadphase.SAP2;
import constraints.DistanceConstraint2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ConstraintTest2d extends StandardGame {
	PhysicsSpace2 space;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug2 physicsdebug;
	List<RigidBody2> bodies;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		// display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		space = new PhysicsSpace2(new VerletIntegration(), new SAP2(),
				new GJK2(new EPA2()), new ImpulseResolution(),
				new ProjectionCorrection(1), new MultiPointManifoldManager2()); // SimpleManifoldManager<Vector2f>());
		space.setGlobalGravitation(new Vector2f(0, 120));

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshader2, font,
				cam);
		physicsdebug = new PhysicsDebug2(inputs, font, space);

		Quad ground = new Quad(400, 550, 300, 20);
		RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		defaultshader2.addObject(ground);

		bodies = new ArrayList<RigidBody2>();

		// Distance constraint
		Circle leftCircle = new Circle(370, 50, 20, 10);
		RigidBody2 rbL = new RigidBody2(PhysicsShapeCreator.create(leftCircle));
		rbL.setMass(1f);
		rbL.setInertia(new Matrix1f(1));
		bodies.add(rbL);
		space.addRigidBody(leftCircle, rbL);
		defaultshader2.addObject(leftCircle);

		Circle rightCircle = new Circle(430, 50, 20, 10);
		RigidBody2 rbR = new RigidBody2(PhysicsShapeCreator.create(rightCircle));
		rbR.setMass(1f);
		rbR.setInertia(new Matrix1f(1));
		bodies.add(rbR);
		space.addRigidBody(rightCircle, rbR);
		defaultshader2.addObject(rightCircle);

		Constraint2 constraint = new DistanceConstraint2(rbL, rbR, 60);
		space.addConstraint(constraint);
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		debugger.begin();
		render2dScene();
		debugger.end();
		physicsdebug.render2d();
	}

	@Override
	public void update(int delta) {
		if (inputs.isKeyDown("1"))
			bodies.get(0).applyCentralImpulse(new Vector2f(0, -20));
		if (inputs.isKeyDown("2"))
			bodies.get(1).applyCentralImpulse(new Vector2f(0, -20));

		debugger.update(fps, 0, 0);
		space.update(delta);
		physicsdebug.update();
		cam.update(delta);
	}
}
