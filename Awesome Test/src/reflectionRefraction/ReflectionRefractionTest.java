package reflectionRefraction;

import game.StandardGame;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import texture.CubeEnvironmentMap;
import texture.CubeMap;
import texture.Texture;
import vector.Vector4f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ReflectionRefractionTest extends StandardGame {
	CubeEnvironmentMap cubemapper;
	Sphere camball;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.setFlySpeed(0.002f);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		// Shader Test 1
		Shader colorshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		Box a = new Box(0, 0, -2, 0.5f, 0.5f, 0.5f);
		a.setShader(colorshader);
		addObject(a);

		// Shader Test 2
		Texture texture = new Texture(
				TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		textureshader.addArgumentName("texture");
		textureshader.addArgument(texture);

		Box b = new Box(0, -2, 0, 3f, 0.1f, 3f);
		b.setRenderHints(false, true, false);
		b.setShader(textureshader);
		addObject(b);

		// Reflection
		Sphere s1 = new Sphere(-2, 0, 0, 0.5f, 32, 32);
		s1.setRenderHints(false, true, true);
		addObject(s1);

		cubemapper = new CubeEnvironmentMap(this, s1.getTranslation());
		cubemapper.updateTexture();

		Shader cubemapreflectionshader = new Shader(
				ShaderLoader.loadShaderFromFile(
						"res/shaders/cubemapreflection.vert",
						"res/shaders/cubemapreflection.frag"));
		cubemapreflectionshader.addArgumentNames("cubeMap");
		cubemapreflectionshader.addArguments(new CubeMap(cubemapper
				.getTextureID()));
		s1.setShader(cubemapreflectionshader);

		camball = new Sphere(0, 0, 0, 0.2f, 32, 32);
		camball.setShader(colorshader);
		addObject(camball);
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
		camball.translateTo(cam.getTranslation());
		cubemapper.updateTexture();
	}
}
