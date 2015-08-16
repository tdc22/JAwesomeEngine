package utils;

import game.StandardGame;
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
import objects.Updateable;
import shader.Shader;
import shape2d.Quad;
import space.PhysicsProfiler;
import vector.Vector4f;

@Deprecated
public class Profiler implements Updateable {
	GameProfiler gameprofiler;
	PhysicsProfiler physicsprofiler;
	int numvalues = 455;
	float maxvalue = 0;
	List<Long> times;
	HashMap<Integer, List<Long>> values;
	boolean showScale = false;
	boolean showGameProfile = false;
	boolean showPhysicsProfile = false;
	InputEvent toggleScale, toggleGameProfile, togglePhysicsProfile;
	Text gameProfileText0, gameProfileText1, gameProfileText2,
			gameProfileText3, physicsProfileText0, physicsProfileText1,
			physicsProfileText2, physicsProfileText3, scaleMin, scaleMid,
			scaleMax;
	Shader backgroundshader, colorShader0, colorShader1, colorShader2,
			colorShader3, colorShader4, colorShader5, colorShader6,
			colorShader7;
	// ProfileLine gameProfileLine0, gameProfileLine1, gameProfileLine2,
	// gameProfileLine3, physicsProfileLine0,
	// physicsProfileLine1, physicsProfileLine2, physicsProfileLine3;
	float sizeX = 600, sizeY = 128;

	public Profiler(StandardGame game, InputManager i, Font f,
			GameProfiler gameprofiler, PhysicsProfiler physicsprofiler) {
		this.gameprofiler = gameprofiler;
		this.physicsprofiler = physicsprofiler;
		setupEvents(i);
		init(game, f);
	}

	public Profiler(StandardGame game, InputManager i, Font f,
			GameProfiler gameprofiler) {
		this.gameprofiler = gameprofiler;
		setupEvents(i);
		init(game, f);
	}

	public Profiler(StandardGame game, InputManager i, Font f,
			PhysicsProfiler physicsprofiler) {
		this.physicsprofiler = physicsprofiler;
		setupEvents(i);
		init(game, f);
	}

