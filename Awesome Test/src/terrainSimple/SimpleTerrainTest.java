package terrainSimple;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import loader.FontLoader;
import terrain.Terrain;
import utils.Debugger;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class SimpleTerrainTest extends StandardGame {
	Debugger debugger;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		debugger = new Debugger(inputs,
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
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}
	}
}
