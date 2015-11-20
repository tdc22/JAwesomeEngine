package debug_EPA2d;

import java.awt.Color;
import java.util.List;

import debug_EPA2d.EPA2dDebugger.Edge;
import objects.ShapedObject2;
import utils.GLConstants;
import vector.Vector2f;

public class Simplex extends ShapedObject2 {
	public Simplex(List<Edge> edges, Edge closest) {
		setRenderMode(GLConstants.LINES);

		int a = 0;
		for (Edge e : edges) {
			boolean oita = isOriginInsideEdgeArea(e);
			Color c;
			if (e.equals(closest)) {
				c = oita ? Color.BLUE : Color.CYAN;
			} else {
				c = oita ? Color.WHITE : Color.RED;
			}
			addVertex(e.a, c);
			addVertex(e.b, c);

			addIndices(a, a + 1);

			a += 2;
		}

		prerender();
	}

	private final float EPSILON = 0.001f;

	private boolean isOriginInsideEdgeArea(Edge e) {
		return (checkEdge(e.a, e.normal) && checkEdge(e.b, e.normal));
	}

	private boolean checkEdge(Vector2f a, Vector2f normal) {
		return (-a.x * normal.x + -a.y * normal.y <= EPSILON);
	}
}
