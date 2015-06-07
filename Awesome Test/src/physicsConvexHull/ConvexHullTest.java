package physicsConvexHull;

import game.StandardGame;
import gui.Font;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import loader.FontLoader;
import objects.ShapedObject;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;
import convexhull.Quickhull;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ConvexHullTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(),
				new PixelFormat().withSamples(0), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, font, cam);

		List<Vector3f> pointcloud = new ArrayList<Vector3f>();
		for (int i = 0; i < 100; i++) {
			pointcloud.add(new Vector3f(Math.random() * 10 - 5,
					Math.random() * 10 - 5, Math.random() * 10 - 5));
		}
		addObject(new PointCloud(pointcloud));

		List<Vector3f> convexHull = Quickhull.computeConvexHull(pointcloud)
				.getVertices();
		addObject(new ConvexHull(convexHull));
		System.out.println("Point Count: " + pointcloud.size()
				+ "; Hull Size: " + convexHull.size());
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugger.update();
		cam.update(delta);
	}

	private class PointCloud extends ShapedObject {
		public PointCloud(List<Vector3f> points) {
			setRenderMode(GLConstants.POINTS);
			for (int i = 0; i < points.size(); i++) {
				addVertex(points.get(i), Color.GRAY, new Vector2f(0, 0),
						new Vector3f(0, 1, 0));
				addIndex(i);
			}
			prerender();
		}
	}

	private class ConvexHull extends ShapedObject {
		public ConvexHull(List<Vector3f> hull) {
			setRenderMode(GLConstants.LINE_LOOP);
			for (int i = 0; i < hull.size(); i++) {
				addVertex(hull.get(i), Color.GRAY, new Vector2f(0, 0),
						new Vector3f(0, 1, 0));
				addIndex(i);
			}
			prerender();
		}
	}
}
