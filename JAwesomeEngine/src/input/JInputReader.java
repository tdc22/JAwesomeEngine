package input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;
import net.java.games.input.Mouse;

public class JInputReader extends InputReader {
	List<Mouse> mousecontrollers;
	List<Keyboard> keyboardcontrollers;
	List<Controller> gamepadcontrollers;

	HashMap<String, Key> keys;

	public JInputReader() {
		mousecontrollers = new ArrayList<Mouse>();
		keyboardcontrollers = new ArrayList<Keyboard>();
		gamepadcontrollers = new ArrayList<Controller>();

		mousex = 0;
		mousey = 0;

		keys = new HashMap<String, Key>();

		updateControllers();
		setupKeys();
	}

	@Override
	public boolean isKeyDown(String componentid) {
		for (Keyboard k : keyboardcontrollers) {
			if (k.isKeyDown(keys.get(componentid)))
				return true;
		}
		return false;
	}

	@Override
	public boolean isMouseButtonDown(String button) {
		Component mousebutton = null;
		for (Mouse m : mousecontrollers) {
			switch (button) {
			case ("Left"):
				mousebutton = m.getLeft();
				break;
			case ("Middle"):
				mousebutton = m.getMiddle();
				break;
			case ("Right"):
				mousebutton = m.getRight();
				break;
			case ("Last"):
				mousebutton = m.getBack();
				break;
			case ("0"):
				mousebutton = m.getComponents()[0];
				break;
			case ("1"):
				mousebutton = m.getComponents()[1];
				break;
			case ("2"):
				mousebutton = m.getComponents()[2];
				break;
			case ("3"):
				mousebutton = m.getComponents()[3];
				break;
			case ("4"):
				mousebutton = m.getComponents()[4];
				break;
			case ("5"):
				mousebutton = m.getComponents()[5];
				break;
			case ("6"):
				mousebutton = m.getComponents()[6];
				break;
			case ("7"):
				mousebutton = m.getComponents()[7];
				break;
			}

			if (mousebutton.getPollData() == 1)
				return true;
		}
		return false;
	}

	public boolean isUseable() {
		return (mousecontrollers.size() > 0) && (keyboardcontrollers.size() > 0);
	}

