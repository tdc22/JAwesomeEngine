package textureCoordinateTest;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import loader.TextureLoader;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Ellipsoid;
import shape.Sphere;
import texture.Texture;

public class TextureCoordinateTest extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0, 0, 5);
		cam.rotateTo(0, 0);

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/textureCoordinateTest.png"));

		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		Box b = new Box(-1, 0, 0, 1, 1, 1);
		b.setRenderHints(false, true, false);
		textureshader.addObject(b);

		Sphere s = new Sphere(2, 0, 0, 1, 6, 6);
		s.setRenderHints(false, true, false);
		textureshader.addObject(s);

		Ellipsoid c = new Ellipsoid(5, 0, 0, 1, 2, 1, 36, 36);
		c.setRenderHints(false, true, false);
		textureshader.addObject(c);

		Cylinder cy = new Cylinder(8, 0, 0, 1, 2, 36);
		cy.setRenderHints(false, true, false);
		textureshader.addObject(cy);
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {

	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}
}
