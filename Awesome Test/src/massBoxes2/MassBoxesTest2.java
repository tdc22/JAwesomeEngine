package massBoxes2;

import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape.Box;
import texture.Texture;
import utils.Debugger;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class MassBoxesTest2 extends StandardGame {
	Debugger debugger;
	Shader textureshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Mass Boxes 2",
				false), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 0);
		cam.rotateTo(180, 0);

		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		Texture texture = new Texture(
				TextureLoader.loadTexture("res/textures/cobblestone.png"));

		textureshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		textureshader.addArgumentName("colorMap");
		textureshader.addArgument(texture);

		for (int i = 0; i < 5000; i++) {
			Box b = new Box((float) Math.random() * 100,
					(float) Math.random() * 100, (float) Math.random() * 100,
					0.5f, 0.5f, 0.5f);
			b.setRenderHints(false, true, false);
			// b.setShader(textureshader);
			addObject(b);
		}
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		textureshader.bind();
		renderScene();
		textureshader.unbind();
	}

	@Override
	public void render2d() {
		render2dScene();
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugger.update();
		cam.update(delta);
	}
}