package debug_EPARegion;

import objects.ShapedObject3;
import utils.GLConstants;

public class Points extends ShapedObject3 {
	int region;

	public Points() {
		rendermode = GLConstants.POINTS;
		// addVertex(new Vector3f(0, 0, 0), Color.GRAY, new Vector2f(0, 0), new
		// Vector3f(0, 1, 0));
		// addIndex(0);
		// translate(vec);
		prerender();
	}

	public void update() {
		prerender();
	}
}
