package physics2dSupportFunction;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.InputLoader;
import manifold.SimpleManifoldManager;
import narrowphase.EPA2;
import narrowphase.GJK2;
import objects.CompoundObject2;
import objects.RigidBody2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shape2d.Circle;
import shape2d.Quad;
import utils.Debugger;
import vector.Vector2f;
import broadphase.SAP2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class SupportFunctionTest extends StandardGame {
	PhysicsSpace2 space;
	Quad q1, q2;
	Circle c1;
	SupportObject so1, so11, so2, so3;
	SupportDifferenceObject support1, support2, support11, support21;
	DirectionRenderer dirrenderer;
	RigidBody2 rb2, rb3;
	Debugger debugger;
	InputEvent toggleMouseBind;

	CompoundObject2 rb1;
	Quad q;
	Circle c;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(),
				new PixelFormat().withSamples(0), new VideoSettings());
		// display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		inputs = InputLoader.load(inputs, "res/inputs.txt");
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(
				new EPA2()), new NullResolution(), new NullCorrection(),
				new SimpleManifoldManager<Vector2f>());

		// q1 = new Quad(400, 200, 25, 25);
		// rb1 = new RigidBody2(PhysicsShapeCreator.create(q1));
		// space.addRigidBody(q1, rb1);
		q = new Quad(400, 80, 20, 20);
		c = new Circle(400, 80, 24, 10);
		rb1 = new CompoundObject2();
		rb1.addCollisionShape(PhysicsShapeCreator.create(q));
		rb1.addCollisionShape(PhysicsShapeCreator.create(c));
		space.addCompoundObject(rb1, q, c);
		// addObject(b1);

		q2 = new Quad(400, 200, 25, 25);
		rb2 = new RigidBody2(PhysicsShapeCreator.create(q2));
		space.addRigidBody(q2, rb2);
		// addObject(b2);

		c1 = new Circle(80, 80, 25, 50);
		rb3 = new RigidBody2(PhysicsShapeCreator.create(c1));
		space.addRigidBody(c1, rb3);
		// addObject(s1);

		dirrenderer = new DirectionRenderer();
		addObject(dirrenderer);

		so1 = new SupportObject(q, rb1.getCollisionShapes().get(0));
		so11 = new SupportObject(c, rb1.getCollisionShapes().get(1));
		so2 = new SupportObject(q2, rb2);
		so3 = new SupportObject(c1, rb3);

		add2dObject(so1);
		add2dObject(so11);
		add2dObject(so2);
		add2dObject(so3);

		support1 = new SupportDifferenceObject(q, rb1.getCollisionShapes().get(
				0), q2, rb2);
		support2 = new SupportDifferenceObject(q, rb1.getCollisionShapes().get(
				0), c1, rb3);
		support11 = new SupportDifferenceObject(c, rb1.getCollisionShapes()
				.get(1), q2, rb2);
		support21 = new SupportDifferenceObject(c, rb1.getCollisionShapes()
				.get(1), c1, rb3);

		add2dObject(support1);
		add2dObject(support2);
		add2dObject(support11);
		add2dObject(support21);

		// addObject(new ResultTetrahedron(new ArrayList<Vector3f>() {{add(new
		// Vector3f(2,5,2)); add(new Vector3f(8,5,2)); add(new Vector3f(2,5,8));
		// add(new Vector3f(4,8,4)); }}));

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		boolean moved = false;
		if (inputs.isEventActive("Translate1")) {
			rb1.translate(0, -delta / 4f);
			moved = true;
		}
		if (inputs.isEventActive("Translate2")) {
			rb1.translate(0, delta / 4f);
			moved = true;
		}
		if (inputs.isEventActive("Translate3")) {
			rb1.translate(-delta / 4f, 0);
			moved = true;
		}
		if (inputs.isEventActive("Translate4")) {
			rb1.translate(delta / 4f, 0);
			moved = true;
		}

		if (inputs.isEventActive("Rotate2")) {
			rb1.rotate(delta / 10f);
			moved = true;
		}
		if (inputs.isEventActive("Rotate3")) {
			rb1.rotate(-delta / 10f);
			moved = true;
		}

		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}

		if (moved) {
			so1.updateShape();
			so11.updateShape();
			so2.updateShape();
			so3.updateShape();

			dirrenderer.setDirections(support1.updateShape());
			dirrenderer.setDirections(support2.updateShape());
		}

		System.out.println(rb1.getInverseRotation() + "; "
				+ rb1.getCollisionShapes().get(0).getInverseRotation());
		// if(space.testlist.size() > 0) {
		// for(List<Vector2f> v : space.testlist) {
		// addObject(new ResultTetrahedron(v));
		// }
		// }

		debugger.update();
		cam.update(delta);
		space.update(delta);
	}
}
