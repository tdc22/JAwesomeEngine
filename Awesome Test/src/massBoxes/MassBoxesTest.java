package massBoxes;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import shape.Box;
import texture.Texture;
import utils.Shader;

public class MassBoxesTest extends StandardGame {
	Debugger debugmanager;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 0);
		cam.rotateTo(180, 0);

		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);

		Texture texture = new Texture(
				TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		textureshader.addArgumentName("colorMap");
		textureshader.addArgument(texture);

		for (int i = 0; i < 5000; i++) {
			Box b = new Box((float) Math.random() * 100,
					(float) Math.random() * 100, (float) Math.random() * 100,
					0.5f, 0.5f, 0.5f);
			b.setRenderHints(false, true, false);
			b.setShader(textureshader);
			addObject(b);
		}
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
		render2dScene();
	}

	@Override
	public void update(int delta) {
		debugmanager.update();
		cam.update(delta);
	}
}