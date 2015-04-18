package gjkRegionTest;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import loader.FontLoader;
import loader.ShaderLoader;
import math.VecMath;
import utils.Debugger;
import vector.Vector2f;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class RegionTest extends StandardGame {
	List<Vector3f> simplices;
	List<Points> pointbatches;
	int pointshader;
	Simplex simplex;
	boolean rebuildsimplex = false;
	Vector3f minbounds = new Vector3f(0, 0, 0), maxbounds = new Vector3f(10,
			10, 10);
	HashMap<Vector3f, Integer> generated;
	Debugger debugger;
	boolean runthreads = true;
	boolean adding = false;

	final Vector2f zero = new Vector2f();
	final Vector3f up = new Vector3f();

	InputEvent toggleMouseBind;

	// TODO: Tetrahedron face orientation
	// TODO: Tetrahedron regions

	@Override
	protected void destroy() {
		runthreads = false;
	}

	// From GJK
	private int doSimplex(Vector3f point) {
		int simplexsize = simplices.size();
		// Line
		if (simplexsize == 2) {
			Vector3f A = simplices.get(1);
			Vector3f B = simplices.get(0);
			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AO = VecMath.subtraction(point, A);
			if (VecMath.dotproduct(AB, AO) > 0) {
				// Region 1
				return 1;
			} else {
				// Region 2
				return 2;
			}
		}
		// Triangle
		if (simplexsize == 3) {
			Vector3f A = simplices.get(2);
			Vector3f B = simplices.get(1);
			Vector3f C = simplices.get(0);
			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AC = VecMath.subtraction(C, A);
			Vector3f ABC = VecMath.crossproduct(AB, AC);
			Vector3f AO = VecMath.subtraction(point, A);

			if (VecMath.dotproduct(VecMath.crossproduct(ABC, AC), AO) > 0) {
				if (VecMath.dotproduct(AC, AO) > 0) {
					// Region 1
					return 1;
				} else {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						return 4;
					} else {
						// Region 5
						return 5;
					}
				}
			} else {
				if (VecMath.dotproduct(VecMath.crossproduct(AB, ABC), AO) > 0) {
					// *
					if (VecMath.dotproduct(AB, AO) > 0) {
						// Region 4
						return 4;
					} else {
						// Region 5
						return 5;
					}
				} else {
					if (VecMath.dotproduct(ABC, AO) > 0) {
						// Region 2
						return 2;
					} else {
						// Region 3
						return 3;
					}
				}
			}
		}
		// Tetrahedron
		if (simplexsize == 4) {
			Vector3f A = simplices.get(3);
			Vector3f B = simplices.get(2);
			Vector3f C = simplices.get(1);
			Vector3f D = simplices.get(0);

			Vector3f AO = VecMath.subtraction(point, A);

			Vector3f AB = VecMath.subtraction(B, A);
			Vector3f AC = VecMath.subtraction(C, A);
			Vector3f AD = VecMath.subtraction(D, A);

			Vector3f ABC = VecMath.crossproduct(AB, AC);
			Vector3f ABD = VecMath.crossproduct(AD, AB);
			Vector3f ACD = VecMath.crossproduct(AC, AD);

			if (VecMath.dotproduct(ABC, AO) > 0) {
				if (VecMath.dotproduct(ABD, AO) > 0) {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Top
						return 1;
					} else {
						// Edge 1
						return 2;
					}
				} else {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Edge 2
						return 3;
					} else {
						// Face 1
						return 4;
					}
				}
			} else {
				if (VecMath.dotproduct(ABD, AO) > 0) {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Edge 3
						return 5;
					} else {
						// Face 2
						return 6;
					}
				} else {
					if (VecMath.dotproduct(ACD, AO) > 0) {
						// Face 3
						return 7;
					} else {
						// Center
						return 8;
					}
				}
			}
		}
		return 0;
	}

	private void fixOrientation() {
		Vector3f A = simplices.get(3);
		Vector3f B = simplices.get(2);
		Vector3f C = simplices.get(1);
		Vector3f D = simplices.get(0);

		Vector3f AB = VecMath.subtraction(B, A);
		Vector3f AC = VecMath.subtraction(C, A);
		Vector3f AD = VecMath.subtraction(D, A);
		Vector3f BA = VecMath.subtraction(A, B);
		Vector3f BC = VecMath.subtraction(C, B);
		Vector3f BD = VecMath.subtraction(D, B);

		Vector3f ABC = VecMath.crossproduct(AB, AC);
		Vector3f ABD = VecMath.crossproduct(AD, AB);
		Vector3f ACD = VecMath.crossproduct(AC, AD);
		Vector3f BCD = VecMath.crossproduct(BD, BC);

		System.out.println("fix");
		System.out.println("A: " + A.toString() + "; B: " + B.toString()
				+ "; C: " + C.toString() + "; D: " + D.toString());
		System.out.println("BC: " + BC.toString() + "; BD: " + BD.toString());
		System.out.println("BCD: " + BCD.toString() + "; BA: " + BA.toString());
		System.out.println("Correct Orientation: "
				+ !(VecMath.dotproduct(BCD, BA) > 0) + "; "
				+ !(VecMath.dotproduct(ACD, AB) > 0) + "; "
				+ !(VecMath.dotproduct(ABD, AC) > 0) + "; "
				+ !(VecMath.dotproduct(ABC, AD) > 0));
		if (VecMath.dotproduct(BCD, BA) > 0) {
			System.out.println("fixed");
			Vector3f temp = new Vector3f(D);
			simplices.set(0, C);
			simplices.set(1, temp);
		}
	}

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		display.bindMouse();
		cam.setFlyCam(true);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		simplices = new ArrayList<Vector3f>();
		pointbatches = new ArrayList<Points>();
		simplex = new Simplex(simplices);
		generated = new HashMap<Vector3f, Integer>();

		pointshader = ShaderLoader.loadShaderFromFile(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag");

		Thread input = new Thread() {
			/**
			 * The thread "input" handles inputs such as: X, X, X: 3-tuple of
			 * float numbers r*: removes the last simplex element start: starts
			 * random point generation stop: stops random point generation
			 */
			@Override
			public void run() {
				Scanner in = new Scanner(System.in);
				System.out.println("start");
				String line = "";
				try {
					while (runthreads) {
						line = in.nextLine();
						System.out.println(line);
						line = line.replace(" ", "");
						String[] points = line.split(",");
						if (points.length >= 3) {
							Vector3f vec = new Vector3f(
									Float.parseFloat(points[0]),
									Float.parseFloat(points[1]),
									Float.parseFloat(points[2]));
							if (simplices.size() < 4) {
								simplices.add(vec);
							} else {
								simplices.remove(3);
								simplices.add(vec);
							}
							if (simplices.size() == 4)
								fixOrientation();

							rebuildsimplex = true;
						} else {
							line = line.toLowerCase();
							if (line.startsWith("r")) {
								simplices.remove(simplices.size() - 1);
								rebuildsimplex = true;
							}
							if (line.startsWith("add")
									|| line.startsWith("generate")) {
								adding = true;
								float num = Float.parseFloat(line.replace(
										"add", "").replace("generate", ""));
								for (int i = 0; i < num; i++) {
									Vector3f point = new Vector3f(
											minbounds.x
													+ Math.random()
													* (maxbounds.x - minbounds.x),
											minbounds.y
													+ Math.random()
													* (maxbounds.y - minbounds.y),
											minbounds.z
													+ Math.random()
													* (maxbounds.z - minbounds.z));
									int region = doSimplex(point);
									generated.put(point, region);
								}
								adding = false;
							}
						}
					}
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (NoSuchElementException e1) {
					e1.printStackTrace();
				}
				in.close();
				System.out.println("stop");
			}
		};
		input.start();
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		simplex.render();
		for (Points p : pointbatches)
			p.render();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		if (rebuildsimplex) {
			simplex.delete();
			simplex = new Simplex(simplices);

			for (Points p : pointbatches)
				p.delete();
			pointbatches.clear();
			int simplexsize = simplices.size();
			int numregions = 0;
			switch (simplexsize) {
			case 2:
				numregions = 2;
				break;
			case 3:
				numregions = 5;
				break;
			case 4:
				numregions = 8;
			}
			for (int i = 0; i < numregions; i++) {
				pointbatches.add(new Points(i, ShaderLoader.loadShaderFromFile(
						"res/shaders/colorshader.vert",
						"res/shaders/colorshader.frag")));
			}

			rebuildsimplex = false;
		}
		if (generated.size() > 0 && !adding) {
			Iterator<Map.Entry<Vector3f, Integer>> it = generated.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Vector3f, Integer> pairs = it.next();
				Points p = pointbatches.get(pairs.getValue() - 1);
				p.addVertex(pairs.getKey(), Color.GRAY, zero, up);
				p.addIndex(p.getIndexCount());
				it.remove();
			}
			for (Points p : pointbatches)
				p.prerender();

			int pointnum = 0;
			for (Points p : pointbatches)
				pointnum += p.getIndexCount();
			System.out.println("Total point number: " + pointnum);
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
}
