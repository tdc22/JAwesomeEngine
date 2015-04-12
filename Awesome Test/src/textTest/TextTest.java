package textTest;

import game.StandardGame;
import gui.Font;
import gui.Text;
import loader.FontLoader;
import collisionshape2d.QuadShape;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class TextTest extends StandardGame {
	QuadShape rotquad;
	Text coloredjumptext;
	float jumppos;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		add2dObject(new Text(
				"Hello this is a test!\nYou can make line breaks.", 100, 100,
				font, 40));
		add2dObject(new Text("This font is rendered in polygons.", 100, 200,
				font, 10));
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}