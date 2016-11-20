package shape;

import java.awt.Color;

import math.VecMath;
import objects.ShapedObject3;
import shapedata.BoxStructure;
import vector.Vector2f;
import vector.Vector3f;

public class Box extends ShapedObject3 implements BoxStructure {
	Vector3f halfsize;

	public Box(float x, float y, float z, float halfsizeX, float halfsizeY,
			float halfsizeZ) {
		super(x, y, z);
		init(halfsizeX, halfsizeY, halfsizeZ);
	}

	public Box(Vector3f pos, float halfsizeX, float halfsizeY, float halfsizeZ) {
		super(pos);
		init(halfsizeX, halfsizeY, halfsizeZ);
	}

	public Box(Vector3f pos, Vector3f halfsize) {
		super(pos);
		init(halfsize.x, halfsize.y, halfsize.z);
	}

	@Override
	public Vector3f getHalfSize() {
		return halfsize;
	}

	@Override
	public Vector3f getSize() {
		return VecMath.scale(halfsize, 2);
	}

	private void init(float hsx, float hsy, float hsz) {
		shapetype = SHAPE_BOX;
		halfsize = new Vector3f(hsx, hsy, hsz);
		Color color = Color.GRAY;

		// Texturecoords
		Vector2f a = new Vector2f(0, 0);
		Vector2f b = new Vector2f(1, 0);
		Vector2f c = new Vector2f(1, 1);
		Vector2f d = new Vector2f(0, 1);

		// Normals
		Vector3f down = new Vector3f(0, -1, 0);
		Vector3f back = new Vector3f(0, 0, -1);
		Vector3f left = new Vector3f(-1, 0, 0);
		Vector3f front = new Vector3f(0, 0, 1);
		Vector3f right = new Vector3f(1, 0, 0);
		Vector3f up = new Vector3f(0, 1, 0);

		// Bottom
		addVertex(new Vector3f(-hsx, -hsy, -hsz), color, a, down);
		addVertex(new Vector3f(hsx, -hsy, -hsz), color, b, down);
		addVertex(new Vector3f(hsx, -hsy, hsz), color, c, down);
		addVertex(new Vector3f(-hsx, -hsy, hsz), color, d, down);

		// Back
		addVertex(new Vector3f(-hsx, -hsy, -hsz), color, b, back);
		addVertex(new Vector3f(-hsx, hsy, -hsz), color, c, back);
		addVertex(new Vector3f(hsx, hsy, -hsz), color, d, back);
		addVertex(new Vector3f(hsx, -hsy, -hsz), color, a, back);

		// Left
		addVertex(new Vector3f(-hsx, -hsy, -hsz), color, a, left);
		addVertex(new Vector3f(-hsx, -hsy, hsz), color, b, left);
		addVertex(new Vector3f(-hsx, hsy, hsz), color, c, left);
		addVertex(new Vector3f(-hsx, hsy, -hsz), color, d, left);

		// Front
		addVertex(new Vector3f(-hsx, -hsy, hsz), color, a, front);
		addVertex(new Vector3f(hsx, -hsy, hsz), color, b, front);
		addVertex(new Vector3f(hsx, hsy, hsz), color, c, front);
		addVertex(new Vector3f(-hsx, hsy, hsz), color, d, front);

		// Right
		addVertex(new Vector3f(hsx, -hsy, -hsz), color, b, right);
		addVertex(new Vector3f(hsx, hsy, -hsz), color, c, right);
		addVertex(new Vector3f(hsx, hsy, hsz), color, d, right);
		addVertex(new Vector3f(hsx, -hsy, hsz), color, a, right);

		// Top
		addVertex(new Vector3f(-hsx, hsy, -hsz), color, d, up);
		addVertex(new Vector3f(-hsx, hsy, hsz), color, a, up);
		addVertex(new Vector3f(hsx, hsy, hsz), color, b, up);
		addVertex(new Vector3f(hsx, hsy, -hsz), color, c, up);

		addQuad(0, 6, 1, 18, 2, 14, 3, 10);
		addQuad(4, 10, 5, 22, 6, 18, 7, 2);
		addQuad(8, 2, 9, 14, 10, 22, 11, 6);
		addQuad(12, 0, 13, 16, 14, 20, 15, 8);
		addQuad(16, 4, 17, 20, 18, 12, 19, 0);
		addQuad(20, 8, 21, 12, 22, 16, 23, 4);
		/*
		 * addQuad(4,5,6,7); addQuad(8,9,10,11); addQuad(12,13,14,15);
		 * addQuad(16,17,18,19); addQuad(20,21,22,23);
		 */

		// List<Vector3f> verts = this.getVertices();
		// for (int v = 0; v < verts.size(); v++) {
		// verts.set(
		// v,
		// VecMath.substraction(verts.get(v), halfsize));
		// }
		// this.setVertices(verts);

		this.prerender();
	}
}
