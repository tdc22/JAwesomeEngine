package physicsSupportFunction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import objects.ShapedObject;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class DirectionRenderer extends ShapedObject {
	public DirectionRenderer() {
		rendermode = GLConstants.LINES;
		setDirections(new ArrayList<Vector3f>());
	}

	public void setDirections(List<Vector3f> dirs) {
		deleteData();

		addVertex(new Vector3f(0, 0, 0), Color.GRAY, new Vector2f(),
				new Vector3f(0, 1, 0));
		for (int d = 0; d < dirs.size(); d++) {
			addVertex(dirs.get(d), Color.GRAY, new Vector2f(), new Vector3f(0,
					1, 0));
			addIndex(0);
			addIndex(d + 1);
		}

		this.prerender();
	}
}
