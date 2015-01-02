package input;

public abstract class InputReader {
	protected float mousedx, mousedy;

	public abstract int getGamepadCount();

	public float getGamepadStickValue(int gamepad, int sticknum) {
		return (float) Math.sqrt(Math.pow(
				getGamepadStickValue(gamepad, sticknum, "x"), 2)
				+ Math.pow(getGamepadStickValue(gamepad, sticknum, "y"), 2));
	}

	public abstract float getGamepadStickValue(int gamepad, int sticknum,
			String axis);

	public float getMouseDX() {
		return mousedx;
	}

	public float getMouseDY() {
		return mousedy;
	}

	public abstract boolean isGamepadButtonDown(int gamepad, String button);

	public boolean isInputActive(Input input) {
		switch (input.getInputType()) {
		case Input.MOUSE_EVENT:
			// Mouse Events
			switch (input.getEventType()) {
			case MouseInput.MOUSE_MOVED:
				return isMouseMoved();
			case MouseInput.MOUSE_BUTTON_PRESSED:
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
			case MouseInput.MOUSE_BUTTON_DOWN:
				return isMouseButtonDown(input.getComponentName());
			case MouseInput.MOUSE_BUTTON_RELEASED:
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
			case KeyInput.KEY_PRESSED:
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
			case KeyInput.KEY_DOWN:
				return isKeyDown(input.getComponentName());
			case KeyInput.KEY_RELEASED:
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
			switch (input.getEventType()) {
			case GamepadInput.STICK_ACTIVE:
				return getGamepadStickValue(
						((GamepadInput) input).getGamepadID(),
						Integer.parseInt(((GamepadInput) input)
								.getComponentName())) > ((GamepadInput) input)
						.getDeadZone();
			case GamepadInput.BUTTON_PRESSED:
				if (input.isFlag()) { // if event was already active during this
										// press interval
					if (!isGamepadButtonDown(
							((GamepadInput) input).getGamepadID(),
							input.getComponentName()))
						input.setFlag(false);
				} else { // if event wasn't yet active during this press
							// interval
					if (isGamepadButtonDown(
							((GamepadInput) input).getGamepadID(),
							input.getComponentName())) {
						input.setFlag(true);
						return true;
					}
				}
				return false;
			case GamepadInput.BUTTON_DOWN:
				return isGamepadButtonDown(
						((GamepadInput) input).getGamepadID(),
						input.getComponentName());
			case GamepadInput.BUTTON_RELEASED:
				if (input.isFlag()) { //
					if (!isGamepadButtonDown(
							((GamepadInput) input).getGamepadID(),
							input.getComponentName())) {
						input.setFlag(false);
						return true;
					}
				} else {
					if (isGamepadButtonDown(
							((GamepadInput) input).getGamepadID(),
							input.getComponentName())) {
						input.setFlag(true);
					}
				}
				return false;
			}
			break;
		}
		return false;
	}

	public abstract boolean isKeyDown(String keyname);

	public abstract boolean isMouseButtonDown(String button);

	public boolean isMouseMoved() {
		return (getMouseDX() != 0 || getMouseDY() != 0);
	}

	public abstract void update();
}