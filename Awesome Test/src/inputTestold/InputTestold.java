package inputTestold;

import game.StandardGame;
import shape.Box;

public class InputTestold extends StandardGame {
	Box box;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initDisplay(false, 800, 600, true);
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		box = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		addObject(box);
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
		if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			box.translate(0, delta / 100f, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			box.translate(0, -delta / 100f, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			box.translate(-delta / 100f, 0, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			box.translate(delta / 100f, 0, 0);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
			box.rotate(delta / 10f, 0, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			box.rotate(-delta / 10f, 0, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
			box.rotate(0, -delta / 10f, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			box.rotate(0, delta / 10f, 0);
		}
		cam.update(delta);
	}

}
