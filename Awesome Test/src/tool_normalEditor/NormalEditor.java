package tool_normalEditor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import input.MouseInput;
import loader.ShaderLoader;
import loader.TextureLoader;
import math.VecMath;
import shader.Shader;
import shape2d.Circle;
import shape2d.Quad;
import texture.Texture;
import utils.Pair;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class NormalEditor extends StandardGame {
	Shader markershader;
	List<NormalMarker> markers;
	NormalMarker lastMarker;
	Circle normalCircle;
	int width, height;
	Texture normalMap;
	Shader normalmapshader;
	Vector2f offset = new Vector2f(400, 300);
	Vector2f size = new Vector2f(200, 200);
	BufferedImage normalMapImage;

	InputEvent leftMousePressed, leftMouseReleased, inputExport;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Normalmap Editor", true, false), new PixelFormat(),
				new VideoSettings());

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(defaultshader);

		BufferedImage diffTex = null;
		try {
			diffTex = ImageIO
					.read(new File("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_hand1_1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = diffTex.getWidth();
		height = diffTex.getHeight();
		Texture diffuseTexture = new Texture(TextureLoader.loadTexture(diffTex, true));
		Shader testtextureshader = new Shader(new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag")));
		testtextureshader.addArgumentName("u_texture");
		testtextureshader.addArgument(diffuseTexture);
		addShader2d(testtextureshader);

		defaultshader.addObject(new Quad(offset, size));
		Quad textureHolder = new Quad(offset, size);
		textureHolder.setRenderHints(false, true, true);
		testtextureshader.addObject(textureHolder);

		Shader colorshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("u_color");
		colorshader.addArgument(new Vector4f(1f, 1f, 1f, 0.5f));
		addShader2d(colorshader);
		normalCircle = new Circle(0, 0, 50, 36);
		colorshader.addObject(normalCircle);
		normalCircle.setRendered(false);

		markers = new ArrayList<NormalMarker>();

		normalmapshader = new Shader(new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag")));
		normalmapshader.addArgumentName("u_texture");
		normalmapshader.addArgument(new Texture());
		addShader2d(normalmapshader);
		Quad normalmapdisplay = new Quad(offset, size);
		normalmapdisplay.setRenderHints(false, true, false);
		normalmapshader.addObject(normalmapdisplay);
		updateNormalMap();

		markershader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader2d(markershader);

		leftMousePressed = new InputEvent("leftMousePressed",
				new Input(Input.MOUSE_EVENT, "0", MouseInput.MOUSE_BUTTON_PRESSED));
		leftMouseReleased = new InputEvent("leftMouseReleased",
				new Input(Input.MOUSE_EVENT, "0", MouseInput.MOUSE_BUTTON_RELEASED));
		inputExport = new InputEvent("inputExport", new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		inputs.addEvent(leftMousePressed);
		inputs.addEvent(leftMouseReleased);
		inputs.addEvent(inputExport);
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

	}

	@Override
	public void update(int delta) {
		if (leftMousePressed.isActive()) {
			NormalMarker marker = new NormalMarker(new Vector2f(inputs.getMouseX(), inputs.getMouseY()),
					new Vector3f(0, 1, 0));
			lastMarker = marker;
			markershader.addObject(marker);
			markers.add(marker);
			normalCircle.translateTo(marker.getTranslation());
			normalCircle.setRendered(true);
		}
		if (leftMouseReleased.isActive()) {
			normalCircle.setRendered(false);
			float x = inputs.getMouseX();
			float y = inputs.getMouseY();
			Vector2f dir = new Vector2f(x - lastMarker.getTranslation().x, y - lastMarker.getTranslation().y);
			if (dir.length() > normalCircle.getRadius()) {
				dir = VecMath.setScale(dir, normalCircle.getRadius());
			}
			dir.scale(1 / (float) normalCircle.getRadius());
			Vector3f result = new Vector3f(dir.x, dir.y, Math.cos(dir.length() * Math.PI / 2f));
			result.normalize();
			lastMarker.normal = result;
			updateNormalMap();
		}
		if (inputExport.isActive()) {
			export();
		}
	}

	public void updateNormalMap() {
		if (normalMap != null) {
			normalMap.delete();
		}
		normalMapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		List<Pair<Float, NormalMarker>> markerdistances = new ArrayList<Pair<Float, NormalMarker>>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int r = 0;
				int g = 0;
				int b = 0;

				markerdistances.clear();
				for (NormalMarker marker : markers) {
					float mx = (((marker.getTranslation().x - offset.x + size.x) / (2 * size.x)) * width);
					float my = (((marker.getTranslation().y - offset.y + size.y) / (2 * size.y)) * height);
					float currdist = (float) new Vector2f(mx - x, my - y).length();
					markerdistances.add(new Pair<Float, NormalMarker>(currdist, marker));
				}

				if (markerdistances.size() > 0) {
					float alldistances = 0;
					for (Pair<Float, NormalMarker> dist : markerdistances) {
						alldistances += dist.getFirst();
					}
					float rv = 0;
					float gv = 0;
					float bv = 0;
					for (Pair<Float, NormalMarker> marker : markerdistances) {
						float w = (float) (Math.pow(marker.getFirst(), -2) / Math.pow(alldistances, -2));
						// System.out.println(w);
						rv += w * marker.getSecond().normal.x;
						gv += w * marker.getSecond().normal.y;
						bv += w * marker.getSecond().normal.z;
					}
					Vector3f color = new Vector3f(rv, gv, bv);
					color.normalize();
					r = (int) (color.x * 127 + 127);
					g = (int) (color.y * 127 + 127);
					b = (int) (color.z * 127 + 127);
				}
				normalMapImage.setRGB(x, y, (127 << 24) | (r << 16) | (g << 8) | b);
			}
		}
		normalMap = new Texture(TextureLoader.loadTexture(normalMapImage, true));
		normalmapshader.setArgument("u_texture", normalMap);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				normalMapImage.setRGB(x, y, (255 << 24) | normalMapImage.getRGB(x, y));
			}
		}
	}

	public void export() {
		File outputfile = new File("/home/oliver/Pictures/normalmap.png");
		try {
			ImageIO.write(normalMapImage, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("File saved!");
	}
}