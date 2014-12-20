package input;

public class GamepadInput extends Input {
	public int gamepadID;
	public float deadzone;

	// Constructor for analog (e.g. stick) events
	public GamepadInput(int gamepadID, String componentname, int eventtype,
			float deadzone) {
		super(GAMEPAD_EVENT, componentname, STICK_ACTIVE);
		this.gamepadID = gamepadID;
		this.deadzone = deadzone;
	}

	// Constructor for digital (e.g. button) events
	public GamepadInput(int gamepadID, String componentname, int eventtype) {
		super(GAMEPAD_EVENT, componentname, eventtype);
		this.gamepadID = gamepadID;
		deadzone = 0;
	}

	public int getGamepadID() {
		return gamepadID;
	}

	public float getDeadZone() {
		return deadzone;
	}

	public static final int STICK_ACTIVE = 0;
	public static final int BUTTON_PRESSED = 1;
	public static final int BUTTON_DOWN = 2;
	public static final int BUTTON_RELEASED = 3;
}