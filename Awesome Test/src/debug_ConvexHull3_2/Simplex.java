package debug_ConvexHull3_2;

import java.awt.Color;
import java.util.List;

import objects.ShapedObject3;
import utils.GLConstants;
import vector.Vector3f;
import debug_ConvexHull3_2.ConvexHullDebugger.Triangle;

public class Simplex extends ShapedObject3 {
	public Simplex(List<Vector3f> vertices, List<Triangle> triangles,
			Triangle next) {
		setRenderMode(GLConstants.TRIANGLES);

		int a = 0;
		for (Triangle t : triangles) {
			Color c;
			if (t.equals(next)) {
				c = Color.BLUE;
				System.out.println("NEXT!!!!!!");
				System.out.println(t + "; " + t.a + "; " + t.b + "; " + t.c
						+ "; " + vertices.get(t.a) + "; " + vertices.get(t.b)
						+ "; " + vertices.get(t.c));
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
