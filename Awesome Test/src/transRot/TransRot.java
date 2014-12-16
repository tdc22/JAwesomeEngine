package transRot;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.Input;
import input.InputEvent;
import input.KeyEvent;
import loader.ModelLoader;
import math.FastMath;
import objects.RenderedObject;

public class TransRot extends StandardGame {
	RenderedObject rabbit1, rabbit2;
	float r2y;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 5, 40);
		cam.rotateTo(0, 0);

		rabbit1 = ModelLoader.load("res/models/bunny.mobj");
		rabbit2 = ModelLoader.load("res/models/bunny.mobj");
		addObject(rabbit1);
		addObject(rabbit2);

		rabbit1.translateTo(-10, 0, 0);
		rabbit2.translateTo(10, 0, 0);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyEvent.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		renderScene();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}

		float speed = 0.01f * delta;
		rabbit1.rotate(speed, speed, speed);
		r2y += speed;
		rabbit2.translateTo(rabbit2.getTranslation().getXf(),
				FastMath.sin(r2y) * 3, 0);
	}

}
