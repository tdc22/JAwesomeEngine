package modelLoader;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ModelLoader;
import loader.ShaderLoader;
import shader.Shader;
import sound.NullSoundEnvironment;

public class ModelTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		defaultshader.addObject(ModelLoader.load("res/models/bunny.mobj"));
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