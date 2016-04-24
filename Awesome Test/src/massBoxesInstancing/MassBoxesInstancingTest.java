package massBoxesInstancing;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import matrix.Matrix4f;
import objects.InstancedObject;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;

public class MassBoxesInstancingTest extends StandardGame {
	Debugger debugger;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "JAwesomeEngine", false), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 0);
		cam.rotateTo(180, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);

		debugger = new Debugger(inputs, defaultshader, defaultshader2, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"),
				cam);

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(ShaderLoader.loadShaderFromFile("res/shaders/textureinstanceshader.vert",
				"res/shaders/textureinstanceshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		InstancedObject boxes = new InstancedObject();

		Box b = new Box(0, 0, 0, 1, 1, 1);
		boxes.setVertices(b.getVertices());
		boxes.setColors(b.getColors());
		boxes.setTextureCoordinates(b.getTextureCoordinates());
		boxes.setNormals(b.getNormals());
		boxes.setIndices(b.getIndices());

		boxes.setRenderHints(false, true, false, true);

		Matrix4f mat = new Matrix4f();
		for (int i = 0; i < 30; i++) {
			mat.translate(new Vector3f(i * 3, 0, 0));
			Matrix4f m = new Matrix4f(mat);
			System.out.println(m);
			boxes.addInstance(m);
		}

		boxes.prerender();
		textureshader.addObject(boxes);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
	}

	@Override
	public void render2d() {
		render2dLayer();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		debugger.update(fps, 0, 0);
		cam.update(delta);
	}
}