package shape2d;

import java.awt.Color;

import objects.ShapedObject2;
import shapedata2d.EllipseStructure;
import utils.GLConstants;
import vector.Vector2f;

public class Ellipse extends ShapedObject2 implements EllipseStructure {
	float radius, halfheight;
	int slices;

	public Ellipse(float x, float y, float radius, float halfheight, int slices) {
		super(x, y);
		init(radius, halfheight, slices, true);
	}

	public Ellipse(Vector2f pos, float radius, float halfheight, int slices) {
		super(pos);
		init(radius, halfheight, slices, true);
	}

	public Ellipse(float x, float y, float radius, float halfheight, int slices, boolean adjacency) {
		super(x, y);
		init(radius, halfheight, slices, adjacency);
	}

	public Ellipse(Vector2f pos, float radius, float halfheight, int slices, boolean adjacency) {
		super(pos);
		init(radius, halfheight, slices, adjacency);
	}

	@Override
	public float getHeight() {
		return 2 * halfheight;
	}

	@Override
	public float getHalfHeight() {
		return halfheight;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	private void init(float radius, float halfheight, int slices, boolean adjacency) {
		shapetype = SHAPE_ELLIPSE;
		this.radius = radius;
		this.halfheight = halfheight;
		this.slices = slices;
		Color color = Color.GRAY;

		float anglestep = (float) (2 * Math.PI / (float) slices);
		addVertex(new Vector2f(0, 0), color, new Vector2f(0.5f, 0.5f));
		for (int s = 0; s < slices; s++) {
			Vector2f t = new Vector2f(Math.sin(s * anglestep), Math.cos(s * anglestep));
			Vector2f v = new Vector2f(radius * t.x, halfheight * t.y);
			t.x = (t.x + 1) * 0.5f;
			t.y = 1 - (t.y + 1) * 0.5f;
			addVertex(v, color, t);
		}

		if (adjacency) {
			setRenderMode(GLConstants.TRIANGLE_ADJACENCY);
			for (int s = 1; s < slices - 2; s++) {
				addTriangle(0, s, s + 1, s + 1, s + 2, s + 3);
			}
			addTriangle(0, slices - 2, slices - 1, slices - 1, slices, 1);
			addTriangle(0, slices - 1, slices, slices, 1, 2);
			addTriangle(0, slices, 1, 1, 2, 3);
		} else {
			setRenderMode(GLConstants.TRIANGLES);
			for (int s = 0; s < slices - 1; s++) {
				addTriangle(0, s + 1, s + 2);
			}
			addTriangle(0, slices, 1);
		}

		this.prerender();
	}
}
