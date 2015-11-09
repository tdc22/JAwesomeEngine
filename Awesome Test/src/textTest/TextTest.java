package textTest;

import game.StandardGame;
import gui.Font;
import gui.Text;
import loader.FontLoader;
import loader.ShaderLoader;
import shader.Shader;
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
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		defaultshader.addObject(new Text("Hello this is a test!\nYou can make line breaks.", 100, 100, font, 40));
		defaultshader.addObject(new Text("This font is rendered in polygons.", 100, 200, font, 10));
	}

	@Override
	public void render() {

	}

	@Override
	public void render2d() {
		render2dLayer();
	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}