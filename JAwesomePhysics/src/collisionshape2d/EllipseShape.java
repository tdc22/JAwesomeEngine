package collisionshape2d;

import math.ComplexMath;
import objects.RigidBody2;
import shapedata2d.EllipseStructure;
import vector.Vector2f;

public class EllipseShape extends RigidBody2 implements EllipseStructure {
	float radius, height;

	public EllipseShape(float x, float y, float radius, float height) {
		super();
		translate(x, y);
		this.radius = radius;
		this.height = height;
		init();
	}

	public EllipseShape(Vector2f pos, float radius, float height) {
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
		setAABB(new Vector2f(-longest, -longest),
				new Vector2f(longest, longest));
	}

	@Override
	public Vector2f supportPointLocal(Vector2f direction) {
		if (direction.length() == 0)
			direction.set(0, 1);
		direction.normalize();
		Vector2f v = ComplexMath
				.transform(this.getInverseRotation(), direction);
		return new Vector2f(v.x * radius, v.y * height);
	}
}