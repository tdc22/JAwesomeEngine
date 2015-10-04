package normalDebug;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.ShapedObject;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import utils.GLConstants;

public class NormalTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0.5f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		Shader normalshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/normaldebug.vert", "res/shaders/normaldebug.frag",
						"res/shaders/normaldebug.geo", GLConstants.TRIANGLE_ADJACENCY, GLConstants.LINE_STRIP, 6));
		normalshader.addArgumentName("u_normalsLength");
		normalshader.addArgument(0.4f);
		addShader(normalshader);

		Sphere a = new Sphere(0, 0, 0, 1, 36, 36);
		a.setRenderHints(true, false, true);
		defaultshader.addObject(a);
		normalshader.addObject(a);

		Box box = new Box(0, 0, -3, 0.5f, 0.5f, 0.5f);
		box.setRenderHints(true, false, true);
		defaultshader.addObject(box);
		normalshader.addObject(box);

		ShapedObject model = ModelLoader.load("res/models/bunny.mobj");
		model.translate(-5f, 0f, -5f);
		model.setRenderHints(true, false, true);
		defaultshader.addObject(model);
		normalshader.addObject(model);

		ShapedObject model2 = ModelLoader.load("res/models/cone.mobj");
		model2.translate(-5f, 0f, 5f);
		model2.setRenderHints(true, false, true);
		defaultshader.addObject(model2);
		normalshader.addObject(model2);
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}
