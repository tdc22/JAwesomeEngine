package physicsSupportFunctionTest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import objects.RigidBody;
import objects.ShapedObject;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class SupportObject extends ShapedObject {
	ShapedObject so;
	RigidBody<Vector3f, ?, ?, ?> rb;

	public SupportObject(ShapedObject s, RigidBody<Vector3f, ?, ?, ?> r) {
		rendermode = GLConstants.POINTS;
		so = s;
		rb = r;
		updateShape();
	}

	public void updateShape() {
		List<Vector3f> v1 = so.getVertices();

		deleteData();

		List<Vector3f> result = new ArrayList<Vector3f>();
		for (Vector3f v : v1) {
			Vector3f res = rb.supportPoint(v);
			if (!result.contains(res))
				result.add(res);
		}

		for (int r = 0; r < result.size(); r++) {
			addVertex(result.get(r), Color.RED, new Vector2f(), new Vector3f(0,
					1, 0));
			addIndex(r);
		}
		this.prerender();
	}
}