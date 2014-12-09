package collisionshape;

import math.QuatMath;
import objects.RigidBody3;
import shapedata.CylinderStructure;
import vector.Vector2f;
import vector.Vector3f;

public class CylinderShape extends RigidBody3 implements CylinderStructure {
	float radius, halfheight;

	public CylinderShape(float x, float y, float z, float radius,
			float halfheight) {
		super();
		translate(x, y, z);
		this.radius = radius;
		this.halfheight = halfheight;
		init();
	}

	public CylinderShape(Vector3f pos, float radius, float halfheight) {
		super();
		translate(pos);
		this.radius = radius;
		this.halfheight = halfheight;
		init();
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

	private void init() {
		float diag = (float) Math.sqrt(radius * radius + halfheight
				* halfheight);
		setAABB(new Vector3f(-diag, -diag, -diag), new Vector3f(diag, diag,
				diag));
	}

	@Override
	public Vector3f supportPointLocal(Vector3f direction) {
		Vector3f v = QuatMath.transform(this.getInverseRotation(), direction);
		Vector2f v2 = new Vector2f(v.x, v.z);
		if (v2.length() == 0)
			v2.set(1, 0);
		;
		v2.normalize();
		return new Vector3f(v2.x * radius, v.y < 0 ? -halfheight : halfheight,
				v2.y * radius);
	}
}
