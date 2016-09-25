package physicsConvexHull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import collisionshape.ConvexShape;
import convexhull.Quickhull3Old;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import math.VecMath;
import objects.ShapedObject3;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class ConvexHullTest extends StandardGame {
	Shader defaultshader;
	Debugger debugger;
	ConvexHull ch;
	InputEvent step, multistep;
	Shader color;
	List<Vector3f> pointcloud;
	int iterations = 51;//170;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings(),
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

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);

		pointcloud = new ArrayList<Vector3f>();
		// for (int i = 0; i < 1000; i++) {
		// pointcloud.add(new Vector3f(Math.random() * 20 - 5, Math.random() *
		// 20 - 5, Math.random() * 20 - 5));
		// }
		ShapedObject3 bunny = ModelLoader.load("res/models/bunny_lowpoly.obj");
		pointcloud = bunny.getVertices();
		// pointcloud.add(new Vector3f(11.404785, 10.446343, -2.761249));
		// pointcloud.add(new Vector3f(5.6260934, 5.6621804, 11.700975));
		// pointcloud.add(new Vector3f(6.812222, -1.6368495, 9.790112));
		// pointcloud.add(new Vector3f(2.2193725, -1.7034389, -4.2405186));
		// pointcloud.add(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		defaultshader.addObject(new PointCloud(pointcloud));

		color = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		color.addArgumentName("u_color");
		color.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(color);

		ConvexShape convexHull = Quickhull3Old.computeConvexHull(pointcloud, iterations);
		ch = new ConvexHull(convexHull.getVertices(), convexHull.getAdjacentsMap(), color);
		defaultshader.addObject(ch);
		System.out.println("Point Count: " + pointcloud.size() + "; Hull Size: " + convexHull.getVertices().size());

		step = new InputEvent("Step", new Input(Input.KEYBOARD_EVENT, " ", KeyInput.KEY_PRESSED));
		multistep = new InputEvent("Multistep", new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_DOWN));
		inputs.addEvent(step);
		inputs.addEvent(multistep);
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
		debugger.update(fps, 0, 0);
		cam.update(delta);

		if (step.isActive() || multistep.isActive()) {
			iterations++;
			System.out.println("Iterations: " + iterations);
			ch.delete();
			ConvexShape convexHull = Quickhull3Old.computeConvexHull(pointcloud, iterations);
			ch = new ConvexHull(convexHull.getVertices(), convexHull.getAdjacentsMap(), color);
			defaultshader.addObject(ch);
		}
	}

	private class PointCloud extends ShapedObject3 {
		public PointCloud(List<Vector3f> points) {
			setRenderMode(GLConstants.POINTS);
			for (int i = 0; i < points.size(); i++) {
				addVertex(points.get(i), Color.GRAY, new Vector2f(0, 0), new Vector3f(0, 1, 0));
				addIndex(i);
			}
			prerender();
		}
	}

	private class ConvexHull extends ShapedObject3 {
		public ConvexHull(List<Vector3f> vertices, HashMap<Integer, Integer[]> adjacents, Shader shader) {
			setRenderMode(GLConstants.TRIANGLES);

			// Center (to only show outfacing triangles
			Vector3f center = new Vector3f(vertices.get(0));
			center.translate(vertices.get(1));
			center.translate(vertices.get(2));
			center.translate(vertices.get(3));
			center.scale(0.25f);

			if (shader.getObjects().size() > 0) {
				shader.getObjects().get(0).delete();
				shader.getObjects().clear();
			}
			shader.addObject(new Box(center, 0.1f, 0.1f, 0.1f));

			List<Vector3f[]> triangles = new ArrayList<Vector3f[]>();
			for (int i = 0; i < vertices.size(); i++) {
				Integer[] adjs = adjacents.get(i);
				// System.out.println(i + " # " + adjs.length);
				for (int b = 0; b < adjs.length; b++) {
					Integer[] adjsB = adjacents.get(adjs[b]);
					for (int c = 0; c < adjs.length; c++) {
						if (b != c) {
							boolean adjB = false;
							for (int x = 0; x < adjsB.length; x++)
								if (adjsB[x] == adjs[c])
									adjB = true;
							if (adjB) {
								Vector3f[] triangle = new Vector3f[3];
								triangle[0] = vertices.get(i);
								triangle[1] = vertices.get(adjs[b]);
								triangle[2] = vertices.get(adjs[c]);
								Vector3f normal = VecMath.computeNormal(triangle[0], triangle[1], triangle[2]);
								if (!contains(triangles, triangle)
										&& VecMath.dotproduct(VecMath.subtraction(center, triangle[0]), normal) < 0) {
									// System.out.println(i + " | " + adjs[b] +
									// " | " + adjs[c]);
									addVertex(triangle[0]);
									addIndex(triangles.size() * 3);
									addVertex(triangle[1]);
									addIndex(triangles.size() * 3 + 1);
									addVertex(triangle[2]);
									addIndex(triangles.size() * 3 + 2);
									triangles.add(triangle);
								}
							}
						}
					}
				}
			}
			System.out.println("Trianglecount: " + triangles.size());
			prerender();
		}

		private boolean contains(List<Vector3f[]> triangles, Vector3f[] triangle) {
			for (Vector3f[] t : triangles) {
				if (t[0].equals(triangle[0]) && t[1].equals(triangle[1]) && t[2].equals(triangle[2]))
					return true;
				if (t[0].equals(triangle[1]) && t[1].equals(triangle[2]) && t[2].equals(triangle[0]))
					return true;
				if (t[0].equals(triangle[2]) && t[1].equals(triangle[0]) && t[2].equals(triangle[1]))
					return true;
				// if(t[0].equals(triangle[0]) && t[1].equals(triangle[2]) &&
				// t[2].equals(triangle[1])) return true;
				// if(t[0].equals(triangle[1]) && t[1].equals(triangle[0]) &&
				// t[2].equals(triangle[2])) return true;
				// if(t[0].equals(triangle[2]) && t[1].equals(triangle[1]) &&
				// t[2].equals(triangle[0])) return true;
			}
			// for(Vector3f[] t : triangles) {
			// System.out.println("- " + t[0] + "; " + t[1] + "; " + t[2]);
			// }
			return false;
		}
	}
}
