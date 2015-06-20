package physics2dCompound;

import game.StandardGame;
import gui.Font;
import integration.VerletIntegration;
import loader.FontLoader;
import manifold.MultiPointManifoldManager2;
import matrix.Matrix1f;
import narrowphase.EPA2;
import narrowphase.GJK2;
import objects.CompoundObject2;
import objects.RigidBody2;
import physics.PhysicsDebug2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.ProjectionCorrection;
import resolution.ImpulseResolution;
import shape2d.Circle;
import shape2d.Quad;
import utils.Debugger;
import vector.Vector2f;
import broadphase.SAP2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class CompoundTest2d extends StandardGame {
	PhysicsSpace2 space;
	int tempdelta = 0;
	Debugger debugger;
	PhysicsDebug2 physicsdebug;
	CompoundObject2 rb1;

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

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug2(inputs, font, space);

		Quad ground = new Quad(400, 550, 300, 20);
		RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		add2dObject(ground);

		Quad q = new Quad(400, 80, 20, 20);
		Circle c = new Circle(400, 80, 24, 10);
		rb1 = new CompoundObject2();
		rb1.addCollisionShape(PhysicsShapeCreator.create(q));
		rb1.addCollisionShape(PhysicsShapeCreator.create(c));
		rb1.setMass(1f);
		rb1.setInertia(new Matrix1f(1));
		space.addCompoundObject(rb1, q, c);
		add2dObject(q);
		add2dObject(c);
		tempdelta = 0;
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		debugger.begin();
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
		physicsdebug.render2d();
	}

	@Override
	public void update(int delta) {
		if (tempdelta > 200) {
			if (inputs.isMouseButtonDown("0")) {
				Quad q = new Quad(400, 80, 20, 20);
				Circle c = new Circle(400, 80, 24, 10);
				CompoundObject2 rb = new CompoundObject2();
				rb.addCollisionShape(PhysicsShapeCreator.create(q));
				rb.addCollisionShape(PhysicsShapeCreator.create(c));
				rb.setMass(1f);
				rb.setInertia(new Matrix1f(1));
				space.addCompoundObject(rb, q, c);
				add2dObject(q);
				add2dObject(c);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("1")) {
				Circle c = new Circle(400, 80, 20, 10);
				RigidBody2 rb = new RigidBody2(PhysicsShapeCreator.create(c));
				rb.setMass(1f);
				rb.setInertia(new Matrix1f(1));
				space.addRigidBody(c, rb);
				add2dObject(c);
				tempdelta = 0;
			}
			if (inputs.isMouseButtonDown("2")) {
				space.getObjects().get(space.getObjects().size() - 1)
						.applyCentralImpulse(new Vector2f(100, 100));
				tempdelta = 0;
			}
		} else {
			tempdelta += delta;
		}

		// for(CompoundObject<?, ?> co : space.getCompoundObjects()) {
		// System.out.println(co.getCompoundBroadphase().getObjects().size() +
		// "; " + co.getCompoundBroadphase().getOverlaps().size());
		// }

		// System.out.println(rb1.getCompoundBroadphase().getObjects().size() +
		// "; " + rb1.getCompoundBroadphase().getOverlaps().size());
		// for(int i = 0; i < rb1.getCollisionShapes().size(); i++)
		// System.out.println(i + "; " +
		// rb1.getCollisionShapes().get(i).getTranslation());

		debugger.update();
		if (inputs.isKeyDown("O"))
			space.update(delta);
		physicsdebug.update();
		cam.update(delta);
	}
}