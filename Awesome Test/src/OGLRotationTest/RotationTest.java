package OGLRotationTest;

import game.StandardGame;
import loader.FontLoader;
import sound.NullSoundEnvironment;
import utils.Debugger;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class RotationTest extends StandardGame {
	RotationObject b;
	Debugger debugmanager;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRender2d(true);
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		b = new RotationObject(0, 0, 0, 1, 1, 1);
		addObject(b);
	}

	@Override
	public void render() {
		debugmanager.update3d();
		render3dLayer();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
		render2dLayer();
	}

	@Override
	public void update(int delta) {
		b.rotateFurther(delta / 10f);
		debugmanager.update();
		cam.update(delta);
	}

}
