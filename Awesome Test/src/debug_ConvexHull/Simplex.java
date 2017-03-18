package debug_ConvexHull;

import gui.Color;

import java.util.List;

import debug_ConvexHull.ConvexHullDebugger.Triangle;
import objects.ShapedObject3;
import utils.GLConstants;
import vector.Vector3f;

public class Simplex extends ShapedObject3 {
	public Simplex(List<Triangle> triangles, Triangle next) {
		setRenderMode(GLConstants.TRIANGLES);

		int a = 0;
		for (Triangle t : triangles) {
			Color c;
			if (t.equals(next)) {
				c = Color.BLUE;
			} else {
				c = Color.WHITE;
			}
			addVertex(new Vector3f(t.a), c);
			addVertex(new Vector3f(t.b), c);
			addVertex(new Vector3f(t.c), c);

			addIndices(a, a + 1, a + 2);

			a += 3;
		}
		// System.out.println("SIMPLEX " + triangles.size());
		// if (triangles.size() == 6) {
		// System.out.println("NOW!");
		// Triangle t = triangles.get(0);
		// addVertex(new Vector3f(t.a));
		// addVertex(new Vector3f(t.b));
		// addVertex(new Vector3f(t.c));
		// addIndices(0, 1, 2);
		// t = triangles.get(1);
		// addVertex(new Vector3f(t.a));
		// addVertex(new Vector3f(t.b));
		// addVertex(new Vector3f(t.c));
		// addIndices(3, 4, 5);
		// t = triangles.get(2);
		// addVertex(new Vector3f(t.a));
		// addVertex(new Vector3f(t.b));
		// addVertex(new Vector3f(t.c));
		// addIndices(6, 7, 8);
		// t = triangles.get(3);
		// addVertex(new Vector3f(t.a));
		// addVertex(new Vector3f(t.b));
		// addVertex(new Vector3f(t.c));
		// addIndices(9, 10, 11);
		// t = triangles.get(4);
		// addVertex(new Vector3f(t.a));
		// addVertex(new Vector3f(t.b));
		// addVertex(new Vector3f(t.c));
		// addIndices(12, 13, 14);
		// t = triangles.get(5);
		// addVertex(new Vector3f(t.a));
		// addVertex(new Vector3f(t.b));
		// addVertex(new Vector3f(t.c));
		// addIndices(15, 16, 17);
		// }
		// System.out.println("SIMPLEX TRIS: " + a / 3f + "; " + indices.size()
		// / 3f + "; " + vertices.size() / 3f);

		prerender();
	}
}
