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
import misc.HalfSphere;
import misc.HalfSphereShape;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.GJK2Util;
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
import shape2d.Circle;
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
	Shader defaultshader, s1, s2, s3, s4, s5, hitmarkershader, planeintersectionshader, supportvectorshader, cshader,
			circleshader, circleshader2, circleshader3;
	RigidBody3 rb1, rb2, rb3, rb4, rb5;
	Debugger debugger;
	PhysicsDebug physicsdebug;
	InputEvent increaseIterations, decreaseIterations;
	Ray3 ray;
	List<Sphere> hitmarkers;

	List<Sphere> planeintersections;
	List<Sphere> supportvectors;
	List<Sphere> firstC;
	List<Circle> projections, projections2, projections3;

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
		planeintersectionshader = new Shader(shaderprogram);
		supportvectorshader = new Shader(shaderprogram);
		cshader = new Shader(shaderprogram);
		circleshader = new Shader(shaderprogram);
		circleshader2 = new Shader(shaderprogram);
		circleshader3 = new Shader(shaderprogram);

		s1.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s2.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s3.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s4.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		s5.addArgument("u_color", new Vector4f(1f, 1f, 1f, 1f));
		hitmarkershader.addArgument("u_color", new Vector4f(0f, 1f, 0f, 1f));
		planeintersectionshader.addArgument("u_color", new Vector4f(0f, 0f, 1f, 1f));
		supportvectorshader.addArgument("u_color", new Vector4f(1f, 1f, 0f, 1f));
		cshader.addArgument("u_color", new Vector4f(1f, 0f, 1f, 1f));
		circleshader.addArgument("u_color", new Vector4f(0f, 1f, 1f, 1f));
		circleshader2.addArgument("u_color", new Vector4f(0.6f, 0.6f, 0.6f, 1f));
		circleshader3.addArgument("u_color", new Vector4f(0, 1, 0, 1f));

		addShader(s1);
		addShader(s2);
		addShader(s3);
		addShader(s4);
		addShader(s5);
		addShader(hitmarkershader);
		addShader(planeintersectionshader);
		addShader(supportvectorshader);
		addShader(cshader);
		addShaderInterface(circleshader);
		addShaderInterface(circleshader2);
		addShaderInterface(circleshader3);

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

		projections = new ArrayList<Circle>();
		for (int i = 0; i < 6 * 3; i++) {
			Circle cSphere = new Circle(-100, -100, 10f, 36);
			circleshader.addObject(cSphere);
			projections.add(cSphere);
		}

		projections2 = new ArrayList<Circle>();
		for (int i = 0; i < 6 * 3; i++) {
			Circle cSphere = new Circle(-100, -100, 10f, 36);
			circleshader2.addObject(cSphere);
			projections2.add(cSphere);
		}

		projections3 = new ArrayList<Circle>();
		for (int i = 0; i < 6; i++) {
			Circle cSphere = new Circle(-100, -100, 10f, 36);
			circleshader3.addObject(cSphere);
			projections3.add(cSphere);
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

	Vector3f base1, base2;
	int maxHitDetectionIterations = 5;

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
		s5.setArgument(0, new Vector4f(1f, 1f, 1f, 1f));

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
		for (Sphere s : firstC)
			s.setRendered(false);
		for (Circle c : projections)
			c.setRendered(false);
		for (Circle c : projections2)
			c.setRendered(false);
		for (Circle c : projections3)
			c.setRendered(false);
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
			if (o.equals(rb5))
				s5.setArgument(0, new Vector4f(1f, 1f, 0f, 1f));

			// /////////// TEST OF METHOD 2 ///////////////////
			final int MAX_ITERATIONS = 10;
			base1 = new Vector3f();
			base2 = new Vector3f();
			SupportMap<Vector3f> Sa = o;

			if (Math.abs(ray.getDirection().x) >= 0.57735f)
				base1.set(ray.getDirection().y, -ray.getDirection().x, 0);
			else
				base1.set(0, ray.getDirection().z, -ray.getDirection().y);

			if (base1.lengthSquared() > 0)
				base1.normalize();
			base2.set(ray.getDirection().getYf() * base1.getZf() - ray.getDirection().getZf() * base1.getYf(),
					ray.getDirection().getZf() * base1.getXf() - ray.getDirection().getXf() * base1.getZf(),
					ray.getDirection().getXf() * base1.getYf() - ray.getDirection().getYf() * base1.getXf());

			// STEP 2: Project support center on plane and adjust base
			// directions/pick start directions
			float t0 = dotRay(Sa.getSupportCenter(), ray.getPosition(), ray.getDirection());
			Vector3f hitOfPlane = VecMath.scale(ray.getDirection(), t0);
			hitOfPlane.translate(ray.getPosition());

			Vector3f centerOnPlane = projectPointOnPlane(hitOfPlane, Sa.getSupportCenter(), ray.getDirection());
			centerOnPlane = VecMath.subtraction(centerOnPlane, Sa.getSupportCenter());

			// STEP 3: Calculate Support(centerOnPlane) and
			// Support(centerOnPlane x normal)
			List<Vector2f> simplex = new ArrayList<Vector2f>();
			List<Vector3f> simplex3 = new ArrayList<Vector3f>();
			Vector2f direction;

			Vector2f centerOnPlane2 = projectPointOn2dPlane(hitOfPlane, Sa.getSupportCenter(), base1, base2);
			centerOnPlane2.negate();
			simplex.clear();
			simplex3.clear();

			Vector3f dir = centerOnPlane;
			System.out.println("-1: " + dir);
			Vector3f point = Sa.supportPoint(dir);
			Vector2f start = projectPointOn2dPlane(point, Sa.getSupportCenter(), base1, base2);
			start.translate(centerOnPlane2);
			simplex.add(start);
			simplex3.add(point);
			dir = VecMath.negate(dir);
			for (int i = 0; i < MAX_ITERATIONS; i++) {
				System.out.println(i + "; " + dir);
				point = Sa.supportPoint(dir);
				Vector2f a = projectPointOn2dPlane(point, Sa.getSupportCenter(), base1, base2);
				a.translate(centerOnPlane2);
				direction = projectPointOn2dPlane(dir, new Vector3f(), base1, base2);
				if (VecMath.dotproduct(a, direction) < 0) {
					System.out.println("Miss");
					break;
				}
				simplex.add(a);
				simplex3.add(point);
				int region = GJK2Util.doSimplexRegion(simplex, direction);
				if (region == 0) {
					System.out.println("Hit");
					break;
				} else {
					switch (region) {
					case 5:
						simplex3.remove(2);
					case 2:
					case 3:
						simplex3.remove(1);
						break;
					case 4:
						simplex3.remove(0);
						break;
					}
					System.out.println(region);
				}
				dir.set(direction.x * base1.x + direction.y * base2.x, direction.x * base1.y + direction.y * base2.y,
						direction.x * base1.z + direction.y * base2.z);
			}

			if (simplex.size() == 3) {
				System.out.println("------------------------------------");
				// final int MAX_ITERATIONS_HIT = 20;
				final float EPSILON = 0.001f;

				Vector3f a = simplex3.get(0);
				Vector3f b = simplex3.get(1);
				Vector3f c = simplex3.get(2);

				Vector2f projA = projectPointOn2dPlane(a, Sa.getSupportCenter(), base1, base2);
				Vector2f projB = projectPointOn2dPlane(b, Sa.getSupportCenter(), base1, base2);
				Vector2f projC = projectPointOn2dPlane(c, Sa.getSupportCenter(), base1, base2);

				System.out.println("ABC2: ");
				System.out.println(simplex3.get(0) + "; " + simplex3.get(1) + "; " + simplex3.get(2));
				System.out.println(a + "; " + b + "; " + c);

				for (int i = 0; i < maxHitDetectionIterations; i++) {
					// Check in which triangle ((a, b, p), (b, c, p), (c, a, p))
					// the hitOnPlane lies
					Vector3f n = VecMath.computeNormal(a, b, c);
					if (VecMath.dotproduct(n, ray.getDirection()) > 0)
						n.negate();
					System.out.println(i + " n: " + n);
					Vector3f p = Sa.supportPoint(n);

					if (VecMath.dotproduct(p, n) - VecMath.dotproduct(n, a) < EPSILON) {
						System.out.println("DONE");
					}

					Vector2f q = projectPointOn2dPlane(p, Sa.getSupportCenter(), base1, base2);
					Vector2f PO = VecMath.subtraction(centerOnPlane2, q);
					Vector2f PA = VecMath.subtraction(projA, q);
					Vector2f PB = VecMath.subtraction(projB, q);
					if (VecMath.dotproduct(PO, PA) > 0 && VecMath.dotproduct(PO, PB) > 0) {
						c = p;
						projC = q;
						System.out.println("region 1");
					} else {
						Vector2f PC = VecMath.subtraction(projC, q);
						if (VecMath.dotproduct(PO, PB) > 0 && VecMath.dotproduct(PO, PC) > 0) {
							a = p;
							projA = q;
							System.out.println("region 2");
						} else {
							if (VecMath.dotproduct(PO, PC) > 0 && VecMath.dotproduct(PO, PA) > 0) {
								b = p;
								projB = q;
								System.out.println("region 3");
							} else {
								System.out.println("ERROR: no region");
							}
						}
					}
				}

				Circle c7 = projections3.get(count);
				c7.translateTo(VecMath.scale(centerOnPlane2, 20));
				c7.translate(400, 300);
				// c7.setRendered(true);

				Circle c1 = projections.get(count * 3);
				c1.translateTo(VecMath.scale(simplex.get(0), 20));
				c1.translate(400, 300);
				// c1.setRendered(true);
				Circle c2 = projections.get(count * 3 + 1);
				c2.translateTo(VecMath.scale(simplex.get(1), 20));
				c2.translate(400, 300);
				// c2.setRendered(true);
				Circle c3 = projections.get(count * 3 + 2);
				c3.translateTo(VecMath.scale(simplex.get(2), 20));
				c3.translate(400, 300);
				// c3.setRendered(true);

				Sphere s1 = planeintersections.get(count);
				Sphere s2 = supportvectors.get(count);
				Sphere s3 = firstC.get(count);

				s1.translateTo(a);
				s2.translateTo(b);
				s3.translateTo(c);

				s1.setRendered(true);
				s2.setRendered(true);
				s3.setRendered(true);

				count++;
			}

			/*
			 * System.out.println(simplex.size());
			 * 
			 * if (simplex.size() == 3) { Sphere pi =
			 * planeintersections.get(count); pi.translateTo(simplex3.get(0));
			 * pi.setRendered(true);
			 * 
			 * Sphere sf = supportvectors.get(count);
			 * sf.translateTo(simplex3.get(1)); sf.setRendered(true);
			 * 
			 * Sphere cs = firstC.get(count); cs.translateTo(simplex3.get(2));
			 * cs.setRendered(true);
			 * 
			 * Circle c1 = projections.get(count * 3);
			 * c1.translateTo(VecMath.scale(simplex.get(0), 20));
			 * c1.translate(400, 300); c1.setRendered(true); Circle c2 =
			 * projections.get(count * 3 + 1);
			 * c2.translateTo(VecMath.scale(simplex.get(1), 20));
			 * c2.translate(400, 300); c2.setRendered(true); Circle c3 =
			 * projections.get(count * 3 + 2);
			 * c3.translateTo(VecMath.scale(simplex.get(2), 20));
			 * c3.translate(400, 300); c3.setRendered(true);
			 * 
			 * Circle c4 = projections2.get(count * 3);
			 * c4.translateTo(VecMath.scale(VecMath.addition(simplex.get(0),
			 * centerOnPlane2), 20)); c4.translate(400, 300);
			 * c4.setRendered(true); Circle c5 = projections2.get(count * 3 +
			 * 1); c5.translateTo(VecMath.scale(VecMath.addition(simplex.get(1),
			 * centerOnPlane2), 20)); c5.translate(400, 300);
			 * c5.setRendered(true); Circle c6 = projections2.get(count * 3 +
			 * 2); c6.translateTo(VecMath.scale(VecMath.addition(simplex.get(2),
			 * centerOnPlane2), 20)); c6.translate(400, 300);
			 * c6.setRendered(true);
			 * 
			 * Circle c7 = projections3.get(count);
			 * c7.translateTo(VecMath.scale(centerOnPlane2, 20));
			 * c7.translate(400, 300); c7.setRendered(true); }
			 * 
			 * count++;
			 */
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
			if (o.equals(rb5))
				s5.setArgument(0, new Vector4f(1f, 0f, 0f, 1f));

			/*
			 * Sphere hitmarker = hitmarkers.get(count);
			 * hitmarker.translateTo(hit.getSecond());
			 * hitmarker.setRendered(true);
			 */

			// count++;
		}

		debugger.update(fps, 0, 0);
		cam.update(delta);
	}

	private void unprojectAndSet(Vector2f point, Vector3f set) {
		set.set(point.x * base1.x + point.y * base2.x, point.x * base1.y + point.y * base2.y,
				point.x * base1.z + point.y * base2.z);
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
}