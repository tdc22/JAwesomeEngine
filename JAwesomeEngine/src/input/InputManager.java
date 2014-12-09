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

	public void setInputReader(InputReader inputreader) {
		this.inputreader = inputreader;
	}

	public InputReader getInputReader() {
		return inputreader;
	}

	public void update() {
		inputreader.update();
		Iterator<InputEvent> it = inputevents.values().iterator();
		while (it.hasNext()) {
			it.next().updateActive(inputreader);
		}
	}

	public boolean isEventActive(String eventname) {
		return inputevents.get(eventname).isActive();
	}

	public boolean isKeyDown(String keyname) {
		return inputreader.isKeyDown(keyname);
	}

	public boolean isMouseButtonDown(String button) {
		return inputreader.isMouseButtonDown(button);
	}

	public int getMouseDX() {
		return inputreader.getMouseDX();
	}

	public int getMouseDY() {
		return inputreader.getMouseDY();
	}

	public boolean isMouseMoved() {
		return inputreader.isMouseMoved();
	}
}