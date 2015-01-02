package depthBufferTest;

import game.Debugger;
import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import shape.Box;
import utils.Shader;
import vector.Vector4f;

public class DepthBufferTest extends StandardGame {
	Debugger debugmanager;

	@Override
	public void init() {
		initDisplay(false, 800, 600, true);
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
		cam.setFlyCam(true);
		cam.translateTo(0f, 5f, 0f);

		float znear = 0.1f;
		float zfar = 200;
		settings.setZNear(znear);
		settings.setZFar(zfar);

		Shader red = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		red.addArgument("color", new Vector4f(1f, 0f, 0f, 1f));
		Shader green = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		green.addArgument("color", new Vector4f(0f, 1f, 0f, 1f));

		Shader red2 = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/zbuffershader.vert",
				"res/shaders/colorshader.frag"));
		red2.addArgument("color", new Vector4f(1f, 0f, 0f, 1f));
		red2.addArgument("depthBufferBits", settings.getPixelFormat()
				.getDepthBits());
		red2.addArgument("zNear", znear);
		red2.addArgument("zFar", zfar);
		Shader green2 = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/zbuffershader.vert",
				"res/shaders/colorshader.frag"));
		green2.addArgument("color", new Vector4f(0f, 1f, 0f, 1f));
		green2.addArgument("depthBufferBits", settings.getPixelFormat()
				.getDepthBits());
		green2.addArgument("zNear", znear);
		green2.addArgument("zFar", zfar);

		int halfdepth = 10000;

		Box ground1 = new Box(-26, 0, halfdepth, 25, 1, halfdepth);
		Box ground2 = new Box(26, 0, halfdepth, 25, 1, halfdepth);
		ground1.setShader(red);
		ground2.setShader(red2);
		addObject(ground1);
		addObject(ground2);

		Box overlay1 = new Box(-26, 1, halfdepth + 0.4f, 20, 0.4f, halfdepth);
		Box overlay2 = new Box(26, 1, halfdepth + 0.4f, 20, 0.4f, halfdepth);
		overlay1.setShader(green);
		overlay2.setShader(green2);
		addObject(overlay1);
		addObject(overlay2);

	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugmanager.update();
		cam.update(delta);
	}

}
