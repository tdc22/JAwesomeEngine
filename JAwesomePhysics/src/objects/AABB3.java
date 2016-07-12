package objects;

import math.VecMath;
import vector.Vector3f;

public class AABB3 extends AABB<Vector3f> {

	public AABB3() {
		super(new Vector3f(), new Vector3f());
	}

	public AABB3(Vector3f min, Vector3f max) {
		super(min, max);
	}

	@Override
	public boolean contains(Vector3f point) {
		return (point.x >= min.x && point.y >= min.y && point.z >= min.z && point.x <= max.x && point.y <= max.y
				&& point.z <= max.z);
	}

	@Override
	public boolean intersects(AABB<Vector3f> aabb) {
		return !(max.x < aabb.min.x || max.y < aabb.min.y || max.z < aabb.min.z || min.x > aabb.max.x
				|| min.y > aabb.max.y || min.z > aabb.max.x);
	}

	@Override
	public AABB<Vector3f> union(AABB<Vector3f> aabb) {
		Vector3f newmin = new Vector3f();
		Vector3f newmax = new Vector3f();

		newmin.x = Math.min(min.x, aabb.min.x);
		newmin.y = Math.min(min.y, aabb.min.y);
		newmin.z = Math.min(min.z, aabb.min.z);
		newmax.x = Math.max(max.x, aabb.max.x);
		newmax.y = Math.max(max.y, aabb.max.y);
		newmax.z = Math.max(max.z, aabb.max.z);

		return new AABB3(newmin, newmax);
	}

	@Override
	public float volume() {
		Vector3f size = VecMath.subtraction(max, min);
		return size.x * size.y * size.z;
	}

	@Override
	public Vector3f getCenter() {
		return new Vector3f(min.x + (max.x - min.x) / 2f, min.y + (max.y - min.y) / 2f, min.z + (max.z - min.z) / 2f);
	}

}
