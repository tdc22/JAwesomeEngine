package tutorialp1;

import shape.Box;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;

public class Tutorial extends StandardGame {
	Box player;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		
		player = new Box(0,0,0,1,1.7f,1);
		this.addObject(player);
	}
	
	@Override
	public void update(int delta) {
		cam.update(delta);
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		
	}

}
