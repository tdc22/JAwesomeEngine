package shape;

import java.awt.Color;

import math.VecMath;
import objects.ShapedObject3;
import shapedata.EllipsoidStructure;
import vector.Vector2f;
import vector.Vector3f;

public class Ellipsoid extends ShapedObject3 implements EllipsoidStructure {
	float radiusX, radiusY, radiusZ;
	int trisH, trisV;

	public Ellipsoid(float x, float y, float z, float radiusX, float radiusY, float radiusZ, int trisH, int trisV) {
		super(x, y, z);
		init(radiusX, radiusY, radiusZ, trisH, trisV);
	}

	public Ellipsoid(Vector3f pos, float radiusX, float radiusY, float radiusZ, int trisH, int trisV) {
		super(pos);
		init(radiusX, radiusY, radiusZ, trisH, trisV);
	}
	
	@Override
	public float getRadiusX() {
		return radiusX;
	}
	
	@Override
	public float getRadiusY() {
		return radiusY;
	}
	
	@Override
	public float getRadiusZ() {
		return radiusZ;
	}

	public int getHorizontalTriangleNumber() {
		return trisH;
	}

	public int getVerticalTriangleNumber() {
		return trisH;
	}

	private void init(float rx, float ry, float rz, int tH, int tV) {
		shapetype = SHAPE_ELLIPSOID;
		radiusX = rx;
		radiusY = ry;
		radiusZ = rz;
		trisH = tH;
		trisV = tV;

		float angleStepv = 360 / (float) trisV;
		float angleSteph = 360 / (float) trisH;
		for (int a = 0; a < trisV; a++) {
			for (int b = 0; b < trisH; b++) {
				Vector3f pos = new Vector3f(
						radiusX * (float) Math.sin(Math.toRadians(angleStepv * (a / (float) 2)))
								* (float) Math.sin(Math.toRadians(angleSteph * b)),
						radiusY * (float) Math.cos(Math.toRadians(angleStepv * (a / (float) 2))),
						radiusZ * (float) Math.sin(Math.toRadians(angleStepv * (a / (float) 2)))
								* (float) Math.cos(Math.toRadians(angleSteph * b)));
				Vector3f normal = VecMath.normalize(pos);
				addVertex(pos, Color.GRAY, new Vector2f(a, b), normal);
			}
		}
		addVertex(VecMath.subtraction(getVertex(0), new Vector3f(0, radiusY * 2, 0)), Color.GRAY,
				new Vector2f(trisV, trisH), new Vector3f(0, -1, 0));

		int lv = trisV;
		int lh = trisH;
		int num = this.getVertices().size() - 1;
		int pos = 0;

		pos += lh;
		addTriangle(0, pos + lh - 1, pos, pos + lh + 1, pos + 1, pos + 2);
		pos++;
		for (int h = 1; h < lh - 2; h++) {
			addTriangle(0, pos - 1, pos, pos + lh + 1, pos + 1, pos + 2);
			pos++;
		}
		addTriangle(0, pos - 1, pos, pos + lh + 1, pos + 1, pos - lh + 2);
		pos++;
		addTriangle(0, pos - 1, pos, pos + 1, pos - lh + 1, pos - lh + 2);
		pos++;
		pos -= lh;
		for (int v = 1; v < lv - 2; v++) {
			addQuad(pos, pos - 1 + lh, pos + lh, pos + 2 * lh + 1, pos + lh + 1, pos + 2 + lh, pos + 1, pos - lh);
			pos++;
			for (int h = 1; h < lh - 2; h++) {
				addQuad(pos, pos - 1, pos + lh, pos + 2 * lh + 1, pos + lh + 1, pos + 2 + lh, pos + 1, pos - lh);
				pos++;
			}
			addQuad(pos, pos - 1, pos + lh, pos + 2 * lh + 1, pos + lh + 1, pos + 2, pos + 1, pos - lh);
			pos++;
			addQuad(pos, pos - 1, pos + lh, pos + lh + 1, pos + 1, pos + 2, pos + 1 - lh, pos - lh);
			pos++;
		}
		addQuad(pos, pos - 1 + lh, pos + lh, num, pos + lh + 1, pos + 2 + lh, pos + 1, pos - lh);
		pos++;
		for (int h = 1; h < lh - 2; h++) {
			addQuad(pos, pos - 1, pos + lh, num, pos + lh + 1, pos + 2 + lh, pos + 1, pos - lh);
			pos++;
		}
		addQuad(pos, pos - 1, pos + lh, num, pos + lh + 1, pos + 2, pos + 1, pos - lh);
		pos++;
		addQuad(pos, pos - 1, pos + lh, pos + lh + 1, pos + 1, pos + 2, pos + 1 - lh, pos - lh);
		pos++;
		addTriangle(pos + 1, pos - lh, pos, pos + lh - 1, num, pos + 2);
		pos++;
		for (int h = 2; h < lh; h++) {
			addTriangle(pos + 1, pos - lh, pos, pos - 1, num, pos + 2);
			pos++;
		}
		addTriangle(pos - lh + 1, pos - lh, pos, pos - 1, num, num);
		pos++;

		this.prerender();
	}
}