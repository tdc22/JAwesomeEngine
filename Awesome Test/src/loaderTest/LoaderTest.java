package loaderTest;

import game.StandardGame;
import loader.ModelLoader;
import objects.ShapedObject;

public class LoaderTest extends StandardGame {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initDisplay(false, 800, 600, false);
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0.5f, 5);
		cam.rotateTo(0, 0);

		ShapedObject obj = ModelLoader.load("res/models/Sintel_Lite_257b.obj");
		addObject(obj);
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
