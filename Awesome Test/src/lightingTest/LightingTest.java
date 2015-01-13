package lightingTest;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.ShapedObject;
import utils.Shader;
import vector.Vector3f;
import vector.Vector4f;

public class LightingTest extends StandardGame {
	Debugger debugmanager;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);

		Shader shader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/pixelphonglighting.vert",
				"res/shaders/pixelphonglighting.frag"));
		shader.addArgumentName("lightpos");
		shader.addArgument(new Vector3f(0, 0, 10));
		shader.addArgumentName("ambient");
		shader.addArgument(new Vector4f(0.1f, 0.1f, 0.1f, 1f));
		shader.addArgumentName("diffuse");
		shader.addArgument(new Vector4f(0.4f, 0.4f, 0.4f, 1f));
		shader.addArgumentName("shininess");
		shader.addArgument(10f);

		ShapedObject bunny = ModelLoader.load("res/models/bunny.mobj");
		bunny.setRenderHints(false, false, true);
		bunny.setShader(shader);
		addObject(bunny);
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
