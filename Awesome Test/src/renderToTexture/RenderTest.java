package renderToTexture;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.Camera3;
import objects.ViewProjection;
import shader.Shader;
import shape.Box;
import texture.FramebufferObject;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;

public class RenderTest extends StandardGame {
	FramebufferObject rtt;
	Debugger debugger;

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
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		defaultshader.addObject(ModelLoader.load("res/models/bunny.mobj"));

		rtt = new FramebufferObject((ViewProjection) this, 800, 800, 0, new Camera3(new Vector3f(0, 2, 8), 0, 0));
		rtt.updateTexture();

		Shader screenshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		screenshader.addArgumentName("u_texture");
		screenshader.addArgument(new Texture(rtt.getColorTextureID()));// rtt.getColorTextureID()));
		addShader(screenshader);

		Box screen = new Box(2, 3, 12, 1, 1, 0.1f);
		screen.setRenderHints(false, true, false);
		screenshader.addObject(screen);
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
		rtt.updateTexture();
		debugger.update(fps, 0, 0);
		cam.update(delta);
	}

}
