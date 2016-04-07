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

	public float getGamepadStickValue(int gamepad, int analogInputID) {
		return inputreader.getGamepadStickValue(gamepad, analogInputID);
	}

	public int getGamepadAxesCount(int gamepad) {
		return inputreader.getGamepadAxesCount(gamepad);
	}

	public int getGamepadButtonsCount(int gamepad) {
		return inputreader.getGamepadButtonsCount(gamepad);
	}

	public LinkedHashMap<String, InputEvent> getInputEvents() {
		return inputevents;
	}

	public InputReader getInputReader() {
		return inputreader;
	}

	public float getMouseX() {
		return inputreader.getMouseX();
	}

	public float getMouseY() {
		return inputreader.getMouseY();
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