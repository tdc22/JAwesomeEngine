package reflectionRefraction;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import texture.CubeEnvironmentMap;
import texture.CubeMap;
import texture.Texture;
import utils.Debugger;
import vector.Vector4f;

public class ReflectionRefractionTest extends StandardGame {
	Debugger debugger;
	CubeEnvironmentMap cubemapper0, cubemapper1, cubemapper2, cubemapper3;
	Sphere camball;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.setFlySpeed(0.002f);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader2);

		debugger = new Debugger(inputs, defaultshader, defaultshader2, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"),
				cam);

		// Shader Test 1
		Shader colorshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(colorshader);

		Box a = new Box(0, 0, -2, 0.5f, 0.5f, 0.5f);
		colorshader.addObject(a);

		// Shader Test 2
		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("texture");
		textureshader.addArgument(texture);

		Box b = new Box(0, -1, 0, 3f, 0.1f, 3f);
		b.setRenderHints(false, true, false);
		textureshader.addObject(b);

		// Reflection Sphere
		Sphere s1 = new Sphere(-2, 0, 1, 0.5f, 32, 32);
		s1.setRenderHints(false, true, true);

		cubemapper0 = new CubeEnvironmentMap(this, s1.getTranslation());

		Shader cubemapreflectionshader = new Shader(ShaderLoader
				.loadShaderFromFile("res/shaders/cubemapreflectrefract.vert", "res/shaders/cubemapreflection.frag"));
		cubemapreflectionshader.addArgumentNames("cubeMap");
		cubemapreflectionshader.addArguments(new CubeMap(cubemapper0.getTextureID()));
		cubemapreflectionshader.addObject(s1);

		// Reflection Box
		Box b1 = new Box(-2, 0, -1, 0.5f, 0.5f, 0.5f);
		b1.setRenderHints(false, true, true);

		cubemapper1 = new CubeEnvironmentMap(this, b1.getTranslation());

		Shader cubemapreflectionshader2 = new Shader(cubemapreflectionshader.getShaderProgram());
		cubemapreflectionshader2.addArgumentNames("cubeMap");
		cubemapreflectionshader2.addArguments(new CubeMap(cubemapper1.getTextureID()));
		cubemapreflectionshader2.addObject(b1);

		// Refraction Sphere
		Sphere s2 = new Sphere(2, 0, 1, 0.5f, 32, 32);
		s2.setRenderHints(false, true, true);

		cubemapper2 = new CubeEnvironmentMap(this, s2.getTranslation());

		Shader cubemaprefractionshader = new Shader(ShaderLoader
				.loadShaderFromFile("res/shaders/cubemapreflectrefract.vert", "res/shaders/cubemaprefraction.frag"));
		cubemaprefractionshader.addArgumentNames("cubeMap");
		cubemaprefractionshader.addArguments(new CubeMap(cubemapper2.getTextureID()));
		cubemaprefractionshader.addArgumentNames("refractionIndex");
		cubemaprefractionshader.addArguments(0.5f);
		cubemaprefractionshader.addObject(s2);

		// Refraction Box
		Box b2 = new Box(2, 0, -1, 0.5f, 0.5f, 0.5f);
		b2.setRenderHints(false, true, true);

		cubemapper3 = new CubeEnvironmentMap(this, b2.getTranslation());

		Shader cubemaprefractionshader2 = new Shader(cubemaprefractionshader.getShaderProgram());
		cubemaprefractionshader2.addArgumentNames("cubeMap");
		cubemaprefractionshader2.addArguments(new CubeMap(cubemapper3.getTextureID()));
		cubemaprefractionshader.addArgumentNames("refractionIndex");
		cubemaprefractionshader.addArguments(0.5f);
		cubemaprefractionshader2.addObject(b2);

		// Camball
		camball = new Sphere(0, 0, 0, 0.2f, 32, 32);
		colorshader.addObject(camball);
	}

	@Override
	public void render() {
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		debugger.end();
	}

	@Override
	public void update(int delta) {
		debugger.update(fps, 0, 0);
		cam.update(delta);
		camball.translateTo(cam.getTranslation());
		cubemapper0.updateTexture();
		cubemapper1.updateTexture();
		cubemapper2.updateTexture();
		cubemapper3.updateTexture();
	}
}
