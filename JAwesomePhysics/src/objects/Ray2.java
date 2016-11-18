package objects;

import vector.Vector2f;

public class Ray2 extends Ray<Vector2f> {

	public Ray2(Vector2f pos, Vector2f dir) {
		super(pos, dir);
	}

	@Override
	public Vector2f pointOnRay(float lambda) {
		return new Vector2f(pos.x + lambda * dir.x, pos.y + lambda * dir.y);
	}

}