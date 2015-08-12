package postProcessingDepth;

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
import shader.Shader;
import shape.Box;
import texture.Texture;
import utils.Debugger;

public class DepthTest extends StandardGame {
	Debugger debugger;
	InputEvent toggleMouseBind, toggleDepthPP;
	Shader depthPPShader;
	boolean depthPPActive;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat().withSamples(0), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		debugger = new Debugger(inputs, defaultshader, defaultshader2, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"),
				cam);

		depthPPShader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/ppDepthshader.vert", "res/shaders/ppDepthshader.frag"));
		depthPPShader.addArgumentName("u_texture");
		depthPPShader.addArgument(new Texture());
		depthPPShader.addArgumentName("u_depthTexture");
		depthPPShader.addArgument(new Texture());
		depthPPShader.addArgumentName("u_depthMin");
		depthPPShader.addArgument(settings.getZNear());
		depthPPShader.addArgumentName("u_depthMax");
		depthPPShader.addArgument(settings.getZFar());

		addPostProcessingShader(depthPPShader);
		setPostProcessingIterations(1);
		depthPPActive = true;

		defaultshader.addObject(ModelLoader.load("res/models/bunny.mobj"));
		for (int i = 0; i < 200; i++) {
			defaultshader.addObject(new Box(0, 0, i * 2, 0.5f, 0.5f, 0.5f));
		}

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);

		toggleDepthPP = new InputEvent("toggleDepthPostProcessingShader",
				new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleDepthPP);
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

		if (toggleDepthPP.isActive()) {
			if (depthPPActive)
				removePostProcessingShader(depthPPShader);
			else
				addPostProcessingShader(depthPPShader);
			depthPPActive = !depthPPActive;
			System.out.println("Depth Shader active: " + depthPPActive);
		}
	}

}
