package physics2dSupportFunction;

import java.util.ArrayList;
import java.util.List;

import gui.Color;
import objects.ShapedObject2;
import utils.GLConstants;
import vector.Vector2f;

public class DirectionRenderer2 extends ShapedObject2 {
	public DirectionRenderer2() {
		rendermode = GLConstants.LINES;
		setDirections(new ArrayList<Vector2f>());
	}

	public void setDirections(List<Vector2f> dirs) {
		deleteData();

		addVertex(new Vector2f(0, 0), Color.GRAY, new Vector2f());
		for (int d = 0; d < dirs.size(); d++) {
			addVertex(dirs.get(d), Color.GRAY, new Vector2f());
			addIndex(0);
			addIndex(d + 1);
		}

		this.prerender();
	}
}
