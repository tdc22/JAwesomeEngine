package pyramidTest;

import game.Debugger;
import game.StandardGame;
import loader.FontLoader;
import shape.Box;

public class PyramidTest extends StandardGame {
	Debugger debugmanager;

	@Override
	public void init() {
		initDisplay(false, 800, 600, false);
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
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
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
		render2dScene();
	}

	@Override
	public void update(int delta) {
		debugmanager.update();
		cam.update(delta);
	}
}
