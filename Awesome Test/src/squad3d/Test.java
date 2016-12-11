package squad3d;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import math.QuatMath;
import quaternion.Quaternionf;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import vector.Vector3f;

public class Test extends StandardGame {
	Box q1, q2;
	Quaternionf c1, c2, c3;
	float t1 = 0, t2 = 0;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);
		display.bindMouse();

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);

		q1 = new Box(0, 0, 0, 1, 1, 1);
		q2 = new Box(3, 0, 0, 1, 1, 1);

		c1 = new Quaternionf();
		c2 = new Quaternionf();
		c3 = new Quaternionf();

		c2.rotate(90, new Vector3f(0, 1, 0));
		c3.rotate(180, new Vector3f(0, 1, 0));

		defaultshader.addObject(q1);
		defaultshader.addObject(q2);
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

		t1 += d;
		if (t1 >= 1)
			t1 -= 1;
		q1.rotate(0, d * 90f, 0);
		q2.rotateTo(QuatMath.squa.lerp(c1, c2, t1));
		q3.rotateTo(QuatMath.slerp(c1, c2, t1));

		t2 += d;
		if (t2 >= 1)
			t2 -= 1;
		q4.rotate(0, d * 180f, 0);
		q5.rotateTo(QuatMath.lerp(c1, c3, t2));
		q6.rotateTo(QuatMath.slerp(c1, c3, t2));

		cam.update(delta);
	}

}
