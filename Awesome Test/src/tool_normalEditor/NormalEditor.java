package tool_normalEditor;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape2d.Quad;
import texture.Texture;

public class NormalEditor extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());

		Texture diffuseTexture = new Texture(
				TextureLoader.loadTexture("/home/oliver/git/2dplatformer/2dPlatformer/res/textures/dumb2_head1.png"));
		Shader testtextureshader = new Shader(new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag")));
		testtextureshader.addArgumentName("u_texture");
		testtextureshader.addArgument(diffuseTexture);
		addShader2d(testtextureshader);

		Quad textureHolder = new Quad(400, 300, 200, 200);
		textureHolder.setRenderHints(false, true, true);
		testtextureshader.addObject(textureHolder);
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

	}
}
