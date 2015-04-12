package lightingTest;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.ShapedObject;
import shader.Shader;
import utils.Debugger;
import vector.Vector3f;
import vector.Vector4f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class LightingTest extends StandardGame {
	Debugger debugger;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);

		Shader shader = new Shader(ShaderLoader.loadShaderFromFile(
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

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugger.update();
		if (display.isMouseBound())
			cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}

}
