package normalDebug;

import game.StandardGame;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.ShapedObject;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import utils.GLConstants;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ShaderTest extends StandardGame {
	Shader normalshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0.5f, 5);
		cam.rotateTo(0, 0);

		normalshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/normaldebug.vert", "res/shaders/normaldebug.geo",
				GLConstants.TRIANGLE_ADJACENCY, GLConstants.LINE_STRIP, 6));
		normalshader.addArgumentName("uNormalsLength");
		normalshader.addArgument(0.4f);

		Sphere a = new Sphere(0, 0, 0, 1, 36, 36);
		a.setRenderHints(false, false, true);
		addObject(a);

		Box box = new Box(0, 0, -3, 0.5f, 0.5f, 0.5f);
		box.setRenderHints(false, false, true);
		addObject(box);

		ShapedObject model = ModelLoader.load("res/models/bunny.mobj");
		model.translate(-5f, 0f, -5f);
		model.setRenderHints(false, false, true);
		addObject(model);

		ShapedObject model2 = ModelLoader.load("res/models/cylinder.mobj");
		model2.translate(-5f, 0f, 5f);
		model2.setRenderHints(false, false, true);
		addObject(model2);
	}

	@Override
	public void render() {
		renderScene();
		normalshader.bind();
		renderScene();
		normalshader.unbind();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}
