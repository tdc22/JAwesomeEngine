package misc;

import math.QuatMath;
import objects.CollisionShape;
import objects.CollisionShape3;
import objects.SupportCalculator;
import quaternion.Quaternionf;
import vector.Vector3f;

public class HalfSphereShape extends CollisionShape3 {
	protected class HalfSphereSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf, Quaternionf> collisionshape;

		public HalfSphereSupport(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			if (direction.y < 0)
				direction.y = 0;
			if (direction.lengthSquared() == 0)
				direction = new Vector3f(0, 1, 0);
			direction.normalize();
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);
			v.scale(radius);
			return v;
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			if (direction.y > 0)
				direction.y = 0;
			if (direction.lengthSquared() == 0)
				direction = new Vector3f(0, -1, 0);
			direction.normalize();
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);
			v.scale(-radius);
			return v;
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	float radius;

	public HalfSphereShape(float x, float y, float z, float radius) {
		super();
		translate(x, y, z);
		this.radius = radius;
		init();
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new HalfSphereSupport(cs);
	}

	private void init() {
		float longest = radius;
		setAABB(new Vector3f(-longest, -longest, -longest), new Vector3f(longest, longest, longest));
		supportcalculator = createSupportCalculator(this);
	}
}