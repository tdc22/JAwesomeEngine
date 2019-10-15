package physicsSupportFunction;

import broadphase.SAP;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.InputLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager3;
import misc.HalfSphere;
import misc.HalfSphereShape;
import narrowphase.EmptyManifoldGenerator;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.RigidBody3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shader.Shader;
import shape.Box;
import shape.Capsule;
import shape.Cylinder;
import shape.Sphere;
import sound.NullSoundEnvironment;
import utils.Debugger;

public class SupportFunctionTest extends StandardGame {
	PhysicsSpace space;
	Box b1, b2;
	Sphere s1;
	Cylinder c1;
	Capsule ca1;
	HalfSphere hs;
	SupportObject so1, so2, so3, so4, so5, so6;
	SupportDifferenceObject support1, support2, support3, support4, support5;
	// DirectionRenderer dirrenderer;
	RigidBody3 rb1, rb2, rb3, rb4, rb5, rb6;
	Debugger debugger;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		inputs = InputLoader.load(inputs, "res/inputs.txt");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		space = new PhysicsSpace(new EulerIntegration(), new SAP(), new GJK(new EmptyManifoldGenerator()),
				new SupportRaycast(), new NullResolution(), new NullCorrection(), new SimpleManifoldManager3());

		// b1 = new Box(-1, 0, 0, 1, 1, 1);
		b1 = new Box(0, 0, 0, 1, 1, 1); // new Sphere(0, 0, 0, 1, 36, 36);
		rb1 = new RigidBody3(PhysicsShapeCreator.create(b1));
		space.addRigidBody(b1, rb1);
		// addObject(b1);

		// b2 = new Box(4, 0, 0, 1.5f, 1.5f, 1.5f);
		b2 = new Box(0, 0, 0, 1, 1, 1);
		rb2 = new RigidBody3(PhysicsShapeCreator.create(b2));
		space.addRigidBody(b2, rb2);
		// addObject(b2);

		s1 = new Sphere(-10, 10, 0, 1, 36, 36);
		rb3 = new RigidBody3(PhysicsShapeCreator.create(s1));
		space.addRigidBody(s1, rb3);
		// addObject(s1);

		c1 = new Cylinder(-10, -10, 0, 1, 2, 36);
		rb4 = new RigidBody3(PhysicsShapeCreator.create(c1));
		space.addRigidBody(c1, rb4);
		defaultshader.addObject(c1);

		ca1 = new Capsule(-10, 0, 0, 2, 1f, 36, 36);
		rb5 = new RigidBody3(PhysicsShapeCreator.create(ca1));
		space.addRigidBody(ca1, rb5);
		defaultshader.addObject(ca1);

		hs = new HalfSphere(0, -10, 0, 1, 36, 36);
		rb6 = new RigidBody3(new HalfSphereShape(0, -10, 0, 1));
		space.addRigidBody(hs, rb6);
		defaultshader.addObject(hs);

		// dirrenderer = new DirectionRenderer();
		// addObject(dirrenderer);

		so1 = new SupportObject(b1, rb1);
		so2 = new SupportObject(b2, rb2);
		so3 = new SupportObject(s1, rb3);
		so4 = new SupportObject(c1, rb4);
		so5 = new SupportObject(ca1, rb5);
		so6 = new SupportObject(hs, rb6);

		defaultshader.addObject(so1);
		defaultshader.addObject(so2);
		defaultshader.addObject(so3);
		defaultshader.addObject(so4);
		defaultshader.addObject(so5);
		defaultshader.addObject(so6);

		support1 = new SupportDifferenceObject(b1, rb1, b2, rb2);
		support2 = new SupportDifferenceObject(b1, rb1, s1, rb3);
		support3 = new SupportDifferenceObject(b1, rb1, c1, rb4);
		support4 = new SupportDifferenceObject(b1, rb1, ca1, rb5);
		support5 = new SupportDifferenceObject(b1, rb1, hs, rb6);

		defaultshader.addObject(support1);
		defaultshader.addObject(support2);
		defaultshader.addObject(support3);
		defaultshader.addObject(support4);
		defaultshader.addObject(support5);

		// addObject(new ResultTetrahedron(new ArrayList<Vector3f>() {{add(new
		// Vector3f(2,5,2)); add(new Vector3f(8,5,2)); add(new Vector3f(2,5,8));
		// add(new Vector3f(4,8,4)); }}));

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
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
			so5.updateShape();
			so6.updateShape();

			// dirrenderer.setDirections(support1.updateShape());
			support1.updateShape();
			support2.updateShape();
			support3.updateShape();
			support4.updateShape();
			support5.updateShape();
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

		debugger.update(fps, 0, 0);
		cam.update(delta);
		space.update(delta);
	}
}
