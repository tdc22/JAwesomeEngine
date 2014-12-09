package collisionshape;

import math.QuatMath;
import objects.RigidBody3;
import shapedata.CapsuleStructure;
import vector.Vector3f;

public class CapsuleShape extends RigidBody3 implements CapsuleStructure {
	float radius, height;

	public CapsuleShape(float x, float y, float z, float radius, float height) {
		super();
		translate(x, y, z);
		this.radius = radius;
		this.height = height;
		init();
	}

	public CapsuleShape(Vector3f pos, float radius, float height) {
		super();
		translate(pos);
		this.radius = radius;
		this.height = height;
		init();
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	private void init() {
		float longest = radius > height ? radius : height;
		setAABB(new Vector3f(-longest, -longest, -longest), new Vector3f(
				longest, longest, longest));
	}

	@Override
	public Vector3f supportPointLocal(Vector3f direction) {
		if (direction.length() == 0)
			direction.set(0, 1, 0);
		direction.normalize();
		Vector3f v = QuatMath.transform(this.getInverseRotation(), direction);
		return new Vector3f(v.x * radius, v.y * height, v.z * radius);
	}
}