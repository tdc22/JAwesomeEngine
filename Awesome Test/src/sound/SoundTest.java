package sound;

import game.StandardGame;
import loader.SoundLoader;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class SoundTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings(), new ALSoundEnvironment());

		Sound sound = new ALSound(SoundLoader.loadSound("res/sounds/snap.ogg")); // TODO:
																					// delete
		sound.setLooping(true);
		sound.play();
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
