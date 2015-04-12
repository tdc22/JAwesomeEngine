package loaderTest;

import game.StandardGame;
import loader.ModelLoader;
import objects.ShapedObject;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class LoaderTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0.5f, 5);
		cam.rotateTo(0, 0);

		ShapedObject obj = ModelLoader.load("res/models/bunny.obj");
		addObject(obj);
		System.out.println("Finished loading!");
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		renderScene();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub
		cam.update(delta);
	}
}
