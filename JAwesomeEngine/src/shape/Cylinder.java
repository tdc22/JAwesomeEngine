package shape;

import gui.Color;
import math.VecMath;
import objects.ShapedObject3;
import shapedata.CylinderStructure;
import vector.Vector2f;
import vector.Vector3f;

public class Cylinder extends ShapedObject3 implements CylinderStructure {
	float radius, halfheight;
	int slices;

	public Cylinder(float x, float y, float z, float radius, float halfheight, int slices) {
		super(x, y, z);
		init(radius, halfheight, slices);
	}

	public Cylinder(Vector3f pos, float radius, float halfheight, int slices) {
		super(pos);
		init(radius, halfheight, slices);
	}

	@Override
	public float getHalfHeight() {
		return halfheight;
	}

	@Override
	public float getHeight() {
		return 2 * halfheight;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	public int getSlices() {
		return slices;
	}

	private void init(float r, float h, int slices) {
		shapetype = SHAPE_CYLINDER;
		radius = r;
		halfheight = h;
		this.slices = slices;
		Color c = Color.WHITE;

		addVertex(new Vector3f(0, halfheight, 0), c, new Vector2f(0, 0), new Vector3f(0, 1, 0));
		final float angleStep = 360 / (float) slices;
		Vector3f[] circle = new Vector3f[slices];
		Vector3f[] circlenormals = new Vector3f[slices];
		for (int a = 0; a < slices; a++) {
			double angleStepARad = Math.toRadians(angleStep * a);
			Vector3f pos = new Vector3f((float) Math.sin(angleStepARad) * radius, 0,
					(float) Math.cos(angleStepARad) * radius);
			circle[a] = pos;
			circlenormals[a] = VecMath.normalize(pos);
		}
		final float textureStep = 1 / (float) slices;
		for (int a = 0; a < slices; a++) {
			Vector3f pos = circle[a];
			float texturepos = a * textureStep;
			addVertex(new Vector3f(pos.x, halfheight, pos.z), c, new Vector2f(texturepos, 1), circlenormals[a]);
		}
		for (int a = 0; a < slices; a++) {
			Vector3f pos = circle[a];
			float texturepos = a * textureStep;
			addVertex(new Vector3f(pos.x, -halfheight, pos.z), c, new Vector2f(texturepos, 0), circlenormals[a]);
		}

		addVertex(new Vector3f(0, -halfheight, 0), c, new Vector2f(1, 1), new Vector3f(0, -1, 0));
		int size = getVertices().size() - 1;

		// Top
		for (int a = 1; a < slices - 1; a++) {
			addTriangle(0, a - 1, a, slices + a + 1, a + 1, a + 2);
		}
		addTriangle(0, slices - 2, slices - 1, slices * 2, slices, 1);
		addTriangle(0, slices - 1, slices, slices + 1, 1, 2);

		// Mantle
		addQuad(1, slices, 1 + slices, size, 2 + slices, slices + 3, 2, 0);
		for (int a = 2; a < slices - 1; a++) {
			addQuad(a, a - 1, a + slices, size, a + slices + 1, a + slices + 2, a + 1, 0);
		}
		addQuad(slices - 1, slices - 2, slices * 2 - 1, size, 2 * slices, slices + 1, slices, 0);
		addQuad(slices, slices - 1, slices * 2, size, 1 + slices, slices + 2, 1, 0);

		// Bottom
		for (int a = 1; a < slices - 1; a++) {
			addTriangle(size, slices + a + 2, slices + a + 1, a, slices + a, slices + a - 1);
		}
		addTriangle(size, slices + 2, slices + 1, slices, 2 * slices, 2 * slices - 1);
		addTriangle(size, slices + 1, 2 * slices, slices - 1, 2 * slices - 1, 2 * slices - 2);

		this.prerender();
	}
}
