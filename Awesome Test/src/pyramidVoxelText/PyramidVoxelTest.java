package pyramidVoxelText;

import game.Debugger;
import game.StandardGame;
import loader.FontLoader;
import objects.SimpleBoxVoxelObject;

public class PyramidVoxelTest extends StandardGame {
	Debugger debugmanager;

	@Override
	public void init() {
		display.bindMouse();
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
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
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
		render2dScene();
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
		debugmanager.update();
		cam.update(delta);
	}
}
