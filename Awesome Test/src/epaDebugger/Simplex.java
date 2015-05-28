package epaDebugger;

import java.awt.Color;
import java.util.List;

import math.VecMath;
import objects.ShapedObject;
import utils.GLConstants;
import vector.Vector3f;
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

	private final float EPSILON = 0.001f;

	private boolean isOriginInsideTriangleArea(Triangle t) {
		return (checkPlane(t.a, t.b, t.normal)
				&& checkPlane(t.b, t.c, t.normal) && checkPlane(t.c, t.a,
					t.normal));
	}

	// (b - a) x normal * a <= EPSILON
	private boolean checkPlane(Vector3f a, Vector3f b, Vector3f normal) {
		Vector3f cross = VecMath
				.crossproduct(VecMath.subtraction(b, a), normal);
		return ((cross.x * -a.x + cross.y * -a.y + cross.z * -a.z) <= EPSILON);
	}
}
