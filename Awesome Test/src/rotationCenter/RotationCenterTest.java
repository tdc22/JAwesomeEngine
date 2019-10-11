package rotationCenter;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import vector.Vector3f;

public class RotationCenterTest extends StandardGame {
	Shader defaultshader;
	Box rotquad1, rotquad2, rotquad3, rotquad4, rotquad5;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		rotquad1 = new Box(0, 0, 0, 1, 1, 1);
		rotquad2 = new Box(4, 0, 0, 1, 1, 1);
		rotquad3 = new Box(8, 0, 0, 1, 1, 1);
		rotquad4 = new Box(12, 0, 0, 1, 1, 1);
		rotquad5 = new Box(16, 0, 0, 1, 1, 1);
		rotquad2.setRotationCenter(new Vector3f(1, 0, 0));
		rotquad3.setRotationCenter(new Vector3f(0, 1, 0));
		rotquad4.setRotationCenter(new Vector3f(0, 0, 1));
		rotquad5.setRotationCenter(new Vector3f(1, 1, 1));
		defaultshader.addObject(rotquad1);
		defaultshader.addObject(rotquad2);
		defaultshader.addObject(rotquad3);
		defaultshader.addObject(rotquad4);
		defaultshader.addObject(rotquad5);
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);

		float rotdelta = delta * 0.1f;
		rotquad1.rotate(0, 0, rotdelta);
		rotquad2.rotate(0, 0, rotdelta);
		rotquad3.rotate(0, 0, rotdelta);
		rotquad4.rotate(0, 0, rotdelta);
		rotquad5.rotate(0, 0, rotdelta);
	}

}
