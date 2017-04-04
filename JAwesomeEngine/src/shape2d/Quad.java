package shape2d;

import gui.Color;
import math.VecMath;
import objects.ShapedObject2;
import shapedata2d.QuadStructure;
import utils.GLConstants;
import vector.Vector2f;

public class Quad extends ShapedObject2 implements QuadStructure {
	Vector2f halfsize;

	public Quad(float x, float y, float halfsizex, float halfsizey) {
		super(x, y);
		init(halfsizex, halfsizey, true);
	}

	public Quad(Vector2f pos, float halfsizex, float halfsizey) {
		super(pos);
		init(halfsizex, halfsizey, true);
	}

	public Quad(Vector2f pos, Vector2f halfsize) {
		super(pos);
		init(halfsize.x, halfsize.y, true);
	}

	public Quad(float x, float y, float halfsizex, float halfsizey, boolean adjacency) {
		super(x, y);
		init(halfsizex, halfsizey, adjacency);
	}

	public Quad(Vector2f pos, float halfsizex, float halfsizey, boolean adjacency) {
		super(pos);
		init(halfsizex, halfsizey, adjacency);
	}

	public Quad(Vector2f pos, Vector2f halfsize, boolean adjacency) {
		super(pos);
		init(halfsize.x, halfsize.y, adjacency);
	}

	@Override
	public Vector2f getHalfSize() {
		return halfsize;
	}

	@Override
	public Vector2f getSize() {
		return VecMath.scale(halfsize, 2);
	}

	private void init(float hsx, float hsy, boolean adjacency) {
		shapetype = SHAPE_QUAD;
		halfsize = new Vector2f(hsx, hsy);
		Color color = Color.WHITE;

		addVertex(new Vector2f(-hsx, hsy), color, new Vector2f(0, 0));
		addVertex(new Vector2f(hsx, hsy), color, new Vector2f(1, 0));
		addVertex(new Vector2f(hsx, -hsy), color, new Vector2f(1, 1));
		addVertex(new Vector2f(-hsx, -hsy), color, new Vector2f(0, 1));

		if (adjacency) {
			setRenderMode(GLConstants.TRIANGLE_ADJACENCY);
			addQuad(0, 0, 1, 1, 2, 2, 3, 3);
		} else {
			setRenderMode(GLConstants.TRIANGLES);
			addQuad(0, 1, 2, 3);
		}

		this.prerender();
	}
}