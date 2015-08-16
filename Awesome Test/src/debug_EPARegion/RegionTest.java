package debug_EPARegion;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.util.ArrayList;
import java.util.List;

import loader.FontLoader;
import loader.ShaderLoader;
import math.VecMath;
import utils.Debugger;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class RegionTest extends StandardGame {
	List<Vector3f> testTrue, testFalse;
	int pointshader;
	Simplex simplex;
	Points truePoints, falsePoints;
	Vector3f minbounds = new Vector3f(-5, -5, -5), maxbounds = new Vector3f(5,
			5, 5);
	Debugger debugger;

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

		simplex = new Simplex();
		testTrue = new ArrayList<Vector3f>();
		testFalse = new ArrayList<Vector3f>();

		pointshader = ShaderLoader.loadShaderFromFile(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag");
		truePoints = new Points(new Vector3f(0f, 1f, 0f), pointshader);
		falsePoints = new Points(new Vector3f(1f, 0f, 0f), pointshader);

		for (int i = 0; i < 1000000; i++) {
			Vector3f point = new Vector3f(minbounds.x + Math.random()
					* (maxbounds.x - minbounds.x), minbounds.y + Math.random()
					* (maxbounds.y - minbounds.y), minbounds.z + Math.random()
					* (maxbounds.z - minbounds.z));
			if (isOriginInsideTriangleArea(point)) {
				truePoints.addVertex(point);
				truePoints.addIndex(truePoints.getIndexCount());
			} else {
				falsePoints.addVertex(point);
				falsePoints.addIndex(falsePoints.getIndexCount());
			}
		}
		System.out.println("ursprung: "
				+ isOriginInsideTriangleArea(new Vector3f()));
		truePoints.prerender();
		falsePoints.prerender();
	}

	private boolean isOriginInsideTriangleArea(Vector3f point) {
		Simplex t = simplex;
		if (VecMath.dotproduct(
				VecMath.crossproduct(VecMath.subtraction(t.b, t.a), t.normal),
				VecMath.subtraction(point, t.a)) <= 0) {
			if (VecMath.dotproduct(VecMath.crossproduct(
					VecMath.subtraction(t.c, t.b), t.normal), VecMath
					.subtraction(point, t.b)) <= 0) {
				if (VecMath.dotproduct(VecMath.crossproduct(
						VecMath.subtraction(t.a, t.c), t.normal), VecMath
						.subtraction(point, t.c)) <= 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void render() {
		debugger.update3d();
		debugger.begin();
		renderScene();
		simplex.render();
		// truePoints.render();
		falsePoints.render();
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
