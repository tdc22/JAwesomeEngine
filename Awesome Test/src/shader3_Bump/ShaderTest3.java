package shader3_Bump;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape.Box;
import texture.Texture;
import vector.Vector3f;
import vector.Vector4f;

public class ShaderTest3 extends StandardGame {
	Texture texture, diffuse, bumpmap;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		// Shader Test 1
		Shader colorshader = new Shader(ShaderLoader.loadShader(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		Box a = new Box(-2, 0, 2, 0.5f, 0.5f, 0.5f);
		a.setShader(colorshader);
		addObject(a);

		// Shader Test 2
		Texture texture = new Texture(
				TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(ShaderLoader.loadShader(
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

		Shader bumpmapshader = new Shader(ShaderLoader.loadShader(
				"res/shaders/bumpmapshader.vert",
				"res/shaders/bumpmapshader.frag"));
		bumpmapshader.addArgumentNames("diffuseTexture", "normalTexture",
				"lightPosition");
		bumpmapshader.addArguments(diffuse, bumpmap, new Vector3f(0, 1, 0));

		Box c = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		c.setRenderHints(false, true, false);
		c.setShader(bumpmapshader);
		addObject(c);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
		cam.update(delta);
	}
}