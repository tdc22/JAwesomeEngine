package tutorialp1;

import game.StandardGame;
import shape.Box;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class Tutorial extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		Box player = new Box(0, 0, 0, 1, 1.7f, 1);
		addObject(player);
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
