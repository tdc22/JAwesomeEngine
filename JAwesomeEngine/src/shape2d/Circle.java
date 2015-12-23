package shape2d;

import shapedata2d.CircleStructure;
import vector.Vector2f;

public class Circle extends Ellipse implements CircleStructure {
	public Circle(float x, float y, float radius, int slices) {
		super(x, y, radius, radius, slices);
		shapetype = SHAPE_CIRCLE;
	}

	public Circle(Vector2f pos, float radius, int slices) {
		super(pos, radius, radius, slices);
		shapetype = SHAPE_CIRCLE;
	}

	public Circle(float x, float y, float radius, int slices, boolean adjacency) {
		super(x, y, radius, radius, slices, adjacency);
		shapetype = SHAPE_CIRCLE;
	}

	public Circle(Vector2f pos, float radius, int slices, boolean adjacency) {
		super(pos, radius, radius, slices, adjacency);
		shapetype = SHAPE_CIRCLE;
	}
}
