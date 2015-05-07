package renderToTexture;

import game.StandardGame;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import objects.Camera;
import shader.Shader;
import shape.Box;
import texture.FramebufferObject;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class RenderTest extends StandardGame {
	FramebufferObject rtt;
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		addObject(ModelLoader.load("res/models/bunny.mobj"));

		rtt = new FramebufferObject(this, 1024, 1024, 0, new Camera(
				new Vector3f(0, 0, 12), 0, 0));
		rtt.updateTexture();

		Shader screenshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		screenshader.addArgumentName("texture");
		screenshader.addArgument(new Texture(rtt.getColorTextureID())); // alternative:
																		// framebuffer.getTextureID()

		Box screen = new Box(2, 3, 12, 2, 1, 0.1f);
		screen.setRenderHints(false, true, false);
		screen.setShader(screenshader);
		addObject(screen);
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		rtt.updateTexture();
		debugger.update();
		cam.update(delta);
	}

}
