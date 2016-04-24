package particleSimple2d;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import particle.SimpleParticleSource2;
import shader.Shader;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.Debugger;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

public class Particle2d extends StandardGame {
	Debugger debugger;
	SimpleParticleSource2 particlesource;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Particles", false), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/particle.png"));
		Shader particleshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/particleshader.vert", "res/shaders/particleshader.frag"));
		particleshader.addArgumentName("u_texture");
		particleshader.addArgument(texture);
		particleshader.addArgumentName("u_color");
		particleshader.addArgument(new Vector3f(1, 0, 0));
		addShader2d(particleshader);

		particlesource = new SimpleParticleSource2(new Vector2f(200, 150), new Vector2f(), new Vector1f(0),
				new Vector1f(360), 0.1f, 0.2f, 10f, 10f, 3000, 3500, 1f);
		particlesource.getObject().setRenderHints(true, true, false);
		particleshader.addObject(particlesource);
	}

	@Override
	public void render() {
	}

	@Override
	public void render2d() {
		debugger.begin();
		render2dLayer();
	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
		debugger.end();
	}

	@Override
	public void update(int delta) {
		particlesource.update(delta);

		if (inputs.isKeyDown("I"))
			cam2d.translate(delta, 0);
		if (inputs.isKeyDown("U"))
			cam2d.translate(-delta, 0);

		debugger.update(fps, 0, 0);
		cam.update(delta);
	}

}
