package slerp2d;

import game.StandardGame;
import loader.ShaderLoader;
import math.ComplexMath;
import quaternion.Complexf;
import shader.Shader;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class Test2d extends StandardGame {
	Quad q1, q2, q3, q4, q5, q6;
	Complexf c1, c2, c3;
	float t1 = 0, t2 = 0;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		q1 = new Quad(200, 130, 30, 30);
		q2 = new Quad(400, 130, 30, 30);
		q3 = new Quad(600, 130, 30, 30);
		q4 = new Quad(200, 330, 30, 30);
		q5 = new Quad(400, 330, 30, 30);
		q6 = new Quad(600, 330, 30, 30);

		c1 = new Complexf();
		c2 = new Complexf();
		c3 = new Complexf();

		c2.rotate(90);
		c3.rotate(180);

		defaultshader.addObject(q1);
		defaultshader.addObject(q2);
		defaultshader.addObject(q3);
		defaultshader.addObject(q4);
		defaultshader.addObject(q5);
		defaultshader.addObject(q6);
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
		float d = delta / 1000f;

		t1 += d;
		if (t1 >= 1)
			t1 -= 1;
		q1.rotate(d * 90f);
		q2.rotateTo(ComplexMath.lerp(c1, c2, t1));
		q3.rotateTo(ComplexMath.slerp(c1, c2, t1));

		t2 += d;
		if (t2 >= 1)
			t2 -= 1;
		q4.rotate(d * 180f);
		q5.rotateTo(ComplexMath.lerp(c1, c3, t2));
		q6.rotateTo(ComplexMath.slerp(c1, c3, t2));

		cam.update(delta);
	}

}
