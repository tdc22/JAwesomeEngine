package physicsSupportFunction;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.InputLoader;
import manifold.SimpleManifoldManager;
import narrowphase.EmptyManifoldGenerator;
import narrowphase.GJK;
import objects.RigidBody3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import vector.Vector3f;
import broadphase.SAP;

public class SupportFunctionTest extends StandardGame {
	PhysicsSpace space;
	Box b1, b2;
	Sphere s1;
	Cylinder c1;
	SupportObject so1, so2, so3, so4;
	SupportDifferenceObject support1, support2, support3;
	DirectionRenderer dirrenderer;
	RigidBody3 rb1, rb2, rb3, rb4;
	Debugger debugmanager;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		inputs = InputLoader.load(inputs, "res/inputs.txt");
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);

		space = new PhysicsSpace(new EulerIntegration(), new SAP(), new GJK(
				new EmptyManifoldGenerator()), new NullResolution(),
				new NullCorrection(), new SimpleManifoldManager<Vector3f>());

		// b1 = new Box(-1, 0, 0, 1, 1, 1);
		b1 = new Box(0, 0, 0, 1, 1, 1); // new Sphere(0, 0, 0, 1, 36, 36);
		rb1 = PhysicsShapeCreator.create(b1);
		space.addRigidBody(b1, rb1);
		// addObject(b1);

		// b2 = new Box(4, 0, 0, 1.5f, 1.5f, 1.5f);
		b2 = new Box(0, 0, 0, 1, 1, 1);
		rb2 = PhysicsShapeCreator.create(b2);
		space.addRigidBody(b2, rb2);
		// addObject(b2);

		s1 = new Sphere(-10, 10, 0, 1, 36, 36);
		rb3 = PhysicsShapeCreator.create(s1);
		space.addRigidBody(s1, rb3);
		// addObject(s1);

		c1 = new Cylinder(-10, -10, 0, 1, 2, 36);
		rb4 = PhysicsShapeCreator.create(c1);
		space.addRigidBody(c1, rb4);
		addObject(c1);

		dirrenderer = new DirectionRenderer();
		addObject(dirrenderer);

		so1 = new SupportObject(b1, rb1);
		so2 = new SupportObject(b2, rb2);
		so3 = new SupportObject(s1, rb3);
		so4 = new SupportObject(c1, rb4);

		addObject(so1);
		addObject(so2);
		addObject(so3);
		addObject(so4);

		support1 = new SupportDifferenceObject(b1, rb1, b2, rb2);
		support2 = new SupportDifferenceObject(b1, rb1, s1, rb3);
		support3 = new SupportDifferenceObject(b1, rb1, c1, rb4);

		addObject(support1);
		addObject(support2);
		addObject(support3);

		// addObject(new ResultTetrahedron(new ArrayList<Vector3f>() {{add(new
		// Vector3f(2,5,2)); add(new Vector3f(8,5,2)); add(new Vector3f(2,5,8));
		// add(new Vector3f(4,8,4)); }}));

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		boolean moved = false;
		if (inputs.isEventActive("Translate1")) {
			b1.translate(0, delta / 100f, 0);
			moved = true;
		}
		if (inputs.isEventActive("Translate2")) {
			b1.translate(0, -delta / 100f, 0);
			moved = true;
		}
		if (inputs.isEventActive("Translate3")) {
			b1.translate(-delta / 100f, 0, 0);
			moved = true;
		}
		if (inputs.isEventActive("Translate4")) {
			b1.translate(delta / 100f, 0, 0);
			moved = true;
		}

		if (inputs.isEventActive("Rotate1")) {
			b1.rotate(delta / 10f, 0, 0);
			moved = true;
		}
		if (inputs.isEventActive("Rotate2")) {
			b1.rotate(-delta / 10f, 0, 0);
			moved = true;
		}
		if (inputs.isEventActive("Rotate3")) {
			b1.rotate(0, -delta / 10f, 0);
			moved = true;
		}
		if (inputs.isEventActive("Rotate4")) {
			b1.rotate(0, delta / 10f, 0);
			moved = true;
		}

		// if (toggleMouseBind.isActive()) {
		// if (!display.isMouseBound())
		// display.bindMouse();
		// else
		// display.unbindMouse();
		// }

		if (moved) {
			so1.updateShape();
			so2.updateShape();
			so3.updateShape();
			so4.updateShape();

			dirrenderer.setDirections(support1.updateShape());
			support2.updateShape();
			support3.updateShape();
			// support2.updateShape();
		}

		// System.out.println("------------------------");
		// System.out.println(b1.getMatrix().toString());
		// System.out.println();
		// System.out.println(rb1.getInverseRotation().toString());

		// if(space.testlist.size() > 0) {
		// for(List<Vector3f> v : space.testlist) {
		// addObject(new ResultTetrahedron(v));
		// }
		// }

		debugmanager.update();
		cam.update(delta);
		space.update(delta);
	}
}
