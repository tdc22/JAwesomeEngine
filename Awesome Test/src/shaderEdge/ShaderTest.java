package shaderEdge;

import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import shader.Shader;
import shape.Box;
import utils.Debugger;
import utils.GLConstants;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ShaderTest extends StandardGame {
	Debugger debugger;
	Shader edgeshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(5, 5, 5);
		cam.rotateTo(45, -35);

		edgeshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/edgeshader.vert", "res/shaders/edgeshader.geo",
				GLConstants.TRIANGLE_ADJACENCY, GLConstants.LINE_STRIP, 6));

		Box a = new Box(0, 0, 0, 1, 1, 1);
		a.setRenderHints(false, false, true);
		addObject(a);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		if (!debugger.isWireframeRendered()) {
			edgeshader.bind();
			renderScene();
			edgeshader.unbind();
		}
	}

	@Override
	public void render2d() {
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugger.update();
		cam.update(delta);
	}
}
