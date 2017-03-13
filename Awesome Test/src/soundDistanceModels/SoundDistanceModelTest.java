package soundDistanceModels;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import gui.Text;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.SoundLoader;
import shader.Shader;
import shape.Box;
import sound.ALSound;
import sound.ALSoundEnvironment;
import sound.Sound;
import sound.SoundEnvironment.DistanceModel;
import utils.VectorConstants;

public class SoundDistanceModelTest extends StandardGame {
	Sound sound;
	InputEvent rofUp, rofDown, rdUp, rdDown, maxUp, maxDown, inverseDist, inverseDistC, linearDist, linearDistC,
			expDist, expDistC;

	float rolloffFactor = 1;
	float referenceDistance = 1;
	float maxDistance = 12;

	Text currROF, currRD, currMAX;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new ALSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		sound = new ALSound(SoundLoader.loadSound("/home/oliver/Music/snap.ogg"));
		sound.setSourcePosition(0, 0, 0);
		sound.setLooping(true);
		sound.setSourcePositionRelative(false);
		sound.play();

		sound.setRolloffFactor(rolloffFactor);
		sound.setReferenceDistance(referenceDistance);
		sound.setMaxDistance(maxDistance);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		defaultshader.addObject(new Box(0, 0, 0, 1, 1, 1));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");

		defaultshaderInterface.addObject(
				new Text("Rolloff Factor (U/I):\nReference Distance (J/K):\nMax Distance (N/M):", 10, 30, font, 20));
		defaultshaderInterface.addObject(new Text(
				"1: Inverse Distance\n2: Inverse Distance Clamped\n3: Linear Distance\n4: Linear Distance Clamped\n5: Exponent Distance\n6: Exponent Distance Clamped",
				10, 490, font, 20));

		currROF = new Text(rolloffFactor + "", 300, 30, font, 20);
		currRD = new Text(referenceDistance + "", 300, 52, font, 20);
		currMAX = new Text(maxDistance + "", 300, 74, font, 20);
		defaultshaderInterface.addObject(currROF);
		defaultshaderInterface.addObject(currRD);
		defaultshaderInterface.addObject(currMAX);

		inverseDist = new InputEvent("inverseDist", new Input(Input.KEYBOARD_EVENT, "1", KeyInput.KEY_PRESSED));
		inverseDistC = new InputEvent("inverseDistC", new Input(Input.KEYBOARD_EVENT, "2", KeyInput.KEY_PRESSED));
		linearDist = new InputEvent("linearDist", new Input(Input.KEYBOARD_EVENT, "3", KeyInput.KEY_PRESSED));
		linearDistC = new InputEvent("linearDistC", new Input(Input.KEYBOARD_EVENT, "4", KeyInput.KEY_PRESSED));
		expDist = new InputEvent("expDist", new Input(Input.KEYBOARD_EVENT, "5", KeyInput.KEY_PRESSED));
		expDistC = new InputEvent("expDistC", new Input(Input.KEYBOARD_EVENT, "6", KeyInput.KEY_PRESSED));

		rofUp = new InputEvent("rofUp", new Input(Input.KEYBOARD_EVENT, "U", KeyInput.KEY_DOWN));
		rofDown = new InputEvent("rofDown", new Input(Input.KEYBOARD_EVENT, "I", KeyInput.KEY_DOWN));
		rdUp = new InputEvent("rdUp", new Input(Input.KEYBOARD_EVENT, "J", KeyInput.KEY_DOWN));
		rdDown = new InputEvent("rdDown", new Input(Input.KEYBOARD_EVENT, "K", KeyInput.KEY_DOWN));
		maxUp = new InputEvent("maxUp", new Input(Input.KEYBOARD_EVENT, "N", KeyInput.KEY_DOWN));
		maxDown = new InputEvent("maxDown", new Input(Input.KEYBOARD_EVENT, "M", KeyInput.KEY_DOWN));

		inputs.addEvent(inverseDist);
		inputs.addEvent(inverseDistC);
		inputs.addEvent(linearDist);
		inputs.addEvent(linearDistC);
		inputs.addEvent(expDist);
		inputs.addEvent(expDistC);

		inputs.addEvent(rofUp);
		inputs.addEvent(rofDown);
		inputs.addEvent(rdUp);
		inputs.addEvent(rdDown);
		inputs.addEvent(maxUp);
		inputs.addEvent(maxDown);
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
		renderInterfaceLayer();
	}

	@Override
	public void update(int delta) {
		cam.update(delta);
		soundEnvironment.setListenerPosition(cam.getTranslation());
		soundEnvironment.setListenerOrientation(VectorConstants.UP, cam.getDirection());

		if (inverseDist.isActive()) {
			soundEnvironment.setDistanceModel(DistanceModel.InverseDistance);
		}
		if (inverseDistC.isActive()) {
			soundEnvironment.setDistanceModel(DistanceModel.InverseDistanceClamped);
		}
		if (linearDist.isActive()) {
			soundEnvironment.setDistanceModel(DistanceModel.LinearDistance);
		}
		if (linearDistC.isActive()) {
			soundEnvironment.setDistanceModel(DistanceModel.LinearDistanceClamped);
		}
		if (expDist.isActive()) {
			soundEnvironment.setDistanceModel(DistanceModel.ExponentDistance);
		}
		if (expDistC.isActive()) {
			soundEnvironment.setDistanceModel(DistanceModel.ExponentDistanceClamped);
		}

		if (rofUp.isActive()) {
			rolloffFactor += 0.01 * delta;
			sound.setRolloffFactor(rolloffFactor);
			currROF.setText(rolloffFactor + "");
		}
		if (rofDown.isActive()) {
			rolloffFactor -= 0.01 * delta;
			sound.setRolloffFactor(rolloffFactor);
			currROF.setText(rolloffFactor + "");
		}
		if (rdUp.isActive()) {
			referenceDistance += 0.01 * delta;
			sound.setReferenceDistance(referenceDistance);
			currRD.setText(referenceDistance + "");
		}
		if (rdDown.isActive()) {
			referenceDistance -= 0.01 * delta;
			sound.setReferenceDistance(referenceDistance);
			currRD.setText(referenceDistance + "");
		}
		if (maxUp.isActive()) {
			maxDistance += 0.01 * delta;
			sound.setMaxDistance(maxDistance);
			currMAX.setText(maxDistance + "");
		}
		if (maxDown.isActive()) {
			maxDistance -= 0.01 * delta;
			sound.setMaxDistance(maxDistance);
			currMAX.setText(maxDistance + "");
		}
	}
}
