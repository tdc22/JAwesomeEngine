package tool_animationEditor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import curves.BezierCurve2;
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
import loader.FileLoader;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import math.ComplexMath;
import math.VecMath;
import matrix.Matrix4f;
import objects.ShapedObject2;
import quaternion.Complexf;
import shader.Shader;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.GLConstants;
import utils.ProjectionHelper;
import vector.Vector2f;
import vector.Vector4f;

public class AnimationEditor extends StandardGame {
	InputEvent leftMousePressed, leftMouseDown, leftMouseReleased, rightMouseReleased, closePath, deleteMarker,
			addLayer, switchLayer, print, togglemarkers, mirror, swapPointOrder;

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

	String[] pathnames = new String[] { "headPath", "bodyPath", "handPath1", "handPath2", "footPath1", "footPath2" };
	String[] rotationnames = new String[] { "headRotation", "bodyRotation", "handRotation1", "handRotation2",
			"footRotation1", "footRotation2" };

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Animation Editor", true, false),
				new PixelFormat().withSamples(0), new VideoSettings(), new NullSoundEnvironment());

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
				TextureLoader.loadTexture("../../2dplatformer/2dPlatformer/res/textures/dumb2_head1.png"));
		textures[1] = new Texture(
				TextureLoader.loadTexture("../../2dplatformer/2dPlatformer/res/textures/dumb2_body1.png"));
		textures[2] = new Texture(
				TextureLoader.loadTexture("../../2dplatformer/2dPlatformer/res/textures/dumb2_hand1_1.png"));
		textures[3] = new Texture(
				TextureLoader.loadTexture("../../2dplatformer/2dPlatformer/res/textures/dumb2_hand1_1.png"));
		textures[4] = new Texture(
				TextureLoader.loadTexture("../../2dplatformer/2dPlatformer/res/textures/dumb2_foot1_1.png"));
		textures[5] = new Texture(
				TextureLoader.loadTexture("../../2dplatformer/2dPlatformer/res/textures/dumb2_foot1_1.png"));

		sizes = new Vector2f[6];
		sizes[0] = new Vector2f(headX * scale, headY * scale);
		sizes[1] = new Vector2f(bodyX * scale, bodyY * scale);
		sizes[2] = new Vector2f(handX * scale, handY * scale);
		sizes[3] = new Vector2f(handX * scale, handY * scale);
		sizes[4] = new Vector2f(footX * scale, footY * scale);
		sizes[5] = new Vector2f(footX * scale, footY * scale);

		animationcenter = new AnimationCenter(new Vector2f(66, 50));

		Texture testtexture = new Texture(
				TextureLoader.loadTexture("../../2dplatformer/2dPlatformer/res/textures/testtexture.png"));
		Shader testtextureshader = new Shader(new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag")));
		testtextureshader.addArgumentName("u_texture");
		testtextureshader.addArgument(testtexture);
		addShader2d(testtextureshader);

		Quad player = new Quad(animationcenter.getTranslation(), 5 * scale, 6 * scale);
		player.setRenderHints(false, true, false);
		testtextureshader.addObject(player);

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

		defaultshader.addObject(animationcenter);

		paths = new ArrayList<AnimationPath>();
		currentpathID = 0;
		currentpath = new AnimationPath(defaultshader, markershader, t1, sizes[0]);
		paths.add(currentpath);

		loadFile();

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
		togglemarkers = new InputEvent("togglemarkers", new Input(Input.KEYBOARD_EVENT, "E", KeyInput.KEY_PRESSED));
		mirror = new InputEvent("mirror", new Input(Input.KEYBOARD_EVENT, "T", KeyInput.KEY_PRESSED));
		swapPointOrder = new InputEvent("swapPointOrder", new Input(Input.KEYBOARD_EVENT, "O", KeyInput.KEY_PRESSED));

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
		inputs.addEvent(mirror);
		inputs.addEvent(swapPointOrder);

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
	
	private Vector2f getClickPosition() {
		Vector2f pos = new Vector2f(inputs.getMouseX() / (float) settings.getResolutionX(),
				inputs.getMouseY() / (float) settings.getResolutionY());
		pos.x *= 133;
		pos.y *= 100;
		return pos;
	}

	@Override
	public void update(int delta) {
		if (leftMousePressed.isActive()) {
			currentpath.clickLeft(getClickPosition());
		}
		if (leftMouseDown.isActive()) {
			currentpath.downLeft(getClickPosition());
		}
		if (leftMouseReleased.isActive()) {
			currentpath.releaseLeft(getClickPosition());
		}
		if (closePath.isActive()) {
			currentpath.closePath();
		}
		if (deleteMarker.isActive()) {
			currentpath.deleteCurve();
		}
		if (addLayer.isActive()) {
			if (paths.size() < MAXLAYERS) {
				addLayer();
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
				// System.out.println("Translationpaths");
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
					System.out.println(
							pathnames[i] + ".addCurve(" + bc.toString().replace("BezierCurve[", "new BezierCurve2(")
									.replace("Vector2f[", "new Vector2f(").replace("]", ")") + ");");
					bc.bezier.getP0().scale(scale);
					bc.bezier.getP1().scale(scale);
					bc.bezier.getP2().scale(scale);
					bc.bezier.getP3().scale(scale);
					bc.bezier.getP0().translate(animationcenter.getTranslation());
					bc.bezier.getP1().translate(animationcenter.getTranslation());
					bc.bezier.getP2().translate(animationcenter.getTranslation());
					bc.bezier.getP3().translate(animationcenter.getTranslation());
				}
				// System.out.println("Rotationpaths");
				for (int b = 0; b < ap.squadcurves.size(); b++) {
					System.out.println(rotationnames[i] + ".addCurve("
							+ ap.squadcurves.get(b).toString().replace("SquadCurve[", "new SquadCurve2(")
									.replace("Complexf[", "new Complexf(").replace("]", ")")
							+ ");");
				}
			}
			System.out.println("---------- End Output ----------");
		}
		if (togglemarkers.isActive()) {
			showmarkers = !showmarkers;
			markershader.setRendered(showmarkers);
		}
		if (mirror.isActive()) {
			Vector2f center = animationcenter.getTranslation();
			for (AnimationPath ap : paths) {
				for (int a = 0; a < ap.beziercurves.size(); a++) {
					RenderedBezierCurve bc = ap.beziercurves.get(a);
					Vector2f a0 = new Vector2f((center.x - bc.bezier.getP0().x) * 2, 0);
					Vector2f a1 = new Vector2f((center.x - bc.bezier.getP1().x) * 2, 0);
					Vector2f a2 = new Vector2f((center.x - bc.bezier.getP2().x) * 2, 0);
					Vector2f a3 = new Vector2f((center.x - bc.bezier.getP3().x) * 2, 0);
					bc.bezier.getP0().translate(a0);
					bc.bezier.getP1().translate(a1);
					bc.bezier.getP2().translate(a2);
					bc.bezier.getP3().translate(a3);
					ap.markers.get(a * 4).translate(bc.bezier.getP0());
					ap.markers.get(a * 4 + 1).translateTo(bc.bezier.getP1());
					ap.markers.get(a * 4 + 3).translateTo(bc.bezier.getP2());
					ap.markers.get(a * 4 + 2).translateTo(bc.bezier.getP3());
					bc.delete();
					bc = new RenderedBezierCurve(bc.bezier);
					ap.beziercurves.set(a, bc);
				}
				for (int b = 0; b < ap.squadcurves.size(); b++) {
					// ap.rotationreferences.get(b).translateTo(invertRotation(ap.markers.get(b*4).getTranslation(),
					// ap.squadcurves.get(b).getR1()));
				}
				ap.updatePathMarker();
			}
			System.out.println("Mirrored");
		}
		if (swapPointOrder.isActive()) {
			/*
			 * for (int i = 0; i < paths.size(); i++) { AnimationPath ap = paths.get(i); int
			 * halfCurveNum = (int) (ap.beziercurves.size() / 2f); AnimationPath newpath =
			 * new AnimationPath(ap); for(int j = 0; j < ap.beziercurves.size(); j++) { int
			 * oldCurveIndex = (j+halfCurveNum) % ap.beziercurves.size(); BezierCurve2
			 * swapCurve = ap.beziercurves.get(oldCurveIndex).bezier;
			 * newpath.addBezierCurve(new RenderedBezierCurve(new
			 * BezierCurve2(swapCurve.getP0(), swapCurve.getP1(), swapCurve.getP2(),
			 * swapCurve.getP3()))); newpath.markers.add(ap.markers.get(oldCurveIndex));
			 * newpath.markers.add(ap.markers.get(oldCurveIndex + 1));
			 * newpath.markers.add(ap.markers.get(oldCurveIndex + 2));
			 * newpath.markers.add(ap.markers.get(oldCurveIndex + 3));
			 * newpath.rotationreferences.add(ap.rotationreferences.get( oldCurveIndex)); }
			 * newpath.closed = ap.closed; newpath.updateSquad();
			 * newpath.updatePathMarker(); ap.textureshader.removeObject(ap.pathmarker);
			 * ap.pathmarker.delete(); paths.set(i, newpath); newpath.updateSquad(); }
			 * System.out.println("Swapped");
			 */

			System.out.println("---------- Start Output SWAPPED ----------");
			float invscale = 1 / scale;
			for (int i = 0; i < paths.size(); i++) {
				System.out.println("Path " + i);
				AnimationPath ap = paths.get(i);
				// System.out.println("Translationpaths");
				int halfCurveNum = (int) (ap.beziercurves.size() / 2f);
				for (int a = 0; a < ap.beziercurves.size(); a++) {
					int j = (a + halfCurveNum) % ap.beziercurves.size();
					RenderedBezierCurve bc = ap.beziercurves.get(j);
					Vector2f neg = VecMath.negate(animationcenter.getTranslation());
					bc.bezier.getP0().translate(neg);
					bc.bezier.getP1().translate(neg);
					bc.bezier.getP2().translate(neg);
					bc.bezier.getP3().translate(neg);
					bc.bezier.getP0().scale(invscale);
					bc.bezier.getP1().scale(invscale);
					bc.bezier.getP2().scale(invscale);
					bc.bezier.getP3().scale(invscale);
					System.out.println(
							pathnames[i] + ".addCurve(" + bc.toString().replace("BezierCurve[", "new BezierCurve2(")
									.replace("Vector2f[", "new Vector2f(").replace("]", ")") + ");");
					bc.bezier.getP0().scale(scale);
					bc.bezier.getP1().scale(scale);
					bc.bezier.getP2().scale(scale);
					bc.bezier.getP3().scale(scale);
					bc.bezier.getP0().translate(animationcenter.getTranslation());
					bc.bezier.getP1().translate(animationcenter.getTranslation());
					bc.bezier.getP2().translate(animationcenter.getTranslation());
					bc.bezier.getP3().translate(animationcenter.getTranslation());
				}
				// System.out.println("Rotationpaths");
				for (int b = 0; b < ap.squadcurves.size(); b++) {
					int j = (b + halfCurveNum) % ap.beziercurves.size();
					System.out.println(rotationnames[i] + ".addCurve("
							+ ap.squadcurves.get(j).toString().replace("SquadCurve[", "new SquadCurve2(")
									.replace("Complexf[", "new Complexf(").replace("]", ")")
							+ ");");
				}
			}
			System.out.println("---------- End Output SWAPPED ----------");
		}
	}

	private void addLayer() {
		Text t = new Text("Layer " + paths.size(), 10, display.getHeight() - 16 * (paths.size()) - 6, font, 16);
		Shader lta = new Shader(colorshader);
		lta.addObject(t);
		layertexts.add(lta);
		addShaderInterface(lta);
		layertexts.get(currentpathID).setArgument("u_color", new Vector4f(1, 1, 1, 1));

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
	}

	private void loadFile() {
		String input = null;
		try {
			input = FileLoader.readFile("src/tool_animationEditor/AnimationInput.txt");
		} catch (FileNotFoundException e) {
		}
		System.out.println("INPUT: " + input);
		if (input != null && !input.isEmpty()) {
			String[] lines = input.split("\n");
			boolean lastBezier = true;
			boolean lastSquad = false;
			int numBezierLayer = 0;
			int numSquadLayer = -1;
			String lastlayername = "";
			String layername = "";
			boolean firstonPath = true;
			for (int i = 0; i < lines.length; i++) {
				String s = lines[i];
				System.out.println(s);
				if (s.contains("BezierCurve")) {
					layername = s.split("new BezierCurve")[0].replace(" ", "").replace("	", "");
					System.out.println(layername + "; " + lastlayername);
					if (!lastBezier || (lastlayername.length() > 0 && !lastlayername.equals(layername))) {
						currentpath.closed = true;
						ShapedObject2 lastmarker = currentpath.markers.get(currentpath.markers.size() - 2);
						currentpath.markershader.removeObject(lastmarker);
						lastmarker.delete();
						currentpath.markers.set(currentpath.markers.size() - 2, currentpath.markers.get(0));
						numBezierLayer++;
						firstonPath = true;
						System.out.println(numBezierLayer + "; " + layername);
						if (paths.size() <= numBezierLayer) {
							addLayer();
						} else {
							currentpath = paths.get(numBezierLayer);
						}
					}
					lastlayername = layername;
					String[] p = s.replace(" ", "").replace("),", "").replace(")", "").replace(";", "")
							.split("newVector2f\\(");
					Vector2f a = new Vector2f(Float.parseFloat(p[1].split(",")[0]),
							Float.parseFloat(p[1].split(",")[1]));
					Vector2f b = new Vector2f(Float.parseFloat(p[2].split(",")[0]),
							Float.parseFloat(p[2].split(",")[1]));
					Vector2f c = new Vector2f(Float.parseFloat(p[3].split(",")[0]),
							Float.parseFloat(p[3].split(",")[1]));
					Vector2f d = new Vector2f(Float.parseFloat(p[4].split(",")[0]),
							Float.parseFloat(p[4].split(",")[1]));
					a.scale(scale);
					b.scale(scale);
					c.scale(scale);
					d.scale(scale);
					a.translate(animationcenter.getTranslation());
					b.translate(animationcenter.getTranslation());
					c.translate(animationcenter.getTranslation());
					d.translate(animationcenter.getTranslation());
					if (firstonPath)
						currentpath.addQuadMarker(a);
					else {
						currentpath.markers.add(currentpath.markers.get(currentpath.markers.size() - 2));
					}
					currentpath.addCircleMarker(b);
					currentpath.addQuadMarker(d);
					currentpath.addCircleMarker(c);
					currentpath.addBezierCurve(new BezierCurve2(a, b, c, d));
					System.out.println("Path_ADDED " + currentpath.markers.size());
					firstonPath = false;
					lastBezier = true;
				} else {
					lastBezier = false;
				}

				if (s.contains("SquadCurve")) {
					layername = s.split("new SquadCurve")[0].replace(" ", "").replace("	", "");
					if (!lastSquad || (lastlayername.length() > 0 && !lastlayername.equals(layername))) {
						numSquadLayer++;
						System.out.println(numSquadLayer + "; " + layername);
						if (paths.size() <= numSquadLayer) {
							addLayer();
						} else {
							currentpath = paths.get(numSquadLayer);
						}
					}
					lastlayername = layername;
					String[] p = s.replace(" ", "").replace("),", "").replace(")", "").replace(";", "")
							.split("newComplexf\\(");
					// Complexf a = new
					// Complexf(Float.parseFloat(p[1].split(",")[0]),
					// Float.parseFloat(p[1].split(",")[1]));
					Complexf b = new Complexf(Float.parseFloat(p[2].split(",")[0]),
							Float.parseFloat(p[2].split(",")[1]));
					// Complexf c = new
					// Complexf(Float.parseFloat(p[3].split(",")[0]),
					// Float.parseFloat(p[3].split(",")[1]));
					// Complexf d = new
					// Complexf(Float.parseFloat(p[4].split(",")[0]),
					// Float.parseFloat(p[4].split(",")[1]));
					currentpath.addRotationMarker(invertRotation(
							currentpath.markers.get(currentpath.rotationreferences.size() * 4).getTranslation(), b));
					lastSquad = true;
				} else {
					lastSquad = false;
				}
			}

			currentpath.closed = true;
			ShapedObject2 lastmarker = currentpath.markers.get(currentpath.markers.size() - 2);
			currentpath.markershader.removeObject(lastmarker);
			lastmarker.delete();
			currentpath.markers.set(currentpath.markers.size() - 2, currentpath.markers.get(0));

			currentpathID = 0;
			currentpath = paths.get(currentpathID);

			for (AnimationPath ap : paths) {
				ap.updateSquad();
				ap.updatePathMarker();
			}
		}
		else {
			System.out.println("No file found or file is empty. Starting normally.");
		}
	}

	private Vector2f invertRotation(Vector2f pathmarker, Complexf rotation) {
		Vector2f result = new Vector2f(1, 0);
		result = ComplexMath.transform(rotation, result);
		result.scale(10);
		result.translate(pathmarker);
		return result;
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
