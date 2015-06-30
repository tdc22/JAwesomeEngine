package debugger;

import game.StandardGame;
import gui.Font;
import loader.FontLoader;
import loader.ModelLoader;
import utils.Debugger;
import utils.GameProfiler;
import utils.Profiler;
import utils.SimpleGameProfiler;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class DebugTest extends StandardGame {
	Debugger debugger;
	Profiler profiler;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		Font f = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, f, cam);
		GameProfiler gp = new SimpleGameProfiler();
		setProfiler(gp);
		profiler = new Profiler(inputs, f, gp, null);
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		addObject(ModelLoader.load("res/models/bunny.mobj"));

		// inputs.createInputEvent("toggle Mouse grab").addEventTrigger(
		// new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_T,
		// KeyEvent.Key_Pressed));
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
		profiler.render2d();
	}

	@Override
	public void update(int delta) {
		// if (inputs.isInputEventActive("toggle Mouse grab")) {
		// mouse.setGrabbed(!mouse.isGrabbed());
		// }

		debugger.update();
		profiler.update(delta);
		cam.update(delta);
	}

}
