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
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glShadeModel;
import input.GLFWInputReader;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.util.List;

import loader.ShaderLoader;
import objects.Camera2;
import objects.GameCamera;
import objects.Updateable;
import shader.PostProcessingShader;
import shader.Shader;
import shape2d.Quad;
import utils.DefaultShader;
import utils.GameProfiler;
import utils.NullGameProfiler;
import utils.ProjectionHelper;
import display.Display;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public abstract class StandardGame extends AbstractGame implements Updateable {
	public VideoSettings settings;
	protected Layer layer3d, layer2d, layerInterface;
	protected Quad screen;
	private Shader screenShader;
	public Display display;
	public GameCamera cam;
	public Camera2 cam2d;
	protected GameProfiler profiler;

	public InputManager inputs;
	protected InputEvent closeEvent;

	protected boolean useFBO = true;

	public void addShader(Shader s) {
		layer3d.addShader(s);
	}

	public void addShader2d(Shader s) {
		layer2d.addShader(s);
	}

	public void addShaderInterface(Shader s) {
		layerInterface.addShader(s);
	}

	public List<Shader> getShader() {
		return layer3d.shader;
	}

	public List<Shader> getShader2d() {
		return layer2d.shader;
	}

	public List<Shader> getShaderInterface() {
		return layerInterface.shader;
	}

	public void addPostProcessingShader(PostProcessingShader shader) {
		shader.getShader().addObject(screen);
		layer3d.postProcessing.add(shader);
	}

	public void addPostProcessingShader2d(PostProcessingShader shader) {
		shader.getShader().addObject(screen);
		layer2d.postProcessing.add(shader);
	}

	public void addPostProcessingShaderInterface(PostProcessingShader shader) {
		shader.getShader().addObject(screen);
		layerInterface.postProcessing.add(shader);
	}

	public void removePostProcessingShader(PostProcessingShader shader) {
		layer3d.postProcessing.remove(shader);
	}

	public void removePostProcessingShader2d(PostProcessingShader shader) {
		layer2d.postProcessing.remove(shader);
	}

	public void removePostProcessingShaderInterface(PostProcessingShader shader) {
		layerInterface.postProcessing.remove(shader);
	}

	public void setRendered(boolean render3d, boolean render2d, boolean renderInterface) {
		layer3d.setActive(render3d);
		layer2d.setActive(render2d);
		layerInterface.setActive(renderInterface);
	}

	@Override
	protected void deleteAllObjects() {
		if (screen != null)
			screen.delete();
		layer3d.delete();
		layer2d.delete();
		layerInterface.delete();
	}

	private void start3d() {
		if (useFBO) {
			layer3d.begin();
		}
	}

	private void end3d() {
		if (useFBO) {
			layer3d.end();
		}
	}

	private void start2d() {
		if (useFBO) {
			layer2d.begin();
		}
	}

	private void end2d() {
		if (useFBO) {
			layer2d.end();
		}
	}

	private void startInterface() {
		if (useFBO) {
			layerInterface.begin();
		}
	}

	private void endInterface() {
		if (useFBO) {
			layerInterface.end();
		}
	}

	protected void endRender() {
		if (useFBO) {
			if (layer3d.isActive()) {
				layer3d.applyPostProcessing(screenShader, display.getWidth(), display.getHeight());
			}
			if (layer2d.isActive()) {
				layer2d.applyPostProcessing(screenShader, display.getWidth(), display.getHeight());
			}
			if (layerInterface.isActive()) {
				layerInterface.applyPostProcessing(screenShader, display.getWidth(), display.getHeight());
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
			layer3d.initLayer(settings.getResolutionX(), settings.getResolutionY(), pixelformat.getSamples());
			layer2d.initLayer(settings.getResolutionX(), settings.getResolutionY(), pixelformat.getSamples());
			layerInterface.initLayer(settings.getResolutionX(), settings.getResolutionY(), pixelformat.getSamples());

			screen = new Quad(0, 0, 1, -1);
			screen.setRenderHints(false, true, false);

			screenShader = new Shader(
					ShaderLoader.loadShader(DefaultShader.SCREEN_SHADER_VERTEX, DefaultShader.SCREEN_SHADER_FRAGMENT));
			screenShader.addObject(screen);
		}

		layer3d.setProjectionMatrix(ProjectionHelper.perspective(settings.getFOVy(),
				settings.getResolutionX() / (float) settings.getResolutionY(), settings.getZNear(),
				settings.getZFar()));
		layer2d.setProjectionMatrix(
				ProjectionHelper.ortho(0, settings.getResolutionX(), settings.getResolutionY(), 0, -1, 1));
		layerInterface.setProjectionMatrix(
				ProjectionHelper.ortho(0, settings.getResolutionX(), settings.getResolutionY(), 0, -1, 1));
	}

	@Override
	protected void initEngine() {
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
		cam2d = new Camera2();

		layer3d = new Layer();
		layer2d = new Layer();
		layerInterface = new Layer();

		profiler = new NullGameProfiler();
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
		if (settings.isBackfaceCulling()) {
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
		} else {
			glDisable(GL_CULL_FACE);
		}

		glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}

	@Override
	public boolean isRunning() {
		return !display.isCloseRequested() && running;
	}

	public abstract void render();

	public abstract void render2d();

	public abstract void renderInterface();

	public void render3dLayer() {
		layer3d.render();
	}

	public void render2dLayer() {
		layer2d.render();
	}

	public void renderInterfaceLayer() {
		layerInterface.render();
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
			display.clear();
			if (layer3d.isActive()) {
				layer3d.setViewMatrix(cam.getMatrixBuffer());
				start3d();
				render();
				end3d();
			}
			profiler.render3dRender2d();
			if (layer2d.isActive()) {
				layer2d.setViewMatrix(cam2d.getMatrixBuffer());
				start2d();
				render2d();
				end2d();
			}
			profiler.render2dDisplay();
			if (layerInterface.isActive()) {
				startInterface();
				renderInterface();
				endInterface();
			}
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

	protected void updateEngine() {
		display.pollInputs();
		inputs.update();
		if (display.isMouseBound())
			display.resetMouse();
		if (closeEvent.isActive())
			running = false;
	}
}