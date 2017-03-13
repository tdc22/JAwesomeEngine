package soundPosition;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.SoundLoader;
import shader.Shader;
import shape.Box;
import sound.ALSound;
import sound.ALSoundEnvironment;
import sound.Sound;
import utils.VectorConstants;

public class SoundPositionTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new ALSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Sound sound = new ALSound(SoundLoader.loadSound("/home/oliver/Music/snap.ogg"));
		sound.setSourcePosition(0, 0, 0);
		sound.setLooping(true);
		sound.setSourcePositionRelative(false);
		sound.play();

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		defaultshader.addObject(new Box(0, 0, 0, 1, 1, 1));
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
		soundEnvironment.setListenerPosition(cam.getTranslation());
		soundEnvironment.setListenerOrientation(VectorConstants.UP, cam.getDirection());
	}
}
