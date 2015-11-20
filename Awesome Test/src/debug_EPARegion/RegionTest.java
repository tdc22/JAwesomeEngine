package debug_EPARegion;

import java.util.ArrayList;
import java.util.List;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ShaderLoader;
import math.VecMath;
import shader.Shader;
import utils.Debugger;
import vector.Vector3f;
import vector.Vector4f;

public class RegionTest extends StandardGame {
	List<Vector3f> testTrue, testFalse;
	int pointshader;
	Simplex simplex;
	Points truePoints, falsePoints;
	Vector3f minbounds = new Vector3f(-5, -5, -5), maxbounds = new Vector3f(5, 5, 5);
	Debugger debugger;

	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings());

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		display.bindMouse();
		cam.setFlyCam(true);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		simplex = new Simplex();
		defaultshader.addObject(simplex);
		testTrue = new ArrayList<Vector3f>();
		testFalse = new ArrayList<Vector3f>();

		pointshader = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag");
		Shader a1 = new Shader(pointshader, "u_color", new Vector4f(0, 1, 0, 1f));
		Shader a2 = new Shader(pointshader, "u_color", new Vector4f(1, 0, 0, 1f));
		truePoints = new Points();
		falsePoints = new Points();
		a1.addObject(truePoints);
		a2.addObject(falsePoints);
		addShader(a1);
		addShader(a2);

		for (int i = 0; i < 1000000; i++) {
			Vector3f point = new Vector3f(minbounds.x + Math.random() * (maxbounds.x - minbounds.x),
					minbounds.y + Math.random() * (maxbounds.y - minbounds.y),
					minbounds.z + Math.random() * (maxbounds.z - minbounds.z));
			if (isOriginInsideTriangleArea(point)) {
				truePoints.addVertex(point);
				truePoints.addIndex(truePoints.getIndexCount());
			} else {
				falsePoints.addVertex(point);
				falsePoints.addIndex(falsePoints.getIndexCount());
			}
		}
		System.out.println("ursprung: " + isOriginInsideTriangleArea(new Vector3f()));
		truePoints.prerender();
		falsePoints.prerender();
	}

	private boolean isOriginInsideTriangleArea(Vector3f point) {
		Simplex t = simplex;
		if (VecMath.dotproduct(VecMath.crossproduct(VecMath.subtraction(t.b, t.a), t.normal),
				VecMath.subtraction(point, t.a)) <= 0) {
			if (VecMath.dotproduct(VecMath.crossproduct(VecMath.subtraction(t.c, t.b), t.normal),
					VecMath.subtraction(point, t.b)) <= 0) {
				if (VecMath.dotproduct(VecMath.crossproduct(VecMath.subtraction(t.a, t.c), t.normal),
						VecMath.subtraction(point, t.c)) <= 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
	}

	@Override
	public void render2d() {
		render2dLayer();
		debugger.end();
	}

	@Override
	public void update(int delta) {

		debugger.update(fps, 0, 0);

		if (display.isMouseBound())
			cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}

	@Override
	public void renderInterface() {
		// TODO Auto-generated method stub

	}
}
