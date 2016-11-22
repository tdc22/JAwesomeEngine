package particleSimple;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import particle.SimpleParticleSource;
import shader.Shader;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;

public class Particle extends StandardGame {
	Debugger debugger;
	SimpleParticleSource particlesource;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Particles", false), new PixelFormat(),
				new VideoSettings(), new NullSoundEnvironment());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);
		display.bindMouse();

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
		addShader(particleshader);

		particlesource = new SimpleParticleSource(new Vector3f(0, 0, 0), new Vector3f(), new Vector3f(0, 0, 0),
				new Vector3f(360, 360, 360), 0.001f, 0.002f, 0.3f, 0.3f, 3000, 3500, 1f, cam, true);
		particlesource.getParticleSystem().getParticleObject().setRenderHints(true, true, false);
		particleshader.addObject(particlesource.getParticleSystem());
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
		particlesource.update(delta);

		debugger.update(fps, 0, 0);
		cam.update(delta);
	}

}
