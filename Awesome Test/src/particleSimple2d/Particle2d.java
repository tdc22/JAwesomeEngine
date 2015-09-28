package particleSimple2d;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.TextureLoader;
import particle.SimpleParticleSource2;
import shader.Shader;
import texture.Texture;
import vector.Vector2f;
import vector.Vector3f;

public class Particle2d extends StandardGame {
	SimpleParticleSource2 particlesource;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/particle.png"));
		Shader particleshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/particleshader.vert", "res/shaders/particleshader.frag"));
		particleshader.addArgumentName("u_texture");
		particleshader.addArgument(texture);
		particleshader.addArgumentName("u_color");
		particleshader.addArgument(new Vector3f(1, 0, 0));
		addShader2d(particleshader);

		particlesource = new SimpleParticleSource2(new Vector2f(200, 150), new Vector2f(10, 10),
				new Vector2f(-0.16, -0.16), new Vector2f(0.16, 0.16), 10f, 10f, 1500, 2000, 5, 6);
		particlesource.getObject().setRenderHints(false, true, false);
		particleshader.addObject(particlesource);
	}

	@Override
	public void render() {
	}

	@Override
	public void render2d() {
		render2dScene();
	}

	@Override
	public void update(int delta) {
		particlesource.update(delta);

		if (inputs.isKeyDown("I"))
			cam2d.translate(delta, 0);
		if (inputs.isKeyDown("U"))
			cam2d.translate(-delta, 0);

		cam.update(delta);
	}

}
