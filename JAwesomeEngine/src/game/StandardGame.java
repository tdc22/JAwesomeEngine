package game;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
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
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import display.Display;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import input.GLFWInputReader;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;
import loader.ShaderLoader;
import math.VecMath;
import matrix.Matrix4f;
import objects.Camera2;
import objects.GameCamera;
import objects.Updateable;
import objects.ViewProjection;
import shader.Shader;
import shape2d.Quad;
import texture.FramebufferObject;
import texture.Texture;
import utils.DefaultShader;
import utils.GameProfiler;
import utils.NullGameProfiler;
import utils.ProjectionHelper;

public abstract class StandardGame extends AbstractGame implements ViewProjection, Updateable {
	protected List<Shader> shader;
	protected List<Shader> shader2d;
	public VideoSettings settings;
	protected FramebufferObject framebufferMultisample, framebuffer, framebufferPostProcessing;
	protected FramebufferObject framebuffer2Multisample, framebuffer2, framebuffer2PostProcessing;
	protected Quad screen;
	private Shader screenShader;
	public Display display;
	public GameCamera cam;
	public Camera2 cam2;
	protected FloatBuffer projectionMatrix, projectionMatrix2;
	protected GameProfiler profiler;

	public InputManager inputs;
	protected InputEvent closeEvent;

	private FloatBuffer identity;
	protected boolean render2d = true;
	protected boolean useFBO = true;
	protected List<Shader> postProcessing, postProcessing2;
	protected int postProcessingIterations = 20; // TODO: Rethink that

	public void add2dShader(Shader s) {
		s.addArgument("projection", projectionMatrix2);
		s.addArgument("view", new Matrix4f());
		s.addArgument("projectionView", new Matrix4f());
		shader2d.add(s);
	}

	public void addShader(Shader s) {
		s.addArgument("projection", projectionMatrix);
		s.addArgument("view", new Matrix4f());
		s.addArgument("projectionView", new Matrix4f());
		shader.add(s);
	}

	public void addPostProcessingShader(Shader shader) {
		shader.addObject(screen);
		postProcessing.add(shader);
	}

	public void addPostProcessingShader2(Shader shader) {
		shader.addObject(screen);
		postProcessing2.add(shader);
	}

	public void removePostProcessingShader(Shader shader) {
		postProcessing.remove(shader);
	}

	public void removePostProcessingShader2(Shader shader) {
		postProcessing2.remove(shader);
	}

	public void setPostProcessingIterations(int ppIterations) {
		postProcessingIterations = ppIterations;
	}

	private void applyPostProcessing(List<Shader> ppshaders, FramebufferObject fbo1, FramebufferObject fbo2) {
		boolean p = true;
		int tex0 = fbo1.getColorTextureID();
		int tex1 = fbo2.getColorTextureID();
		int tex0depth = fbo1.getDepthTextureID();
		int tex1depth = fbo2.getDepthTextureID();
		for (Shader s : ppshaders) {
			// TODO: Create Multipass shader-class with integer for number of
			// iterations.... + try to put this in there?
			for (int i = 0; i < postProcessingIterations; i++) {
				FramebufferObject current = p ? fbo2 : fbo1;
				current.bind();
				current.clear();
				((Texture) s.getArgument("u_texture")).setTextureID(p ? tex0 : tex1);
				((Texture) s.getArgument("u_depthTexture")).setTextureID(p ? tex0depth : tex1depth);
				// s.bind();
				// screen.render();
				// s.unbind();
				s.render();
				current.unbind();
				p = !p;
			}
		}
		glBindTexture(GL_TEXTURE_2D, p ? tex0 : tex1);
		screenShader.render();
		// screen.render();
	}

	@Override
	protected void deleteAllObjects() {
		for (Shader s : shader) {
			s.delete();
		}
		for (Shader s : shader2d) {
			s.delete();
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
		}
	}

	private void end3d() {
		if (useFBO) {
			framebufferMultisample.end();
			framebuffer.clear();
			framebufferMultisample.copyTo(framebuffer);
		}
	}

