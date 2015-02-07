package terrainSimple;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import loader.FontLoader;
import terrain.Terrain;

public class SimpleTerrainTest extends StandardGame {
	Debugger debugmanager;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		display.bindMouse();
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

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(
				Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
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

	@Override
	public void update(int delta) {
		debugmanager.update();
		cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}
}
