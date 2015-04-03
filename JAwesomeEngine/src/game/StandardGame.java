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
import static org.lwjgl.opengl.GL11.glBindTexture;
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

import org.lwjgl.BufferUtils;

import shader.Shader;
import shape2d.Quad;
import texture.FrameBufferObject;

public abstract class StandardGame extends AbstractGame {
	protected List<RenderedObject> objects;
	protected List<RenderedObject> objects2d;
	public VideoSettings settings;
	protected FrameBufferObject framebufferMultisample, framebuffer,
			framebufferPostProcessing;
	protected FrameBufferObject framebuffer2Multisample, framebuffer2,
			framebuffer2PostProcessing;
	protected Quad screen;
	public Display display;
	public GameCamera cam;

	public InputManager inputs;
	protected InputEvent closeEvent;

	private FloatBuffer identity;
	boolean render2d = true;
	boolean useFBO = true;
	protected List<Shader> postProcessing, postProcessing2;
	protected int postProcessingIterations = 20; // TODO: Rethink that

	public void add2dObject(RenderedObject element) {
		objects2d.add(element);
	}

	public void addObject(RenderedObject obj) {
		objects.add(obj);
	}

	public void addPostProcessingShader(Shader shader) {
		postProcessing.add(shader);
	}

	public void addPostProcessingShader2(Shader shader) {
		postProcessing2.add(shader);
	}

	private void applyPostProcessing(List<Shader> ppshaders,
			FrameBufferObject fbo1, FrameBufferObject fbo2) {
		boolean p = true;
		int tex0 = fbo1.getTextureID();
		int tex1 = fbo2.getTextureID();
		for (Shader s : ppshaders) {
			// TODO: Create Multipass shader-class with integer for number of
			// iterations.... + try to put this in there?
			for (int i = 0; i < postProcessingIterations; i++) {
				FrameBufferObject current = p ? fbo2 : fbo1;
				current.bind();
				current.clear();
				s.setArgument("texture", p ? tex0 : tex1);
				s.bind();
				screen.render();
				s.unbind();
				current.unbind();
				p = !p;
			}
		}
		glBindTexture(GL_TEXTURE_2D, p ? tex0 : tex1);
		screen.render();
	}

	@Override
	protected void deleteAllObjects() {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).delete();
		}
		for (int i = 0; i < objects2d.size(); i++) {
			objects2d.get(i).delete();
		}
		if (screen != null)
			screen.delete();
		if (framebufferMultisample != null)
			framebufferMultisample.delete();
		if (framebuffer != null)
			framebuffer.delete();
		if (framebuffer2Multisample != null)
			framebuffer2Multisample.delete();
		if (framebuffer2 != null)
			framebuffer2.delete();
		if (framebufferPostProcessing != null)
			framebufferPostProcessing.delete();
		if (framebuffer2PostProcessing != null)
			framebuffer2PostProcessing.delete();
	}

	private void end2d() {
		if (useFBO) {
			framebuffer2Multisample.end();
			framebuffer2.clear();
			framebuffer2Multisample.copyTo(framebuffer2);
		} else {
			mode3d();
		}
	}

	private void end3d() {
		cam.end();
		if (useFBO) {
			framebufferMultisample.end();
			framebuffer.clear();
			framebufferMultisample.copyTo(framebuffer);
		}
	}

	protected void endRender() {
		if (useFBO) {
			if (!render2d)
				mode2d();
			applyPostProcessing(postProcessing, framebuffer,
					framebufferPostProcessing);
			if (render2d) {
				applyPostProcessing(postProcessing2, framebuffer2,
						framebuffer2PostProcessing);
			}
			glBindTexture(GL_TEXTURE_2D, 0);
			mode3d();
		}
		display.swap();
	}

	public List<RenderedObject> get2dObjects() {
		return objects2d;
	}

	public GameCamera getCamera() {
		return cam;
	}

	public Display getDisplay() {
		return display;
	}

	public List<RenderedObject> getObjects() {
		return objects;
	}

	public VideoSettings getVideoSettings() {
		return settings;
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

		if (useFBO) {
			framebufferMultisample = new FrameBufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), pixelformat.getSamples());
			framebuffer = new FrameBufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);
			framebufferPostProcessing = new FrameBufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);
			framebuffer2Multisample = new FrameBufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), pixelformat.getSamples());
			framebuffer2 = new FrameBufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);
			framebuffer2PostProcessing = new FrameBufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);

			float halfResX = videosettings.getResolutionX() / 2f;
			float halfResY = videosettings.getResolutionY() / 2f;
			screen = new Quad(halfResX, halfResY, halfResX, -halfResY);
			screen.invertAllTriangles();
			screen.setRenderHints(false, true, false);
		}
	}

	@Override
	protected void initEngine() {
		objects = new ArrayList<RenderedObject>();
		objects2d = new ArrayList<RenderedObject>();

		// JInputReader jinput = new JInputReader();
		// if (jinput.isUseable()) {
		// inputs = new InputManager(jinput);
		// System.out.println("Using JInput.");
		// } else {
		inputs = new InputManager(new GLFWInputReader());
		System.out.println("Using GLFW input.");
		// }
		closeEvent = new InputEvent("game_close", new Input(
				Input.KEYBOARD_EVENT, "Escape", KeyInput.KEY_DOWN));
		inputs.addEvent(closeEvent);

		cam = new GameCamera(inputs);

		postProcessing2 = new ArrayList<Shader>();
		postProcessing = new ArrayList<Shader>();

		identity = BufferUtils.createFloatBuffer(16 * 4);
		VecMath.identityMatrix().store(identity);
		identity.flip();

		lastFPS = getTime();
	}

	protected void initOpenGL() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0f); // Depth Buffer Setup
		glClearStencil(0);

		glShadeModel(GL_SMOOTH); // Enable Smooth Shading
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

	protected void mode2d() {
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadMatrix(identity);
		glOrtho(0, settings.getResolutionX(), settings.getResolutionY(), 0, -1,
				1);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrix(identity);
	}

	protected void mode3d() {
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
	}

	protected void prepareRender() {
		display.clear();
		if (useFBO) {
			framebufferMultisample.begin();
		}
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

	public void setShadersActive(boolean active) {
		for (RenderedObject o : objects)
			o.setShaderActive(active);
	}

	public void setShadersActive2d(boolean active) {
		for (RenderedObject o : objects2d)
			o.setShaderActive(active);
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
			prepareRender();
			render();
			end3d();
			if (render2d) {
				start2d();
				render2d();
				end2d();
			}
			endRender();
		}
		System.out.println("EXIT LOOP");
		destroyEngine();
		System.out.println("Destroy Engine");
		display.close();
		System.out.println("Destroy Display");
		System.exit(0);
	}

	private void start2d() {
		mode2d();
		if (useFBO) {
			framebuffer2Multisample.begin();
		}
	}

	protected void updateEngine() {
		display.pollInputs();
		inputs.update();
		if (display.isMouseBound())
			display.resetMouse();
		if (closeEvent.isActive())
			running = false;
	}
}