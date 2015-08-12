package shape2d;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import math.VecMath;
import objects.ShapedObject2;
import shapedata2d.QuadStructure;
import utils.Debugger;
import vector.Vector2f;

public class Quad extends ShapedObject2 implements QuadStructure {
	Vector2f halfsize;

	public Quad(float x, float y, float halfsizex, float halfsizey) {
		super();
		translateTo(x, y);
		init(halfsizex, halfsizey);
	}

	public Quad(Vector2f pos, float halfsizex, float halfsizey) {
		super();
		translateTo(pos);
		init(halfsizex, halfsizey);
	}

	public Quad(Vector2f pos, Vector2f halfsize) {
		super();
		translateTo(pos);
		init(halfsize.x, halfsize.y);
	}

	@Override
	public Vector2f getHalfSize() {
		return halfsize;
	}

	@Override
	public Vector2f getSize() {
		return VecMath.scale(halfsize, 0.5f);
	}

	private void init(float hsx, float hsy) {
		shapetype = SHAPE_QUAD;
		halfsize = new Vector2f(hsx, hsy);
		Color color = Color.GRAY;

		addVertex(new Vector2f(-hsx, hsy), color, new Vector2f(0, 0));
		addVertex(new Vector2f(hsx, hsy), color, new Vector2f(1, 0));
		addVertex(new Vector2f(hsx, -hsy), color, new Vector2f(1, 1));
		addVertex(new Vector2f(-hsx, -hsy), color, new Vector2f(0, 1));
		addVertex(new Vector2f(-hsx, -hsy+1), color, new Vector2f(0, 1));

		addQuad(0, 1, 2, 3);

		this.prerender();
	}
}