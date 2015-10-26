package collisionshape;

import math.QuatMath;
import objects.CollisionShape;
import objects.CollisionShape3;
import objects.SupportCalculator;
import quaternion.Quaternionf;
import shapedata.CapsuleStructure;
import shapedata.EllipsoidStructure;
import vector.Vector2f;
import vector.Vector3f;

public class CapsuleShape extends CollisionShape3 implements CapsuleStructure {
	protected class CapsuleSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf, Quaternionf> collisionshape;

		public CapsuleSupport(
				CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);
			Vector2f v2 = new Vector2f(v.x, v.z);
			if (v2.lengthSquared() != 0)
				v2.normalize();
			return new Vector3f(v2.x * radius, v.y < 0 ? -halfheight
					: halfheight, v2.y * radius);
			// TODO
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			if (direction.lengthSquared() == 0)
				direction = new Vector3f(0, -1, 0);
			direction.normalize();
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);
			return new Vector3f(-v.x * radiusX, -v.y * radiusY, -v.z * radiusZ);
			// TODO
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	float radius, halfheight;

	public CapsuleShape(float x, float y, float z, float radius, float halfheight) {
		super();
		translate(x, y, z);
		this.radius = radius;
		this.halfheight = halfheight;
		init();
	}

	public CapsuleShape(Vector3f pos, float radius, float halfheight) {
		super();
		translate(pos);
		this.radius = radius;
		this.halfheight = halfheight;
		init();
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new CapsuleSupport(cs);
	}

	@Override
	public float getRadius() {
		return radius;
	}
	
	@Override
	public float getHalfHeight() {
		return halfheight;
	}
	
	@Override
	public float getHeight() {
		return 2 * halfheight;
	}

	private void init() {
		float longest = halfheight + radius;
		setAABB(new Vector3f(-longest, -longest, -longest), new Vector3f(
				longest, longest, longest));
		supportcalculator = createSupportCalculator(this);
	}
}