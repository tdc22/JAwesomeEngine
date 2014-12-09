package inputTest2;

import game.StandardGame;
import loader.InputLoader;
import shape.Box;

public class InputTest extends StandardGame {
	Box box;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initDisplay(false, 800, 600, true);
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		inputs = InputLoader.load("res/inputs.txt");
		box = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		addObject(box);
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
		if (inputs.isInputEventActive("Translate1")) {
			box.translate(0, delta / 100f, 0);
		}
		if (inputs.isInputEventActive("Translate2")) {
			box.translate(0, -delta / 100f, 0);
		}
		if (inputs.isInputEventActive("Translate3")) {
			box.translate(-delta / 100f, 0, 0);
		}
		if (inputs.isInputEventActive("Translate4")) {
			box.translate(delta / 100f, 0, 0);
		}

		if (inputs.isInputEventActive("Rotate1")) {
			box.rotate(delta / 10f, 0, 0);
		}
		if (inputs.isInputEventActive("Rotate2")) {
			box.rotate(-delta / 10f, 0, 0);
		}
		if (inputs.isInputEventActive("Rotate3")) {
			box.rotate(0, -delta / 10f, 0);
		}
		if (inputs.isInputEventActive("Rotate4")) {
			box.rotate(0, delta / 10f, 0);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			box.rotate(delta / 10f, 0, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
			box.rotate(-delta / 10f, 0, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
			box.rotate(0, delta / 10f, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
			box.rotate(0, -delta / 10f, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			box.rotate(0, 0, delta / 10f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			box.rotate(0, 0, -delta / 10f);
		}
		cam.update(delta);
	}

}
