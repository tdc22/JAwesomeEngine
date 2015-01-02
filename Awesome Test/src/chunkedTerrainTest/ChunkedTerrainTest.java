package chunkedTerrainTest;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import loader.FontLoader;
import terrain.ChunkedTerrain;
import vector.Vector3f;

public class ChunkedTerrainTest extends StandardGame {
	ChunkedTerrain terrain;
	Debugger debugmanager;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0, 3, 0);
		cam.rotateTo(225, 0);

		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);

		BufferedImage heightmap = null;
		try {
			heightmap = ImageIO.read(new File("res/heightmaps/world.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		terrain = new ChunkedTerrain(heightmap);
		terrain.scale(new Vector3f(0.1f, 0.3f, 0.1f));
		terrain.setLODs(160, 80, 40, 20, 10);
		terrain.updateChunks(cam.getTranslation());
		addObject(terrain);
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugmanager.update();
		cam.update(delta);
		terrain.updateLOD(cam.getTranslation());
	}
}
