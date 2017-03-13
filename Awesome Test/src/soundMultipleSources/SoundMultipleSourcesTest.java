package soundMultipleSources;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.SoundLoader;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import sound.ALSound;
import sound.ALSoundEnvironment;
import sound.Sound;
import utils.VectorConstants;

public class SoundMultipleSourcesTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new ALSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		int soundhandle = SoundLoader.loadSound("/home/oliver/Music/snap.ogg");

		Sound sound = new ALSound(soundhandle);
		sound.setSourcePosition(0, 0, 0);
		sound.setLooping(true);
		sound.setSourcePositionRelative(false);
		sound.play();

		Sound sound2 = new ALSound(soundhandle);
		sound2.setSourcePosition(10, 0, 0);
		sound2.setPitch(2f);
		sound2.setLooping(true);
		sound2.setSourcePositionRelative(false);
		sound2.play();

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		defaultshader.addObject(new Box(0, 0, 0, 1, 1, 1));
		defaultshader.addObject(new Sphere(10, 0, 0, 1, 32, 32));
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
