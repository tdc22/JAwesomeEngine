package displayFullscreen;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import sound.NullSoundEnvironment;

public class DisplayTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Fullscreen Test", true, false, true), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
		display.bindMouse(); // Hint: always bind mouse when in full screen!
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
