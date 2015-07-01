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
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glShadeModel;
import input.GLFWInputReader;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.GameCamera;
import objects.Renderable;
import objects.RenderedObject;
import objects.Updateable;

import org.lwjgl.BufferUtils;

import shader.Shader;
import shape2d.Quad;
import texture.FramebufferObject;
import texture.Texture;
import utils.GameProfiler;
import utils.NullGameProfiler;
import utils.ViewFrustum;
import display.Display;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public abstract class StandardGame extends AbstractGame implements Renderable,
		Updateable {
	protected List<RenderedObject> objects;
	protected List<RenderedObject> objects2d;
	public VideoSettings settings;
	protected FramebufferObject framebufferMultisample, framebuffer,
			framebufferPostProcessing;
	protected FramebufferObject framebuffer2Multisample, framebuffer2,
			framebuffer2PostProcessing;
	protected Quad screen;
	public Display display;
	public GameCamera cam;
	protected GameProfiler profiler;

	public InputManager inputs;
	protected InputEvent closeEvent;

	private ViewFrustum frustum;
	private FloatBuffer identity;
	protected boolean render2d = true;
	protected boolean useFBO = true;
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
			FramebufferObject fbo1, FramebufferObject fbo2) {
		boolean p = true;
		int tex0 = fbo1.getColorTextureID();
		int tex1 = fbo2.getColorTextureID();
		for (Shader s : ppshaders) {
			// TODO: Create Multipass shader-class with integer for number of
			// iterations.... + try to put this in there?
			for (int i = 0; i < postProcessingIterations; i++) {
				FramebufferObject current = p ? fbo2 : fbo1;
				current.bind();
				current.clear();
				((Texture) s.getArgument("texture")).setTextureID(p ? tex0
						: tex1);
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
			framebufferMultisample = new FramebufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), pixelformat.getSamples());
			framebuffer = new FramebufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);
			framebufferPostProcessing = new FramebufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);
			framebuffer2Multisample = new FramebufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), pixelformat.getSamples());
			framebuffer2 = new FramebufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);
			framebuffer2PostProcessing = new FramebufferObject(this,
					videosettings.getResolutionX(),
					videosettings.getResolutionY(), 0);

			float halfResX = videosettings.getResolutionX() / 2f;
			float halfResY = videosettings.getResolutionY() / 2f;
			screen = new Quad(halfResX, halfResY, halfResX, halfResY);
			// screen.invertAllTriangles();
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

		profiler = new NullGameProfiler();

		lastFPS = getTime();
	}

	protected void initOpenGL() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0f);
		glClearStencil(0);

		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		frustum = new ViewFrustum(settings.getResolutionX(),
				settings.getResolutionY(), settings.getZNear(),
				settings.getZFar(), settings.getFOVy());
		frustum.apply();

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
		glLoadMatrixf(identity);
		glOrtho(0, settings.getResolutionX(), settings.getResolutionY(), 0, -1,
				1);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrixf(identity);
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

	@Override
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

	public void setProfiler(GameProfiler profiler) {
		this.profiler = profiler;
	}

	@Override
	public void start() {
		initEngine();
		init();
		initOpenGL();
		getDelta();

		while (isRunning()) {
			profiler.frameStart();
			int delta = getDelta();
			updateFPS();
			updateEngine();
			update(delta);
			profiler.updateRender3d();
			prepareRender();
			render();
			end3d();
			profiler.render3dRender2d();
			if (render2d) {
				start2d();
				render2d();
				end2d();
			}
			profiler.render2dDisplay();
			endRender();
			profiler.frameEnd();
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