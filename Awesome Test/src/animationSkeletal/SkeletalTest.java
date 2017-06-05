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
import math.VecMath;
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

	String modelname = "res/models/Collada_test2.dae";

	Box test1, test2, test3;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		Matrix4f correction = new Matrix4f();
		correction.rotate(-90, new Vector3f(1, 0, 0));

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
		animationskeleton.getRootJoint().setLocalBindTransform(
				VecMath.transformMatrix(animationskeleton.getRootJoint().getLocalBindTransform(), correction));

		System.out.println("LBT 1: " + animationskeleton.getRootJoint().getLocalBindTransform());
		System.out.println("LBT 2: " + animationskeleton.getRootJoint().getChildren().get(0).getLocalBindTransform());
		System.out.println("LBT 3: "
				+ animationskeleton.getRootJoint().getChildren().get(0).getChildren().get(0).getLocalBindTransform());

		animationskeleton.update(0);
		animationskeleton.getShape().setRenderHints(false, true, false);
		textureshader.addObject(animationskeleton.getShape());

		textureshader.addArgumentName("u_jointTransforms");
		textureshader.addArgument(animationskeleton.getJointTransforms());

		animationskeleton.getAnimation().setSpeed(0.0003f);

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
		animationskeleton.update(delta);

		System.out.println("---------------------------------");
		System.out.println(animationskeleton.getAnimationTimer());
		Matrix4f[] jointTransforms = animationskeleton.getJointTransforms();
		for (Matrix4f m : jointTransforms) {
			System.out.println(m);
			// m.setIdentity();
			// m.translate(0, i * 30, 0);
			// i++;
		}
		/*
		 * jointTransforms[0] = new Matrix4f(0.99239707f, 0.12066141f,
		 * -0.024272077f, 0.36732027f, -0.11411235f, 0.8281412f, -0.5487809f,
		 * -0.5231762f, -0.046115983f, 0.5473783f, 0.8356138f, 1.6663418f, 0.0f,
		 * 0.0f, 0.0f, 1.0f); jointTransforms[1] = new Matrix4f(0.99239707f,
		 * 0.12066141f, -0.024272077f, 0.36732024f, -0.11411235f, 0.8281412f,
		 * -0.5487809f, -0.5231763f, -0.046115983f, 0.5473783f, 0.8356138f,
		 * 1.6663414f, 0.0f, 0.0f, 0.0f, 1.0f); jointTransforms[2] = new
		 * Matrix4f(0.99239707f, 0.120661415f, -0.02427207f, 0.3673203f,
		 * -0.114112355f, 0.8281413f, -0.54878086f, -0.52317643f, -0.046115987f,
		 * 0.54737824f, 0.83561385f, 1.6663415f, 0.0f, 0.0f, 0.0f, 1.0f);
		 */

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
