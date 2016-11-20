package objects;

import vector.Vector3f;

public class Ray3 extends Ray<Vector3f> {

	public Ray3(Vector3f pos, Vector3f dir) {
		super(pos, dir);
	}

	@Override
	public Vector3f pointOnRay(float lambda) {
		return new Vector3f(pos.x + lambda * dir.x, pos.y + lambda * dir.y,
				pos.z + lambda * dir.z);
	}
}