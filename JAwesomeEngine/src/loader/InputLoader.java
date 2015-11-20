package loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import input.GamepadInput;
import input.Input;
import input.InputEvent;
import input.InputManager;

public class InputLoader {
	protected static String getCleanString(String line) {
		return line.split(":")[1].replace(" ", "");
	}

	public static InputManager load(InputManager inputmanager, File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			try {
				save(inputmanager, file);
				System.out.println("Settings file created.");
				reader = new BufferedReader(new FileReader(file));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("\"")) {
					String eventname = line.split("\"")[1];
					InputEvent inputevent = new InputEvent(eventname);
					line = line.split(":")[1];
					// System.out.println(line);
					String[] eventtriggers = line.split("/");
					for (String et : eventtriggers) {
						String[] params = et.split(";");
						int type = Integer.parseInt(params[0].replace(" ", ""));
						Input trigger = null;
						if (type == Input.GAMEPAD_EVENT) {
							int gamepadid = Integer.parseInt(params[1].replace(" ", ""));
							String componentname = params[2];
							int eventtype = Integer.parseInt(params[3].replace(" ", ""));
							float deadzone = Float.parseFloat(params[4].replace(" ", ""));
							trigger = new GamepadInput(gamepadid, componentname, eventtype, deadzone);
						} else {
							String componentname = params[1];
							int eventtype = Integer.parseInt(params[2].replace(" ", ""));
							trigger = new Input(type, componentname, eventtype);
						}
						inputevent.addEventTrigger(trigger);
					}
					inputmanager.addEvent(inputevent);
				}
			}
			reader.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputmanager;
	}

	public static InputManager load(InputManager inputmanager, String path) {
		return load(inputmanager, new File(path));
	}

	public static void save(InputManager inputs, File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write("Input Settings\n");
		StringBuilder sb;
		List<InputEvent> inputevents = new ArrayList<InputEvent>(inputs.getInputEvents().values());
		for (InputEvent e : inputevents) {
			sb = new StringBuilder();
			sb.append("\"" + e.getName() + "\":");
			for (Input i : e.getEventTriggers()) {
				int itype = i.getInputType();
				sb.append(" " + itype + ";");
				if (itype == Input.GAMEPAD_EVENT) {
					sb.append(((GamepadInput) i).getGamepadID() + ";");
					sb.append(((GamepadInput) i).getComponentName() + ";");
					sb.append(((GamepadInput) i).getEventType() + ";");
					sb.append(((GamepadInput) i).getDeadZone());
				} else {
					sb.append(i.getComponentName() + ";");
					sb.append(i.getEventType());
				}
				sb.append("/");
				// sb.append(i.getComponentIdentifier() + ";" + i + "/");
			}
			sb.append("\n");
			writer.write(sb.toString());
		}

		writer.close();
	}

	public static void save(InputManager settings, String path) {
		try {
			save(settings, new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
