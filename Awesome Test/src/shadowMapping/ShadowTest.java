package shadowMapping;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import matrix.Matrix4f;
import objects.Camera3;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import texture.FramebufferObject;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;

public class ShadowTest extends StandardGame {
	Debugger debugger;
	FramebufferObject depthMap;
	Shader shadowshader;
	boolean shadow = false;

	Camera3 lightCam;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshader2);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		defaultshader.addObject(new Box(0, -1, 0, 100, 0.5f, 100));
		defaultshader.addObject(ModelLoader.load("res/models/bunny.mobj"));
		defaultshader.addObject(new Sphere(2, 0, 0, 1, 36, 36));
		defaultshader.addObject(new Cylinder(8, 0, 0, 1, 2, 36));

		lightCam = new Camera3(new Vector3f(0, 10, 0), 0, -80);
		depthMap = new FramebufferObject(this, 1024, 1024, 0, lightCam, true, true, true, true);

		shadowshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/shadowmapping.vert", "res/shaders/shadowmapping.frag"));
		shadowshader.addArgumentName("shadowMap");
		shadowshader.addArgument(new Texture(depthMap.getDepthTextureID()));
		shadowshader.addArgumentName("lightPos");
		Matrix4f mat = lightCam.getMatrix();
		// mat.invert();
		shadowshader.addArgument(mat);

		depthMap.updateTexture();
		shadow = true;

		Shader depthshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/ppDepthshader.vert", "res/shaders/ppDepthshader.frag"));
		depthshader.addArgumentName("texture");
		depthshader.addArgument(new Texture());
		depthshader.addArgumentName("depthTexture");
		depthshader.addArgument(new Texture(depthMap.getDepthTextureID()));
		depthshader.addArgumentName("depthMin");
		depthshader.addArgument(settings.getZNear());
		depthshader.addArgumentName("depthMax");
		depthshader.addArgument(settings.getZFar());

		Quad q = new Quad(180, 100, 180, 100);
		q.setRenderHints(false, true, false);
		defaultshader2.addObject(q);
	}

	@Override
	public void render() {
		debugger.begin();
		if (shadow)
			shadowshader.bind();
		render3dLayer();
		if (shadow)
			shadowshader.unbind();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		debugger.update(fps, 0, 0);
		depthMap.updateTexture();
		cam.update(delta);

		if (inputs.isKeyDown("U")) {
			lightCam.translate(0.1f, 0, 0);
			depthMap.updateTexture();
			System.out.println("+x");
		}
		if (inputs.isKeyDown("I")) {
			lightCam.translate(-0.1f, 0, 0);
			depthMap.updateTexture();
			System.out.println("-x");
		}
		if (inputs.isKeyDown("O")) {
			lightCam.translate(0, 0, 0.1f);
			depthMap.updateTexture();
			System.out.println("+z");
		}
		if (inputs.isKeyDown("P")) {
			lightCam.translate(0, 0, -0.1f);
			depthMap.updateTexture();
			System.out.println("-z");
		}
	}

}