	protected void endRender() {
		if (useFBO) {
			applyPostProcessing(postProcessing, framebuffer, framebufferPostProcessing);
			if (render2d) {
				applyPostProcessing(postProcessing2, framebuffer2, framebuffer2PostProcessing);
			}
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		display.swap();
	}

	public GameCamera getCamera() {
		return cam;
	}

	public Display getDisplay() {
		return display;
	}

	public VideoSettings getVideoSettings() {
		return settings;
	}

	public void initDisplay(Display display, DisplayMode displaymode, PixelFormat pixelformat,
			VideoSettings videosettings) {
		this.display = display;
		display.open(displaymode, pixelformat);
		this.settings = videosettings;

		// GLFW input fix
		if (inputs.getInputReader() instanceof GLFWInputReader)
			((GLFWInputReader) inputs.getInputReader()).addWindowID(((GLDisplay) display).getWindowID());

		if (useFBO) {
			framebufferMultisample = new FramebufferObject(this, settings.getResolutionX(), settings.getResolutionY(),
					pixelformat.getSamples());
			framebuffer = new FramebufferObject(this, settings.getResolutionX(), settings.getResolutionY(), 0);
			framebufferPostProcessing = new FramebufferObject(this, settings.getResolutionX(),
					settings.getResolutionY(), 0);
			framebuffer2Multisample = new FramebufferObject(this, settings.getResolutionX(), settings.getResolutionY(),
					pixelformat.getSamples());
			framebuffer2 = new FramebufferObject(this, settings.getResolutionX(), settings.getResolutionY(), 0);
			framebuffer2PostProcessing = new FramebufferObject(this, settings.getResolutionX(),
					settings.getResolutionY(), 0);

			screen = new Quad(0, 0, 1, -1);
			screen.setRenderHints(false, true, false);

			screenShader = new Shader(
					ShaderLoader.loadShader(DefaultShader.SCREEN_SHADER_VERTEX, DefaultShader.SCREEN_SHADER_FRAGMENT));
			screenShader.addObject(screen);
		}

		projectionMatrix = storeMatrix(ProjectionHelper.perspective(settings.getFOVy(),
				settings.getResolutionX() / (float) settings.getResolutionY(), settings.getZNear(),
				settings.getZFar()));
		projectionMatrix2 = storeMatrix(
				ProjectionHelper.ortho(0, settings.getResolutionX(), settings.getResolutionY(), 0, -1, 1));
	}

	@Override
	protected void initEngine() {
		shader = new ArrayList<Shader>();
		shader2d = new ArrayList<Shader>();

		// JInputReader jinput = new JInputReader();
		// if (jinput.isUseable()) {
		// inputs = new InputManager(jinput);
		// System.out.println("Using JInput.");
		// } else {
		inputs = new InputManager(new GLFWInputReader());
		System.out.println("Using GLFW input.");
		// }
		closeEvent = new InputEvent("game_close", new Input(Input.KEYBOARD_EVENT, "Escape", KeyInput.KEY_DOWN));
		inputs.addEvent(closeEvent);

		cam = new GameCamera(inputs);
		cam2 = new Camera2();

		postProcessing = new ArrayList<Shader>();
		postProcessing2 = new ArrayList<Shader>();

		identity = BufferUtils.createFloatBuffer(16);
		VecMath.identityMatrix().store(identity);
		identity.flip();

		profiler = new NullGameProfiler();

		resetTimers();
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

		glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}

	@Override
	public boolean isRunning() {
		return !display.isCloseRequested() && running;
	}

	protected void prepareRender() {
		display.clear();
		if (useFBO) {
			framebufferMultisample.begin();
		}
	}

	@Override
	public abstract void render();

	public abstract void render2d();

	public void render2dScene() {
		for (Shader s : shader2d) {
			s.render();
		}
	}

	public void renderScene() {
		for (Shader s : shader) {
			s.render();
		}
	}

	public void setRender2d(boolean r) {
		render2d = r;
	}

	public void setProfiler(GameProfiler profiler) {
		this.profiler = profiler;
	}

	@Override
	public void start() {
		initEngine();
		init();
		initOpenGL();
		resetTimers();

		while (isRunning()) {
			profiler.frameStart();
			int delta = getDelta();
			updateFPS();
			updateEngine();
			update(delta);
			profiler.updateRender3d();
			prepareRender();
			updateShaderMatrices();
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
		if (useFBO) {
			framebuffer2Multisample.begin();
		}
	}

	protected void updateShaderMatrices() {
		for (Shader s : shader) {
			s.setArgumentDirect("view", cam.getMatrixBuffer());
		}
		for (Shader s : shader2d) {
			s.setArgumentDirect("view", cam2.getMatrixBuffer());
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

	@Override
	public void setViewMatrix(FloatBuffer buffer) {
		for (Shader s : shader) {
			s.setArgumentDirect("view", buffer);
		}
	}

	@Override
	public void setProjectionMatrix(FloatBuffer buffer) {
		for (Shader s : shader) {
			s.setArgumentDirect("projection", buffer);
		}
	}

	@Override
	public void setViewProjectionMatrix(FloatBuffer viewBuffer, FloatBuffer projectionBuffer) {
		for (Shader s : shader) {
			s.setArgumentDirect("view", viewBuffer);
			s.setArgumentDirect("projection", projectionBuffer);
		}
	}

	@Override
	public void setViewMatrix(Matrix4f matrix) {
		FloatBuffer buf = storeMatrix(matrix);
		for (Shader s : shader) {
			s.setArgumentDirect("view", buf);
		}
	}

	@Override
	public void setProjectionMatrix(Matrix4f matrix) {
		FloatBuffer buf = storeMatrix(matrix);
		for (Shader s : shader) {
			s.setArgumentDirect("projection", buf);
		}
	}

	@Override
	public void setViewProjectionMatrix(Matrix4f viewMatrix, Matrix4f projectionMatrix) {
		FloatBuffer viewBuf = storeMatrix(viewMatrix);
		FloatBuffer projBuf = storeMatrix(projectionMatrix);
		for (Shader s : shader) {
			s.setArgumentDirect("view", viewBuf);
			s.setArgumentDirect("projection", projBuf);
		}
	}

	@Override
	public FloatBuffer getViewMatrixBuffer() {
		return cam.getMatrixBuffer();
	}

	@Override
	public FloatBuffer getProjectionMatrixBuffer() {
		return projectionMatrix;
	}

	protected FloatBuffer storeMatrix(Matrix4f mat) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		mat.store(buf);
		buf.flip();
		return buf;
	}
}