package utils;

import gui.Font;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.util.List;

import objects.Updateable;
import space.PhysicsSpaceProfiler;

public class Profiler implements Updateable {
	Font font;
	GameProfiler gameprofiler;
	PhysicsSpaceProfiler physicsprofiler;
	int storeValues = 5000; // storetime in MS
	List<Integer> deltas;
	List<Integer>[] values;
	boolean showScale = false;
	boolean showGameProfile = false;
	boolean showPhysicsProfile = false;
	InputEvent toggleScale, toggleGameProfile, togglePhysicsProfile;

	public Profiler(InputManager i, Font f, GameProfiler gameprofiler,
			PhysicsSpaceProfiler physicsprofiler) {
		this.gameprofiler = gameprofiler;
		this.physicsprofiler = physicsprofiler;
		font = f;
		setupEvents(i);
	}

	public Profiler(InputManager i, Font f, GameProfiler gameprofiler) {
		this.gameprofiler = gameprofiler;
		font = f;
		setupEvents(i);
	}

	public Profiler(InputManager i, Font f, PhysicsSpaceProfiler physicsprofiler) {
		this.physicsprofiler = physicsprofiler;
		font = f;
		setupEvents(i);
	}

	public void setGameProfiler(GameProfiler gameprofiler) {
		this.gameprofiler = gameprofiler;
	}

	public void setPhysicsProfiler(PhysicsSpaceProfiler physicsprofiler) {
		this.physicsprofiler = physicsprofiler;
	}

	private void setupEvents(InputManager inputs) {
		toggleScale = new InputEvent("profile_showscale", new Input(
				Input.KEYBOARD_EVENT, "F9", KeyInput.KEY_PRESSED));
		toggleGameProfile = new InputEvent("profile_showgameprofile",
				new Input(Input.KEYBOARD_EVENT, "F10", KeyInput.KEY_PRESSED));
		togglePhysicsProfile = new InputEvent("profile_showphysicsprofile",
				new Input(Input.KEYBOARD_EVENT, "F11", KeyInput.KEY_PRESSED));

		inputs.addEvent(toggleScale);
		inputs.addEvent(toggleGameProfile);
		inputs.addEvent(togglePhysicsProfile);
	}

	public void render2d() {

	}

	@Override
	public void update(int delta) {
		if (gameprofiler != null) {

		}
		if (physicsprofiler != null) {

		}
	}
}
