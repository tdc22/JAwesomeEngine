package depthBufferTest;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import loader.FontLoader;
import loader.ShaderLoader;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import utils.Debugger;
import vector.Vector4f;

public class DepthBufferTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		float znear = 0.1f;
		float zfar = 200;
		PixelFormat pixelformat = new PixelFormat();
		initDisplay(new GLDisplay(), new DisplayMode(), pixelformat, new VideoSettings(800, 600, 90, znear, zfar),
				new NullSoundEnvironment());

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);

		cam.setFlyCam(true);
		cam.translateTo(0f, 5f, 0f);
		display.bindMouse();

		Shader red = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		red.addArgument("color", new Vector4f(1f, 0f, 0f, 1f));
		Shader green = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		green.addArgument("color", new Vector4f(0f, 1f, 0f, 1f));

		Shader red2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/zbuffershader.vert", "res/shaders/colorshader.frag"));
		red2.addArgument("color", new Vector4f(1f, 0f, 0f, 1f));
		red2.addArgument("depthBufferBits", pixelformat.getDepth());
		red2.addArgument("zNear", znear);
		red2.addArgument("zFar", zfar);
		Shader green2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/zbuffershader.vert", "res/shaders/colorshader.frag"));
		green2.addArgument("color", new Vector4f(0f, 1f, 0f, 1f));
		green2.addArgument("depthBufferBits", pixelformat.getDepth());
		green2.addArgument("zNear", znear);
		green2.addArgument("zFar", zfar);

		addShader(red);
		addShader(green);
		addShader(red2);
		addShader(green2);

		int halfdepth = 10000;

		Box ground1 = new Box(-26, 0, halfdepth, 25, 1, halfdepth);
		Box ground2 = new Box(26, 0, halfdepth, 25, 1, halfdepth);
		red.addObject(ground1);
		red2.addObject(ground2);

		Box overlay1 = new Box(-26, 1, halfdepth + 0.4f, 20, 0.4f, halfdepth);
		Box overlay2 = new Box(26, 1, halfdepth + 0.4f, 20, 0.4f, halfdepth);
		green.addObject(overlay1);
		green2.addObject(overlay2);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void update(int delta) {
		debugger.update(fps, 0, 0);
		cam.update(delta);
	}

	@Override
	public void renderInterface() {
		debugger.end();
	}

}
