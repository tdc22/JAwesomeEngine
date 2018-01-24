package debug_ConvexHull4;

import java.util.List;

import debug_ConvexHull4.ConvexHullDebugger.Triangle;
import gui.Color;
import objects.ShapedObject3;
import utils.GLConstants;
import vector.Vector3f;

public class Simplex extends ShapedObject3 {
	public Simplex(List<Vector3f> vertices, List<Triangle> triangles, Triangle next) {
		setRenderMode(GLConstants.TRIANGLES);

		int a = 0;
		for (Triangle t : triangles) {
			Color c;
			if (t.equals(next)) {
				c = Color.BLUE;
			} else {
				c = Color.WHITE;
			}
			addVertex(new Vector3f(vertices.get(t.a)), c);
			addVertex(new Vector3f(vertices.get(t.b)), c);
			addVertex(new Vector3f(vertices.get(t.c)), c);

			addIndices(a, a + 1, a + 2);

			a += 3;
		}

		prerender();
	}
}
