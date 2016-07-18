package physics2dRaycasting;

import math.VecMath;
import objects.Ray2;
import objects.ShapedObject2;
import utils.GLConstants;

public class RayVisualization extends ShapedObject2 {
	Ray2 ray;

	public RayVisualization(Ray2 ray) {
		this.ray = ray;
		setRenderMode(GLConstants.LINES);
		updateVisualization();
	}

	public void updateVisualization() {
		delete();
		addVertex(ray.getPosition());
		addVertex(VecMath.scale(ray.getDirection(), 100000));
		addIndices(0, 1);
		prerender();
	}
}