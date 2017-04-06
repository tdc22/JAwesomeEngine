package shape;

import gui.Color;
import math.VecMath;
import objects.ShapedObject3;
import shapedata.PlaneStructure;
import vector.Vector2f;
import vector.Vector3f;

public class Plane extends ShapedObject3 implements PlaneStructure {
	Vector2f halfsize;

	public Plane(float x, float y, float z, float halfsizeX, float halfsizeY, boolean renderBack) {
		super(x, y, z);
		init(halfsizeX, halfsizeY, renderBack);
	}

	public Plane(Vector3f pos, float halfsizeX, float halfsizeY, boolean renderBack) {
		super(pos);
		init(halfsizeX, halfsizeY, renderBack);
	}

	public Plane(Vector3f pos, Vector2f halfsize, boolean renderBack) {
		super(pos);
		init(halfsize.x, halfsize.y, renderBack);
	}

	@Override
	public Vector2f getHalfSize() {
		return halfsize;
	}

	@Override
	public Vector2f getSize() {
		return VecMath.scale(halfsize, 2);
	}

	private void init(float hsx, float hsy, boolean renderBack) {
		shapetype = SHAPE_PLANE;
		halfsize = new Vector2f(hsx, hsy);
		Color color = Color.WHITE;

		// Normals
		Vector3f up = new Vector3f(0, 1, 0);

		addVertex(new Vector3f(-hsx, 0, -hsy), color, new Vector2f(0, 1), up);
		addVertex(new Vector3f(-hsx, 0, hsy), color, new Vector2f(0, 0), up);
		addVertex(new Vector3f(hsx, 0, hsy), color, new Vector2f(1, 0), up);
		addVertex(new Vector3f(hsx, 0, -hsy), color, new Vector2f(1, 1), up);
		addQuad(0, 0, 1, 1, 2, 2, 3, 3);

		if (renderBack) {
			addQuad(0, 0, 3, 3, 2, 2, 1, 1);
		}

		this.prerender();
	}
}
