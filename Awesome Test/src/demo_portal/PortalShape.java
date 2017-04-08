package demo_portal;

import gui.Color;
import objects.ShapedObject3;
import shapedata.CylinderStructure;
import vector.Vector2f;
import vector.Vector3f;

public class PortalShape extends ShapedObject3 implements CylinderStructure {
	float radius;

	public PortalShape(Vector3f pos, float radius, float height, int slices) {
		super(pos);
		init(radius, slices);
		scale(1, 1, height);
	}

	@Override
	public float getHalfHeight() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	float TINY_SPACE = 0.005f;

	private void init(float r, int slices) {
		shapetype = SHAPE_CYLINDER;
		radius = r;
		Color c = Color.WHITE;

		addVertex(new Vector3f(0, 0, 0), c, new Vector2f(0.5, 0.5), new Vector3f(0, 1, 0));
		float angleStep = 360 / (float) slices;
		Vector2f[] circle = new Vector2f[slices];
		for (int a = 0; a < slices; a++) {
			circle[a] = new Vector2f((float) Math.sin(Math.toRadians(angleStep * a)) * radius,
					(float) Math.cos(Math.toRadians(angleStep * a)) * radius);
		}
		float anglestep = (float) (2 * Math.PI / (float) slices);
		for (int a = 0; a < slices; a++) {
			Vector2f pos = circle[a];
			Vector2f t = new Vector2f((Math.sin(a * anglestep) + 1) / 2f, 1 - (Math.cos(a * anglestep) + 1) / 2f);
			Vector3f normal = new Vector3f(pos.x, 0, pos.y);
			normal.normalize();
			addVertex(new Vector3f(pos.x, TINY_SPACE, pos.y), c, t, normal);
		}

		// Top
		for (int a = 1; a < slices - 1; a++) {
			addTriangle(0, a - 1, a, slices + a + 1, a + 1, a + 2);
		}
		addTriangle(0, slices - 2, slices - 1, slices * 2, slices, 1);
		addTriangle(0, slices - 1, slices, slices + 1, 1, 2);

		this.prerender();
	}
}