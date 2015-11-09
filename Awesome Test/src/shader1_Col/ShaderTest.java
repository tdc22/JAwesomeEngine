package shader1_Col;

import game.StandardGame;
import loader.ShaderLoader;
import shader.Shader;
import shape.Box;
import vector.Vector4f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ShaderTest extends StandardGame {
	Shader shader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		shader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		shader.addArgumentName("u_color");
		shader.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(shader);

		Box a = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		shader.addObject(a);
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
