package physics2dSupportFunction;

import broadphase.SAP2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.InputLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import narrowphase.EPA2;
import narrowphase.GJK2;
import narrowphase.SupportRaycast2;
import objects.CompoundObject2;
import objects.RigidBody2;
import objects.ShapedObject2;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace2;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shader.Shader;
import shape2d.Circle;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import utils.Debugger;
import vector.Vector2f;

public class SupportFunctionTest extends StandardGame {
	PhysicsSpace2 space;
	Quad q1, q2;
	Circle c1;
	SupportObject2 so1, so11, so2, so3;
	SupportDifferenceObject2 support1, support2, support11, support21;
	DirectionRenderer2 dirrenderer;
	RigidBody2 rb2, rb3;
	Debugger debugger;

	CompoundObject2 rb1;
	Quad q;
	Circle c;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings(),
				new NullSoundEnvironment());
		// display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		inputs = InputLoader.load(inputs, "res/inputs.txt");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		space = new PhysicsSpace2(new EulerIntegration(), new SAP2(), new GJK2(new EPA2()), new SupportRaycast2(),
				new NullResolution(), new NullCorrection(), new SimpleManifoldManager<Vector2f>());

		// q1 = new Quad(400, 200, 25, 25);
		// rb1 = new RigidBody2(PhysicsShapeCreator.create(q1));
		// space.addRigidBody(q1, rb1);
		q = new Quad(400, 80, 20, 20);
		c = new Circle(400, 80, 24, 10);
		rb1 = new CompoundObject2();
		rb1.addCollisionShape(PhysicsShapeCreator.create(q));
		rb1.addCollisionShape(PhysicsShapeCreator.create(c));
		space.addCompoundObject(rb1, new ShapedObject2[] { q, c });
		// addObject(b1);

		q2 = new Quad(400, 200, 25, 25);
		rb2 = new RigidBody2(PhysicsShapeCreator.create(q2));
		space.addRigidBody(q2, rb2);
		// addObject(b2);

		c1 = new Circle(80, 80, 25, 50);
		rb3 = new RigidBody2(PhysicsShapeCreator.create(c1));
		space.addRigidBody(c1, rb3);
		// addObject(s1);

		dirrenderer = new DirectionRenderer2();
		defaultshader.addObject(dirrenderer);

		so1 = new SupportObject2(q, rb1.getCollisionShapes().get(0));
		so11 = new SupportObject2(c, rb1.getCollisionShapes().get(1));
		so2 = new SupportObject2(q2, rb2);
		so3 = new SupportObject2(c1, rb3);

		defaultshader2.addObject(so1);
		defaultshader2.addObject(so11);
		defaultshader2.addObject(so2);
		defaultshader2.addObject(so3);

		support1 = new SupportDifferenceObject2(q, rb1.getCollisionShapes().get(0), q2, rb2);
		support2 = new SupportDifferenceObject2(q, rb1.getCollisionShapes().get(0), c1, rb3);
		support11 = new SupportDifferenceObject2(c, rb1.getCollisionShapes().get(1), q2, rb2);
		support21 = new SupportDifferenceObject2(c, rb1.getCollisionShapes().get(1), c1, rb3);

		defaultshader2.addObject(support1);
		defaultshader2.addObject(support2);
		defaultshader2.addObject(support11);
		defaultshader2.addObject(support21);

		// addObject(new ResultTetrahedron(new ArrayList<Vector3f>() {{add(new
		// Vector3f(2,5,2)); add(new Vector3f(8,5,2)); add(new Vector3f(2,5,8));
		// add(new Vector3f(4,8,4)); }}));
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

		if (moved) {
			so1.updateShape();
			so11.updateShape();
			so2.updateShape();
			so3.updateShape();

			dirrenderer.setDirections(support1.updateShape());
			dirrenderer.setDirections(support2.updateShape());
		}

		System.out.println(rb1.getRotation() + "; " + rb1.getInverseRotation());
		// if(space.testlist.size() > 0) {
		// for(List<Vector2f> v : space.testlist) {
		// addObject(new ResultTetrahedron(v));
		// }
		// }

		debugger.update(fps, 0, 0);
		cam.update(delta);
		space.update(delta);
	}
}
