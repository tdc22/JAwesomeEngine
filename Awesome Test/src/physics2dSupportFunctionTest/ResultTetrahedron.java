package physics2dSupportFunctionTest;

import java.awt.Color;
import java.util.List;

import objects.ShapedObject2;
import vector.Vector2f;

public class ResultTetrahedron extends ShapedObject2 {
	public ResultTetrahedron(List<Vector2f> simplex) {
		setRenderMode(GL11.GL_TRIANGLES);

		for (int v = simplex.size() - 1; v > -1; v--) {
			// System.out.println(v);
			addVertex(simplex.get(v), Color.GREEN, new Vector2f());
			addIndex(v);
		}

		if (simplex.size() == 4) {
			addIndices(2, 3, 3, 1, 0, 2, 0, 1);
		}

		prerender();
	}
}