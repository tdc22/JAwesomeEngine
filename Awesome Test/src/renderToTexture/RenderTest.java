package renderToTexture;

import game.Debugger;
import game.StandardGame;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import shape.Box;
import utils.RenderToTexture;
import utils.Shader;
import vector.Vector3f;

public class RenderTest extends StandardGame {
	RenderToTexture rtt;
	Shader screenshader;
	Debugger debugmanager;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initDisplay(false, 800, 600, false);
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);
		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
		addObject(ModelLoader.load("res/models/bunny.mobj"));

		rtt = new RenderToTexture(this, new Vector3f(0, 0, 10), new Vector3f(0,
				0, -1), new Vector3f(0, 1, 0));
		rtt.updateTexture();

		screenshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag"));
		screenshader.addArgumentName("colorMap");
		screenshader.addArgument(rtt.getTextureID());

		Box screen = new Box(2, 3, 12, 2, 1, 0.1f);
		addObject(screen);
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
	}

	@Override
	public void update(int delta) {
		rtt.updateTexture();
		debugmanager.update();
		cam.update(delta);
	}

}
