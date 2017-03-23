package textTest;

import collisionshape2d.QuadShape;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.BitmapFont;
import gui.Font;
import gui.Text;
import loader.FontLoader;
import loader.ShaderLoader;
import shader.Shader;
import sound.NullSoundEnvironment;

public class TextTest extends StandardGame {
	QuadShape rotquad;
	Text coloredjumptext;
	float jumppos;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		defaultshader.addObject(new Text("Hello this is a test!\nYou can make line breaks.", 100, 100, font, 40));
		defaultshader.addObject(new Text("This font is rendered in lines.", 100, 200, font, 10));

		BitmapFont font2 = (BitmapFont) FontLoader.loadFont("res/fonts/DejaVuSans24.png");
		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(font2.getBitmap());
		addShader2d(textureshader);

		textureshader
				.addObject(new Text("Hello this is a test!\nYou can make line breaks.", 100, 300, font2, 40, 0.3f, 0f));
		textureshader.addObject(new Text("This font is rendered in bitmaps.", 100, 400, font2, 18, 0.3f, 0f));
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