package debugger;

import game.StandardGame;
import gui.Font;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import shader.Shader;
import utils.Debugger;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class DebugTest extends StandardGame {
	Debugger debugger;

	// Profiler profiler;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshader2, font,
				cam);
		// GameProfiler gp = new SimpleGameProfiler();
		// setProfiler(gp);
		// profiler = new Profiler(inputs, font, gp, null);

		defaultshader.addObject(ModelLoader.load("res/models/bunny.mobj"));

		// inputs.createInputEvent("toggle Mouse grab").addEventTrigger(
		// new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_T,
		// KeyEvent.Key_Pressed));
	}

	@Override
	public void render() {
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		// profiler.render2d();
	}

	@Override
	public void update(int delta) {
		// if (inputs.isInputEventActive("toggle Mouse grab")) {
		// mouse.setGrabbed(!mouse.isGrabbed());
		// }

		debugger.update(fps, 0, 0);
		// profiler.update(delta);
		cam.update(delta);
	}

}
