package animationSkeletal;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ModelLoader;
import loader.ShaderLoader;
import shader.Shader;
import sound.NullSoundEnvironment;
import vector.Vector4f;

public class SkeletalTest extends StandardGame {
	Shader shader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		shader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		shader.addArgumentName("u_color");
		shader.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(shader);

		shader.addObject(ModelLoader.load("res/models/model.dae"));
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
