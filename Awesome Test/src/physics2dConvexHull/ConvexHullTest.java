package physics2dConvexHull;

import game.StandardGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import loader.ShaderLoader;
import objects.ShapedObject2;
import shader.Shader;
import sound.NullSoundEnvironment;
import utils.GLConstants;
import vector.Vector2f;
import convexhull.Quickhull2;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ConvexHullTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(),
				new PixelFormat().withSamples(0), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader2 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);

		List<Vector2f> pointcloud = new ArrayList<Vector2f>();
		for (int i = 0; i < 100; i++) {
			pointcloud.add(new Vector2f(Math.random() * 500 + 20,
					Math.random() * 500 + 20));
		}
		defaultshader2.addObject(new PointCloud(pointcloud));

		List<Vector2f> convexHull = Quickhull2.computeConvexHull(pointcloud)
				.getVertices();
		defaultshader2.addObject(new ConvexHull(convexHull));
		System.out.println("Point Count: " + pointcloud.size()
				+ "; Hull Size: " + convexHull.size());
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		render2dLayer();
	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}

	private class PointCloud extends ShapedObject2 {
		public PointCloud(List<Vector2f> points) {
			setRenderMode(GLConstants.POINTS);
			for (int i = 0; i < points.size(); i++) {
				addVertex(points.get(i), Color.GRAY, new Vector2f(0, 0));
				addIndex(i);
			}
			prerender();
		}
	}

	private class ConvexHull extends ShapedObject2 {
		public ConvexHull(List<Vector2f> hull) {
			setRenderMode(GLConstants.LINE_LOOP);
			for (int i = 0; i < hull.size(); i++) {
				addVertex(hull.get(i), Color.GRAY, new Vector2f(0, 0));
				addIndex(i);
			}
			prerender();
		}
	}
}
