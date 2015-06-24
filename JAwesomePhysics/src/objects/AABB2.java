package objects;

import math.VecMath;
import vector.Vector2f;

public class AABB2 extends AABB<Vector2f> {

	public AABB2() {
		super(new Vector2f(), new Vector2f());
	}

	public AABB2(Vector2f min, Vector2f max) {
		super(min, max);
	}

	@Override
	public boolean contains(Vector2f point) {
		return (point.x >= min.x && point.y >= min.y && point.x <= max.x && point.y <= max.y);
	}

	@Override
	public boolean intersects(AABB<Vector2f> aabb) {
		return !(max.x < aabb.min.x || max.y < aabb.min.y || min.x > aabb.max.x || min.y > aabb.max.y);
	}

	@Override
	public AABB<Vector2f> union(AABB<Vector2f> aabb) {
		Vector2f min = new Vector2f();
		Vector2f max = new Vector2f();

		min.x = Math.min(min.x, aabb.min.x);
		min.y = Math.min(min.y, aabb.min.y);
		max.x = Math.max(max.x, aabb.max.x);
		max.y = Math.max(max.y, aabb.max.y);

		return new AABB2(min, max);
	}

	@Override
	public float volume() {
		Vector2f size = VecMath.subtraction(max, min);
		return size.x * size.y;
	}

	@Override
	public Vector2f getCenter() {
		return new Vector2f(min.x + (max.x - min.x) / 2f, min.y
				+ (max.y - min.y) / 2f);
	}

}
