package slerp2d;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.TextureLoader;
import math.ComplexMath;
import quaternion.Complexf;
import shader.Shader;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import texture.Texture;

public class Test2d extends StandardGame {
	Quad q1, q2, q3, q4, q5, q6;
	Complexf c1, c2, c3;
	float t = 0;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(new Texture(TextureLoader.loadTexture("res/textures/textureCoordinateTest.png")));
		addShader2d(textureshader);

		q1 = new Quad(200, 130, 30, 30);
		q2 = new Quad(400, 130, 30, 30);
		q3 = new Quad(600, 130, 30, 30);
		q4 = new Quad(200, 330, 30, 30);
		q5 = new Quad(400, 330, 30, 30);
		q6 = new Quad(600, 330, 30, 30);

		q1.setRenderHints(false, true, false);
		q2.setRenderHints(false, true, false);
		q3.setRenderHints(false, true, false);
		q4.setRenderHints(false, true, false);
		q5.setRenderHints(false, true, false);
		q6.setRenderHints(false, true, false);

		c1 = new Complexf();
		c2 = new Complexf();
		c3 = new Complexf();

		c2.rotate(90);
		c3.rotate(180);

		textureshader.addObject(q1);
		textureshader.addObject(q2);
		textureshader.addObject(q3);
		textureshader.addObject(q4);
		textureshader.addObject(q5);
		textureshader.addObject(q6);
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

		t += d;
		if (t >= 1)
			t -= 1;
		q1.rotate(d * 90f);
		q2.rotateTo(ComplexMath.lerp(c1, c2, t));
		q3.rotateTo(ComplexMath.slerp(c1, c2, t));

		q4.rotate(d * 180f);
		q5.rotateTo(ComplexMath.lerp(c1, c3, t));
		q6.rotateTo(ComplexMath.slerp(c1, c3, t));

		cam.update(delta);
	}

}
