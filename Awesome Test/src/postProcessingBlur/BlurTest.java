package postProcessingBlur;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import shader.PostProcessingShader;
import shader.Shader;
import sound.NullSoundEnvironment;
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
				new VideoSettings(), new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile(
						"res/shaders/defaultshader.vert",
						"res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
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

		addPostProcessingShader(new PostProcessingShader(blurPPShader, 20));

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
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
