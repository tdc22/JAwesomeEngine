package shape;

import shapedata.SphereStructure;
import vector.Vector3f;

public class Sphere extends Capsule implements SphereStructure {
	public Sphere(float x, float y, float z, float radius, int trisH, int trisV) {
		super(x, y, z, radius, radius, trisH, trisV);
		shapetype = SHAPE_SPHERE;
	}

	public Sphere(Vector3f pos, float radius, int trisH, int trisV) {
		super(pos, radius, radius, trisH, trisV);
		shapetype = SHAPE_SPHERE;
	}
}