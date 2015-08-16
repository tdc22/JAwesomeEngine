package display;

import game.StandardGame;

public class DisplayTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {

	}

	@Override
	public void update(int delta) {

	}
}
