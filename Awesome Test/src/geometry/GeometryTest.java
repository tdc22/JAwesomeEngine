package geometry;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import loader.FontLoader;
import shape.Box;
import shape.Capsule;
import shape.Cylinder;
import shape.IsoSphere;
import shape.Sphere;

public class GeometryTest extends StandardGame {
	Debugger debugmanager;

	// Shader lineshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);

		// inputs.setInputReader(new GLFWInputReader(((GLDisplay) display)
		// .getWindowID()));
		/*
		 * lineshader = new Shader(ShaderLoader.loadShaderPair(
		 * "res/shaders/geometrytestshader.vert",
		 * "res/shaders/geometrytestshader.geo",
		 * ARBGeometryShader4.GL_TRIANGLES_ADJACENCY_ARB, GL11.GL_LINE_STRIP,
		 * 6));
		 */

		addObject(new Box(-1, 0, 0, 1, 1, 1));
		addObject(new Sphere(2, 0, 0, 1, 36, 36));
		addObject(new Capsule(5, 0, 0, 1, 2, 36, 36));
		addObject(new Cylinder(8, 0, 0, 1, 2, 36));
		addObject(new IsoSphere(11, 0, 0, 1, 0));
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
		debugmanager.render3d();
		// lineshader.bind();
		renderScene();
		// lineshader.unbind();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
		render2dScene();
	}

	@Override
	public void update(int delta) {
		// System.out.println("------------------------------------");
		debugmanager.update();
		cam.update(delta);
		// System.out.println(cam.getPosition().toString());
	}

}
