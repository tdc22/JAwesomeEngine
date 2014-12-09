package input;

import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_3;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_4;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_5;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_6;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_7;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_8;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
import static org.lwjgl.system.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.system.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.system.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.system.glfw.GLFW.glfwGetKey;
import static org.lwjgl.system.glfw.GLFW.glfwGetMouseButton;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.glfw.GLFW;

public class GLFWInputReader extends InputReader {
	List<Long> windowids;
	HashMap<String, Integer> keys;
	private DoubleBuffer tmpMousePosX, tmpMousePosY;
	private int lastMousePosX, lastMousePosY;

	public GLFWInputReader(Long... windowids) {
		this.windowids = new ArrayList<Long>();
		for (Long id : windowids)
			this.windowids.add(id);

		tmpMousePosX = BufferUtils.createDoubleBuffer(1);
		tmpMousePosY = BufferUtils.createDoubleBuffer(1);
		lastMousePosX = 0;
		lastMousePosY = 0;
		updateMouseData();
		mousedx = 0;
		mousedy = 0;

		keys = new HashMap<String, Integer>();
		setupKeys();
	}

	public void addWindowID(Long windowid) {
		windowids.add(windowid);
	}

	@Override
	public void update() {
		updateMouseData();
	}

	private void updateMouseData() {
		double tmpmx, tmpmy;
		int currx = 0;
		int curry = 0;
		for (Long id : windowids) {
			glfwGetCursorPos(id, tmpMousePosX, tmpMousePosY);
			tmpMousePosX.rewind();
			tmpMousePosY.rewind();
			tmpmx = tmpMousePosX.get(0);
			tmpmy = tmpMousePosY.get(0);
			if (tmpmx != 0 || tmpmy != 0) {
				currx += tmpmx;
				curry += tmpmy;
			}
		}
		mousedx = currx - lastMousePosX;
		mousedy = curry - lastMousePosY;
		lastMousePosX = currx;
		lastMousePosY = curry;
	}

	@Override
	public boolean isKeyDown(String keyname) {
		for (Long i : windowids)
			if (glfwGetKey(i, keys.get(keyname)) == GLFW_PRESS)
				return true;
		return false;
	}

	@Override
	public boolean isMouseButtonDown(String button) {
		int buttonvalue = 0;
		switch (button) {
		case ("Left"):
			buttonvalue = GLFW_MOUSE_BUTTON_LEFT;
			break;
		case ("Middle"):
			buttonvalue = GLFW_MOUSE_BUTTON_MIDDLE;
			break;
		case ("Right"):
			buttonvalue = GLFW_MOUSE_BUTTON_RIGHT;
			break;
		case ("Last"):
			buttonvalue = GLFW_MOUSE_BUTTON_LAST;
			break;
		case ("0"):
			buttonvalue = GLFW_MOUSE_BUTTON_1;
			break;
		case ("1"):
			buttonvalue = GLFW_MOUSE_BUTTON_2;
			break;
		case ("2"):
			buttonvalue = GLFW_MOUSE_BUTTON_3;
			break;
		case ("3"):
			buttonvalue = GLFW_MOUSE_BUTTON_4;
			break;
		case ("4"):
			buttonvalue = GLFW_MOUSE_BUTTON_5;
			break;
		case ("5"):
			buttonvalue = GLFW_MOUSE_BUTTON_6;
			break;
		case ("6"):
			buttonvalue = GLFW_MOUSE_BUTTON_7;
			break;
		case ("7"):
			buttonvalue = GLFW_MOUSE_BUTTON_8;
			break;
		}

		for (Long i : windowids)
			if (glfwGetMouseButton(i, buttonvalue) == GLFW_PRESS)
				return true;
		return false;
	}

