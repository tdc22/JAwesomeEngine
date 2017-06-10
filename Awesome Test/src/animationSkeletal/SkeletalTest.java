package animationSkeletal;

import anim.BoneAnimationSkeleton3;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import loader.AnimationLoader;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import matrix.Matrix4f;
import objects.ShapedObject3;
import shader.Shader;
import shape.Box;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.Debugger;
import vector.Vector3f;

public class SkeletalTest extends StandardGame {
	Shader textureshader;
	BoneAnimationSkeleton3 animationskeleton;
	Debugger debugger;

	String modelname = "res/models/tmModel.dae";

	Box test1, test2, test3;

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
		animationskeleton = AnimationLoader.load(modelname, model);

		System.out.println("LBT 1: " + animationskeleton.getRootJoint().getLocalBindTransform());
		System.out.println("LBT 2: " + animationskeleton.getRootJoint().getChildren().get(0).getLocalBindTransform());
		System.out.println("LBT 3: "
				+ animationskeleton.getRootJoint().getChildren().get(0).getChildren().get(0).getLocalBindTransform());

		animationskeleton.update(0);
		animationskeleton.getShape().setRenderHints(false, true, false);
		textureshader.addObject(animationskeleton.getShape());

		textureshader.addArgumentName("u_jointTransforms");
		textureshader.addArgument(animationskeleton.getJointTransforms());

		animationskeleton.getAnimation().setSpeed(0.001f);

		test1 = new Box(10, 0, 0, 1, 1, 1);
		defaultshader.addObject(test1);
		test2 = new Box(15, 0, 0, 1, 1, 1);
		defaultshader.addObject(test2);
		test3 = new Box(20, 0, 0, 1, 1, 1);
		defaultshader.addObject(test3);
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
		animationskeleton.update(10);

		Matrix4f[] jointTransforms = animationskeleton.getJointTransforms();
		test1.rotateTo(jointTransforms[0].getSubMatrix().toQuaternionf());
		test1.translateTo((Vector3f) jointTransforms[0].getTranslation());
		test2.rotateTo(jointTransforms[1].getSubMatrix().toQuaternionf());
		test2.translateTo((Vector3f) jointTransforms[1].getTranslation());
		test3.rotateTo(jointTransforms[2].getSubMatrix().toQuaternionf());
		test3.translateTo((Vector3f) jointTransforms[2].getTranslation());
		textureshader.setArgument("u_jointTransforms", jointTransforms); // TODO:
																			// perf
	}
}
