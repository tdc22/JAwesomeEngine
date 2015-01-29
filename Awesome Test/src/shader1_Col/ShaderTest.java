package shader1_Col;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import loader.ShaderLoader;
import shader.Shader;
import shape.Box;
import vector.Vector4f;

public class ShaderTest extends StandardGame {
	Shader shader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		shader = new Shader(ShaderLoader.loadShader(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		shader.addArgumentName("color");
		shader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		Box a = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		addObject(a);
	}

	@Override
	public void render() {
		shader.bind();
		renderScene();
		shader.unbind();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}
