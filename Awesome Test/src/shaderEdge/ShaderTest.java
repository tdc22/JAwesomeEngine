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
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(5, 5, 5);
		cam.rotateTo(45, -35);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		edgeshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/edgeshader.vert", "res/shaders/edgeshader.frag",
						"res/shaders/edgeshader.geo", GLConstants.TRIANGLE_ADJACENCY, GLConstants.LINE_STRIP, 4));
		addShader(edgeshader);

		Box a = new Box(0, 0, 0, 1, 1, 1);
		a.setRenderHints(true, false, true);
		defaultshader.addObject(a);
		edgeshader.addObject(a);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
		// if (!debugger.isWireframeRendered()) {
		// edgeshader.bind();
		// renderScene();
		// edgeshader.unbind();
		// }
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
}
