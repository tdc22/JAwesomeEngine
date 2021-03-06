package slerp3d;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.TextureLoader;
import math.QuatMath;
import quaternion.Quaternionf;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import texture.Texture;
import vector.Vector3f;

public class Test extends StandardGame {
	Box q1, q2, q3, q4, q5, q6, q7, q8, q9;
	Quaternionf c1, c2, c3, c4;
	float t = 0;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);
		display.bindMouse();

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(new Texture(TextureLoader.loadTexture("res/textures/textureCoordinateTest.png")));
		addShader(textureshader);

		q1 = new Box(0, 0, 0, 1, 1, 1);
		q2 = new Box(3, 0, 0, 1, 1, 1);
		q3 = new Box(6, 0, 0, 1, 1, 1);
		q4 = new Box(0, 3, 0, 1, 1, 1);
		q5 = new Box(3, 3, 0, 1, 1, 1);
		q6 = new Box(6, 3, 0, 1, 1, 1);
		q7 = new Box(0, 6, 0, 1, 1, 1);
		q8 = new Box(3, 6, 0, 1, 1, 1);
		q9 = new Box(6, 6, 0, 1, 1, 1);

		q1.setRenderHints(false, true, false);
		q2.setRenderHints(false, true, false);
		q3.setRenderHints(false, true, false);
		q4.setRenderHints(false, true, false);
		q5.setRenderHints(false, true, false);
		q6.setRenderHints(false, true, false);
		q7.setRenderHints(false, true, false);
		q8.setRenderHints(false, true, false);
		q9.setRenderHints(false, true, false);

		c1 = new Quaternionf();
		c2 = new Quaternionf();
		c3 = new Quaternionf();
		c4 = new Quaternionf();

		c2.rotate(90, new Vector3f(0, 1, 0));
		c3.rotate(180, new Vector3f(0, 1, 0));
		c4.rotate(-90, new Vector3f(0, 1, 0));

		textureshader.addObject(q1);
		textureshader.addObject(q2);
		textureshader.addObject(q3);
		textureshader.addObject(q4);
		textureshader.addObject(q5);
		textureshader.addObject(q6);
		textureshader.addObject(q7);
		textureshader.addObject(q8);
		textureshader.addObject(q9);
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
		float d = delta / 1000f;

		t += d;
		if (t >= 1)
			t -= 1;
		q1.rotate(0, d * 90f, 0);
		q2.rotateTo(QuatMath.lerp(c1, c2, t));
		q3.rotateTo(QuatMath.slerp(c1, c2, t));

		q4.rotate(0, d * 180f, 0);
		q5.rotateTo(QuatMath.lerp(c1, c3, t));
		q6.rotateTo(QuatMath.slerp(c1, c3, t));

		q7.rotate(0, -d * 90f, 0);
		q8.rotateTo(QuatMath.lerp(c1, c4, t));
		q9.rotateTo(QuatMath.slerp(c1, c4, t));

		cam.update(delta);
	}

}
