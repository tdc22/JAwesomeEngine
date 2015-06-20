package physics2dSupportFunction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import objects.CollisionShape;
import objects.ShapedObject2;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class SupportObject extends ShapedObject2 {
	ShapedObject2 so;
	CollisionShape<Vector2f, ?, ?> rb;

	public SupportObject(ShapedObject2 s, CollisionShape<Vector2f, ?, ?> r) {
		rendermode = GLConstants.POINTS;
		so = s;
		rb = r;
		updateShape();
	}

	public void updateShape() {
		List<Vector3f> v1 = so.getVertices();

		deleteData();

		List<Vector2f> result = new ArrayList<Vector2f>();
		for (Vector3f v : v1) {
			if (v.length() > 0) {
				Vector2f res = rb.supportPoint(new Vector2f(v.x, v.y));
				if (!result.contains(res))
					result.add(res);
			}
		}

		for (int r = 0; r < result.size(); r++) {
			addVertex(result.get(r), Color.RED, new Vector2f());
			addIndex(r);
		}
		this.prerender();
	}
}