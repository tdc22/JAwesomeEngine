package physicsConvexHull;

import game.StandardGame;
import gui.Font;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import loader.FontLoader;
import loader.ShaderLoader;
import objects.ShapedObject3;
import shader.Shader;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;
import collisionshape.ConvexShape;
import convexhull.Quickhull;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ConvexHullTest extends StandardGame {
	Shader defaultshader;
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings());
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

		List<Vector3f> pointcloud = new ArrayList<Vector3f>();
		for (int i = 0; i < 1000; i++) {
			pointcloud.add(new Vector3f(Math.random() * 20 - 5, Math.random() * 20 - 5, Math.random() * 20 - 5));
		}
		// pointcloud.add(new Vector3f(11.404785, 10.446343, -2.761249));
		// pointcloud.add(new Vector3f(5.6260934, 5.6621804, 11.700975));
		// pointcloud.add(new Vector3f(6.812222, -1.6368495, 9.790112));
		// pointcloud.add(new Vector3f(2.2193725, -1.7034389, -4.2405186));
		// pointcloud.add(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		defaultshader.addObject(new PointCloud(pointcloud));

		ConvexShape convexHull = Quickhull.computeConvexHull(pointcloud);
		for (Vector3f v : convexHull.getVertices())
			System.out.println(v);
		for (int i = 0; i < convexHull.getVertices().size(); i++) {
			for (Integer a : convexHull.getAdjacentsMap().get(i))
				System.out.print(a + "; ");
			System.out.println();
		}
		defaultshader.addObject(new ConvexHull(convexHull.getVertices(), convexHull.getAdjacentsMap()));
		System.out.println("Point Count: " + pointcloud.size() + "; Hull Size: " + convexHull.getVertices().size());
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
		public ConvexHull(List<Vector3f> vertices, HashMap<Integer, Integer[]> adjacents) {
			setRenderMode(GLConstants.TRIANGLES);
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
								if (!contains(triangles, triangle)) {
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
