package collisionshape;

import math.QuatMath;
import objects.CollisionShape;
import objects.CollisionShape3;
import objects.SupportCalculator;
import quaternion.Quaternionf;
import shapedata.EllipsoidStructure;
import vector.Vector3f;

public class EllipsoidShape extends CollisionShape3 implements EllipsoidStructure {
	protected class EllipsoidSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf, Quaternionf> collisionshape;

		public EllipsoidSupport(
				CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			if (direction.lengthSquared() == 0)
				direction = new Vector3f(0, 1, 0);
			direction.normalize();
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);
			return new Vector3f(v.x * radiusX, v.y * radiusY, v.z * radiusZ);
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			if (direction.lengthSquared() == 0)
				direction = new Vector3f(0, -1, 0);
			direction.normalize();
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);
			return new Vector3f(-v.x * radiusX, -v.y * radiusY, -v.z * radiusZ);
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	float radiusX, radiusY, radiusZ;

	public EllipsoidShape(float x, float y, float z, float radiusX, float radiusY, float radiusZ) {
		super();
		translate(x, y, z);
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.radiusZ = radiusZ;
		init();
	}

	public EllipsoidShape(Vector3f pos, float radiusX, float radiusY, float radiusZ) {
		super();
		translate(pos);
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.radiusZ = radiusZ;
		init();
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new EllipsoidSupport(cs);
	}

	@Override
	public float getRadiusX() {
		return radiusX;
	}
	
	@Override
	public float getRadiusY() {
		return radiusY;
	}
	
	@Override
	public float getRadiusZ() {
		return radiusZ;
	}

	private void init() {
		float longest = radiusX > radiusY ? (radiusX > radiusZ ? radiusX : radiusZ) : (radiusY > radiusZ ? radiusY : radiusZ);
		setAABB(new Vector3f(-longest, -longest, -longest), new Vector3f(
				longest, longest, longest));
		supportcalculator = createSupportCalculator(this);
	}
}