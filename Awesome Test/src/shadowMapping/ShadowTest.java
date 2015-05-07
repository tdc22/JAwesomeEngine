package shadowMapping;

import game.StandardGame;
import loader.FontLoader;
import loader.ModelLoader;
import objects.Camera;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import texture.FramebufferObject;
import utils.Debugger;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ShadowTest extends StandardGame {
	Debugger debugger;
	FramebufferObject depthMap;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		addObject(new Box(0, -1, 0, 10, 0.5f, 10));
		addObject(ModelLoader.load("res/models/bunny.mobj"));
		addObject(new Sphere(2, 0, 0, 1, 36, 36));
		addObject(new Cylinder(8, 0, 0, 1, 2, 36));

		depthMap = new FramebufferObject(this, 1024, 1024, 0, new Camera(
				new Vector3f(0, 0, 12), 0, 0), false, true);
		depthMap.updateTexture();

		// Shader screenshader = new Shader(ShaderLoader.loadShaderFromFile(
		// "res/shaders/textureshader.vert",
		// "res/shaders/textureshader.frag"));
		// screenshader.addArgumentName("texture");
		// screenshader.addArgument(new Texture(depthMap.getDepthTextureID()));

		Box screen = new Box(2, 3, 12, 2, 1, 0.1f);
		screen.setRenderHints(false, true, false);
		// screen.setShader(screenshader);
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
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugger.update();
		cam.update(delta);
	}

}
