package simpleTerrainTest;

import game.Debugger;
import game.StandardGame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import loader.FontLoader;
import terrain.Terrain;

public class SimpleTerrainTest extends StandardGame {
	Debugger debugmanager;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initDisplay(false, 800, 600, false);
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
		cam.setFlyCam(true);
		cam.translateTo(0, 3, 0);
		cam.rotateTo(225, 0);

		BufferedImage heightmap = null;
		try {
			heightmap = ImageIO.read(new File("res/heightmaps/worldsmall.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Terrain terrain = new Terrain(heightmap);
		terrain.scale(0.1f, 0.5f, 0.1f);
		addObject(terrain);
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