	private void setupKeys() {
		keys.clear();

		keys.put("Void", Key.VOID);
		keys.put("Escape", Key.ESCAPE);
		keys.put("1", Key._1);
		keys.put("2", Key._2);
		keys.put("3", Key._3);
		keys.put("4", Key._4);
		keys.put("5", Key._5);
		keys.put("6", Key._6);
		keys.put("7", Key._7);
		keys.put("8", Key._8);
		keys.put("9", Key._9);
		keys.put("0", Key._0);
		keys.put("-", Key.MINUS);
		keys.put("=", Key.EQUALS);
		keys.put("Back", Key.BACK);
		keys.put("Tab", Key.TAB);
		keys.put("Q", Key.Q);
		keys.put("W", Key.W);
		keys.put("E", Key.E);
		keys.put("R", Key.R);
		keys.put("T", Key.T);
		keys.put("Y", Key.Y);
		keys.put("U", Key.U);
		keys.put("I", Key.I);
		keys.put("O", Key.O);
		keys.put("P", Key.P);
		keys.put("[", Key.LBRACKET);
		keys.put("]", Key.RBRACKET);
		keys.put("Return", Key.RETURN);
		keys.put("Left Control", Key.LCONTROL);
		keys.put("A", Key.A);
		keys.put("S", Key.S);
		keys.put("D", Key.D);
		keys.put("F", Key.F);
		keys.put("G", Key.G);
		keys.put("H", Key.H);
		keys.put("J", Key.J);
		keys.put("K", Key.K);
		keys.put("L", Key.L);
		keys.put(";", Key.SEMICOLON);
		keys.put("'", Key.APOSTROPHE);
		keys.put("~", Key.GRAVE);
		keys.put("Left Shift", Key.LSHIFT);
		keys.put("\\", Key.BACKSLASH);
		keys.put("Z", Key.Z);
		keys.put("X", Key.X);
		keys.put("C", Key.C);
		keys.put("V", Key.V);
		keys.put("B", Key.B);
		keys.put("N", Key.N);
		keys.put("M", Key.M);
		keys.put(",", Key.COMMA);
		keys.put(".", Key.PERIOD);
		keys.put("/", Key.SLASH);
		keys.put("Right Shift", Key.RSHIFT);
		keys.put("Multiply", Key.MULTIPLY);
		keys.put("Left Alt", Key.LALT);
		keys.put(" ", Key.SPACE);
		keys.put("Caps Lock", Key.CAPITAL);
		keys.put("F1", Key.F1);
		keys.put("F2", Key.F2);
		keys.put("F3", Key.F3);
		keys.put("F4", Key.F4);
		keys.put("F5", Key.F5);
		keys.put("F6", Key.F6);
		keys.put("F7", Key.F7);
		keys.put("F8", Key.F8);
		keys.put("F9", Key.F9);
		keys.put("F10", Key.F10);
		keys.put("Num Lock", Key.NUMLOCK);
		keys.put("Scroll Lock", Key.SCROLL);
		keys.put("Num 7", Key.NUMPAD7);
		keys.put("Num 8", Key.NUMPAD8);
		keys.put("Num 9", Key.NUMPAD9);
		keys.put("Num -", Key.SUBTRACT);
		keys.put("Num 4", Key.NUMPAD4);
		keys.put("Num 5", Key.NUMPAD5);
		keys.put("Num 6", Key.NUMPAD6);
		keys.put("Num +", Key.ADD);
		keys.put("Num 1", Key.NUMPAD1);
		keys.put("Num 2", Key.NUMPAD2);
		keys.put("Num 3", Key.NUMPAD3);
		keys.put("Num 0", Key.NUMPAD0);
		keys.put("Num .", Key.DECIMAL);
		keys.put("F11", Key.F11);
		keys.put("F12", Key.F12);
		keys.put("F13", Key.F13);
		keys.put("F14", Key.F14);
		keys.put("F15", Key.F15);
		keys.put("Kana", Key.KANA);
		keys.put("Convert", Key.CONVERT);
		keys.put("Noconvert", Key.NOCONVERT);
		keys.put("Yen", Key.YEN);
		keys.put("Num =", Key.NUMPADEQUAL);
		keys.put("Circumflex", Key.CIRCUMFLEX);
		keys.put("At", Key.AT);
		keys.put("Colon", Key.COLON);
		keys.put("Underline", Key.UNDERLINE);
		keys.put("Kanji", Key.KANJI);
		keys.put("Stop", Key.STOP);
		keys.put("Ax", Key.AX);
		keys.put("Unlabeled", Key.UNLABELED);
		keys.put("Num Enter", Key.NUMPADENTER);
		keys.put("Right Control", Key.RCONTROL);
		keys.put("Num ,", Key.NUMPADCOMMA);
		keys.put("Num /", Key.DIVIDE);
		keys.put("SysRq", Key.SYSRQ);
		keys.put("Right Alt", Key.RALT);
		keys.put("Pause", Key.PAUSE);
		keys.put("Home", Key.HOME);
		keys.put("Up", Key.UP);
		keys.put("Pg Up", Key.PAGEUP);
		keys.put("Left", Key.LEFT);
		keys.put("Right", Key.RIGHT);
		keys.put("End", Key.END);
		keys.put("Down", Key.DOWN);
		keys.put("Pg Down", Key.PAGEDOWN);
		keys.put("Insert", Key.INSERT);
		keys.put("Delete", Key.DELETE);
		keys.put("Left Windows", Key.LWIN);
		keys.put("Right Windows", Key.RWIN);
		keys.put("Apps", Key.APPS);
		keys.put("Power", Key.POWER);
		keys.put("Sleep", Key.SLEEP);
		keys.put("Unknown", Key.UNKNOWN);
	}

	@Override
	public void update() {
		mousex = 0;
		mousey = 0;
		for (Mouse m : mousecontrollers) {
			m.poll();
			if (m.getX().getPollData() != 0 || m.getY().getPollData() != 0) {
				mousex += m.getX().getPollData();
				mousey += m.getY().getPollData();
			}
		}

		for (Keyboard k : keyboardcontrollers) {
			k.poll();
		}
		for (Controller c : gamepadcontrollers) {
			c.poll();
		}
	}

	public void updateControllers() {
		mousecontrollers.clear();
		keyboardcontrollers.clear();
		gamepadcontrollers.clear();

		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (Controller c : ca) {
			Type t = c.getType();
			if (t == Type.MOUSE)
				mousecontrollers.add((Mouse) c);
			if (t == Type.KEYBOARD)
				keyboardcontrollers.add((Keyboard) c);
			if (t == Type.GAMEPAD)
				gamepadcontrollers.add(c);
		}
	}
}