package reflectionRefraction;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class ReflectionRefractionTest extends StandardGame {
	Debugger debugger;
	CubeEnvironmentMap cubemapper0, cubemapper1, cubemapper2, cubemapper3;
	Sphere camball;
	InputEvent screenshotEvent;

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
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		// Shader Test 1
		Shader colorshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("u_color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(colorshader);

		Box a = new Box(0, 0, -2, 0.5f, 0.5f, 0.5f);
		colorshader.addObject(a);

		// Shader Test 2
		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/stone.png"));

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		Box b = new Box(0, -1, 0, 3f, 0.1f, 3f);
		b.setRenderHints(false, true, false);
		textureshader.addObject(b);

		// Reflection Sphere
		Sphere s1 = new Sphere(-2, 0, 1, 0.5f, 32, 32);
		s1.setRenderHints(false, true, true);

		cubemapper0 = new CubeEnvironmentMap(layer3d, s1.getTranslation());

		Shader cubemapreflectionshader = new Shader(ShaderLoader
				.loadShaderFromFile("res/shaders/cubemapreflectrefract.vert", "res/shaders/cubemapreflection.frag"));
		cubemapreflectionshader.addArgumentNames("u_cubeMap");
		cubemapreflectionshader.addArguments(new CubeMap(cubemapper0.getTextureID()));
		cubemapreflectionshader.addObject(s1);
		addShader(cubemapreflectionshader);

		// Reflection Box
		Box b1 = new Box(-2, 0, -1, 0.5f, 0.5f, 0.5f);
		b1.setRenderHints(false, true, true);

		cubemapper1 = new CubeEnvironmentMap(layer3d, b1.getTranslation());

		Shader cubemapreflectionshader2 = new Shader(cubemapreflectionshader.getShaderProgram());
		cubemapreflectionshader2.addArgumentNames("u_cubeMap");
		cubemapreflectionshader2.addArguments(new CubeMap(cubemapper1.getTextureID()));
		cubemapreflectionshader2.addObject(b1);
		addShader(cubemapreflectionshader2);

		// Refraction Sphere
		Sphere s2 = new Sphere(2, 0, 1, 0.5f, 32, 32);
		s2.setRenderHints(false, true, true);

		cubemapper2 = new CubeEnvironmentMap(layer3d, s2.getTranslation());

		Shader cubemaprefractionshader = new Shader(ShaderLoader
				.loadShaderFromFile("res/shaders/cubemapreflectrefract.vert", "res/shaders/cubemaprefraction.frag"));
		cubemaprefractionshader.addArgumentNames("u_cubeMap");
		cubemaprefractionshader.addArguments(new CubeMap(cubemapper2.getTextureID()));
		cubemaprefractionshader.addArgumentNames("refractionIndex");
		cubemaprefractionshader.addArguments(0.5f);
		cubemaprefractionshader.addObject(s2);
		addShader(cubemaprefractionshader);

		// Refraction Box
		Box b2 = new Box(2, 0, -1, 0.5f, 0.5f, 0.5f);
		b2.setRenderHints(false, true, true);

		cubemapper3 = new CubeEnvironmentMap(layer3d, b2.getTranslation());

		Shader cubemaprefractionshader2 = new Shader(cubemaprefractionshader.getShaderProgram());
		cubemaprefractionshader2.addArgumentNames("u_cubeMap");
		cubemaprefractionshader2.addArguments(new CubeMap(cubemapper3.getTextureID()));
		cubemaprefractionshader2.addArgumentNames("refractionIndex");
		cubemaprefractionshader2.addArguments(0.5f);
		cubemaprefractionshader2.addObject(b2);
		addShader(cubemaprefractionshader2);

		// Camball
		camball = new Sphere(0, 0, 0, 0.2f, 32, 32);
		colorshader.addObject(camball);

		screenshotEvent = new InputEvent("screenshot", new Input(Input.KEYBOARD_EVENT, "2", KeyInput.KEY_PRESSED));
		inputs.addEvent(screenshotEvent);
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
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
		cam.update(delta);
		camball.translateTo(cam.getTranslation());
		cubemapper0.updateTexture();
		cubemapper1.updateTexture();
		cubemapper2.updateTexture();
		cubemapper3.updateTexture();

		if (screenshotEvent.isActive()) {
			BufferedImage bi = layer3d.getFramebuffer().getColorTextureImage();
			File outputfile = new File(Math.random() * 1000000 + "saved.png");
			try {
				ImageIO.write(bi, "png", outputfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Screenshot saved!");
		}
	}
}
