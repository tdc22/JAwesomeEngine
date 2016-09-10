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
			System.out.println(t.a + "; " + t.b + "; " + t.c);
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
		// addVertex(new Vector3f(11.404785, 10.446343, -2.761249));
		// addVertex(new Vector3f(5.6260934, 5.6621804, 11.700975));
		// addVertex(new Vector3f(6.812222, -1.6368495, 9.790112));
		// addIndices(0, 1, 2);
		// addVertex(new Vector3f(11.404785, 10.446343, -2.761249));
		// addVertex(new Vector3f(6.812222, -1.6368495, 9.790112));
		// addVertex(new Vector3f(2.2193725, -1.7034389, -4.2405186));
		// addIndices(3, 4, 5);
		// addVertex(new Vector3f(11.404785, 10.446343, -2.761249));
		// addVertex(new Vector3f(2.2193725, -1.7034389, -4.2405186));
		// addVertex(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		// addIndices(6, 7, 8);
		// addVertex(new Vector3f(5.6260934, 5.6621804, 11.700975));
		// addVertex(new Vector3f(11.404785, 10.446343, -2.761249));
		// addVertex(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		// addIndices(9, 10, 11);
		// addVertex(new Vector3f(6.812222, -1.6368495, 9.790112));
		// addVertex(new Vector3f(5.6260934, 5.6621804, 11.700975));
		// addVertex(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		// addIndices(12, 13, 14);
		// addVertex(new Vector3f(2.2193725, -1.7034389, -4.2405186));
		// addVertex(new Vector3f(6.812222, -1.6368495, 9.790112));
		// addVertex(new Vector3f(-0.8538285, 3.822501, 4.7598157));
		// addIndices(15, 16, 17);
		System.out.println("SIMPLEX TRIS: " + a / 3f + "; " + indices.size() / 3f + "; " + vertices.size() / 3f);

		prerender();
	}
}
