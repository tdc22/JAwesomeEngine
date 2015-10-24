package shape;

import java.awt.Color;

import math.VecMath;
import objects.ShapedObject3;
import shapedata.CapsuleStructure;
import shapedata.EllipsoidStructure;
import vector.Vector2f;
import vector.Vector3f;

public class Capsule extends ShapedObject3 implements CapsuleStructure {
	float radius, halfheight;
	int trisH, trisV;

	public Capsule(float x, float y, float z, float radius, float halfheight, int trisH, int trisV) {
		super(x, y, z);
		init(radius, halfheight, trisH, trisV);
	}

	public Capsule(Vector3f pos, float radius, float halfheight, int trisH, int trisV) {
		super(pos);
		init(radius, halfheight, trisH, trisV);
	}
	
	@Override
	public float getRadius() {
		return radius;
	}
	
	@Override
	public float getHalfHeight() {
		return halfheight;
	}
	
	@Override
	public float getHeight() {
		return 2 * halfheight;
	}

	public int getHorizontalTriangleNumber() {
		return trisH;
	}

	public int getVerticalTriangleNumber() {
		return trisH;
	}

	private void init(float radius, float halfheight, int tH, int tV) {
		shapetype = SHAPE_CAPSULE;
		this.radius = radius;
		this.halfheight = halfheight;
		trisH = tH;
		trisV = tV;

		// TODO

		this.prerender();
	}
}