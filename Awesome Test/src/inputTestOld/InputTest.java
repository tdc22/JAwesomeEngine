package inputTestOld;

import game.StandardGame;
import input.Input;
import input.KeyInput;
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

		initEvents();
		box = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		addObject(box);
	}

	private void initEvents() {
		inputs.showControllerOutput(true);

		InputEvent released = inputs.createInputEvent("KeyReleased");
		InputEvent pressed = inputs.createInputEvent("KeyPressed");
		InputEvent down = inputs.createInputEvent("KeyDown");

		InputEvent translate1 = inputs.createInputEvent("Translate1");
		InputEvent translate2 = inputs.createInputEvent("Translate2");
		InputEvent translate3 = inputs.createInputEvent("Translate3");
		InputEvent translate4 = inputs.createInputEvent("Translate4");

		InputEvent rotate1 = inputs.createInputEvent("Rotate1");
		InputEvent rotate2 = inputs.createInputEvent("Rotate2");
		InputEvent rotate3 = inputs.createInputEvent("Rotate3");
		InputEvent rotate4 = inputs.createInputEvent("Rotate4");

		released.addEventTrigger(new Input(Input.KEYBOARD_EVENT,
				Keyboard.KEY_B, KeyInput.Key_Released));
		pressed.addEventTrigger(new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_N,
				KeyInput.Key_Pressed));
		down.addEventTrigger(new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_M,
				KeyInput.Key_Down));

		translate1.addEventTrigger(new Input(Input.KEYBOARD_EVENT,
				Keyboard.KEY_T, KeyInput.Key_Down));
		translate2.addEventTrigger(new Input(Input.KEYBOARD_EVENT,
				Keyboard.KEY_G, KeyInput.Key_Down));
		translate3.addEventTrigger(new Input(Input.KEYBOARD_EVENT,
				Keyboard.KEY_F, KeyInput.Key_Down));
		translate4.addEventTrigger(new Input(Input.KEYBOARD_EVENT,
				Keyboard.KEY_H, KeyInput.Key_Down));

		rotate1.addEventTrigger(new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_I,
				KeyInput.Key_Down));
		rotate2.addEventTrigger(new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_K,
				KeyInput.Key_Down));
		rotate3.addEventTrigger(new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_J,
				KeyInput.Key_Down));
		rotate4.addEventTrigger(new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_L,
				KeyInput.Key_Down));

		translate1.addEventTrigger(new Input(Input.CONTROLLER_EVENT, "y", 0,
				0.5f));
		// translate2.addEventTrigger(new Input(Input.CONTROLLER_EVENT, "y", 0,
		// -0.5f));
		translate3.addEventTrigger(new Input(Input.CONTROLLER_EVENT, "x", 0,
				0.5f));
		translate4.addEventTrigger(new Input(Input.CONTROLLER_EVENT, "x", 0,
				-0.5f));
		//
		// rotate1.addEventTrigger(new Input(Input.CONTROLLER_EVENT,
		// Keyboard.KEY_I, KeyEvent.Key_Down));
		// rotate2.addEventTrigger(new Input(Input.CONTROLLER_EVENT,
		// Keyboard.KEY_K, KeyEvent.Key_Down));
		// rotate3.addEventTrigger(new Input(Input.CONTROLLER_EVENT,
		// Keyboard.KEY_J, KeyEvent.Key_Down));
		// rotate4.addEventTrigger(new Input(Input.CONTROLLER_EVENT,
		// Keyboard.KEY_L, KeyEvent.Key_Down));
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
		if (inputs.isInputEventActive("KeyReleased")) {
			System.out.println("Key released.");
		}
		if (inputs.isInputEventActive("KeyPressed")) {
			System.out.println("Key pressed.");
		}
		if (inputs.isInputEventActive("KeyDown")) {
			System.out.println("Key down.");
		}

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
		cam.update(delta);
	}

}
