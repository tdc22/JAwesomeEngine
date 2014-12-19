package game;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glClearStencil;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadMatrix;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glShadeModel;
import gui.Display;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.GLFWInputReader;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.RenderedObject;
import objects.ShapedObject2;

import org.lwjgl.BufferUtils;

public abstract class StandardGame extends AbstractGame {
	protected List<RenderedObject> objects;
	protected List<ShapedObject2> objects2d;
	public VideoSettings settings;
	public Display display;
	public Camera cam;

	public InputManager inputs;
	protected InputEvent closeEvent;

	private FloatBuffer identity;
	boolean render2d = false;

	public void addObject(RenderedObject obj) {
		objects.add(obj);
	}

	public void add2dObject(ShapedObject2 element) {
		if (!render2d)
			render2d = true;
		objects2d.add(element);
	}

	@Override
	protected void deleteAllObjects() {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).delete();
		}
		for (int i = 0; i < objects2d.size(); i++) {
			objects2d.get(i).delete();
		}
	}

	public List<RenderedObject> getObjects() {
		return objects;
	}

	public List<ShapedObject2> get2dObjects() {
		return objects2d;
	}

	public void initDisplay(Display display, DisplayMode displaymode,
			PixelFormat pixelformat, VideoSettings videosettings) {
		this.display = display;
		display.open(displaymode, pixelformat);
		this.settings = videosettings;
		// GLFW input fix
		if (inputs.getInputReader() instanceof GLFWInputReader)
			((GLFWInputReader) inputs.getInputReader())
					.addWindowID(((GLDisplay) display).getWindowID());

	}

	@Override
	protected void initEngine() {
		objects = new ArrayList<RenderedObject>();
		objects2d = new ArrayList<ShapedObject2>();

		// JInputReader jinput = new JInputReader();
		// if (jinput.isUseable()) {
		// inputs = new InputManager(jinput);
		// System.out.println("Using JInput.");
		// } else {
		inputs = new InputManager(new GLFWInputReader());
		System.out.println("Using GLFW input.");
		// }
		closeEvent = new InputEvent("Game_Close", new Input(
				Input.KEYBOARD_EVENT, "Escape", KeyInput.KEY_DOWN));
		inputs.addEvent(closeEvent);

		cam = new Camera(inputs);

		identity = BufferUtils.createFloatBuffer(16 * 4);
		VecMath.identityMatrix().store(identity);
		identity.flip();

		lastFPS = getTime();
	}

	protected void initOpenGL() {
		glShadeModel(GL_SMOOTH); // Enable Smooth Shading
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		glClearDepth(1.0f); // Depth Buffer Setup
		glClearStencil(0);
		glEnable(GL_DEPTH_TEST); // Enables Depth Testing
		glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing To Do
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(identity); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		// GLU.gluPerspective(settings.getFOVy(), settings.getResolutionX()
		// / (float) settings.getResolutionY(), settings.getZNear(),
		// settings.getZFar());
		float fH = (float) (Math.tan(settings.getFOVy() / 360f * Math.PI) * settings
				.getZNear());
		float fW = fH * settings.getResolutionX()
				/ (float) settings.getResolutionY();
		glFrustum(-fW, fW, -fH, fH, settings.getZNear(), settings.getZFar());

		glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix

		// Really Nice Perspective Calculations
		glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}

	@Override
	public boolean isRunning() {
		return !display.isCloseRequested() && running;
	}

	public void mode2d() {
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadMatrix(identity);
		glOrtho(0, settings.getResolutionX(), settings.getResolutionY(), 0, -1,
				1);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrix(identity);
	}

	public void mode3d() {
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
	}

	public void updateEngine() {
		inputs.update();
		if (closeEvent.isActive())
			running = false;
		cam.begin();
	}

	public abstract void render();

	public abstract void render2d();

	public void render2dScene() {
		for (int i = 0; i < objects2d.size(); i++) {
			objects2d.get(i).render();
		}
	}

	public void renderScene() {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).render();
		}
	}

	public void setRendering2d(boolean r) {
		render2d = r;
	}

	@Override
	public void start() {
		initEngine();
		init();
		initOpenGL();

		getDelta();

		while (isRunning()) {
			int delta = getDelta();
			updateFPS();
			updateEngine();
			update(delta);
			display.clear();
			render();
			if (render2d) {
				mode2d();
				render2d();
				mode3d();
			}
			cam.end();
			display.swap();
		}
		System.out.println("EXIT LOOP");
		destroyEngine();
		System.out.println("Destroy Engine");
		display.close();
		System.out.println("Destroy Display");
	}
}