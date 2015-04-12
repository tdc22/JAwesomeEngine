package test2d;

import game.StandardGame;
import gui.Font;
import gui.Text;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import math.FastMath;
import shader.Shader;
import shape.Box;
import shape2d.Circle;
import shape2d.Ellipse;
import shape2d.Quad;
import texture.Texture;
import vector.Vector4f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class Test2d extends StandardGame {
	Quad rotquad;
	Text coloredjumptext;
	float jumppos;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");

		addObject(new Box(0, 0, 0, 0.5f, 0.5f, 0.5f));
		add2dObject(new Quad(25, 25, 50, 50));
		add2dObject(new Text("2D Fonttest... seems to work perfectly!", 100,
				100, font));

		rotquad = new Quad(500, 50, 25, 25);
		add2dObject(rotquad);

		add2dObject(new Ellipse(600, 80, 20, 30, 36));
		add2dObject(new Ellipse(660, 80, 30, 20, 36));
		add2dObject(new Circle(600, 180, 30, 36));
		add2dObject(new Circle(660, 180, 20, 36));

		Shader colorshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		coloredjumptext = new Text("This text jumps and is colored.", 100, 300,
				font);
		coloredjumptext.setShader(colorshader);
		add2dObject(coloredjumptext);

		Texture texture = new Texture(
				TextureLoader
						.loadTexture("res/textures/textureCoordinateTest.png"));
		Shader textureshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		textureshader.addArgumentName("texture");
		textureshader.addArgument(texture);

		Quad texquad = new Quad(500, 300, 25, 25);
		texquad.setRenderHints(false, true, false);
		texquad.setShader(textureshader);
		add2dObject(texquad);
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
		rotquad.rotate(delta * 0.1f);

		jumppos += delta * 0.01f;
		coloredjumptext.translate(0, FastMath.sin(jumppos));

		cam.update(delta);
	}

}
