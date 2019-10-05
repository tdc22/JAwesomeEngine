package postProcessingTextures;

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
import objects.ShapedObject3;
import shader.PostProcessingShader;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.Debugger;
import utils.DefaultValues;

public class PostProcessingTest extends StandardGame {
	Debugger debugger;
	InputEvent toggleMouseBind, togglePPTexture;
	PostProcessingShader depthPPShader, normalPPShader;
	int shadermode;

	Shader textureshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);

		layer3d.initLayer(settings.getResolutionX(), settings.getResolutionY(),
				DefaultValues.DEFAULT_PIXELFORMAT_SAMPLES, true, true, true);

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile("res/shaders/defaultshaderNormal.vert",
				"res/shaders/defaultshaderNormal.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		Shader depthShader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/ppDepthshader.vert", "res/shaders/ppDepthshader.frag"));
		depthShader.addArgumentName("u_texture");
		depthShader.addArgument(new Texture());
		depthShader.addArgumentName("u_depthTexture");
		depthShader.addArgument(new Texture());
		depthShader.addArgumentName("u_normalTexture");
		depthShader.addArgument(new Texture());
		depthShader.addArgumentName("u_depthMin");
		depthShader.addArgument(settings.getZNear());
		depthShader.addArgumentName("u_depthMax");
		depthShader.addArgument(settings.getZFar());

		depthPPShader = new PostProcessingShader(depthShader, 1);
		addPostProcessingShader(depthPPShader);

		Shader normalShader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/ppNormalshader.vert", "res/shaders/ppNormalshader.frag"));
		normalShader.addArgumentName("u_texture");
		normalShader.addArgument(new Texture());
		normalShader.addArgumentName("u_depthTexture");
		normalShader.addArgument(new Texture());
		normalShader.addArgumentName("u_normalTexture");
		normalShader.addArgument(new Texture());

		normalPPShader = new PostProcessingShader(normalShader, 1);

		shadermode = 0;

		ShapedObject3 bunny = ModelLoader.load("res/models/bunny.mobj");
		bunny.setRenderHints(true, false, true);
		defaultshader.addObject(bunny);
		for (int i = 0; i < 200; i++) {
			Box b = new Box(0, 0, i * 2, 0.5f, 0.5f, 0.5f);
			b.setRenderHints(true, false, true);
			defaultshader.addObject(b);
		}

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		togglePPTexture = new InputEvent("toggleDepthPostProcessingShader",
				new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(togglePPTexture);
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

		if (togglePPTexture.isActive()) {
			if (shadermode == 0) {
				removePostProcessingShader(depthPPShader);
				addPostProcessingShader(normalPPShader);
			} else if (shadermode == 1) {
				removePostProcessingShader(normalPPShader);
			} else {
				addPostProcessingShader(depthPPShader);
			}
			shadermode++;
			shadermode %= 3;
			System.out.println("Mode active: " + shadermode);
		}
	}

}
