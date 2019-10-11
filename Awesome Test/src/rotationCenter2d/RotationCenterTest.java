package rotationCenter2d;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import shader.Shader;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import vector.Vector2f;

public class RotationCenterTest extends StandardGame {
	Shader defaultshader;
	Quad rotquad1, rotquad2, rotquad3, rotquad4, rotquad5;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		rotquad1 = new Quad(100, 150, 25, 25);
		rotquad2 = new Quad(250, 150, 25, 25);
		rotquad3 = new Quad(400, 150, 25, 25);
		rotquad4 = new Quad(550, 150, 25, 25);
		rotquad5 = new Quad(700, 150, 25, 25);
		rotquad2.setRotationCenter(new Vector2f(25, 0));
		rotquad3.setRotationCenter(new Vector2f(0, 25));
		rotquad4.setRotationCenter(new Vector2f(25, 25));
		rotquad5.setRotationCenter(new Vector2f(-50, -50));
		defaultshader.addObject(rotquad1);
		defaultshader.addObject(rotquad2);
		defaultshader.addObject(rotquad3);
		defaultshader.addObject(rotquad4);
		defaultshader.addObject(rotquad5);
	}

	@Override
	public void render() {
	}

	@Override
	public void render2d() {
		render2dLayer();
	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		float rotdelta = delta * 0.1f;
		rotquad1.rotate(rotdelta);
		rotquad2.rotate(rotdelta);
		rotquad3.rotate(rotdelta);
		rotquad4.rotate(rotdelta);
		rotquad5.rotate(rotdelta);
	}

}
