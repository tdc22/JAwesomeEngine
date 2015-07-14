package shadowMapping;

import game.StandardGame;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import matrix.Matrix4f;
import objects.Camera;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import texture.FramebufferObject;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ShadowTest extends StandardGame {
	Debugger debugger;
	FramebufferObject depthMap;
	Shader shadowshader;
	boolean shadow = false;

	Camera lightCam;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		debugger = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		addObject(new Box(0, -1, 0, 100, 0.5f, 100));
		addObject(ModelLoader.load("res/models/bunny.mobj"));
		addObject(new Sphere(2, 0, 0, 1, 36, 36));
		addObject(new Cylinder(8, 0, 0, 1, 2, 36));

		lightCam = new Camera(new Vector3f(0, 3, 0), 90, 90);
		depthMap = new FramebufferObject(this, 1024, 1024, 0, lightCam, true,
				true, true, true);

		shadowshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/shadowmapping.vert",
				"res/shaders/shadowmapping.frag"));
		shadowshader.addArgumentName("shadowMap");
		shadowshader.addArgument(new Texture(depthMap.getDepthTextureID()));
		shadowshader.addArgumentName("lightPos");
		Matrix4f mat = lightCam.getMatrix();
		// mat.invert();
		shadowshader.addArgument(mat);

		depthMap.updateTexture();
		shadow = true;
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		if (shadow)
			shadowshader.bind();
		renderScene();
		if (shadow)
			shadowshader.unbind();
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
