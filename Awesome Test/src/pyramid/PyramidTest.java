package pyramid;

import game.StandardGame;
import loader.FontLoader;
import shape.Box;
import utils.Debugger;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class PyramidTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(-1, 1, -1);
		cam.rotateTo(225, 30);

		int startsidelength = 33;
		int sidelength = startsidelength;
		int height = 0;
		while (sidelength >= 1) {
			int delta = (startsidelength - sidelength) / 2;
			for (int x = 0; x < sidelength; x++) {
				for (int z = 0; z < sidelength; z++) {
					addObject(new Box(delta + x, height, delta + z, 1, 1, 1));
				}
			}
			sidelength -= 2;
			height++;
		}
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
