package gjkDebugger;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import loader.FontLoader;
import math.QuatMath;
import math.VecMath;
import objects.RigidBody3;
import objects.ShapedObject;
import objects.SupportMap;
import physics.PhysicsShapeCreator;
import physicsSupportFunction.SupportDifferenceObject;
import quaternion.Quaternionf;
import shape.Box;
import shape.Sphere;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class GJKDebugger extends StandardGame {
	Simplex GJKsimplex;
	boolean rebuildsimplex = false;
	Debugger debugger;
	RigidBody3 rb1, rb2;
	SupportDifferenceObject support1;
	Line line;
	Sphere newPoint, mostRecentPoint;

	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(),
				new PixelFormat().withSamples(0), new VideoSettings());
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		display.bindMouse();
		cam.setFlyCam(true);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		// Box b1 = new Box(-8.960001f, 8.190001f, 0.0f, 1f, 1f, 1f);
		// b1.setRotation(new Quaternionf(0.87097573f, -0.41262922f,
		// 0.26483604f,
		// 0.03165175f));
		// rb1 = new RigidBody3(PhysicsShapeCreator.create(b1));
		//
		// Sphere s1 = new Sphere(-10, 10, 0, 1, 36, 36);
		// rb2 = new RigidBody3(PhysicsShapeCreator.create(s1));

		Box b1 = new Box(4.1700006f, 2.1599996f, 0.0f, 1f, 1f, 1f);
		b1.setRotation(new Quaternionf(0.25023422f, -0.09507953f, -0.8314483f,
				-0.48689112f));
		rb1 = new RigidBody3(PhysicsShapeCreator.create(b1));

		Box s1 = new Box(4, 0, 0, 1.5f, 1.5f, 1.5f);
		rb2 = new RigidBody3(PhysicsShapeCreator.create(s1));

		newPoint = new Sphere(0, 0, 0, 0.05f, 36, 36);
		newPoint.setColor(Color.CYAN);
		addObject(newPoint);

		mostRecentPoint = new Sphere(0, 0, 0, 0.05f, 36, 36);
		mostRecentPoint.setColor(Color.BLUE);
		addObject(mostRecentPoint);

		// Fix transformation (usually done in PhysicsSpace-class of Engine
		rb1.setRotation(b1.getRotation());
		rb1.setTranslation(b1.getTranslation());

		rb2.setRotation(s1.getRotation());
		rb2.setTranslation(s1.getTranslation());

		// Visualize the support functions
		support1 = new SupportDifferenceObject(b1, rb1, s1, rb2);

		initGJK();
		GJKsimplex = new Simplex(simplex);

		line = new Line();
		line.update(new Vector3f(), direction);

		BRUTEFORCE();

		InputEvent stepGJK = new InputEvent("Step GJK", new Input(
				Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(stepGJK);
	}

	public void BRUTEFORCE() {
		for (int i = 0; i < 1000; i++) {
			Vector3f v1 = randomVec();
			Vector3f v2 = randomVec();
			Vector3f v3 = randomVec();
			addObject(new TriangleShape(support(rb1, rb2, v1), support(rb1,
					rb2, v2), support(rb1, rb2, v3)));
			// addObject(new Line(new Vector3f(), v1));
			// addObject(new Line(new Vector3f(), v2));
			// addObject(new Line(new Vector3f(), v3));
		}
		for (int i = 0; i < 2000; i++) {
			Vector3f v1 = randomVec();
			Vector3f v2 = randomVec();
			addObject(new Point(VecMath.subtraction(rb1.supportPoint(v1), rb2.supportPointNegative(v2))));
		}
	}

	private Vector3f randomVec() {
		Quaternionf quat = new Quaternionf();
		quat.rotate(Math.random() * 360, new Vector3f(0, 0, 1));
		quat.rotate(Math.random() * 360, new Vector3f(0, 1, 0));
		quat.rotate(Math.random() * 360, new Vector3f(1, 0, 0));
		return QuatMath.transform(quat, new Vector3f(0, 1, 0));
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		GJKsimplex.render();
		support1.render();
		line.render();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("Step GJK")) {
			stepGJK();
			GJKsimplex.delete();
			GJKsimplex = new Simplex(simplex);
			line.update(new Vector3f(), direction);
			mostRecentPoint.translateTo(newPoint.getTranslation());
			newPoint.translateTo(support(rb1, rb2, direction));
		}
		debugger.update();

		if (display.isMouseBound())
			cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}

	public void initGJK() {
		// System.out.println("---------- New Loop: ----------");
		simplex = new ArrayList<Vector3f>();
		// S = Support(?)
		direction = support(rb1, rb2, new Vector3f(1, 1, 1));
		// [] = S
		simplex.add(direction);
		// D = -S
		direction = VecMath.negate(direction);
	}

	public void stepGJK() {
		// A = Support(D)
		Vector3f a = support(rb1, rb2, direction);
		// System.out.println("New Point: " + a);
		// if AtD < 0 No Intersection
		System.out.println();
		System.out.println(simplex.size() + " dir: " + direction + "; " + a
				+ "; " + VecMath.dotproduct(a, direction));
		if (VecMath.dotproduct(a, direction) < 0)
			System.out.println("Failure!");
		// [] += A
		simplex.add(a);
		// if DoSimplex([], D) Intersection
		if (doSimplex()) {
			System.out.println("Scuccess!");
		}
	}

	// ------------------- GJK ---------------------
	List<Vector3f> simplex;
	Vector3f direction;

	private boolean doSimplex() {
		int simplexsize = simplex.size();
		// Line
		if (simplexsize == 2) {
			// System.out.print("line ");
			Vector3f A = simplex.get(1);
			Vector3f B = simplex.get(0);
			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AO = VecMath.negate(A);
			if (VecMath.dotproduct(AB, AO) > 0) {
				// Region 1
				direction = edgeDirection(AB, AO);
				// System.out.print(AB.toString() + "; " + AO.toString());
				System.out.print("line region 1");
			} else {
				// Region 2
				simplex.remove(1);
				direction = AO;
				System.out.print("line region 2");
			}
			// System.out.println(" " + A + "; " + B + "; " + direction);
		}
		// Triangle
		if (simplexsize == 3) {
			// System.out.print("triangle ");
			Vector3f A = simplex.get(2);
			Vector3f B = simplex.get(1);
			Vector3f C = simplex.get(0);
			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AC = VecMath.subtraction(C, A);
			Vector3f ABC = VecMath.crossproduct(AB, AC);
			Vector3f AO = VecMath.negate(A);

			if (VecMath.dotproduct(VecMath.crossproduct(ABC, AC), AO) > 0) {
				if (VecMath.dotproduct(AC, AO) > 0) {
					// Region 1
					simplex.remove(1);
					direction = edgeDirection(AC, AO);
					System.out.print("r 1");
				} else {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
						System.out.print("r 4");
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction = AO;
						System.out.print("r 5");
					}
				}
			} else {
				if (VecMath.dotproduct(VecMath.crossproduct(AB, ABC), AO) > 0) {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
						System.out.print("r 4(2)");
					} else {
						// Region 5
						simplex.remove(2);
						simplex.remove(1);
						direction = AO;
						System.out.print("r 5(2)");
					}
				} else {
					if (VecMath.dotproduct(ABC, AO) >= 0) {
						// Region 2
						direction = ABC;
						System.out.print("r 2");
					} else {
						// Region 3
						Vector3f temp = simplex.get(0);
						simplex.set(0, simplex.get(1));
						simplex.set(1, temp);
						direction = VecMath.negate(ABC);
						System.out.print("r 3");
					}
				}
			}
			// System.out.println(" " + A + "; " + B + "; " + C + "; " +
			// direction);
		}
		// Tetrahedron
		if (simplexsize == 4) {
			// //System.out.print("tetrahedron ");
			Vector3f A = simplex.get(3);
			Vector3f B = simplex.get(2);
			Vector3f C = simplex.get(1);
			Vector3f D = simplex.get(0);

			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AC = VecMath.subtraction(C, A);
			Vector3f AD = VecMath.subtraction(D, A);

			Vector3f ABC = VecMath.crossproduct(AB, AC);
			Vector3f ACD = VecMath.crossproduct(AC, AD);
			Vector3f ADB = VecMath.crossproduct(AD, AB);

			Vector3f AO = VecMath.negate(A);

			// TEST
			// Vector3f BA = VecMath.substraction(A, B);
			// Vector3f BC = VecMath.substraction(C, B);
			// Vector3f BD = VecMath.substraction(D, B);
			// Vector3f BDC = VecMath.crossproduct(BD, BC);
			//
			// if ((VecMath.dotproduct(BDC, BA) > 0)
			// || (VecMath.dotproduct(ACD, AB) > 0)
			// || (VecMath.dotproduct(ADB, AC) > 0)
			// || (VecMath.dotproduct(ABC, AD) > 0)) {
			// System.out.print("Correct Orientation: "
			// + !(VecMath.dotproduct(BDC, BA) > 0) + "; "
			// + !(VecMath.dotproduct(ACD, AB) > 0) + "; "
			// + !(VecMath.dotproduct(ADB, AC) > 0) + "; "
			// + !(VecMath.dotproduct(ABC, AD) > 0));
			// System.out.print("Orientation: "
			// + (VecMath.dotproduct(BDC, BA) == 0) + "; "
			// + (VecMath.dotproduct(ACD, AB) == 0) + "; "
			// + (VecMath.dotproduct(ADB, AC) == 0) + "; "
			// + (VecMath.dotproduct(ABC, AD) == 0));
			// System.out.println(A.toString() + "; " + B.toString() + "; "
			// + C.toString() + "; " + D.toString());
			// }
			// TEST END

			// //System.out.print(ABC + "; " + ADB + "; " + ACD + "; ");
			if (VecMath.dotproduct(ABC, AO) > 0) {
				if (VecMath.dotproduct(ADB, AO) > 0) {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Top
						simplex.remove(2);
						simplex.remove(1);
						simplex.remove(0);
						direction = AO;
						System.out.print("top");
					} else {
						// Edge 1
						simplex.remove(1);
						simplex.remove(0);
						direction = edgeDirection(AB, AO);
						System.out.print("edge 1");
					}
				} else {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Edge 2
						simplex.remove(2);
						simplex.remove(0);
						direction = edgeDirection(AC, AO);
						System.out.print("edge 2");
					} else {
						// Face 1
						simplex.remove(0);
						direction = ABC;
						System.out.print("face 1");
					}
				}
			} else {
				if (VecMath.dotproduct(ADB, AO) > 0) {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Edge 3
						simplex.remove(2);
						simplex.remove(1);
						direction = edgeDirection(AD, AO);
						System.out.print("edge 3");
					} else {
						// Face 2
						simplex.remove(1); // CHANGE ORIENTATION?????
						Vector3f temp = simplex.get(0);
						simplex.set(0, simplex.get(1));
						simplex.set(1, temp);
						direction = ADB;
						System.out.print("face 2");
					}
				} else {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Face 3
						simplex.remove(2);
						direction = ACD;
						System.out.print("face 3");
					} else {
						// Center
						System.out.print("center");
						// System.out.println(" " + A + "; " + B + "; " + C +
						// "; " + D);
						return true;
					}
				}
			}
			// System.out.println(" " + A + "; " + B + "; " + C + "; " + D +
			// "; " + direction);
		}
		return false;
	}

	// private Vector3f randomvector() {
	// return new Vector3f((int) (Math.random() * 2) * 2 - 1,
	// (int) (Math.random() * 2) * 2 - 1,
	// (int) (Math.random() * 2) * 2 - 1);
	// }

	private Vector3f edgeDirection(Vector3f edge, Vector3f origin) {
		return VecMath.crossproduct(VecMath.crossproduct(edge, origin), edge);
	}

	private Vector3f support(SupportMap<Vector3f> Sa, SupportMap<Vector3f> Sb,
			Vector3f dir) {
		// System.out.println("sup: " +
		// VecMath.substraction(Sa.supportPoint(dir),
		// Sb.supportPoint(VecMath.negate(dir))) + ": " + Sa.supportPoint(dir) +
		// "; " + Sb.supportPoint(VecMath.negate(dir)) + "; " + dir);
		return VecMath.subtraction(Sa.supportPoint(dir),
				Sb.supportPointNegative(dir));
	}
	
	private class Point extends ShapedObject {
		public Point(Vector3f point) {
			setRenderMode(GLConstants.POINTS);
			addVertex(point);
			addIndex(0);
			prerender();
		}
	}

	private class Line extends ShapedObject {
		Color c;

		public Line() {
			setRenderMode(GLConstants.LINES);
			c = Color.CYAN;
		}

		public Line(Vector3f start, Vector3f end) {
			setRenderMode(GLConstants.LINES);
			c = Color.WHITE;
			update(start, end);
		}

		public void update(Vector3f start, Vector3f end) {
			delete();
			addVertex(start, c);
			addVertex(end, c);
			addIndices(0, 1);
			prerender();
		}
	}

	private class TriangleShape extends ShapedObject {
		public TriangleShape(Vector3f a, Vector3f b, Vector3f c) {
			setRenderMode(GLConstants.TRIANGLES);
			addVertex(a);
			addVertex(b);
			addVertex(c);
			addIndices(0, 1, 2);
			prerender();
		}
	}
}
