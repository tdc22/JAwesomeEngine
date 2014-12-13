package shaderEdge;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import loader.FontLoader;
import loader.ShaderLoader;
import shape.Box;
import utils.GLConstants;
import utils.Shader;

public class ShaderTest extends StandardGame {
	Debugger debugmanager;
	Shader edgeshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(5, 5, 5);
		cam.rotateTo(45, -35);

		edgeshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/edgeshader.vert", "res/shaders/edgeshader.geo",
				GLConstants.TRIANGLE_ADJACENCY,
				GLConstants.LINE_STRIP, 6));

		Box a = new Box(0, 0, 0, 1, 1, 1);
		a.setRenderHints(false, false, true);
		addObject(a);
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
		if (!debugmanager.isWireframeRendered()) {
			edgeshader.bind();
			renderScene();
			edgeshader.unbind();
		}
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
	}

	@Override
	public void update(int delta) {
		debugmanager.update();
		cam.update(delta);
	}
}
