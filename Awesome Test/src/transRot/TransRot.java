package transRot;

import game.StandardGame;
import input.Input;
import loader.ModelLoader;
import math.FastMath;
import objects.RenderedObject;

public class TransRot extends StandardGame {
	RenderedObject rabbit1, rabbit2;
	float r2y;

	@Override
	public void init() {
		initDisplay(false, 800, 600, true);
		cam.setFlyCam(true);
		cam.translateTo(0, 5, 40);
		cam.rotateTo(0, 0);

		rabbit1 = ModelLoader.load("res/models/bunny.mobj");
		rabbit2 = ModelLoader.load("res/models/bunny.mobj");
		addObject(rabbit1);
		addObject(rabbit2);

		rabbit1.translateTo(-10, 0, 0);
		rabbit2.translateTo(10, 0, 0);

		inputs.createInputEvent("toggle Mouse grab").addEventTrigger(
				new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_T,
						KeyEvent.Key_Pressed));
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
		if (inputs.isInputEventActive("toggle Mouse grab")) {
			mouse.setGrabbed(!mouse.isGrabbed());
		}

		float speed = 0.01f * delta;
		rabbit1.rotate(speed, speed, speed);
		r2y += speed;
		rabbit2.translateTo(rabbit2.getTranslation().getXf(),
				FastMath.sin(r2y) * 3, 0);
		cam.update(delta);
	}

}
