package input;

public abstract class InputReader {
	public abstract void update();

	public abstract boolean isKeyDown(String keyname);

	public abstract boolean isMouseButtonDown(String button);

	protected int mousedx, mousedy;

	public int getMouseDX() {
		return mousedx;
	}

	public int getMouseDY() {
		return mousedy;
	}

	public boolean isMouseMoved() {
		return (getMouseDX() != 0 || getMouseDY() != 0);
	}

	public boolean isInputActive(Input input) {
		switch (input.getInputType()) {
		case Input.MOUSE_EVENT:
			// Mouse Events
			switch (input.getEventType()) {
			case MouseEvent.MOUSE_MOVED:
				return isMouseMoved();
			case MouseEvent.MOUSE_BUTTON_PRESSED:
				if (input.isFlag()) { // if event was already active during this
					// press interval
					if (!isMouseButtonDown(input.getComponentName()))
						input.setFlag(false);
				} else { // if event wasn't yet active during this press
							// interval
					if (isMouseButtonDown(input.getComponentName())) {
						input.setFlag(true);
						return true;
					}
				}
				return false;
			case MouseEvent.MOUSE_BUTTON_DOWN:
				return isMouseButtonDown(input.getComponentName());
			case MouseEvent.MOUSE_BUTTON_RELEASED:
				if (input.isFlag()) { //
					if (!isMouseButtonDown(input.getComponentName())) {
						input.setFlag(false);
						return true;
					}
				} else {
					if (isMouseButtonDown(input.getComponentName())) {
						input.setFlag(true);
					}
				}
				return false;
			}
			break;
		case Input.KEYBOARD_EVENT:
			// Key Events
			switch (input.getEventType()) {
			case KeyEvent.KEY_PRESSED:
				if (input.isFlag()) { // if event was already active during this
										// press interval
					if (!isKeyDown(input.getComponentName()))
						input.setFlag(false);
				} else { // if event wasn't yet active during this press
							// interval
					if (isKeyDown(input.getComponentName())) {
						input.setFlag(true);
						return true;
					}
				}
				return false;
			case KeyEvent.KEY_DOWN:
				return isKeyDown(input.getComponentName());
			case KeyEvent.KEY_RELEASED:
				if (input.isFlag()) { //
					if (!isKeyDown(input.getComponentName())) {
						input.setFlag(false);
						return true;
					}
				} else {
					if (isKeyDown(input.getComponentName())) {
						input.setFlag(true);
					}
				}
				return false;
			}
			break;
		case Input.GAMEPAD_EVENT:
			// Gamepad Events

			break;
		}
		return false;
	}
}