package debug_GJK;

import java.util.List;

import gui.Color;
import objects.ShapedObject3;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class Simplex extends ShapedObject3 {
	public Simplex(List<Vector3f> simplices) {
		int simplexsize = simplices.size();
		if (simplexsize == 1) {
			setRenderMode(GLConstants.POINTS);
		}
		if (simplexsize == 2) {
			setRenderMode(GLConstants.LINES);
		}
		if (simplexsize >= 3) {
			setRenderMode(GLConstants.TRIANGLES);
		}

		for (int i = 0; i < simplices.size(); i++) {
			addVertex(simplices.get(i), Color.GRAY, new Vector2f(0, 0), new Vector3f(0, 1, 0));
			addIndex(i);
		}
		if (simplices.size() == 4) {
			addIndices(1, 0, 3, 2, 1, 3, 0, 2);
		}

		prerender();
	}
}
