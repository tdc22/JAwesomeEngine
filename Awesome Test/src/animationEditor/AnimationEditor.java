package animationEditor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import input.MouseInput;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import math.VecMath;
import matrix.Matrix4f;
import objects.ShapedObject2;
import shader.Shader;
import shape2d.Quad;
import texture.Texture;
import utils.GLConstants;
import utils.ProjectionHelper;
import vector.Vector2f;
import vector.Vector4f;

public class AnimationEditor extends StandardGame {
	InputEvent leftMousePressed, leftMouseDown, leftMouseReleased, rightMouseReleased, closePath, deleteMarker,
			addLayer, switchLayer, print, togglemarkers;

	Matrix4f clickprojectionmatrix;
	Shader defaultshader, markershader, textureshader, colorshader;
	float aspect;

	Font font;
	Texture[] textures;
	Vector2f[] sizes;

	List<Shader> layertexts;
	List<AnimationPath> paths;
	AnimationPath currentpath;
	int currentpathID;

	final float scale = 6f;

	AnimationCenter animationcenter;

	boolean showmarkers = true;

	JSlider slider;

	private final static float headX = 2.2f, headY = 2.2f;
	private final static float bodyX = 2.2f, bodyY = 3.4f;
	private final static float handX = 1, handY = 1;
	private final static float footX = 1.5f, footY = 0.8f;
	final int MAXLAYERS = 6;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Animation Editor", true, false),
				new PixelFormat().withSamples(0), new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		aspect = settings.getResolutionX() / (float) settings.getResolutionY();
		Vector2f screensize = new Vector2f(aspect * 100, 100);
		clickprojectionmatrix = ProjectionHelper.ortho(0, screensize.x, screensize.y, 0, -1, 1);
		layer2d.setProjectionMatrix(clickprojectionmatrix);
		System.out.println(clickprojectionmatrix);

