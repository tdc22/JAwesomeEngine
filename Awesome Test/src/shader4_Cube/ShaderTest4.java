package shader4_Cube;

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

public class ShaderTest4 extends StandardGame {
	Texture texture, diffuse, bumpmap;
	CubeEnvironmentMap cubemapper;
	Debugger debugger;
	Sphere camball;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.setFlySpeed(0.002f);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);
		debugger = new Debugger(inputs, FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		// Shader Test 1
		Shader colorshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		Box a = new Box(-2, 0, 2, 0.5f, 0.5f, 0.5f);
		a.setShader(colorshader);
		addObject(a);

		// Shader Test 2
		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("texture");
		textureshader.addArgument(texture);

		Box b = new Box(2, 0, 2, 0.5f, 0.5f, 0.5f);
		b.setRenderHints(false, true, false);
		b.setShader(textureshader);
		addObject(b);

		// Shader Test 3
		diffuse = new Texture(TextureLoader.loadTexture("res/textures/diffuse.jpg"));
		bumpmap = new Texture(TextureLoader.loadTexture("res/textures/normal.jpg"));

		Shader bumpmapshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/bumpmapshader.vert", "res/shaders/bumpmapshader.frag"));
		bumpmapshader.addArgumentNames("colorTexture", "normalTexture");
		bumpmapshader.addArguments(diffuse, bumpmap);

		Box c = new Box(0, -2, 2, 0.5f, 0.5f, 0.5f);
		c.setRenderHints(false, true, false);
		c.setShader(bumpmapshader);
		addObject(c);

		// Shader Test 4
		Sphere s = new Sphere(0, 0, 0, 0.5f, 32, 32);
		s.setRenderHints(false, true, false);
		addObject(s);

		cubemapper = new CubeEnvironmentMap(this, s.getTranslation());
		cubemapper.updateTexture();

		Shader cubemapshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/cubemapshader.vert", "res/shaders/cubemapshader.frag"));
		cubemapshader.addArgumentNames("cubeMap");
		cubemapshader.addArguments(new CubeMap(cubemapper.getTextureID()));
		s.setShader(cubemapshader);

		camball = new Sphere(0, 0, 0, 0.2f, 32, 32);
		camball.setShader(colorshader);
		addObject(camball);
	}

	@Override
	public void render() {
		debugger.update3d();
		debugger.begin();
		renderScene();
	}

	@Override
	public void render2d() {
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	@Override
	public void update(int delta) {
		debugger.update();
		cam.update(delta);
		camball.translateTo(cam.getTranslation());
		cubemapper.updateTexture();
	}
}
