package modelloader;

import game.StandardGame;
import loader.ModelLoader;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ModelTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);
		addObject(ModelLoader.load("res/models/bunny.mobj"));
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}