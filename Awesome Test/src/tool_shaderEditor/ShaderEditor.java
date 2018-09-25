package tool_shaderEditor;

import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FileLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.ShapedObject3;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import sound.NullSoundEnvironment;

public class ShaderEditor extends StandardGame {
	ShapedObject3 box, sphere, bunny;
	InputEvent showBox, showSphere, showBunny;
	Shader shader;
	JTextArea vertexShaderSource, fragmentShaderSource;
	boolean recompileShaders;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(false);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);

		String vertexSource = null, fragmentSource = null;
		try {
			vertexSource = FileLoader.readFile("res/shaders/defaultshader.vert");
			fragmentSource = FileLoader.readFile("res/shaders/defaultshader.frag");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		shader = new Shader(ShaderLoader.loadShader(vertexSource, fragmentSource));
		addShader(shader);

		box = new Box(0, 0, 0, 1, 1, 1);
		sphere = new Sphere(0, 0, 0, 1, 36, 36);
		bunny = ModelLoader.load("res/models/bunny.mobj");
		bunny.scale(0.25f);
		shader.addObject(box);
		shader.addObject(sphere);
		shader.addObject(bunny);

		sphere.setRendered(false);
		bunny.setRendered(false);

		showBox = new InputEvent("ShowBox", new Input(Input.KEYBOARD_EVENT, "1", KeyInput.KEY_PRESSED));
		showSphere = new InputEvent("ShowSphere", new Input(Input.KEYBOARD_EVENT, "2", KeyInput.KEY_PRESSED));
		showBunny = new InputEvent("ShowBunny", new Input(Input.KEYBOARD_EVENT, "3", KeyInput.KEY_PRESSED));
		inputs.addEvent(showBox);
		inputs.addEvent(showSphere);
		inputs.addEvent(showBunny);

		initShaderEditor(vertexSource, fragmentSource);
	}

	private void initShaderEditor(String vertexSource, String fragmentSource) {
		vertexShaderSource = new JTextArea(vertexSource);
		fragmentShaderSource = new JTextArea(fragmentSource);

		DocumentListener shaderSourceChanged = new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				recompileShaders = true;
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				recompileShaders = true;
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				recompileShaders = true;
			}
		};

		vertexShaderSource.getDocument().addDocumentListener(shaderSourceChanged);
		fragmentShaderSource.getDocument().addDocumentListener(shaderSourceChanged);

		JFrame vertframe = new JFrame();
		vertframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vertframe.setSize(500, 400);
		vertframe.add(vertexShaderSource);

		JFrame fragframe = new JFrame();
		fragframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fragframe.setSize(500, 400);
		fragframe.add(fragmentShaderSource);

		vertframe.setVisible(true);
		fragframe.setVisible(true);
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {

	}

	private void toggleObjectVisibility(boolean showBox, boolean showSphere, boolean showBunny) {
		box.setRendered(showBox);
		sphere.setRendered(showSphere);
		bunny.setRendered(showBunny);
	}

	private void compileShader(String vertSource, String fragSource) {
		Shader newshader = new Shader(ShaderLoader.loadShader(vertSource, fragSource));
		addShader(newshader);
		newshader.addObject(box);
		newshader.addObject(sphere);
		newshader.addObject(bunny);
		shader.removeObject(box);
		shader.removeObject(sphere);
		shader.removeObject(bunny);
		removeShader(shader);
		shader.delete();
		shader = newshader;
	}

	@Override
	public void update(int delta) {
		if (showBox.isActive()) {
			toggleObjectVisibility(true, false, false);
		}
		if (showSphere.isActive()) {
			toggleObjectVisibility(false, true, false);
		}
		if (showBunny.isActive()) {
			toggleObjectVisibility(false, false, true);
		}
		if (recompileShaders) {
			compileShader(vertexShaderSource.getText(), fragmentShaderSource.getText());
			recompileShaders = false;
		}

		cam.update(delta);
	}
}