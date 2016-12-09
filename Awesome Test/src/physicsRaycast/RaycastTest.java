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
import input.InputEvent;
import integration.EulerIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import manifold.SimpleManifoldManager;
import math.VecMath;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.Ray3;
import objects.RigidBody;
import objects.RigidBody3;
import objects.ShapedObject2;
import objects.SupportMap;
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
	Shader defaultshader, s1, s2, s3, s4, hitmarkershader, planeintersectionshader, supportvectorshader, cshader;
	RigidBody3 rb1, rb2, rb3, rb4;
	Debugger debugger;
	PhysicsDebug physicsdebug;
	InputEvent toggleMouseBind, giveMeData;
	Ray3 ray;
	List<Sphere> hitmarkers;

	List<Sphere> planeintersections;
	List<Sphere> supportvectors;
	List<Sphere> firstC;

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
		hitmarkershader = new Shader(shaderprogram);
		planeintersectionshader = new Shader(shaderprogram);
		supportvectorshader = new Shader(shaderprogram);
		cshader = new Shader(shaderprogram);

		s1.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s2.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s3.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s4.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		hitmarkershader.addArgument("u_color", new Vector4f(0f, 1f, 0f, 1f));
		planeintersectionshader.addArgument("u_color", new Vector4f(0f, 0f, 1f, 1f));
		supportvectorshader.addArgument("u_color", new Vector4f(1f, 1f, 0f, 1f));
		cshader.addArgument("u_color", new Vector4f(1f, 0f, 1f, 1f));

		addShader(s1);
		addShader(s2);
		addShader(s3);
		addShader(s4);
		addShader(hitmarkershader);
		addShader(planeintersectionshader);
		addShader(supportvectorshader);
		addShader(cshader);

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

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space, defaultshader);

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

		planeintersections = new ArrayList<Sphere>();
		for (int i = 0; i < 6; i++) {
			Sphere planeintersection = new Sphere(-100, -100, -100, 0.1f, 36, 36);
			planeintersectionshader.addObject(planeintersection);
			planeintersections.add(planeintersection);
		}

		supportvectors = new ArrayList<Sphere>();
		for (int i = 0; i < 6; i++) {
			Sphere supportvector = new Sphere(-100, -100, -100, 0.1f, 36, 36);
			supportvectorshader.addObject(supportvector);
			supportvectors.add(supportvector);
		}

		firstC = new ArrayList<Sphere>();
		for (int i = 0; i < 6; i++) {
			Sphere cSphere = new Sphere(-100, -100, -100, 0.1f, 36, 36);
			cshader.addObject(cSphere);
			firstC.add(cSphere);
		}
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

	@Override
	public void update(int delta) {
		space.update(delta);
		physicsdebug.update();

		ray.setPosition(cam.getTranslation());
		ray.setDirection(cam.getDirection());

		s1.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s2.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s3.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));
		s4.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));

		if (inputs.isKeyDown("Q")) {
			cam.translate(0, 0.002f * delta, 0);
		}
		if (inputs.isKeyDown("E")) {
			cam.translate(0, -0.002f * delta, 0);
		}

		for (Sphere s : planeintersections)
			s.setRendered(false);
		for (Sphere s : supportvectors)
			s.setRendered(false);
		for (Sphere s : supportvectors)
			s.setRendered(false);
		int count = 0;

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

			///////////// TEST OF METHOD 2 ///////////////////
			final int MAX_ITERATIONS = 10;
			Vector3f b1 = new Vector3f(), b2 = new Vector3f();
			SupportMap<Vector3f> Sa = o;

			if (Math.abs(ray.getDirection().x) >= 0.57735f)
				b1.set(ray.getDirection().y, -ray.getDirection().x, 0);
			else
				b1.set(0, ray.getDirection().z, -ray.getDirection().y);

			if (b1.lengthSquared() > 0)
				b1.normalize();
			b2.set(ray.getDirection().getYf() * b1.getZf() - ray.getDirection().getZf() * b1.getYf(),
					ray.getDirection().getZf() * b1.getXf() - ray.getDirection().getXf() * b1.getZf(),
					ray.getDirection().getXf() * b1.getYf() - ray.getDirection().getYf() * b1.getXf());

			// STEP 2: Project support center on plane and adjust base
			// directions/pick start directions
			float t0 = dotRay(Sa.getSupportCenter(), ray.getPosition(), ray.getDirection());
			Vector3f hitOfPlane = VecMath.scale(ray.getDirection(), t0);
			hitOfPlane.translate(ray.getPosition());
			// Vector2f centerOnPlane = projectPointOn2dPlane(hitOfPlane,
			// Sa.getSupportCenter(), b1, b2); // negated!
			Vector3f centerOnPlane = projectPointOnPlane(hitOfPlane, Sa.getSupportCenter(), ray.getDirection());
			centerOnPlane = VecMath.subtraction(centerOnPlane, Sa.getSupportCenter());

			// STEP 3: Calculate Support(centerOnPlane) and
			// Support(centerOnPlane x normal)
			Vector3f a = Sa.supportPoint(centerOnPlane);
			// TODO: cancel if closer than epsilon
			Vector3f dir2 = VecMath.negate(centerOnPlane);
			Vector3f b = Sa.supportPoint(dir2);
			// TODO: can we choose dir2 a little bit smarter? (See: Cylinders)
			Vector3f c = null;
			Vector2f centerOnPlane2 = projectPointOn2dPlane(hitOfPlane, Sa.getSupportCenter(), b1, b2);

			// STEP 4: Check if triangle contains hitOfPlane, otherwise refine
			// triangle
			for (int i = 0; i < MAX_ITERATIONS; i++) {
				Vector3f AB = VecMath.subtraction(b, a);
				// Vector3f ABn = VecMath.crossproduct(AB, ray.getDirection());
				// Project AB on plane and check on which side the centerOnPlane
				// lies
				Vector2f ABonPlane2 = projectPointOn2dPlane(AB, new Vector3f(), b1, b2);
				Vector2f ABonPlane2N = new Vector2f(-ABonPlane2.y, ABonPlane2.x);
				Vector3f dir3;
				if (VecMath.dotproduct(ABonPlane2N, centerOnPlane2) > 0) {
					dir3 = VecMath.crossproduct(ray.getDirection(), AB);
				} else {
					dir3 = VecMath.crossproduct(AB, ray.getDirection());
				}
				c = Sa.supportPoint(dir3);

				Vector2f BConPlane2 = projectPointOn2dPlane(VecMath.subtraction(c, b), Sa.getSupportCenter(), b1, b2);
				Vector2f BConPlane2N = new Vector2f(-BConPlane2.y, BConPlane2.x);
				Vector2f CAonPlane2 = VecMath.addition(ABonPlane2, BConPlane2);
				Vector2f CAonPlane2N = new Vector2f(-CAonPlane2.y, CAonPlane2.x);
				// System.out.println(VecMath.dotproduct(ABonPlane2N,
				// centerOnPlane2) + "; " + VecMath.dotproduct(BConPlane2N,
				// centerOnPlane2)
				// + "; " + VecMath.dotproduct(CAonPlane2N, centerOnPlane2));

				// STEP 4.1: Get new third point

				// middle vector between dir1 and dir2

				// Vector3f dir3 = getMiddleVector(dir1, dir2, );
				// c = support function of middle vector

				// STEP 4.2: Check if triangle contains hitOfPlane
				// STEP 4.3: if not contained: discard old point
			}

			///////////////// END TEST, START VISUALIZATION
			///////////////// ////////////////////////////////
			Sphere pi = planeintersections.get(count);
			pi.translateTo(a);
			pi.setRendered(true);

			Sphere sf = supportvectors.get(count);
			sf.translateTo(b);
			sf.setRendered(true);

			Sphere cs = firstC.get(count);
			cs.translateTo(c);
			cs.setRendered(true);

			count++;
		}

		Set<Pair<RigidBody<Vector3f, ?, ?, ?>, Vector3f>> hits = space.raycastAll(ray);
		for (Sphere s : hitmarkers)
			s.setRendered(false);
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

			Sphere hitmarker = hitmarkers.get(count);
			hitmarker.translateTo(hit.getSecond());
			hitmarker.setRendered(true);

			count++;
		}

		debugger.update(fps, 0, 0);
		cam.update(delta);
	}

	private Vector3f projectPointOnPlane(Vector3f point, Vector3f origin, Vector3f normal) {
		float dist = dotRay(point, origin, normal);
		return new Vector3f(point.x - dist * normal.x, point.y - dist * normal.y, point.z - dist * normal.z);
	}

	private Vector2f projectPointOn2dPlane(Vector3f point, Vector3f origin, Vector3f base1, Vector3f base2) {
		return new Vector2f(dotRay(point, origin, base1), dotRay(point, origin, base2));
	}

	private float dotRay(Vector3f vecA, Vector3f vecB, Vector3f vecCheck) {
		return vecCheck.x * (vecA.x - vecB.x) + vecCheck.y * (vecA.y - vecB.y) + vecCheck.z * (vecA.z - vecB.z);
	}

	private Vector3f getMiddleVector(Vector3f a, Vector3f b, Vector3f dir) {
		Vector3f c = new Vector3f(a);
		c.translate(b);

		if (c.lengthSquared() > 0) {
			c.normalize();
			if (VecMath.dotproduct(c, dir) < 0) {
				c.negate();
			}
		} else
			c = dir;

		return c;
	}
}
