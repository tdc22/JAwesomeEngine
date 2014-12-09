package input;

import java.util.ArrayList;
import java.util.List;

public class InputEvent {
	private String name;
	private boolean active;
	List<Input> eventtriggers;

	public InputEvent(String name) {
		this.name = name;
		eventtriggers = new ArrayList<Input>();
	}

	public InputEvent(String name, Input... inputs) {
		this.name = name;
		eventtriggers = new ArrayList<Input>();
		for (Input input : inputs) {
			eventtriggers.add(input);
		}
	}

	public void addEventTrigger(Input input) {
		eventtriggers.add(input);
	}

	public List<Input> getEventTriggers() {
		return eventtriggers;
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public boolean updateActive(InputReader inputreader) {
		for (Input i : eventtriggers) {
			if (inputreader.isInputActive(i)) {
				return active = true;
			}
		}
		return active = false;
	}
}