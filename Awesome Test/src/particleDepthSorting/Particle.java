package particleDepthSorting;

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
	SimpleParticleSource particlesource, particlesource2;

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

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/particleCross.png"));
		Shader particleshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/particleshader.vert", "res/shaders/particleshader2.frag"));
		particleshader.addArgumentName("u_texture");
		particleshader.addArgument(texture);
		addShader(particleshader);

		particlesource = new SimpleParticleSource(new Vector3f(0, 0, 0), new Vector3f(), new Vector3f(0, 0, 0),
				new Vector3f(360, 360, 360), 0.001f, 0.002f, 0.3f, 0.3f, 3000, 3500, 0.1f, cam, true);
		particlesource.getParticleSystem().getParticleObject().setRenderHints(true, true, false); // Color is important for particles as it represents lifetime
		particleshader.addObject(particlesource.getParticleSystem());
		
		particlesource2 = new SimpleParticleSource(new Vector3f(5, 0, 0), new Vector3f(), new Vector3f(0, 0, 0),
				new Vector3f(360, 360, 360), 0.001f, 0.002f, 0.3f, 0.3f, 3000, 3500, 0.1f, cam, true); // no depth sorting!
		particlesource2.getParticleSystem().getParticleObject().setRenderHints(true, true, false);
		particleshader.addObject(particlesource2.getParticleSystem());
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
		particlesource2.update(delta);

		debugger.update(fps, 0, 0);
		cam.update(delta);
	}

}
