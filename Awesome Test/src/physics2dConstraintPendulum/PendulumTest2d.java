package physics2dConstraintPendulum;

import broadphase.SAP2;
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
import shape2d.Quad;
import sound.NullSoundEnvironment;
import utils.Debugger;
import vector.Vector2f;

public class PendulumTest2d extends StandardGame {
	PhysicsSpace2 space;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug2 physicsdebug;
	double time = 0;
	RigidBody2 rbB1, rbB21, rbB22, rbB31, rbB32, rbB33;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(1400, 600), new PixelFormat(), new VideoSettings(1400, 600),
				new NullSoundEnvironment());
		// display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		space = new PhysicsSpace2(new VerletIntegration(), new SAP2(), new GJK2(new EPA2()), new ImpulseResolution(),
				new ProjectionCorrection(1), new MultiPointManifoldManager2()); // SimpleManifoldManager<Vector2f>());
		space.setGlobalGravitation(new Vector2f(0, 120));

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

		Quad base1 = new Quad(200, 20, 5, 5);
		RigidBody2 rb1 = new RigidBody2(PhysicsShapeCreator.create(base1));
		space.addRigidBody(base1, rb1);
		defaultshader2.addObject(base1);

		Quad base2 = new Quad(600, 20, 5, 5);
		RigidBody2 rb2 = new RigidBody2(PhysicsShapeCreator.create(base2));
		space.addRigidBody(base2, rb2);
		defaultshader2.addObject(base2);

		Quad base3 = new Quad(1000, 20, 5, 5);
		RigidBody2 rb3 = new RigidBody2(PhysicsShapeCreator.create(base3));
		space.addRigidBody(base3, rb3);
		defaultshader2.addObject(base3);

		// Pendulum 1
		Circle body1 = new Circle(0, 0, 20, 100);
		rbB1 = new RigidBody2(PhysicsShapeCreator.create(body1));
		rbB1.setMass(1f);
		rbB1.setInertia(new Matrix1f(1));
		rbB1.setLinearDamping(0.0f);
		space.addRigidBody(body1, rbB1);
		defaultshader2.addObject(body1);

		Constraint2 constraint = new OLDDistanceConstraint2(rb1, rbB1, 180);
		space.addConstraint(constraint);

		// Pendulum 2
		Circle body21 = new Circle(400, 0, 20, 100);
		rbB21 = new RigidBody2(PhysicsShapeCreator.create(body21));
		rbB21.setMass(1f);
		rbB21.setInertia(new Matrix1f(1));
		rbB21.setLinearDamping(0.0f);
		space.addRigidBody(body21, rbB21);
		defaultshader2.addObject(body21);

		Circle body22 = new Circle(400, 180, 20, 100);
		rbB22 = new RigidBody2(PhysicsShapeCreator.create(body22));
		rbB22.setMass(1f);
		rbB22.setInertia(new Matrix1f(1));
		rbB22.setLinearDamping(0.0f);
		space.addRigidBody(body22, rbB22);
		defaultshader2.addObject(body22);

		Constraint2 constraint21 = new OLDDistanceConstraint2(rb2, rbB21, 180);
		Constraint2 constraint22 = new OLDDistanceConstraint2(rbB21, rbB22, 180);
		space.addConstraint(constraint21);
		space.addConstraint(constraint22);

		// Pendulum 3
		Circle body31 = new Circle(800, 0, 20, 100);
		rbB31 = new RigidBody2(PhysicsShapeCreator.create(body31));
		rbB31.setMass(1f);
		rbB31.setInertia(new Matrix1f(1));
		rbB31.setLinearDamping(0.0f);
		space.addRigidBody(body31, rbB31);
		defaultshader2.addObject(body31);

		Circle body32 = new Circle(800, 180, 20, 100);
		rbB32 = new RigidBody2(PhysicsShapeCreator.create(body32));
		rbB32.setMass(1f);
		rbB32.setInertia(new Matrix1f(1));
		rbB32.setLinearDamping(0.0f);
		space.addRigidBody(body32, rbB32);
		defaultshader2.addObject(body32);

		Circle body33 = new Circle(980, 180, 20, 100);
		rbB33 = new RigidBody2(PhysicsShapeCreator.create(body33));
		rbB33.setMass(1f);
		rbB33.setInertia(new Matrix1f(1));
		rbB33.setLinearDamping(0.0f);
		space.addRigidBody(body33, rbB33);
		defaultshader2.addObject(body33);

		Constraint2 constraint31 = new OLDDistanceConstraint2(rb3, rbB31, 180);
		Constraint2 constraint32 = new OLDDistanceConstraint2(rbB31, rbB32, 180);
		Constraint2 constraint33 = new OLDDistanceConstraint2(rbB32, rbB33, 180);
		space.addConstraint(constraint31);
		space.addConstraint(constraint32);
		space.addConstraint(constraint33);
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

	private void reset() {
		rbB1.setLinearVelocity(new Vector2f());
		rbB1.translateTo(0, 0);
		rbB21.setLinearVelocity(new Vector2f());
		rbB21.translateTo(400, 0);
		rbB22.setLinearVelocity(new Vector2f());
		rbB22.translateTo(400, 180);
		rbB31.setLinearVelocity(new Vector2f());
		rbB31.translateTo(800, 0);
		rbB32.setLinearVelocity(new Vector2f());
		rbB32.translateTo(980, 180);
		rbB33.setLinearVelocity(new Vector2f());
		rbB33.translateTo(980, 180);
	}

	@Override
	public void update(int delta) {
		time += delta;
		if (time > 60000 * 2) {
			reset();
			time = 0;
		}

		debugger.update(fps, 0, 0);
		space.update(delta);
		physicsdebug.update();
		cam.update(delta);
	}
}
