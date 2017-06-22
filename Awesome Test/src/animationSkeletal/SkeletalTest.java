package animationSkeletal;

import anim.BoneAnimationSkeleton3;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import loader.AnimationLoader;
import loader.ColladaLoader;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import objects.ShapedObject3;
import shader.Shader;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.Debugger;

public class SkeletalTest extends StandardGame {
	Shader textureshader;
	BoneAnimationSkeleton3 animationskeleton;
	Debugger debugger;

	String modelname = "res/models/tmModel.dae";

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface, font, cam);

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/tmModelTexture.png"));
		textureshader = new Shader(ShaderLoader.loadShaderFromFile("res/shaders/animationshader.vert",
				"res/shaders/animationshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		ShapedObject3 model = ModelLoader.load(modelname);
		animationskeleton = AnimationLoader.load(modelname, model, ColladaLoader.getLastVertexIndexMap());
		animationskeleton.getShape().setRenderHints(false, true, false);
		animationskeleton.getAnimation().setSpeed(0.001f);

		textureshader.addObject(animationskeleton.getShape());
		model.rotate(-90, 0, 0);

		textureshader.addArgumentName("u_jointTransforms");
		textureshader.addArgument(animationskeleton.getJointTransforms());
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
		debugger.end();
		renderInterfaceLayer();
	}

	@Override
	public void update(int delta) {
		debugger.update(fps, 0, 0);
		cam.update(delta);
		animationskeleton.update(delta);

		textureshader.setArgument("u_jointTransforms", animationskeleton.getJointTransforms()); // TODO:
																								// perf
	}
}
