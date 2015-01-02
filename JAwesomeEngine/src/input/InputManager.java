package input;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class InputManager {
	InputReader inputreader;
	LinkedHashMap<String, InputEvent> inputevents;

	public InputManager(InputReader inputreader) {
		this.inputreader = inputreader;
		this.inputevents = new LinkedHashMap<String, InputEvent>();
	}

	public void addEvent(InputEvent inputevent) {
		inputevents.put(inputevent.getName(), inputevent);
	}

	public int getGamepadCount() {
		return inputreader.getGamepadCount();
	}

	public float getGamepadStickValue(int gamepad, int sticknum) {
		return inputreader.getGamepadStickValue(gamepad, sticknum);
	}

	public float getGamepadStickValue(int gamepad, int sticknum, String axis) {
		return inputreader.getGamepadStickValue(gamepad, sticknum, axis);
	}

	public LinkedHashMap<String, InputEvent> getInputEvents() {
		return inputevents;
	}

	public InputReader getInputReader() {
		return inputreader;
	}

	public float getMouseDX() {
		return inputreader.getMouseDX();
	}

	public float getMouseDY() {
		return inputreader.getMouseDY();
	}

	public boolean isEventActive(String eventname) {
		return inputevents.get(eventname).isActive();
	}

	public boolean isGamepadButtonDown(int gamepad, String button) {
		return inputreader.isGamepadButtonDown(gamepad, button);
	}

	public boolean isKeyDown(String keyname) {
		return inputreader.isKeyDown(keyname);
	}

	public boolean isMouseButtonDown(String button) {
		return inputreader.isMouseButtonDown(button);
	}

	public boolean isMouseMoved() {
		return inputreader.isMouseMoved();
	}

	public void setInputReader(InputReader inputreader) {
		this.inputreader = inputreader;
	}

	public void update() {
		inputreader.update();
		Iterator<InputEvent> it = inputevents.values().iterator();
		while (it.hasNext()) {
			it.next().updateActive(inputreader);
		}
	}
}