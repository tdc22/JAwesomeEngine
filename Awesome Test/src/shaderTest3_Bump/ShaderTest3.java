package shaderTest3_Bump;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.VideoSettings;
import input.Input;
import loader.ShaderLoader;
import loader.TextureLoader;
import shape.Box;
import texture.Texture;
import utils.Shader;
import vector.Vector4f;

public class ShaderTest3 extends StandardGame {
	Texture texture, diffuse, bumpmap;

	@Override
	public void init() {
		setDisplay(new GLDisplay(), new DisplayMode(), new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		// Shader Test 1
		Shader colorshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		Box a = new Box(-2, 0, 2, 0.5f, 0.5f, 0.5f);
		a.setShader(colorshader);
		addObject(a);

		// Shader Test 2
		Texture texture = new Texture(
				TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		textureshader.addArgumentName("colorMap");
		textureshader.addArgument(texture);

		Box b = new Box(2, 0, 2, 0.5f, 0.5f, 0.5f);
		b.setRenderHints(false, true, false);
		b.setShader(textureshader);
		addObject(b);

		// Shader Test 3
		diffuse = new Texture(
				TextureLoader.loadTexture("res/textures/diffuse.jpg"));
		bumpmap = new Texture(
				TextureLoader.loadTexture("res/textures/normal.jpg"));

		Shader bumpmapshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/bumpmapshader.vert",
				"res/shaders/bumpmapshader.frag"));
		bumpmapshader.addArgumentNames("diffuseTexture", "normalTexture");
		bumpmapshader.addArguments(diffuse, bumpmap);

		Box c = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		c.setRenderHints(false, true, false);
		c.setShader(bumpmapshader);
		addObject(c);

		inputs.createInputEvent("toggle Mouse grab").addEventTrigger(
				new Input(Input.KEYBOARD_EVENT, Keyboard.KEY_T,
						KeyEvent.Key_Pressed));
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		renderScene();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub
		if (inputs.isInputEventActive("toggle Mouse grab")) {
			mouse.setGrabbed(!mouse.isGrabbed());
		}
		cam.update(delta);
	}
}
