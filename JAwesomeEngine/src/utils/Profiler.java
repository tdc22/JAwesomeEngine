package utils;

import gui.Font;
import gui.Text;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import loader.ShaderLoader;
import objects.ShapedObject2;
import objects.Updateable;
import shader.Shader;
import shape2d.Quad;
import space.PhysicsProfiler;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class Profiler implements Updateable {
	GameProfiler gameprofiler;
	PhysicsProfiler physicsprofiler;
	int storeValues = 5000000; // store time in microseconds
	long maxvalue;
	List<Long> times;
	HashMap<Integer, List<Long>> values;
	boolean showScale = false;
	boolean showGameProfile = false;
	boolean showPhysicsProfile = false;
	InputEvent toggleScale, toggleGameProfile, togglePhysicsProfile;
	Text gameProfileText0, gameProfileText1, gameProfileText2,
			physicsProfileText0, physicsProfileText1, physicsProfileText2,
			physicsProfileText3;
	Quad background;
	ProfileLine gameProfileLine0, gameProfileLine1, gameProfileLine2,
			physicsProfileLine0, physicsProfileLine1, physicsProfileLine2,
			physicsProfileLine3;

	public Profiler(InputManager i, Font f, GameProfiler gameprofiler,
			PhysicsProfiler physicsprofiler) {
		this.gameprofiler = gameprofiler;
		this.physicsprofiler = physicsprofiler;
		setupEvents(i);
		init(f);
	}

	public Profiler(InputManager i, Font f, GameProfiler gameprofiler) {
		this.gameprofiler = gameprofiler;
		setupEvents(i);
		init(f);
	}

	public Profiler(InputManager i, Font f, PhysicsProfiler physicsprofiler) {
		this.physicsprofiler = physicsprofiler;
		setupEvents(i);
		init(f);
	}

	private void init(Font f) {
		times = new ArrayList<Long>();
		values = new HashMap<Integer, List<Long>>();
		values.put(0, new ArrayList<Long>());
		values.put(1, new ArrayList<Long>());
		values.put(2, new ArrayList<Long>());
		values.put(3, new ArrayList<Long>());
		values.put(4, new ArrayList<Long>());
		values.put(5, new ArrayList<Long>());
		values.put(6, new ArrayList<Long>());

		gameProfileText0 = new Text("", 10, 485, f);
		gameProfileText1 = new Text("", 10, 500, f);
		gameProfileText2 = new Text("", 10, 515, f);
		physicsProfileText0 = new Text("", 10, 535, f);
		physicsProfileText1 = new Text("", 10, 550, f);
		physicsProfileText2 = new Text("", 10, 565, f);
		physicsProfileText3 = new Text("", 10, 580, f);

		// TODO: Make independent from shader file!
		Shader gameProfileColor0 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert",
						"res/shaders/colorshader.frag"), "color", new Vector4f(
						1f, 0f, 0f, 1f));
		gameProfileText0.setShader(gameProfileColor0);
		gameProfileText1.setShader(new Shader(gameProfileColor0
				.getShaderProgram(), "color", new Vector4f(0f, 1f, 0f, 1f)));
		gameProfileText2.setShader(new Shader(gameProfileColor0
				.getShaderProgram(), "color", new Vector4f(0f, 0f, 1f, 1f)));
		physicsProfileText0.setShader(new Shader(gameProfileColor0
				.getShaderProgram(), "color",
				new Vector4f(0.5f, 0.5f, 0.5f, 1f)));
		physicsProfileText1.setShader(new Shader(physicsProfileText0
				.getShader()));
		physicsProfileText2.setShader(new Shader(physicsProfileText0
				.getShader()));
		physicsProfileText3.setShader(new Shader(physicsProfileText0
				.getShader()));

		background = new Quad(205, 540, 200, 55);
		background.setShader(new Shader(gameProfileColor0.getShaderProgram(),
				"color", new Vector4f(1f, 1f, 1f, 0.5f)));

		gameProfileLine0 = new ProfileLine(gameProfileText0.getShader());
		gameProfileLine1 = new ProfileLine(gameProfileText1.getShader());
		gameProfileLine2 = new ProfileLine(gameProfileText2.getShader());
		physicsProfileLine0 = new ProfileLine(physicsProfileText0.getShader());
		physicsProfileLine1 = new ProfileLine(physicsProfileText1.getShader());
		physicsProfileLine2 = new ProfileLine(physicsProfileText2.getShader());
		physicsProfileLine3 = new ProfileLine(physicsProfileText3.getShader());
	}

	public void setGameProfiler(GameProfiler gameprofiler) {
		this.gameprofiler = gameprofiler;
	}

	public void setPhysicsProfiler(PhysicsProfiler physicsprofiler) {
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
		if (showScale) {
			background.render();
		}
		if (gameprofiler != null && showGameProfile) {
			gameProfileText0.setText("Update: "
					+ values.get(0).get(values.get(0).size() - 1));
			gameProfileText1.setText("Render 3d: "
					+ values.get(1).get(values.get(1).size() - 1));
			gameProfileText2.setText("Render 2d: "
					+ values.get(2).get(values.get(2).size() - 1));
			gameProfileText0.render();
			gameProfileText1.render();
			gameProfileText2.render();
			gameProfileLine0.render();
			gameProfileLine1.render();
			gameProfileLine2.render();
		}
		if (physicsprofiler != null && showPhysicsProfile) {
			physicsProfileText0.setText("Broadphase: "
					+ values.get(3).get(values.get(3).size() - 1));
			physicsProfileText1.setText("Narrowphase: "
					+ values.get(4).get(values.get(4).size() - 1));
			physicsProfileText2.setText("Resolution: "
					+ values.get(5).get(values.get(5).size() - 1));
			physicsProfileText3.setText("Integration: "
					+ values.get(6).get(values.get(6).size() - 1));
			physicsProfileText0.render();
			physicsProfileText1.render();
			physicsProfileText2.render();
			physicsProfileText3.render();
			physicsProfileLine0.render();
			physicsProfileLine1.render();
			physicsProfileLine2.render();
			physicsProfileLine3.render();
		}
	}

	public void setShowScale(boolean s) {
		showScale = s;
	}

	public void setShowGameProfile(boolean s) {
		showGameProfile = s;
	}

	public void setShowPhysicsProfile(boolean s) {
		showPhysicsProfile = s;
	}

	public void toggleScale() {
		setShowScale(!showScale);
	}

	public void toggleGameProfile() {
		setShowGameProfile(!showGameProfile);
	}

	public void togglePhysicsProfile() {
		setShowPhysicsProfile(!showPhysicsProfile);
	}

	@Override
	public void update(int delta) {
		if (toggleScale.isActive())
			toggleScale();
		if (toggleGameProfile.isActive())
			toggleGameProfile();
		if (togglePhysicsProfile.isActive())
			togglePhysicsProfile();

		System.out.println(times.size());
		if (gameprofiler != null) {
			long[] profilevalues = gameprofiler.getValues();
			times.add(profilevalues[0]);
			for (int i = 0; i < 3; i++) {
				values.get(i).add(profilevalues[i + 1]);
			}
			gameProfileLine0.addValue(profilevalues[1]);
			gameProfileLine1.addValue(profilevalues[2]);
			gameProfileLine2.addValue(profilevalues[3]);
		}
		if (physicsprofiler != null) {
			long[] profilevalues = physicsprofiler.getValues();
			if (gameprofiler == null)
				times.add(profilevalues[0]);
			for (int i = 0; i < 4; i++) {
				values.get(i + 3).add(profilevalues[i + 1]);
			}
			physicsProfileLine0.addValue(profilevalues[1]);
			physicsProfileLine1.addValue(profilevalues[2]);
			physicsProfileLine2.addValue(profilevalues[3]);
			physicsProfileLine3.addValue(profilevalues[4]);
		}

		long time = times.get(times.size() - 1);
		for (int i = 0; i < times.size() - 1; i++) {
			if (time - times.get(i) > storeValues) {
				times.remove(i);
				if (gameprofiler != null) {
					values.get(0).remove(i);
					values.get(1).remove(i);
					values.get(2).remove(i);
					gameProfileLine0.removeLastValue();
					gameProfileLine1.removeLastValue();
					gameProfileLine2.removeLastValue();
				}
				if (physicsprofiler != null) {
					values.get(3).remove(i);
					values.get(4).remove(i);
					values.get(5).remove(i);
					values.get(6).remove(i);
					physicsProfileLine0.removeLastValue();
					physicsProfileLine1.removeLastValue();
					physicsProfileLine2.removeLastValue();
					physicsProfileLine3.removeLastValue();
				}
			} else {
				break;
			}
		}

		if (gameprofiler != null) {
			System.out.println("a");
			gameProfileLine0.prerender();
			gameProfileLine1.prerender();
			gameProfileLine2.prerender();
		}
		if (physicsprofiler != null) {
			physicsProfileLine0.prerender();
			physicsProfileLine1.prerender();
			physicsProfileLine2.prerender();
			physicsProfileLine3.prerender();
		}
	}

	private class ProfileLine extends ShapedObject2 {
		public ProfileLine(Shader s) {
			setShader(s);
			setRenderMode(GLConstants.LINE_STRIP);
		}

		public void addValue(long value) {
			if (value > maxvalue)
				maxvalue = value;
			addIndex(getVertexCount());
			addVertex(new Vector2f(200, 600 - value));
			for (Vector3f v : getVertices()) {
				v.translate(1, 0, 0);
			}
		}

		public void removeLastValue() {
			removeVertex(getVertexCount() - 1);
			removeIndex(getVertexCount());
		}
	}
}
