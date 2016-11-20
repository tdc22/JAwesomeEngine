package display;

import game.StandardGame;
import sound.NullSoundEnvironment;

public class DisplayTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {

	}
}
