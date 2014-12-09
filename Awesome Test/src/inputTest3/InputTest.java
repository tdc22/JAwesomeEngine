package inputTest3;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.GLFWInputReader;
import input.Input;
import input.InputEvent;
import input.MouseEvent;
import shape.Box;

public class InputTest extends StandardGame {
	Box box;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		inputs.setInputReader(new GLFWInputReader(((GLDisplay) display)
				.getWindowID()));

		initEvents();
	}

	private void initEvents() {
		inputs.addEvent(new InputEvent("MouseMoved", new Input(
				Input.MOUSE_EVENT, "", MouseEvent.MOUSE_MOVED)));
		inputs.addEvent(new InputEvent("MouseButton0", new Input(
				Input.MOUSE_EVENT, "0", MouseEvent.MOUSE_BUTTON_DOWN)));
		inputs.addEvent(new InputEvent("MouseButton1Pressed", new Input(
				Input.MOUSE_EVENT, "1", MouseEvent.MOUSE_BUTTON_PRESSED)));
		inputs.addEvent(new InputEvent("MouseButton1Released", new Input(
				Input.MOUSE_EVENT, "1", MouseEvent.MOUSE_BUTTON_RELEASED)));
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
		if (inputs.isEventActive("MouseMoved"))
			System.out.println("Mouse movement: " + inputs.getMouseDX() + "; "
					+ inputs.getMouseDY());
		if (inputs.isEventActive("MouseButton0"))
			System.out.println("Mouse Button 1 Down");
		if (inputs.isEventActive("MouseButton1Pressed"))
			System.out.println("Mouse Button 2 Pressed");
		if (inputs.isEventActive("MouseButton1Released"))
			System.out.println("Mouse Button 2 Released");
	}

}
