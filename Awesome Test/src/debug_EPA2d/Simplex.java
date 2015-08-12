package debug_EPA2d;

import java.awt.Color;
import java.util.List;

import debug_EPA2d.EPA2dDebugger.Edge;
import objects.ShapedObject2;
import utils.GLConstants;
import vector.Vector2f;

public class Simplex extends ShapedObject2 {
	public Simplex(List<Vector2f> verts, Edge closest) {
		setRenderMode(GLConstants.LINE_LOOP);
		translate(200, 200);

		for (int i = 0; i < verts.size(); i++) {
			Vector2f v = verts.get(i);
			Color c = (closest.a.equals(v) || closest.b.equals(v)) ? Color.BLUE : Color.WHITE;
			addVertex(v, c);
			addIndex(i);
		}

		prerender();
	}
}
