package shader2_Tex;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape.Box;
import texture.Texture;
import vector.Vector4f;

public class ShaderTest2 extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		// Shader Test 1
		Shader colorshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("u_color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(colorshader);

		Box a = new Box(-2, 0, 2, 0.5f, 0.5f, 0.5f);
		colorshader.addObject(a);

		// Shader Test 2
		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		Box b = new Box(0, 0, 0, 0.5f, 0.5f, 0.5f);
		b.setRenderHints(false, true, false);
		textureshader.addObject(b);
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}
