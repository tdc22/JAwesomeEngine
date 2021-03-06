package tool_animationEditor3d;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import curves.BezierCurve3;
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
import loader.ModelLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import math.QuatMath;
import math.VecMath;
import matrix.Matrix4f;
import objects.ShapedObject3;
import quaternion.Quaternionf;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import sound.NullSoundEnvironment;
import texture.Texture;
import tool_animationEditor.AnimationPath;
import tool_animationEditor.RenderedBezierCurve;
import utils.Debugger;
import utils.GLConstants;
import utils.ProjectionHelper;
import utils.VectorConstants;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class AnimationEditor3 extends StandardGame {
	InputEvent toggleMouseBind, leftMousePressed, leftMouseDown, leftMouseReleased, rightMouseReleased, closePath, deleteMarker, addLayer, switchLayer, print;
	
	Shader colorshader;
	Matrix4f inverseprojectionmatrix;
	
	Font font;
	
	List<Shader> layertexts;
	List<AnimationPath3> paths;
	AnimationPath3 currentpath;
	int currentpathID;
	
	Sphere debugsphere;
	Box debugbox;
	
	Shader defaultshader;
	ShapedObject3[] bodyparts;
	
	final float scale = 1f;
	
	AnimationCenter3 animationcenter;
	
	JSlider slider;
	
	final int MAXLAYERS = 6;
	
	Debugger debugger;
	
	String[] pathnames = new String[] { "headPath", "bodyPath", "handPath1", "handPath2", "footPath1", "footPath2" };
	String[] rotationnames = new String[] { "headRotation", "bodyRotation", "handRotation1", "handRotation2",
			"footRotation1", "footRotation2" };

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Animation Editor", true, false),
				new PixelFormat().withSamples(0), new VideoSettings(), new NullSoundEnvironment());
		//display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 1, 2);
		cam.setFlySpeed(0.005f);
		
		JFrame sliderframe = new JFrame("Animation-Timer");
		slider = new JSlider(0, 100000, 0);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float pathvalue = slider.getValue() / 100000f;
				for (AnimationPath3 abp : paths)
					abp.setAnimationTimer(pathvalue);
			}
		});
		sliderframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sliderframe.getContentPane().add(slider);
		sliderframe.pack();
		sliderframe.setSize(display.getWidth(), sliderframe.getHeight());
		sliderframe.setLocation(display.getPositionX(), display.getPositionY() + display.getHeight() + 5);
		sliderframe.setVisible(true);
		
		defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		
		Shader textureShader = new Shader(
				ShaderLoader.loadShaderFromFile("../../Schall-Game/res/shaders/textureshader.vert", "../../Schall-Game/res/shaders/textureshader.frag"));
		textureShader.addArgument("u_texture",
				new Texture(TextureLoader.loadTexture("../../Schall-Game/res/textures/DefaultPalette.png")));
		addShader(textureShader);
		
		bodyparts = new ShapedObject3[6];
		
		ShapedObject3 head = ModelLoader.load("../../Schall-Game/res/models/playerModel2/head_centered.obj");
		bodyparts[0] = head;
		head.setRenderHints(false, true, true);
		textureShader.addObject(head);
		ShapedObject3 torso = ModelLoader.load("../../Schall-Game/res/models/playerModel2/torso_centered.obj");
		bodyparts[1] = torso;
		torso.setRenderHints(false, true, true);
		textureShader.addObject(torso);
		ShapedObject3 leftHand = ModelLoader.load("../../Schall-Game/res/models/playerModel2/leftHand_centered.obj");
		bodyparts[2] = leftHand;
		leftHand.setRenderHints(false, true, true);
		textureShader.addObject(leftHand);
		ShapedObject3 rightHand = ModelLoader.load("../../Schall-Game/res/models/playerModel2/rightHand_centered.obj");
		bodyparts[3] = rightHand;
		rightHand.setRenderHints(false, true, true);
		textureShader.addObject(rightHand);
		ShapedObject3 leftFoot = ModelLoader.load("../../Schall-Game/res/models/playerModel2/leftFoot_centered.obj");
		bodyparts[4] = leftFoot;
		leftFoot.setRenderHints(false, true, true);
		textureShader.addObject(leftFoot);
		ShapedObject3 rightFoot = ModelLoader.load("../../Schall-Game/res/models/playerModel2/rightFoot_centered.obj");
		bodyparts[5] = rightFoot;
		rightFoot.setRenderHints(false, true, true);
		textureShader.addObject(rightFoot);
		
		animationcenter = new AnimationCenter3(new Vector3f());
		
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
		
		inverseprojectionmatrix = ProjectionHelper.perspective(settings.getFOVy(),
				settings.getResolutionX() / (float) settings.getResolutionY(), settings.getZNear(),
				settings.getZFar());
		inverseprojectionmatrix.invert();
		
		paths = new ArrayList<AnimationPath3>();
		currentpathID = 0;
		currentpath = new AnimationPath3(defaultshader, defaultshader, head);
		paths.add(currentpath);
		
		loadFile();

		toggleMouseBind = new InputEvent("toggleMouseBind",
				new Input(Input.KEYBOARD_EVENT, "Q", MouseInput.MOUSE_BUTTON_PRESSED));
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
		print = new InputEvent("print", new Input(Input.KEYBOARD_EVENT, "P", KeyInput.KEY_PRESSED));

		inputs.addEvent(toggleMouseBind);
		inputs.addEvent(leftMousePressed);
		inputs.addEvent(leftMouseDown);
		inputs.addEvent(leftMouseReleased);
		inputs.addEvent(rightMouseReleased);
		inputs.addEvent(closePath);
		inputs.addEvent(deleteMarker);
		inputs.addEvent(addLayer);
		inputs.addEvent(switchLayer);
		inputs.addEvent(print);
		
		debugger = new Debugger(inputs, defaultshader, lt1, font, cam);
	}
	
	private Vector3f screenPositionToRayDirection(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / settings.getResolutionX() - 1f;
		float y = (2.0f * mouseY) / settings.getResolutionY() - 1f;
		Vector4f clipCoords = new Vector4f(x, -y, -1.0f, 1.0f);
		clipCoords.transform(inverseprojectionmatrix);
		clipCoords.z = -1.0f;
		clipCoords.w = 0;
		
		Matrix4f invertedView = new Matrix4f(cam.getMatrix());
		invertedView.invert();
		clipCoords.transform(invertedView);
		Vector3f mouseRay = new Vector3f(clipCoords.x, clipCoords.y, clipCoords.z);
		mouseRay.normalize();
		
		return mouseRay;
	}
	
	private Vector3f projectClickOntoObjectPlane(Vector3f campos, Vector3f dir, Vector3f planepos) {
		Vector3f camToPlane = VecMath.subtraction(planepos, campos);
		float camToPlaneLength = (float) camToPlane.length();
		if(camToPlane.lengthSquared() > 0) {
			camToPlane.normalize();
		}
		return VecMath.addition(campos, VecMath.scale(dir, camToPlaneLength / VecMath.dotproduct(dir, camToPlane)));
	}

	@Override
	public void update(int delta) {
		if(toggleMouseBind.isActive()) {
			if(display.isMouseBound()) {
				display.unbindMouse();
			}
			else {
				display.bindMouse();
			}
		}
		if (leftMousePressed.isActive()) {
			Vector3f clickdir = screenPositionToRayDirection(inputs.getMouseX(), inputs.getMouseY());
			currentpath.clickLeft(cam.getTranslation(), clickdir, projectClickOntoObjectPlane(cam.getTranslation(), clickdir, VectorConstants.ZERO));
		}
		if (leftMouseDown.isActive()) {
			Vector3f clickdir = screenPositionToRayDirection(inputs.getMouseX(), inputs.getMouseY());
			Vector3f planepos = currentpath.draggedMarker != null ? currentpath.draggedMarker.getTranslation() : currentpath.bodypart.getTranslation();
			Vector3f projectedPos = projectClickOntoObjectPlane(cam.getTranslation(), clickdir, planepos);
			currentpath.downLeft(projectedPos);
		}
		if (leftMouseReleased.isActive()) {
			Vector3f clickdir = screenPositionToRayDirection(inputs.getMouseX(), inputs.getMouseY());
			Vector3f planepos = currentpath.draggedMarker != null ? currentpath.draggedMarker.getTranslation() : currentpath.bodypart.getTranslation();
			currentpath.releaseLeft(projectClickOntoObjectPlane(cam.getTranslation(), clickdir, planepos));
		}
		if (closePath.isActive() && !display.isMouseBound()) {
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
				AnimationPath3 ap = paths.get(i);
				// System.out.println("Translationpaths");
				for (int a = 0; a < ap.beziercurves.size(); a++) {
					RenderedBezierCurve3 bc = ap.beziercurves.get(a);
					Vector3f neg = VecMath.negate(animationcenter.getTranslation());
					bc.bezier.getP0().translate(neg);
					bc.bezier.getP1().translate(neg);
					bc.bezier.getP2().translate(neg);
					bc.bezier.getP3().translate(neg);
					bc.bezier.getP0().scale(invscale);
					bc.bezier.getP1().scale(invscale);
					bc.bezier.getP2().scale(invscale);
					bc.bezier.getP3().scale(invscale);
					System.out.println(
							pathnames[i] + ".addCurve(" + bc.toString().replace("BezierCurve[", "new BezierCurve3(")
									.replace("Vector3f[", "new Vector3f(").replace("]", ")") + ");");
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
							+ ap.squadcurves.get(b).toString().replace("SquadCurve[", "new SquadCurve3(")
									.replace("Quaternionf[", "new Quaternionf(").replace("]", ")")
							+ ");");
				}
			}
			System.out.println("---------- End Output ----------");
		}
		
		debugger.update(fps, 0, 0);
		if(display.isMouseBound()) {
			cam.update(delta);
		}
	}
	
	private void addLayer() {
		Text t = new Text("Layer " + paths.size(), 10, display.getHeight() - 16 * (paths.size()) - 6, font, 16);
		Shader lta = new Shader(colorshader);
		lta.addObject(t);
		layertexts.add(lta);
		addShaderInterface(lta);
		layertexts.get(currentpathID).setArgument("u_color", new Vector4f(1, 1, 1, 1));

		/*Shader ta = new Shader(textureshader);
		ta.addArgument("u_texture", textures[paths.size()]);
		addShader2d(ta);*/
		paths.add(new AnimationPath3(defaultshader, defaultshader, bodyparts[paths.size()]));
		System.out.println("Layer added!");

		layer2d.getShader().remove(defaultshader);
		//layer2d.getShader().remove(markershader);
		layer2d.getShader().add(defaultshader);
		//layer2d.getShader().add(markershader);

		currentpathID++;
		currentpath = paths.get(currentpathID);
	}
	
	private void loadFile() {
		String input = null;
		try {
			input = FileLoader.readFile("src/tool_animationEditor3d/AnimationInput.txt");
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
						ShapedObject3 lastmarker = currentpath.markers.get(currentpath.markers.size() - 2);
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
							.split("newVector3f\\(");
					Vector3f a = new Vector3f(Float.parseFloat(p[1].split(",")[0]),
							Float.parseFloat(p[1].split(",")[1]), Float.parseFloat(p[1].split(",")[2]));
					Vector3f b = new Vector3f(Float.parseFloat(p[2].split(",")[0]),
							Float.parseFloat(p[2].split(",")[1]), Float.parseFloat(p[2].split(",")[2]));
					Vector3f c = new Vector3f(Float.parseFloat(p[3].split(",")[0]),
							Float.parseFloat(p[3].split(",")[1]), Float.parseFloat(p[3].split(",")[2]));
					Vector3f d = new Vector3f(Float.parseFloat(p[4].split(",")[0]),
							Float.parseFloat(p[4].split(",")[1]), Float.parseFloat(p[4].split(",")[2]));
					a.scale(scale);
					b.scale(scale);
					c.scale(scale);
					d.scale(scale);
					a.translate(animationcenter.getTranslation());
					b.translate(animationcenter.getTranslation());
					c.translate(animationcenter.getTranslation());
					d.translate(animationcenter.getTranslation());
					if (firstonPath)
						currentpath.addBoxMarker(a);
					else {
						currentpath.markers.add(currentpath.markers.get(currentpath.markers.size() - 2));
					}
					currentpath.addSphereMarker(b);
					currentpath.addBoxMarker(d);
					currentpath.addSphereMarker(c);
					currentpath.addBezierCurve(new BezierCurve3(a, b, c, d));
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
						.split("newQuaternionf\\(");
					// Complexf a = new
					// Complexf(Float.parseFloat(p[1].split(",")[0]),
					// Float.parseFloat(p[1].split(",")[1]));
					String[] bs = p[2].split(",");
					Quaternionf b = new Quaternionf(Float.parseFloat(bs[0]),
							Float.parseFloat(bs[1]), Float.parseFloat(bs[2]),
							Float.parseFloat(bs[3]));
					System.out.println("b " + b);
					// Complexf c = new
					// Complexf(Float.parseFloat(p[3].split(",")[0]),
					// Float.parseFloat(p[3].split(",")[1]));
					// Complexf d = new
					// Complexf(Float.parseFloat(p[4].split(",")[0]),
					// Float.parseFloat(p[4].split(",")[1]));
					System.out.println("G "  + currentpath + "; " + currentpath.markers.size() + "; "  + currentpath.rotationreferences.size());
					Vector3f markerpos = currentpath.markers.get(currentpath.rotationreferences.size() * 4).getTranslation();
					currentpath.addRotationMarker(invertRotation(markerpos, b, VectorConstants.AXIS_X));
					currentpath.addSecondaryRotationMarker(invertRotation(markerpos, b, VectorConstants.AXIS_Y));
					lastSquad = true;
				} else {
					lastSquad = false;
				}
			}

			currentpath.closed = true;
			ShapedObject3 lastmarker = currentpath.markers.get(currentpath.markers.size() - 2);
			currentpath.markershader.removeObject(lastmarker);
			lastmarker.delete();
			currentpath.markers.set(currentpath.markers.size() - 2, currentpath.markers.get(0));

			currentpathID = 0;
			currentpath = paths.get(currentpathID);

			for (AnimationPath3 ap : paths) {
				ap.updateSquad();
				ap.updatePathMarker();
			}
		}
		else {
			System.out.println("No file found or file is empty. Starting normally.");
		}
	}
	
	private Vector3f invertRotation(Vector3f pathmarker, Quaternionf rotation, Vector3f referenceaxis) {
		Vector3f result = new Vector3f(referenceaxis);
		result = QuatMath.transform(rotation, result);
		result.scale(AnimationPath3.refpointdistance);
		result.translate(pathmarker);
		return result;
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
	
	private class AnimationCenter3 extends ShapedObject3 {
		public AnimationCenter3(Vector3f pos) {
			super(pos);
			scale(0.1f);
			setRenderMode(GLConstants.TRIANGLES);
			addVertex(new Vector3f(-1, -1, -1));
			addVertex(new Vector3f(-1, -1, 1));
			addVertex(new Vector3f(1, -1, 0));
			addVertex(new Vector3f(0, 1, 0));
			addIndices(0, 2, 1, 0, 1, 3, 1, 2, 3, 2, 0, 3);
			prerender();
		}
	}
}