package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.StandardGame;
import gui.Font;
import gui.Text;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;
import loader.ShaderLoader;
import objects.ShapedObject2;
import objects.Updateable;
import shader.Shader;
import shape2d.Quad;
import space.PhysicsProfiler;
import vector.Vector2f;
import vector.Vector4f;

public class Profiler implements Updateable {
	GameProfiler gameprofiler;
	PhysicsProfiler physicsprofiler;
	final int numvalues = 455;
	long maxvalue = 0;
	List<Long> times;
	HashMap<Integer, List<Long>> values;
	boolean showScale = false;
	boolean showGameProfile = false;
	boolean showPhysicsProfile = false;
	InputEvent toggleScale, toggleGameProfile, togglePhysicsProfile;
	Text gameProfileText0, gameProfileText1, gameProfileText2, gameProfileText3, physicsProfileText0,
			physicsProfileText1, physicsProfileText2, physicsProfileText3, scaleMin, scaleMid, scaleMax;
	Shader backgroundshader, scaleshader, colorShader0, colorShader1, colorShader2, colorShader3, colorShader4,
			colorShader5, colorShader6, colorShader7;
	ProfileLine gameProfileLine0, gameProfileLine1, gameProfileLine2, gameProfileLine3, physicsProfileLine0,
			physicsProfileLine1, physicsProfileLine2, physicsProfileLine3;
	float sizeX = 600, sizeY = 128;

	public Profiler(StandardGame game, InputManager i, Font f, GameProfiler gameprofiler,
			PhysicsProfiler physicsprofiler) {
		this.gameprofiler = gameprofiler;
		this.physicsprofiler = physicsprofiler;
		setupEvents(i);
		init(game, f);
	}

	public Profiler(StandardGame game, InputManager i, Font f, GameProfiler gameprofiler) {
		this.gameprofiler = gameprofiler;
		setupEvents(i);
		init(game, f);
	}

	public Profiler(StandardGame game, InputManager i, Font f, PhysicsProfiler physicsprofiler) {
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

		String c = "u_color";
		int colorShaderID = ShaderLoader.loadShader(DefaultShader.COLOR_SHADER_VERTEX,
				DefaultShader.COLOR_SHADER_FRAGMENT);

		Quad background = new Quad(305, 533, sizeX / 2f, sizeY / 2f);
		backgroundshader = new Shader(colorShaderID, c, new Vector4f(1f, 1f, 1f, 0.5f));
		backgroundshader.addObject(background);
		game.addShaderInterface(backgroundshader);

		gameProfileText0 = new Text("", 10, 480, f);
		gameProfileText1 = new Text("", 10, 495, f);
		gameProfileText2 = new Text("", 10, 510, f);
		gameProfileText3 = new Text("", 10, 525, f);
		physicsProfileText0 = new Text("", 10, 545, f);
		physicsProfileText1 = new Text("", 10, 560, f);
		physicsProfileText2 = new Text("", 10, 575, f);
		physicsProfileText3 = new Text("", 10, 590, f);

		colorShader0 = new Shader(colorShaderID, c, new Vector4f(1f, 0f, 0f, 1f));
		colorShader1 = new Shader(colorShaderID, c, new Vector4f(0f, 1f, 0f, 1f));
		colorShader2 = new Shader(colorShaderID, c, new Vector4f(0f, 0f, 1f, 1f));
		colorShader3 = new Shader(colorShaderID, c, new Vector4f(1f, 1f, 0f, 1f));
		colorShader4 = new Shader(colorShaderID, c, new Vector4f(0f, 1f, 1f, 1f));
		colorShader5 = new Shader(colorShaderID, c, new Vector4f(1f, 0f, 1f, 1f));
		colorShader6 = new Shader(colorShaderID, c, new Vector4f(1f, 1f, 1f, 1f));
		colorShader7 = new Shader(colorShaderID, c, new Vector4f(0.5f, 0.5f, 0.5f, 1f));

		game.addShaderInterface(colorShader0);
		game.addShaderInterface(colorShader1);
		game.addShaderInterface(colorShader2);
		game.addShaderInterface(colorShader3);
		game.addShaderInterface(colorShader4);
		game.addShaderInterface(colorShader5);
		game.addShaderInterface(colorShader6);
		game.addShaderInterface(colorShader7);

		colorShader0.addObject(gameProfileText0);
		colorShader1.addObject(gameProfileText1);
		colorShader2.addObject(gameProfileText2);
		colorShader3.addObject(gameProfileText3);
		colorShader4.addObject(physicsProfileText0);
		colorShader5.addObject(physicsProfileText1);
		colorShader6.addObject(physicsProfileText2);
		colorShader7.addObject(physicsProfileText3);

		scaleMin = new Text("0", 610, 595, f);
		scaleMid = new Text("0", 610, 535, f);
		scaleMax = new Text("0", 610, 475, f);

		scaleshader = new Shader(colorShaderID, c, new Vector4f(1f, 1f, 1f, 1f));
		scaleshader.addObject(scaleMin);
		scaleshader.addObject(scaleMid);
		scaleshader.addObject(scaleMax);
		scaleshader.setRendered(false);
		game.addShaderInterface(scaleshader);

		gameProfileLine0 = new ProfileLine();
		gameProfileLine1 = new ProfileLine();
		gameProfileLine2 = new ProfileLine();
		gameProfileLine3 = new ProfileLine();
		physicsProfileLine0 = new ProfileLine();
		physicsProfileLine1 = new ProfileLine();
		physicsProfileLine2 = new ProfileLine();
		physicsProfileLine3 = new ProfileLine();

		colorShader0.addObject(gameProfileLine0);
		colorShader1.addObject(gameProfileLine1);
		colorShader2.addObject(gameProfileLine2);
		colorShader3.addObject(gameProfileLine3);
		colorShader4.addObject(physicsProfileLine0);
		colorShader5.addObject(physicsProfileLine1);
		colorShader6.addObject(physicsProfileLine2);
		colorShader7.addObject(physicsProfileLine3);

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
		toggleScale = new InputEvent("profile_showscale", new Input(Input.KEYBOARD_EVENT, "F9", KeyInput.KEY_PRESSED));
		toggleGameProfile = new InputEvent("profile_showgameprofile",
				new Input(Input.KEYBOARD_EVENT, "F10", KeyInput.KEY_PRESSED));
		togglePhysicsProfile = new InputEvent("profile_showphysicsprofile",
				new Input(Input.KEYBOARD_EVENT, "F11", KeyInput.KEY_PRESSED));

		inputs.addEvent(toggleScale);
		inputs.addEvent(toggleGameProfile);
		inputs.addEvent(togglePhysicsProfile);
	}

