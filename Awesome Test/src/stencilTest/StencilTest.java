package stencilTest;

import game.Debugger;
import game.StandardGame;

import java.awt.Color;

import loader.FontLoader;
import loader.ShaderLoader;
import objects.ShapedObject;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import vector.Vector3f;

public class StencilTest extends StandardGame {
	Vector3f lightpos = new Vector3f(2, 4, 1);
	Shader shadowvolume;
	Debugger debugmanager;

	@Override
	public void init() {
		initDisplay(false, 800, 600, true);
		cam.setFlyCam(true);
		cam.translateTo(0, 2, 20);
		cam.rotateTo(0, 0);

		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);

		shadowvolume = new Shader(ShaderLoader.loadShader(
				"res/shaders/shadowvolume.vert", null,
				"res/shaders/shadowvolume.geo",
				ARBGeometryShader4.GL_TRIANGLES_ADJACENCY_ARB,
				GL11.GL_TRIANGLE_STRIP, 6));
		shadowvolume.addArgument(lightpos);
		shadowvolume.addArgumentName("lightpos");

		ShapedObject ground = new Box(0, -5, 0, 20, 1, 20);
		addObject(ground);

		ShapedObject box = new Box(0, 0, 0, 1, 1, 1);
		box.setColor(Color.BLUE);
		addObject(box);

		ShapedObject sphere = new Sphere(4, -3, 4, 0.5f, 32, 32);
		sphere.setColor(Color.RED);
		addObject(sphere);
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
		shadowvolume.bind();
		renderScene();
		shadowvolume.unbind();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size());
	}

	@Override
	public void update(int delta) {
		debugmanager.update();
		cam.update(delta);
	}
}
