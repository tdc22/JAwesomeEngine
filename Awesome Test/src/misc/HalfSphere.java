package misc;

import java.awt.Color;

import math.VecMath;
import objects.ShapedObject3;
import vector.Vector2f;
import vector.Vector3f;

public class HalfSphere extends ShapedObject3 {
	int radius, trisH, trisV;

	public HalfSphere(int x, int y, int z, int radius, int trisH, int trisV) {
		super(x, y, z);
		init(radius, trisH, trisV);
	}

	private void init(int r, int tH, int tV) {
		radius = r;
		trisH = tH;
		trisV = tV;

		float angleStepv = 360 / (float) trisV;
		float angleSteph = 360 / (float) trisH;
		for (int a = 0; a < trisV / 2f; a++) {
			for (int b = 0; b < trisH; b++) {
				Vector3f pos = new Vector3f(
						radius * (float) Math.sin(Math.toRadians(angleStepv * (a / (float) 2)))
								* (float) Math.sin(Math.toRadians(angleSteph * b)),
						radius * (float) Math.cos(Math.toRadians(angleStepv * (a / (float) 2))),
						radius * (float) Math.sin(Math.toRadians(angleStepv * (a / (float) 2)))
								* (float) Math.cos(Math.toRadians(angleSteph * b)));
				Vector3f normal = VecMath.normalize(pos);
				addVertex(pos, Color.GRAY, new Vector2f(a, b), normal);
			}
		}
		addVertex(VecMath.subtraction(getVertex(0), new Vector3f(0, radius * 2, 0)), Color.GRAY,
				new Vector2f(trisV, trisH), new Vector3f(0, -1, 0));

		int lv = (int) (trisV / 2f);
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
				addQuad(pos, pos - 1, pos + lh, pos + 2 * lh + 1, pos + lh + 1, pos + lh + 2, pos + 1, pos - lh);
				pos++;
			}
			addQuad(pos, pos - 1, pos + lh, pos + 2 * lh + 1, pos + lh + 1, pos + 2, pos + 1, pos - lh);
			pos++;
			addQuad(pos, pos - 1, pos + lh, pos + lh + 1, pos + 1, pos + 2, pos + 1 - lh, pos - lh);
			pos++;
		}

		this.prerender();
	}
}