package collisionshape;

import math.QuatMath;
import matrix.Matrix3f;
import objects.CollisionShape;
import objects.CollisionShape3;
import objects.InertiaCalculator;
import objects.SupportCalculator;
import quaternion.Quaternionf;
import shapedata.CapsuleStructure;
import vector.Vector3f;

public class CapsuleShape extends CollisionShape3 implements CapsuleStructure {
	protected class CapsuleInertia implements InertiaCalculator<Quaternionf> {
		@Override
		public Quaternionf calculateInertia(float mass) {
			float fmass = mass / 5f;
			Matrix3f inertiaMatrix = new Matrix3f(fmass
					* (height * height + radius * radius), 0, 0, 0, fmass
					* (radius * radius * 2), 0, 0, 0, fmass
					* (radius * radius + height * height));
			return inertiaMatrix.toQuaternionDiagonalf();
		}
	}

	protected class CapsuleSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf, Quaternionf> collisionshape;

		public CapsuleSupport(
				CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			if (direction.length() == 0)
				direction = new Vector3f(0, 1, 0);
			direction.normalize();
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);
			return new Vector3f(v.x * radius, v.y * height, v.z * radius);
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			if (direction.length() == 0)
				direction = new Vector3f(0, -1, 0);
			direction.normalize();
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);
			return new Vector3f(-v.x * radius, -v.y * height, -v.z * radius);
		}
	}

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
	public InertiaCalculator<Quaternionf> createInertiaCalculator() {
		return new CapsuleInertia();
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new CapsuleSupport(cs);
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
		supportcalculator = createSupportCalculator(this);
	}
}