	public void setShowScale(boolean s) {
		showScale = s;
		backgroundshader.setRendered(s);
		scaleshader.setRendered(s);
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

	private final StringBuilder stringbuilder = new StringBuilder();

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
			if (times.size() > numvalues) {
				times.remove(0);
				values.get(0).remove(0);
				values.get(1).remove(0);
				values.get(2).remove(0);
				values.get(3).remove(0);
			}
			gameProfileLine0.addValue(profilevalues[1]);
			gameProfileLine1.addValue(profilevalues[2]);
			gameProfileLine2.addValue(profilevalues[3]);
			gameProfileLine3.addValue(profilevalues[4]);

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_UPDATE);
			stringbuilder.append(profilevalues[1]);
			gameProfileText0.setText(stringbuilder.toString());

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_RENDER_3D);
			stringbuilder.append(profilevalues[2]);
			gameProfileText1.setText(stringbuilder.toString());

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_RENDER_2D);
			stringbuilder.append(profilevalues[3]);
			gameProfileText2.setText(stringbuilder.toString());

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_DISPLAY);
			stringbuilder.append(profilevalues[4]);
			gameProfileText3.setText(stringbuilder.toString());
		}
		if (physicsprofiler != null) {
			long[] profilevalues = physicsprofiler.getValues();
			if (gameprofiler == null)
				times.add(profilevalues[0]);
			for (int i = 0; i < 4; i++) {
				values.get(i + 4).add(profilevalues[i + 1]);
			}
			if (times.size() > numvalues) {
				if (gameprofiler == null)
					times.remove(0);
				values.get(4).remove(0);
				values.get(5).remove(0);
				values.get(6).remove(0);
				values.get(7).remove(0);
			}
			physicsProfileLine0.addValue(profilevalues[1]);
			physicsProfileLine1.addValue(profilevalues[2]);
			physicsProfileLine2.addValue(profilevalues[3]);
			physicsProfileLine3.addValue(profilevalues[4]);

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_BROADPHASE);
			stringbuilder.append(profilevalues[1]);
			physicsProfileText0.setText(stringbuilder.toString());

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_NARROWPHASE);
			stringbuilder.append(profilevalues[2]);
			physicsProfileText1.setText(stringbuilder.toString());

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_RESOLUTION);
			stringbuilder.append(profilevalues[3]);
			physicsProfileText2.setText(stringbuilder.toString());

			stringbuilder.setLength(0);
			stringbuilder.append(StringConstants.PROFILER_INTEGRATION);
			stringbuilder.append(profilevalues[4]);
			physicsProfileText3.setText(stringbuilder.toString());
		}

		if (gameprofiler != null && showScale && showGameProfile) {
			gameProfileLine0.prerender();
			gameProfileLine1.prerender();
			gameProfileLine2.prerender();
			gameProfileLine3.prerender();
		}
		if (physicsprofiler != null && showScale && showPhysicsProfile) {
			physicsProfileLine0.prerender();
			physicsProfileLine1.prerender();
			physicsProfileLine2.prerender();
			physicsProfileLine3.prerender();
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
		scale = -sizeY / scale;
		gameProfileLine0.scaleProfile(scale);
		gameProfileLine1.scaleProfile(scale);
		gameProfileLine2.scaleProfile(scale);
		gameProfileLine3.scaleProfile(scale);
		physicsProfileLine0.scaleProfile(scale);
		physicsProfileLine1.scaleProfile(scale);
		physicsProfileLine2.scaleProfile(scale);
		physicsProfileLine3.scaleProfile(scale);
	}

	private class ProfileLine extends ShapedObject2 {
		public ProfileLine() {
			setRenderMode(GLConstants.LINE_STRIP);
			translateTo(new Vector2f(150, 595));
		}

		public void addValue(long value) {
			if (value > maxvalue) {
				maxvalue = value;
				scaleMid.setText(maxvalue / 2000f + " ms");
				scaleMax.setText(maxvalue / 1000f + " ms");
				scaleProfileLines(maxvalue);
			}
			for (Vector2f v : getVertices()) {
				v.translate(1, 0);
			}

			if (getIndexCount() == numvalues) {
				Vector2f vert = vertices.get(0);
				if (vert.y >= maxvalue) {
					maxvalue = findNextMax();
					scaleMid.setText(maxvalue / 2000f + " ms");
					scaleMax.setText(maxvalue / 1000f + " ms");
					scaleProfileLines(maxvalue);
				}
				removeVertex(0);
				vert.set(0, value);
				addVertex(vert);
			} else {
				addIndex(getVertexCount());
				addVertex(new Vector2f(0, value));
			}
		}

		public void scaleProfile(float scale) {
			scaleTo(1, scale);
		}
	}
}
