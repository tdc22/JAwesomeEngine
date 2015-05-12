package epaDebugger;

import java.awt.Color;
import java.util.List;

import math.VecMath;
import objects.ShapedObject;
import utils.GLConstants;
import epaDebugger.EPADebugger.Triangle;

public class Simplex extends ShapedObject {
	public Simplex(List<Triangle> triangles, Triangle closest) {
		setRenderMode(GLConstants.TRIANGLES);

		int a = 0;
		for (Triangle t : triangles) {
			boolean oita = isOriginInsideTriangleArea(t);
			Color c;
			if (t.equals(closest)) {
				c = oita ? Color.BLUE : Color.CYAN;
			} else {
				c = oita ? Color.WHITE : Color.RED;
			}
			addVertex(t.a, c);
			addVertex(t.b, c);
			addVertex(t.c, c);

			addIndices(a, a + 1, a + 2);

			a += 3;
		}

		prerender();
	}

	private boolean isOriginInsideTriangleArea(Triangle t) {
		if (VecMath.dotproduct(
				VecMath.crossproduct(VecMath.subtraction(t.b, t.a), t.normal),
				VecMath.negate(t.a)) <= 0) {
			if (VecMath.dotproduct(VecMath.crossproduct(
					VecMath.subtraction(t.c, t.b), t.normal), VecMath
					.negate(t.b)) <= 0) {
				if (VecMath.dotproduct(VecMath.crossproduct(
						VecMath.subtraction(t.a, t.c), t.normal), VecMath
						.negate(t.c)) <= 0) {
					return true;
				}
			}
		}
		return false;
	}
}
