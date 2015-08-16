package inputTest;

import game.StandardGame;
import input.GamepadInput;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import input.MouseInput;
import shape.Box;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class InputTest extends StandardGame {
	Box box;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		// inputs.setInputReader(new GLFWInputReader(((GLDisplay) display)
		// .getWindowID()));

		initEvents();
	}

	private void initEvents() {
		inputs.addEvent(new InputEvent("MouseMoved", new Input(
				Input.MOUSE_EVENT, "", MouseInput.MOUSE_MOVED)));
		inputs.addEvent(new InputEvent("MouseButton0", new Input(
				Input.MOUSE_EVENT, "0", MouseInput.MOUSE_BUTTON_DOWN)));
		inputs.addEvent(new InputEvent("MouseButton1Pressed", new Input(
				Input.MOUSE_EVENT, "1", MouseInput.MOUSE_BUTTON_PRESSED)));
		inputs.addEvent(new InputEvent("MouseButton1Released", new Input(
				Input.MOUSE_EVENT, "1", MouseInput.MOUSE_BUTTON_RELEASED)));

		inputs.addEvent(new InputEvent("Key_W_Down", new Input(
				Input.KEYBOARD_EVENT, "W", KeyInput.KEY_DOWN)));
		inputs.addEvent(new InputEvent("Key_A_Released", new Input(
				Input.KEYBOARD_EVENT, "A", KeyInput.KEY_RELEASED)));
		inputs.addEvent(new InputEvent("Key_D_Pressed", new Input(
				Input.KEYBOARD_EVENT, "D", KeyInput.KEY_PRESSED)));

		// Button naming for XBox 360 Gamepad
		inputs.addEvent(new InputEvent("A Down", new GamepadInput(0, "0",
				GamepadInput.BUTTON_DOWN)));
		inputs.addEvent(new InputEvent("B Pressed", new GamepadInput(0, "1",
				GamepadInput.BUTTON_PRESSED)));
		inputs.addEvent(new InputEvent("X Released", new GamepadInput(0, "2",
				GamepadInput.BUTTON_RELEASED)));
		inputs.addEvent(new InputEvent("Stick Active", new GamepadInput(0, "0",
				GamepadInput.STICK_ACTIVE, 0.2f)));
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		if (inputs.isEventActive("MouseMoved"))
			System.out.println("Mouse movement: " + inputs.getMouseDX() + "; "
					+ inputs.getMouseDY());
		if (inputs.isEventActive("MouseButton0"))
			System.out.println("Mouse Button 1 Down");
		if (inputs.isEventActive("MouseButton1Pressed"))
			System.out.println("Mouse Button 2 Pressed");
		if (inputs.isEventActive("MouseButton1Released"))
			System.out.println("Mouse Button 2 Released");

		System.out.println("Key_W_Down: " + inputs.isEventActive("Key_W_Down"));
		if (inputs.isEventActive("Key_W_Down"))
			System.out.println("Key W is Down.");
		if (inputs.isEventActive("Key_A_Released"))
			System.out.println("Key A is Released.");
		if (inputs.isEventActive("Key_D_Pressed"))
			System.out.println("Key D is Pressed.");

		if (inputs.isEventActive("A Down"))
			System.out.println("A Button Down");
		if (inputs.isEventActive("B Pressed"))
			System.out.println("B Button Pressed");
		if (inputs.isEventActive("X Released"))
			System.out.println("X Button Released");
		if (inputs.isEventActive("Stick Active"))
			System.out.println("Stick Active: "
					+ inputs.getGamepadStickValue(0, 0, "x") + "; "
					+ inputs.getGamepadStickValue(0, 0, "y"));
	}

}
