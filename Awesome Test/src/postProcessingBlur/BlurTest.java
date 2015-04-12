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
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		addObject(ModelLoader.load("res/models/bunny.mobj"));

		Shader blurPPShader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/ppBlurshader.vert",
				"res/shaders/ppBlurshader.frag"));
		blurPPShader.addArgumentName("texture");
		blurPPShader.addArgument(new Texture());
		blurPPShader.addArgumentName("uShift");
		blurPPShader.addArgument(new Vector2f(0.001f, 0));

		addPostProcessingShader(blurPPShader);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugger.update();
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
