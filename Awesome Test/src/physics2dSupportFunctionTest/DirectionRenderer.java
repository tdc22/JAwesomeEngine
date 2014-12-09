package physics2dSupportFunctionTest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import objects.ShapedObject2;
import vector.Vector2f;

public class DirectionRenderer extends ShapedObject2 {
	public DirectionRenderer() {
		rendermode = GL11.GL_LINES;
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