	private void setupKeys() {
		keys.clear();

		keys.put("Void", null);
		keys.put("Escape", GLFW.GLFW_KEY_ESCAPE);
		keys.put("1", GLFW.GLFW_KEY_1);
		keys.put("2", GLFW.GLFW_KEY_2);
		keys.put("3", GLFW.GLFW_KEY_3);
		keys.put("4", GLFW.GLFW_KEY_4);
		keys.put("5", GLFW.GLFW_KEY_5);
		keys.put("6", GLFW.GLFW_KEY_6);
		keys.put("7", GLFW.GLFW_KEY_7);
		keys.put("8", GLFW.GLFW_KEY_8);
		keys.put("9", GLFW.GLFW_KEY_9);
		keys.put("0", GLFW.GLFW_KEY_0);
		keys.put("-", GLFW.GLFW_KEY_MINUS);
		keys.put("=", GLFW.GLFW_KEY_EQUAL);
		keys.put("Back", GLFW.GLFW_KEY_BACKSPACE);
		keys.put("Tab", GLFW.GLFW_KEY_TAB);
		keys.put("Q", GLFW.GLFW_KEY_Q);
		keys.put("W", GLFW.GLFW_KEY_W);
		keys.put("E", GLFW.GLFW_KEY_E);
		keys.put("R", GLFW.GLFW_KEY_R);
		keys.put("T", GLFW.GLFW_KEY_T);
		keys.put("Y", GLFW.GLFW_KEY_Y);
		keys.put("U", GLFW.GLFW_KEY_U);
		keys.put("I", GLFW.GLFW_KEY_I);
		keys.put("O", GLFW.GLFW_KEY_O);
		keys.put("P", GLFW.GLFW_KEY_P);
		keys.put("[", GLFW.GLFW_KEY_LEFT_BRACKET);
		keys.put("]", GLFW.GLFW_KEY_RIGHT_BRACKET);
		keys.put("Return", GLFW.GLFW_KEY_ENTER);
		keys.put("Left Control", GLFW.GLFW_KEY_LEFT_CONTROL);
		keys.put("A", GLFW.GLFW_KEY_A);
		keys.put("S", GLFW.GLFW_KEY_S);
		keys.put("D", GLFW.GLFW_KEY_D);
		keys.put("F", GLFW.GLFW_KEY_F);
		keys.put("G", GLFW.GLFW_KEY_G);
		keys.put("H", GLFW.GLFW_KEY_H);
		keys.put("J", GLFW.GLFW_KEY_J);
		keys.put("K", GLFW.GLFW_KEY_K);
		keys.put("L", GLFW.GLFW_KEY_L);
		keys.put(";", GLFW.GLFW_KEY_SEMICOLON);
		keys.put("'", GLFW.GLFW_KEY_APOSTROPHE);
		keys.put("~", GLFW.GLFW_KEY_GRAVE_ACCENT);
		keys.put("Left Shift", GLFW.GLFW_KEY_LEFT_SHIFT);
		keys.put("\\", GLFW.GLFW_KEY_BACKSLASH);
		keys.put("Z", GLFW.GLFW_KEY_Z);
		keys.put("X", GLFW.GLFW_KEY_X);
		keys.put("C", GLFW.GLFW_KEY_C);
		keys.put("V", GLFW.GLFW_KEY_V);
		keys.put("B", GLFW.GLFW_KEY_B);
		keys.put("N", GLFW.GLFW_KEY_N);
		keys.put("M", GLFW.GLFW_KEY_M);
		keys.put(",", GLFW.GLFW_KEY_COMMA);
		keys.put(".", GLFW.GLFW_KEY_PERIOD);
		keys.put("/", GLFW.GLFW_KEY_SLASH);
		keys.put("Right Shift", GLFW.GLFW_KEY_RIGHT_SHIFT);
		keys.put("Multiply", GLFW.GLFW_KEY_KP_MULTIPLY);
		keys.put("Left Alt", GLFW.GLFW_KEY_LEFT_ALT);
		keys.put(" ", GLFW.GLFW_KEY_SPACE);
		keys.put("Caps Lock", GLFW.GLFW_KEY_CAPS_LOCK);
		keys.put("F1", GLFW.GLFW_KEY_F1);
		keys.put("F2", GLFW.GLFW_KEY_F2);
		keys.put("F3", GLFW.GLFW_KEY_F3);
		keys.put("F4", GLFW.GLFW_KEY_F4);
		keys.put("F5", GLFW.GLFW_KEY_F5);
		keys.put("F6", GLFW.GLFW_KEY_F6);
		keys.put("F7", GLFW.GLFW_KEY_F7);
		keys.put("F8", GLFW.GLFW_KEY_F8);
		keys.put("F9", GLFW.GLFW_KEY_F9);
		keys.put("F10", GLFW.GLFW_KEY_F10);
		keys.put("Num Lock", GLFW.GLFW_KEY_NUM_LOCK);
		keys.put("Scroll Lock", GLFW.GLFW_KEY_SCROLL_LOCK);
		keys.put("Num 7", GLFW.GLFW_KEY_KP_7);
		keys.put("Num 8", GLFW.GLFW_KEY_KP_8);
		keys.put("Num 9", GLFW.GLFW_KEY_KP_9);
		keys.put("Num -", GLFW.GLFW_KEY_KP_SUBTRACT);
		keys.put("Num 4", GLFW.GLFW_KEY_KP_4);
		keys.put("Num 5", GLFW.GLFW_KEY_KP_5);
		keys.put("Num 6", GLFW.GLFW_KEY_KP_6);
		keys.put("Num +", GLFW.GLFW_KEY_KP_ADD);
		keys.put("Num 1", GLFW.GLFW_KEY_KP_1);
		keys.put("Num 2", GLFW.GLFW_KEY_KP_2);
		keys.put("Num 3", GLFW.GLFW_KEY_KP_3);
		keys.put("Num 0", GLFW.GLFW_KEY_KP_0);
		keys.put("Num .", GLFW.GLFW_KEY_KP_DECIMAL);
		keys.put("F11", GLFW.GLFW_KEY_F11);
		keys.put("F12", GLFW.GLFW_KEY_F12);
		keys.put("F13", GLFW.GLFW_KEY_F13);
		keys.put("F14", GLFW.GLFW_KEY_F14);
		keys.put("F15", GLFW.GLFW_KEY_F15);
		// keys.put("Kana", GLFW.GLFW_KEY_);
		// keys.put("Convert", GLFW.GLFW_CO);
		// keys.put("Noconvert", GLFW.GLFW_KEY_NOCONVERT);
		// keys.put("Yen", GLFW.GLFW_KEY_YEN);
		keys.put("Num =", GLFW.GLFW_KEY_KP_EQUAL);
		// keys.put("Circumflex", GLFW.GLFW_KEY_CIRCUMFLEX);
		// keys.put("At", GLFW.GLFW_KEY_AT);
		// keys.put("Colon", GLFW.GLFW_KEY_);
		// keys.put("Underline", GLFW.GLFW_KEY_UNDERLINE);
		// keys.put("Kanji", GLFW.GLFW_KEY_KANJI);
		// keys.put("Stop", GLFW.GLFW_KEY_STOP);
		// keys.put("Ax", GLFW.GLFW_KEY_AX);
		// keys.put("Unlabeled", GLFW.GLFW_KEY_UNLABELED);
		keys.put("Num Enter", GLFW.GLFW_KEY_KP_ENTER);
		keys.put("Right Control", GLFW.GLFW_KEY_RIGHT_CONTROL);
		// keys.put("Num ,", GLFW.GLFW_KEY_NUMPADCOMMA);
		keys.put("Num /", GLFW.GLFW_KEY_KP_DIVIDE);
		// keys.put("SysRq", GLFW.GLFW_KEY_);
		keys.put("Right Alt", GLFW.GLFW_KEY_RIGHT_ALT);
		keys.put("Pause", GLFW.GLFW_KEY_PAUSE);
		keys.put("Home", GLFW.GLFW_KEY_HOME);
		keys.put("Up", GLFW.GLFW_KEY_UP);
		keys.put("Pg Up", GLFW.GLFW_KEY_PAGE_UP);
		keys.put("Left", GLFW.GLFW_KEY_LEFT);
		keys.put("Right", GLFW.GLFW_KEY_RIGHT);
		keys.put("End", GLFW.GLFW_KEY_END);
		keys.put("Down", GLFW.GLFW_KEY_DOWN);
		keys.put("Pg Down", GLFW.GLFW_KEY_PAGE_DOWN);
		keys.put("Insert", GLFW.GLFW_KEY_INSERT);
		keys.put("Delete", GLFW.GLFW_KEY_DELETE);
		keys.put("Left Windows", GLFW.GLFW_KEY_LEFT_SUPER);
		keys.put("Right Windows", GLFW.GLFW_KEY_RIGHT_SUPER);
		// keys.put("Apps", GLFW.GLFW_KEY_APPS);
		// keys.put("Power", GLFW.GLFW_KEY_POWER);
		// keys.put("Sleep", GLFW.GLFW_KEY_SLEEP);
		keys.put("Unknown", GLFW.GLFW_KEY_UNKNOWN);
	}
}
