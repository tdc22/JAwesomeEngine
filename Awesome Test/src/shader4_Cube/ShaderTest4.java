package shader4_Cube;

import game.StandardGame;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import objects.Camera;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import texture.CubeEnvironmentMap;
import texture.CubeMap;
import texture.FramebufferObject;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;
import vector.Vector4f;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ShaderTest4 extends StandardGame {
	Texture texture, diffuse, bumpmap;
	CubeEnvironmentMap cubemapper;
	Sphere camball;
	Debugger debugger;
	FramebufferObject rtt;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.setFlySpeed(0.002f);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshader2 = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		add2dShader(defaultshader2);

		debugger = new Debugger(inputs, defaultshader, defaultshader2,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		// Shader Test 1
		Shader colorshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("u_color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(colorshader);

		Box a = new Box(-2, 0, 2, 0.5f, 0.5f, 0.5f);
		colorshader.addObject(a);

		Sphere s = new Sphere(0, 0, 0, 0.5f, 32, 32);
		s.setRenderHints(false, true, false);
		cubemapper = new CubeEnvironmentMap(this, s.getTranslation());
		cubemapper.updateTexture();

		rtt = new FramebufferObject(this, 1024, 1024, 0, new Camera(
				new Vector3f(0, 0, 12), 0, 0));
		rtt.updateTexture();

		// Shader Test 2
		Texture texture = new Texture(
				TextureLoader.loadTexture("res/textures/cobblestone.png"));

		Shader textureshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(new Texture(rtt.getColorTextureID()));
		addShader(textureshader);

		Box b = new Box(2, 0, 2, 0.5f, 0.5f, 0.5f);
		b.setRenderHints(false, true, false);
		textureshader.addObject(b);

		// Shader Test 3
		diffuse = new Texture(
				TextureLoader.loadTexture("res/textures/diffuse.jpg"));
		bumpmap = new Texture(
				TextureLoader.loadTexture("res/textures/normal.jpg"));

		Shader bumpmapshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/bumpmapshader.vert",
				"res/shaders/bumpmapshader.frag"));
		bumpmapshader.addArgumentNames("u_texture", "u_bumpmap");
		bumpmapshader.addArguments(diffuse, bumpmap);
		addShader(bumpmapshader);

		Box c = new Box(0, -2, 2, 0.5f, 0.5f, 0.5f);
		c.setRenderHints(false, true, false);
		bumpmapshader.addObject(c);

		// Shader Test 4

		Shader cubemapshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/cubemapshader.vert",
				"res/shaders/cubemapshader.frag"));
		cubemapshader.addArgumentNames("u_cubeMap");
		cubemapshader.addArguments(new CubeMap(cubemapper.getTextureID()));
		cubemapshader.addObject(s);
		addShader(cubemapshader);

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
		render2dScene();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		cubemapper.updateTexture();
		rtt.updateTexture();
		debugger.update(fps, 0, 0);
		cam.update(delta);
		camball.translateTo(cam.getTranslation());
	}
}
