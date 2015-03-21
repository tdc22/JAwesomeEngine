package pyramidVoxel;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import loader.FontLoader;
import objects.SimpleBoxVoxelObject;

public class PyramidVoxelTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		cam.setFlyCam(true);
		cam.translateTo(-1, 1, -1);
		cam.rotateTo(225, 30);

		int startsidelength = 33;
		int sidelength = startsidelength;
		int height = 0;

		int[][][] data = new int[sidelength][sidelength][sidelength];
		resetData(data);
		while (sidelength >= 1) {
			int delta = (startsidelength - sidelength) / 2;
			for (int x = 0; x < sidelength; x++) {
				for (int z = 0; z < sidelength; z++) {
					data[delta + x][height][delta + z] = 1;
				}
			}
			sidelength -= 2;
			height++;
		}

		SimpleBoxVoxelObject voxel = new SimpleBoxVoxelObject(data);
		voxel.updateShapes();
		addObject(voxel);
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

	public void resetData(int[][][] data) {
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {
				for (int z = 0; z < data[x][y].length; z++) {
					data[x][y][z] = 0;
				}
			}
		}
	}

	@Override
	public void update(int delta) {
		debugger.update();
		cam.update(delta);
	}
}