		JFrame sliderframe = new JFrame("Animation-Timer");
		slider = new JSlider(0, 100000, 0);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float pathvalue = slider.getValue() / 100000f;
				for (AnimationPath abp : paths)
					abp.setAnimationTimer(pathvalue);
			}
		});
		sliderframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sliderframe.getContentPane().add(slider);
		sliderframe.pack();
		sliderframe.setSize(display.getWidth(), sliderframe.getHeight());
		sliderframe.setLocation(display.getPositionX(), display.getPositionY() + display.getHeight() + 5);
		sliderframe.setVisible(true);

		textures = new Texture[6];
		textures[0] = new Texture(
				TextureLoader.loadTexture("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_head1.png"));
		textures[1] = new Texture(
				TextureLoader.loadTexture("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_body1.png"));
		textures[2] = new Texture(
				TextureLoader.loadTexture("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_hand1_1.png"));
		textures[3] = new Texture(
				TextureLoader.loadTexture("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_hand1_1.png"));
		textures[4] = new Texture(
				TextureLoader.loadTexture("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_foot1_1.png"));
		textures[5] = new Texture(
				TextureLoader.loadTexture("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_foot1_1.png"));

		sizes = new Vector2f[6];
		sizes[0] = new Vector2f(headX * scale, headY * scale);
		sizes[1] = new Vector2f(bodyX * scale, bodyY * scale);
		sizes[2] = new Vector2f(handX * scale, handY * scale);
		sizes[3] = new Vector2f(handX * scale, handY * scale);
		sizes[4] = new Vector2f(footX * scale, footY * scale);
		sizes[5] = new Vector2f(footX * scale, footY * scale);

		textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		Shader t1 = new Shader(textureshader);
		t1.addArgument("u_texture", textures[0]);
		addShader2d(t1);
		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);
		markershader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(markershader);

		colorshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("u_color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		layertexts = new ArrayList<Shader>();

		font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		Text t = new Text("Layer 0", 10, display.getHeight() - 6, font, 16);
		Shader lt1 = new Shader(colorshader);
		lt1.addObject(t);
		layertexts.add(lt1);
		addShaderInterface(lt1);

		animationcenter = new AnimationCenter(new Vector2f(66, 50));
		defaultshader.addObject(animationcenter);

		paths = new ArrayList<AnimationPath>();
		currentpathID = 0;
		currentpath = new AnimationPath(defaultshader, markershader, t1, sizes[0]);
		paths.add(currentpath);

		leftMousePressed = new InputEvent("leftMousePressed",
				new Input(Input.MOUSE_EVENT, "0", MouseInput.MOUSE_BUTTON_PRESSED));
		leftMouseDown = new InputEvent("leftMouseDown",
				new Input(Input.MOUSE_EVENT, "0", MouseInput.MOUSE_BUTTON_DOWN));
		leftMouseReleased = new InputEvent("leftMouseReleased",
				new Input(Input.MOUSE_EVENT, "0", MouseInput.MOUSE_BUTTON_RELEASED));
		rightMouseReleased = new InputEvent("rightMouseReleased",
				new Input(Input.MOUSE_EVENT, "1", MouseInput.MOUSE_BUTTON_RELEASED));
		closePath = new InputEvent("closePath", new Input(Input.KEYBOARD_EVENT, "W", KeyInput.KEY_PRESSED));
		deleteMarker = new InputEvent("deleteCurve", new Input(Input.KEYBOARD_EVENT, "D", KeyInput.KEY_PRESSED));
		addLayer = new InputEvent("addLayer", new Input(Input.KEYBOARD_EVENT, "R", KeyInput.KEY_PRESSED));
		switchLayer = new InputEvent("switchLayer", new Input(Input.KEYBOARD_EVENT, "Tab", KeyInput.KEY_PRESSED));
		print = new InputEvent("print", new Input(Input.KEYBOARD_EVENT, "Q", KeyInput.KEY_PRESSED));
		togglemarkers = new InputEvent("rightMouseReleased",
				new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));

		inputs.addEvent(leftMousePressed);
		inputs.addEvent(leftMouseDown);
		inputs.addEvent(leftMouseReleased);
		inputs.addEvent(rightMouseReleased);
		inputs.addEvent(closePath);
		inputs.addEvent(deleteMarker);
		inputs.addEvent(addLayer);
		inputs.addEvent(switchLayer);
		inputs.addEvent(print);
		inputs.addEvent(togglemarkers);

		defaultshader.addObject(new Quad(133, 100, 1, 1));
	}

	@Override
	public void render() {
	}

	@Override
	public void render2d() {
		render2dLayer();
	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
	}

	@Override
	public void update(int delta) {
		if (leftMousePressed.isActive()) {
			Vector2f pos = new Vector2f(inputs.getMouseX() / (float) settings.getResolutionX(),
					inputs.getMouseY() / (float) settings.getResolutionY());
			pos.x *= 133;
			pos.y *= 100;

			currentpath.clickLeft(pos);
		}
		if (leftMouseDown.isActive()) {
			Vector2f pos = new Vector2f(inputs.getMouseX() / (float) settings.getResolutionX(),
					inputs.getMouseY() / (float) settings.getResolutionY());
			pos.x *= 133;
			pos.y *= 100;

			currentpath.downLeft(pos);
		}
		if (leftMouseReleased.isActive()) {
			Vector2f pos = new Vector2f(inputs.getMouseX() / (float) settings.getResolutionX(),
					inputs.getMouseY() / (float) settings.getResolutionY());
			pos.x *= 133;
			pos.y *= 100;

			currentpath.releaseLeft(pos);
		}
		if (closePath.isActive()) {
			currentpath.closePath();
		}
		if (deleteMarker.isActive()) {
			currentpath.deleteCurve();
		}
		if (addLayer.isActive()) {
			if (paths.size() < MAXLAYERS) {
				Text t = new Text("Layer " + paths.size(), 10, display.getHeight() - 16 * (paths.size()) - 6, font, 16);
				Shader lta = new Shader(colorshader);
				lta.addObject(t);
				layertexts.add(lta);
				addShaderInterface(lta);
				layertexts.get(paths.size() - 1).setArgument("u_color", new Vector4f(1, 1, 1, 1));

				Shader ta = new Shader(textureshader);
				ta.addArgument("u_texture", textures[paths.size()]);
				addShader2d(ta);
				paths.add(new AnimationPath(defaultshader, markershader, ta, sizes[paths.size()]));
				System.out.println("Layer added!");

				layer2d.getShader().remove(defaultshader);
				layer2d.getShader().remove(markershader);
				layer2d.getShader().add(defaultshader);
				layer2d.getShader().add(markershader);

				currentpathID++;
				currentpath = paths.get(currentpathID);
			} else {
				System.out.println("Maximum number of paths reached!");
			}
		}
		if (switchLayer.isActive()) {
			layertexts.get(currentpathID).setArgument("u_color", new Vector4f(1, 1, 1, 1));
			currentpathID++;
			currentpathID %= paths.size();
			currentpath = paths.get(currentpathID);
			layertexts.get(currentpathID).setArgument("u_color", new Vector4f(1, 0, 0, 1));
			System.out.println("Switched to layer: " + currentpathID);
		}
		if (print.isActive()) {
			System.out.println("---------- Start Output ----------");
			float invscale = 1 / scale;
			for (int i = 0; i < paths.size(); i++) {
				System.out.println("Path " + i);
				AnimationPath ap = paths.get(i);
				System.out.println("Translationpaths");
				for (int a = 0; a < ap.beziercurves.size(); a++) {
					RenderedBezierCurve bc = ap.beziercurves.get(a);
					Vector2f neg = VecMath.negate(animationcenter.getTranslation());
					bc.bezier.getP0().translate(neg);
					bc.bezier.getP1().translate(neg);
					bc.bezier.getP2().translate(neg);
					bc.bezier.getP3().translate(neg);
					bc.bezier.getP0().scale(invscale);
					bc.bezier.getP1().scale(invscale);
					bc.bezier.getP2().scale(invscale);
					bc.bezier.getP3().scale(invscale);
					System.out.println(bc);
					bc.bezier.getP0().scale(scale);
					bc.bezier.getP1().scale(scale);
					bc.bezier.getP2().scale(scale);
					bc.bezier.getP3().scale(scale);
					bc.bezier.getP0().translate(animationcenter.getTranslation());
					bc.bezier.getP1().translate(animationcenter.getTranslation());
					bc.bezier.getP2().translate(animationcenter.getTranslation());
					bc.bezier.getP3().translate(animationcenter.getTranslation());
				}
				System.out.println("Rotationpaths");
				for (int b = 0; b < ap.squadcurves.size(); b++) {
					System.out.println(ap.squadcurves.get(b));
				}
			}
			System.out.println("---------- End Output ----------");
		}
		if (togglemarkers.isActive()) {
			showmarkers = !showmarkers;
			markershader.setRendered(showmarkers);
		}
	}

	private class AnimationCenter extends ShapedObject2 {
		public AnimationCenter(Vector2f pos) {
			super(pos);
			setRenderMode(GLConstants.TRIANGLES);
			addVertex(new Vector2f(0, 1));
			addVertex(new Vector2f(1, -1));
			addVertex(new Vector2f(-1, -1));
			addIndices(0, 1, 2);
			prerender();
		}
	}
}
