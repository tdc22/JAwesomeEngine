package physicsRaycast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import broadphase.DynamicAABBTree3;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import misc.HalfSphere;
import misc.HalfSphereShape;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.Ray3;
import objects.RigidBody;
import objects.RigidBody3;
import objects.ShapedObject2;
import physics.PhysicsDebug;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.NullCorrection;
import resolution.NullResolution;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import sound.NullSoundEnvironment;
import utils.Debugger;
import utils.GLConstants;
import utils.Pair;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class RaycastTest extends StandardGame {
	PhysicsSpace space;
	Box b1, b2;
	Sphere sp1;
	Cylinder c1;
	HalfSphere hs;
	Shader defaultshader, s1, s2, s3, s4, s5, hitmarkershader;
	RigidBody3 rb1, rb2, rb3, rb4, rb5;
	Debugger debugger;
	PhysicsDebug physicsdebug;
	InputEvent increaseIterations, decreaseIterations;
	Ray3 ray;
	List<Sphere> hitmarkers;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		int shaderprogram = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert",
				"res/shaders/colorshader.frag");
		s1 = new Shader(shaderprogram);
		s2 = new Shader(shaderprogram);
		s3 = new Shader(shaderprogram);
		s4 = new Shader(shaderprogram);
		s5 = new Shader(shaderprogram);
		hitmarkershader = new Shader(shaderprogram);

		s1.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s2.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s3.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s4.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s5.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		hitmarkershader.addArgument("u_color", new Vector4f(0f, 1f, 0f, 1f));

		addShader(s1);
		addShader(s2);
		addShader(s3);
		addShader(s4);
		addShader(s5);
		addShader(hitmarkershader);

		space = new PhysicsSpace(new EulerIntegration(), new DynamicAABBTree3(), new GJK(new EPA()),
				new SupportRaycast(), new NullResolution(), new NullCorrection(),
				new SimpleManifoldManager<Vector3f>());
		space.setCullStaticOverlaps(false);

		b1 = new Box(4, 0, 0, 1.5f, 1.5f, 1.5f);
		rb1 = new RigidBody3(PhysicsShapeCreator.create(b1));
		space.addRigidBody(b1, rb1);
		s1.addObject(b1);

		b2 = new Box(-10, -10, 0, 1.5f, 1.5f, 1.5f);
		rb2 = new RigidBody3(PhysicsShapeCreator.create(b2));
		space.addRigidBody(b2, rb2);
		s2.addObject(b2);

		sp1 = new Sphere(-10, 10, 0, 1, 36, 36);
		rb3 = new RigidBody3(PhysicsShapeCreator.create(sp1));
		space.addRigidBody(sp1, rb3);
		s3.addObject(sp1);

		c1 = new Cylinder(10, 10, 0, 1, 1, 36);
		rb4 = new RigidBody3(PhysicsShapeCreator.create(c1));
		space.addRigidBody(c1, rb4);
		s4.addObject(c1);

		hs = new HalfSphere(10, -10, 0, 1, 36, 36);
		rb5 = new RigidBody3(new HalfSphereShape(10, -10, 0, 1));
		space.addRigidBody(hs, rb5);
		s5.addObject(hs);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space, defaultshader);

		increaseIterations = new InputEvent("IncreaseIterations",
				new Input(Input.KEYBOARD_EVENT, "2", KeyInput.KEY_PRESSED));
		decreaseIterations = new InputEvent("DecreaseIterations",
				new Input(Input.KEYBOARD_EVENT, "1", KeyInput.KEY_PRESSED));
		inputs.addEvent(increaseIterations);
		inputs.addEvent(decreaseIterations);

		ray = new Ray3(cam.getTranslation(), cam.getDirection());

		hitmarkers = new ArrayList<Sphere>();
		for (int i = 0; i < 6; i++) {
			Sphere hitmarker = new Sphere(-100, -100, -100, 0.1f, 36, 36);
			hitmarkershader.addObject(hitmarker);
			hitmarkers.add(hitmarker);
		}

		ShapedObject2 crosshair = new ShapedObject2();
		crosshair.setRenderMode(GLConstants.LINES);
		float halfwidth = settings.getResolutionX() / 2f;
		float halfheight = settings.getResolutionY() / 2f;
		int size = 10;
		crosshair.addVertex(new Vector2f(halfwidth - size, halfheight));
		crosshair.addVertex(new Vector2f(halfwidth + size, halfheight));
		crosshair.addVertex(new Vector2f(halfwidth, halfheight - size));
		crosshair.addVertex(new Vector2f(halfwidth, halfheight + size));
		crosshair.addIndices(0, 1, 2, 3);
		crosshair.prerender();
		defaultshaderInterface.addObject(crosshair);
	}

	@Override
	public void render() {
		debugger.begin();
		physicsdebug.render3d();
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

	Vector3f base1, base2;
	int maxHitDetectionIterations = 0;

	@Override
	public void update(int delta) {
		space.update(delta);
		physicsdebug.update();

		s1.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s2.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s3.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s4.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s5.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));

		if (inputs.isKeyDown("Q")) {
			cam.translate(0, 0.002f * delta, 0);
		}
		if (inputs.isKeyDown("E")) {
			cam.translate(0, -0.002f * delta, 0);
		}
		cam.update(delta);

		ray.setPosition(cam.getTranslation());
		ray.setDirection(cam.getDirection());

		if (increaseIterations.isActive()) {
			maxHitDetectionIterations++;
			System.out.println("Increased Iterations to: " + maxHitDetectionIterations);
		}
		if (decreaseIterations.isActive() && maxHitDetectionIterations > 0) {
			maxHitDetectionIterations--;
			System.out.println("Decreased Iterations to: " + maxHitDetectionIterations);
		}

		for (Sphere s : hitmarkers)
			s.setRendered(false);
		int count = 0;

		// Just raycast in broadphase
		Set<RigidBody<Vector3f, ?, ?, ?>> broadphaseHits = space.raycastAllBroadphase(ray);
		for (RigidBody<Vector3f, ?, ?, ?> o : broadphaseHits) {
			if (o.equals(rb1))
				s1.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb2))
				s2.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb3))
				s3.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb4))
				s4.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
			if (o.equals(rb5))
				s5.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));
		}

		// Raycast broad- and narrowphase
		Set<Pair<RigidBody<Vector3f, ?, ?, ?>, Vector3f>> hits = space.raycastAll(ray);
		for (Pair<RigidBody<Vector3f, ?, ?, ?>, Vector3f> hit : hits) {
			RigidBody<Vector3f, ?, ?, ?> o = hit.getFirst();
			if (o.equals(rb1))
				s1.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb2))
				s2.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb3))
				s3.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb4))
				s4.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));
			if (o.equals(rb5))
				s5.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));

			Sphere hitsphere = hitmarkers.get(count);
			hitsphere.translateTo(hit.getSecond());
			hitsphere.setRendered(true);

			count++;
		}

		debugger.update(fps, 0, 0);
	}
}