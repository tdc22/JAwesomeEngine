package transRot;

import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.ModelLoader;
import loader.ShaderLoader;
import math.FastMath;
import objects.ShapedObject3;
import shader.Shader;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class TransRot extends StandardGame {
	ShapedObject3 rabbit1, rabbit2;
	float r2y;
	InputEvent toggleMouseBind;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 5, 40);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		rabbit1 = ModelLoader.load("res/models/bunny.mobj");
		rabbit2 = ModelLoader.load("res/models/bunny.mobj");
		defaultshader.addObject(rabbit1);
		defaultshader.addObject(rabbit2);

		rabbit1.translateTo(-10, 0, 0);
		rabbit2.translateTo(10, 0, 0);

		toggleMouseBind = new InputEvent("toggleMouseBind", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		inputs.addEvent(toggleMouseBind);
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
		if (display.isMouseBound())
			cam.update(delta);
		if (toggleMouseBind.isActive()) {
			if (!display.isMouseBound())
				display.bindMouse();
			else
				display.unbindMouse();
		}

		float speed = 0.01f * delta;
		rabbit1.rotate(speed, speed, speed);
		r2y += speed;
		rabbit2.translateTo(rabbit2.getTranslation().getXf(), FastMath.sin(r2y) * 3, 0);
	}

}
