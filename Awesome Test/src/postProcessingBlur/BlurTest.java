package postProcessingBlur;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import shader.Shader;
import texture.Texture;
import utils.Debugger;
import vector.Vector2f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class BlurTest extends StandardGame {
	Debugger debugger;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		debugger = new Debugger(inputs, defaultshader, defaultshader2,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		defaultshader.addObject(ModelLoader.load("res/models/bunny.mobj"));

		Shader blurPPShader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/ppBlurshader.vert",
				"res/shaders/ppBlurshader.frag"));
		blurPPShader.addArgumentName("u_texture");
		blurPPShader.addArgument(new Texture());
		blurPPShader.addArgumentName("u_depthTexture");
		blurPPShader.addArgument(new Texture());
		blurPPShader.addArgumentName("u_shift");
		blurPPShader.addArgument(new Vector2f(0.001f, 0));

		addPostProcessingShader(blurPPShader);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
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
