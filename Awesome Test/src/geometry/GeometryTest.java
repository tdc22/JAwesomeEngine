package geometry;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import shader.Shader;
import shape.Box;
import shape.Capsule;
import shape.Cylinder;
import shape.IsoSphere;
import shape.Sphere;
import utils.Debugger;

public class GeometryTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		defaultshader.addObject(new Box(-1, 0, 0, 1, 1, 1));
		defaultshader.addObject(new Sphere(2, 0, 0, 1, 36, 36));
		defaultshader.addObject(new Capsule(5, 0, 0, 1, 2, 36, 36));
		defaultshader.addObject(new Cylinder(8, 0, 0, 1, 2, 36));
		defaultshader.addObject(new IsoSphere(11, 0, 0, 1, 0));
		// addObject(new IsoSphere(14, 0, 0, 1, 1));
		// addObject(new IsoSphere(17, 0, 0, 1, 2));
		// addObject(new IsoSphere(20, 0, 0, 1, 3));
		// addObject(new IsoSphere(23, 0, 0, 1, 4));
		// addObject(new IsoSphere(26, 0, 0, 1, 5));
		// addObject(new IsoSphere(29, 0, 0, 1, 6));
		// addObject(new IsoSphere(32, 0, 0, 1, 7));
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
		cam.update(delta);
	}

}
