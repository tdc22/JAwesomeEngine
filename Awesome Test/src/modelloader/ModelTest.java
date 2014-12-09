package modelloader;

import game.StandardGame;
import loader.ModelLoader;

public class ModelTest extends StandardGame {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initDisplay(false, 800, 600, false);
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);
		addObject(ModelLoader.load("res/models/bunny.mobj"));
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