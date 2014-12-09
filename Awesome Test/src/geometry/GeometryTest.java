package geometry;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.GLFWInputReader;
import shape.Box;
import shape.Capsule;
import shape.Cylinder;
import shape.Sphere;

public class GeometryTest extends StandardGame {
	// Shader lineshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

//		inputs.setInputReader(new GLFWInputReader(((GLDisplay) display)
//				.getWindowID()));
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
	}

	@Override
	public void render() {
		// lineshader.bind();
		renderScene();
		// lineshader.unbind();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		// System.out.println("------------------------------------");
		cam.update(delta);
		// System.out.println(cam.getPosition().toString());
	}

}
