package lightingTest;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.ShapedObject;
import shader.Shader;
import utils.Debugger;
import vector.Vector3f;

public class LightingTest extends StandardGame {
	Debugger debugger;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);

		debugger = new Debugger(inputs, defaultshader, defaultshader2, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"),
				cam);

		Shader shader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/phongshader.vert", "res/shaders/phongshader.frag"));
		shader.addArgumentName("u_lightpos");
		shader.addArgument(new Vector3f(0, 0, 10));
		shader.addArgumentName("u_ambient");
		shader.addArgument(new Vector3f(0.1f, 0.1f, 0.1f));
		shader.addArgumentName("u_diffuse");
		shader.addArgument(new Vector3f(0.4f, 0.4f, 0.4f));
		shader.addArgumentName("u_shininess");
		shader.addArgument(10f);
		addShader(shader);

		ShapedObject bunny = ModelLoader.load("res/models/bunny.mobj");
		bunny.setRenderHints(false, false, true);
		shader.addObject(bunny);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		debugger.update(fps, 0, 0);
		if (display.isMouseBound())
			cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}

}
