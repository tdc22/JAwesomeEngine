package pyramid;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import shader.Shader;
import shape.Box;
import utils.Debugger;

public class PyramidTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(-1, 1, -1);
		cam.rotateTo(225, 30);
		
		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		debugger = new Debugger(inputs, defaultshader, defaultshader2, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		
		int startsidelength = 33;
		int sidelength = startsidelength;
		int height = 0;
		while (sidelength >= 1) {
			int delta = (startsidelength - sidelength) / 2;
			for (int x = 0; x < sidelength; x++) {
				for (int z = 0; z < sidelength; z++) {
					defaultshader.addObject(new Box(delta + x, height, delta + z, 1, 1, 1));
				}
			}
			sidelength -= 2;
			height++;
		}
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
		cam.update(delta);
	}
}
