package shader3_Bump;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape.Box;
import texture.Texture;
import vector.Vector4f;

public class ShaderTest3 extends StandardGame {
	Texture texture, diffuse, bumpmap;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		// Shader Test 1
		Shader colorshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("u_color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(colorshader);

		Box a = new Box(-2, 0, 2, 0.5f, 0.5f, 0.5f);
		colorshader.addObject(a);

		// Shader Test 2
		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/stone.png"));

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		Box b = new Box(2, 0, 2, 0.5f, 0.5f, 0.5f);
		b.setRenderHints(false, true, false);
		textureshader.addObject(b);

		// Shader Test 3
		diffuse = new Texture(TextureLoader.loadTexture("res/textures/stone.png"));
		bumpmap = new Texture(TextureLoader.loadTexture("res/textures/stone_normal.png"));

		Shader bumpmapshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/bumpmapshader.vert", "res/shaders/bumpmapshader.frag"));
		bumpmapshader.addArgumentNames("u_texture", "u_bumpmap");
		bumpmapshader.addArguments(diffuse, bumpmap);
		addShader(bumpmapshader);

		Box c = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		c.setRenderHints(false, true, false);
		bumpmapshader.addObject(c);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
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
