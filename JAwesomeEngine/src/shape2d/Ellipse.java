package shape2d;

import java.awt.Color;

import objects.ShapedObject2;
import shapedata2d.EllipseStructure;
import vector.Vector2f;

public class Ellipse extends ShapedObject2 implements EllipseStructure {
	float radius, height;
	int slices;

	public Ellipse(float x, float y, float radius, float height, int slices) {
		super();
		translateTo(x, y);
		init(radius, height, slices);
	}

	public Ellipse(Vector2f pos, float radius, float height, int slices) {
		super();
		translateTo(pos);
		init(radius, height, slices);
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	private void init(float radius, float height, int slices) {
		shapetype = SHAPE_ELLIPSE;
		this.radius = radius;
		this.height = height;
		this.slices = slices;
		Color color = Color.GRAY;

		float anglestep = 360 / (float) slices;
		addVertex(new Vector2f(0, 0), color, new Vector2f(0, 0));
		for (int s = 0; s < slices; s++) {
			Vector2f v = new Vector2f(radius
					* (float) (Math.sin(Math.toRadians(s * anglestep))), height
					* (float) (Math.cos(Math.toRadians(s * anglestep))));
			addVertex(v, color, v);
		}
		for(int s = 0; s < slices-1; s++) {
			addTriangle(0, s + 1, s + 2);
		}
		addTriangle(0, slices, 1);

		this.prerender();
	}
}