	private void init(StandardGame game, Font f) {
		times = new ArrayList<Long>();
		values = new HashMap<Integer, List<Long>>();
		values.put(0, new ArrayList<Long>());
		values.put(1, new ArrayList<Long>());
		values.put(2, new ArrayList<Long>());
		values.put(3, new ArrayList<Long>());
		values.put(4, new ArrayList<Long>());
		values.put(5, new ArrayList<Long>());
		values.put(6, new ArrayList<Long>());
		values.put(7, new ArrayList<Long>());

		gameProfileText0 = new Text("", 10, 470, f);
		gameProfileText1 = new Text("", 10, 485, f);
		gameProfileText2 = new Text("", 10, 500, f);
		gameProfileText3 = new Text("", 10, 515, f);
		physicsProfileText0 = new Text("", 10, 535, f);
		physicsProfileText1 = new Text("", 10, 550, f);
		physicsProfileText2 = new Text("", 10, 565, f);
		physicsProfileText3 = new Text("", 10, 580, f);

		String c = "u_color";
		colorShader0 = new Shader(ShaderLoader.loadShader(
				DefaultShader.COLOR_SHADER_VERTEX,
				DefaultShader.COLOR_SHADER_FRAGMENT), c, new Vector4f(1f, 0f,
				0f, 1f));

		colorShader1 = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(0f, 0f, 1f, 1f));
		colorShader2 = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(1f, 1f, 1f, 1f));
		colorShader3 = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(1f, 1f, 0f, 1f));
		colorShader4 = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(0f, 1f, 1f, 1f));
		colorShader5 = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(1f, 0f, 1f, 1f));
		colorShader6 = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(0.5f, 0.5f, 0.5f, 1f));
		colorShader7 = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(0.5f, 0.5f, 0.5f, 1f));

		game.add2dShader(colorShader0);
		game.add2dShader(colorShader1);
		game.add2dShader(colorShader2);
		game.add2dShader(colorShader3);
		game.add2dShader(colorShader4);
		game.add2dShader(colorShader5);
		game.add2dShader(colorShader6);
		game.add2dShader(colorShader7);

		colorShader0.addObject(gameProfileText0);
		colorShader1.addObject(gameProfileText1);
		colorShader2.addObject(gameProfileText2);
		colorShader3.addObject(gameProfileText3);
		colorShader4.addObject(physicsProfileText0);
		colorShader5.addObject(physicsProfileText1);
		colorShader6.addObject(physicsProfileText2);
		colorShader7.addObject(physicsProfileText3);

		Quad background = new Quad(305, 533, sizeX / 2f, sizeY / 2f);
		backgroundshader = new Shader(colorShader0.getShaderProgram(), c,
				new Vector4f(1f, 1f, 1f, 0.6f));
		backgroundshader.addObject(background);
		game.add2dShader(backgroundshader);

		scaleMin = new Text("0", 610, 580, f);
		scaleMid = new Text("0", 610, 525, f);
		scaleMax = new Text("0", 610, 470, f);

		// gameProfileLine0 = new ProfileLine();
		// gameProfileLine1 = new ProfileLine();
		// gameProfileLine2 = new ProfileLine();
		// gameProfileLine3 = new ProfileLine();
		// physicsProfileLine0 = new ProfileLine();
		// physicsProfileLine1 = new ProfileLine();
		// physicsProfileLine2 = new ProfileLine();
		// physicsProfileLine3 = new ProfileLine();
		//
		// colorShader0.addObject(gameProfileLine0);
		// colorShader1.addObject(gameProfileLine1);
		// colorShader2.addObject(gameProfileLine2);
		// colorShader3.addObject(gameProfileLine3);
		// colorShader4.addObject(physicsProfileLine0);
		// colorShader5.addObject(physicsProfileLine1);
		// colorShader6.addObject(physicsProfileLine2);
		// colorShader7.addObject(physicsProfileLine3);

		backgroundshader.setRendered(false);
		colorShader0.setRendered(false);
		colorShader1.setRendered(false);
		colorShader2.setRendered(false);
		colorShader3.setRendered(false);
		colorShader4.setRendered(false);
		colorShader5.setRendered(false);
		colorShader6.setRendered(false);
		colorShader7.setRendered(false);
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

	// public void render2d() {
	// if (showScale) {
	// background.render();
	// scaleMin.render();
	// scaleMid.render();
	// scaleMax.render();
	// }
	// if (gameprofiler != null && showGameProfile) {
	// gameProfileText0.render();
	// gameProfileText1.render();
	// gameProfileText2.render();
	// gameProfileText3.render();
	// if (showScale) {
	// gameProfileLine0.render();
	// gameProfileLine1.render();
	// gameProfileLine2.render();
	// gameProfileLine3.render();
	// }
	// }
	// if (physicsprofiler != null && showPhysicsProfile) {
	// physicsProfileText0.render();
	// physicsProfileText1.render();
	// physicsProfileText2.render();
	// physicsProfileText3.render();
	// if (showScale) {
	// physicsProfileLine0.render();
	// physicsProfileLine1.render();
	// physicsProfileLine2.render();
	// physicsProfileLine3.render();
	// }
	// }
	// }

	public void setShowScale(boolean s) {
		showScale = s;
		backgroundshader.setRendered(s);
		if (gameprofiler != null && showGameProfile) {
			colorShader0.setRendered(s);
			colorShader1.setRendered(s);
			colorShader2.setRendered(s);
			colorShader3.setRendered(s);
		}
		if (physicsprofiler != null && showPhysicsProfile) {
			colorShader4.setRendered(s);
			colorShader5.setRendered(s);
			colorShader6.setRendered(s);
			colorShader7.setRendered(s);
		}
	}

	public void setShowGameProfile(boolean s) {
		showGameProfile = s;
		if (showScale) {
			colorShader0.setRendered(s);
			colorShader1.setRendered(s);
			colorShader2.setRendered(s);
			colorShader3.setRendered(s);
		}
	}

	public void setShowPhysicsProfile(boolean s) {
		showPhysicsProfile = s;
		if (showScale) {
			colorShader4.setRendered(s);
			colorShader5.setRendered(s);
			colorShader6.setRendered(s);
			colorShader7.setRendered(s);
		}
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

		if (gameprofiler != null) {
			long[] profilevalues = gameprofiler.getValues();
			times.add(profilevalues[0]);
			for (int i = 0; i < 4; i++) {
				values.get(i).add(profilevalues[i + 1]);
			}
			// gameProfileLine0.addValue(profilevalues[1]);
			// gameProfileLine1.addValue(profilevalues[2]);
			// gameProfileLine2.addValue(profilevalues[3]);
			// gameProfileLine3.addValue(profilevalues[4]);

			gameProfileText0.setText("Update: "
					+ values.get(0).get(values.get(0).size() - 1));
			gameProfileText1.setText("Render 3d: "
					+ values.get(1).get(values.get(1).size() - 1));
			gameProfileText2.setText("Render 2d: "
					+ values.get(2).get(values.get(2).size() - 1));
			gameProfileText3.setText("Display: "
					+ values.get(3).get(values.get(3).size() - 1));
		}
		if (physicsprofiler != null) {
			long[] profilevalues = physicsprofiler.getValues();
			if (gameprofiler == null)
				times.add(profilevalues[0]);
			for (int i = 0; i < 4; i++) {
				values.get(i + 4).add(profilevalues[i + 1]);
			}
			// physicsProfileLine0.addValue(profilevalues[1]);
			// physicsProfileLine1.addValue(profilevalues[2]);
			// physicsProfileLine2.addValue(profilevalues[3]);
			// physicsProfileLine3.addValue(profilevalues[4]);

			physicsProfileText0.setText("Broadphase: "
					+ values.get(4).get(values.get(4).size() - 1));
			physicsProfileText1.setText("Narrowphase: "
					+ values.get(5).get(values.get(5).size() - 1));
			physicsProfileText2.setText("Resolution: "
					+ values.get(6).get(values.get(6).size() - 1));
			physicsProfileText3.setText("Integration: "
					+ values.get(7).get(values.get(7).size() - 1));
		}

		if (times.size() > numvalues) {
			times.remove(numvalues);
			if (gameprofiler != null) {
				// gameProfileLine0.removeLastValue();
				// gameProfileLine1.removeLastValue();
				// gameProfileLine2.removeLastValue();
				// gameProfileLine3.removeLastValue();
				boolean rm1 = values.get(0).remove(0) == maxvalue;
				boolean rm2 = values.get(1).remove(0) == maxvalue;
				boolean rm3 = values.get(2).remove(0) == maxvalue;
				boolean rm4 = values.get(3).remove(0) == maxvalue;
				if (rm1 || rm2 || rm3 || rm4) {
					maxvalue = findNextMax();
					scaleMid.setText(maxvalue / 2000f + " ms");
					scaleMax.setText(maxvalue / 1000f + " ms");
					scaleProfileLines(maxvalue);
				}
			}
			if (physicsprofiler != null) {
				// physicsProfileLine0.removeLastValue();
				// physicsProfileLine1.removeLastValue();
				// physicsProfileLine2.removeLastValue();
				// physicsProfileLine3.removeLastValue();
				boolean rm1 = values.get(4).remove(0) == maxvalue;
				boolean rm2 = values.get(5).remove(0) == maxvalue;
				boolean rm3 = values.get(6).remove(0) == maxvalue;
				boolean rm4 = values.get(7).remove(0) == maxvalue;
				if (rm1 || rm2 || rm3 || rm4) {
					maxvalue = findNextMax();
					scaleMid.setText(maxvalue / 2000f + " ms");
					scaleMax.setText(maxvalue / 1000f + " ms");
					scaleProfileLines(maxvalue);
				}
			}
		}

		if (gameprofiler != null && showScale && showGameProfile) {
			// gameProfileLine0.prerender();
			// gameProfileLine1.prerender();
			// gameProfileLine2.prerender();
			// gameProfileLine3.prerender();
		}
		if (physicsprofiler != null && showScale && showPhysicsProfile) {
			// physicsProfileLine0.prerender();
			// physicsProfileLine1.prerender();
			// physicsProfileLine2.prerender();
			// physicsProfileLine3.prerender();
		}
	}

	private Long findNextMax() {
		Long max = 0L;
		for (List<Long> list : values.values()) {
			for (Long l : list) {
				if (l > max) {
					max = l;
				}
			}
		}
		return max;
	}

	private void scaleProfileLines(float scale) {
		scale = sizeY / scale;
		// gameProfileLine0.scaleProfile(scale);
		// gameProfileLine1.scaleProfile(scale);
		// gameProfileLine2.scaleProfile(scale);
		// gameProfileLine3.scaleProfile(scale);
		// physicsProfileLine0.scaleProfile(scale);
		// physicsProfileLine1.scaleProfile(scale);
		// physicsProfileLine2.scaleProfile(scale);
		// physicsProfileLine3.scaleProfile(scale);
	}

	// private class ProfileLine extends ShapedObject2 {
	// public ProfileLine() {
	// setRenderMode(GLConstants.LINE_STRIP);
	// translateTo(new Vector2f(150, 595));
	// }
	//
	// public void addValue(long value) {
	// if (value > maxvalue) {
	// maxvalue = value;
	// scaleMid.setText(maxvalue / 2000f + " ms");
	// scaleMax.setText(maxvalue / 1000f + " ms");
	// scaleProfileLines(maxvalue);
	// }
	// for (Vector3f v : getVertices()) {
	// v.translate(1, 0, 0);
	// }
	// addIndex(getVertexCount());
	// addVertex(new Vector2f(0, -value));
	// }
	//
	// public void scaleProfile(float scale) {
	// scaleTo(1, scale, 1);
	// }
	//
	// public void removeLastValue() {
	// removeVertex(0);
	// removeIndex(getVertexCount());
	// }
	// }
}
