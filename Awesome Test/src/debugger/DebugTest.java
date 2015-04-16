package debugger;

import game.StandardGame;
import loader.FontLoader;
import loader.ModelLoader;
import utils.Debugger;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class DebugTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
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
	}

	@Override
	public void update(int delta) {
		// if (inputs.isInputEventActive("toggle Mouse grab")) {
		// mouse.setGrabbed(!mouse.isGrabbed());
		// }

		debugger.update();
		cam.update(delta);
	}

}
