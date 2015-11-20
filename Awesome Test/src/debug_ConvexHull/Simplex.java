package debug_ConvexHull;

import java.awt.Color;
import java.util.List;

import debug_ConvexHull.ConvexHullDebugger.Triangle;
import objects.ShapedObject3;
import utils.GLConstants;

public class Simplex extends ShapedObject3 {
	public Simplex(List<Triangle> triangles, Triangle next) {
		setRenderMode(GLConstants.TRIANGLES);

		int a = 0;
		for (Triangle t : triangles) {
			System.out.println(t.a);
			Color c;
			if (t.equals(next)) {
				c = Color.BLUE;
			} else {
				c = Color.WHITE;
			}
			addVertex(t.a, c);
			addVertex(t.b, c);
			addVertex(t.c, c);

			addIndices(a, a + 1, a + 2);

			a += 3;
		}

		prerender();
	}
}
