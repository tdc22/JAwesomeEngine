package input;

public class Input {
	public static final int KEYBOARD_EVENT = 0;
	public static final int MOUSE_EVENT = 1;
	public static final int GAMEPAD_EVENT = 2;

	private int inputtype, eventtype;
	String componentname;
	boolean flag = false; // used for pressed and release events

	public Input(int inputtype, String componentname, int eventtype) {
		this.inputtype = inputtype;
		this.componentname = componentname;
		this.eventtype = eventtype;
	}

	public int getInputType() {
		return inputtype;
	}

	public String getComponentName() {
		return componentname;
	}

	public int getEventType() {
		return eventtype;
	}

	public boolean isControllerInput() {
		return inputtype == GAMEPAD_EVENT;
	}

	public boolean isKeyboardInput() {
		return inputtype == KEYBOARD_EVENT;
	}

	public boolean isMouseInput() {
		return inputtype == MOUSE_EVENT;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isFlag() {
		return flag;
	}
}