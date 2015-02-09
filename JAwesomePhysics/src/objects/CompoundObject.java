package objects;

import java.util.List;

import quaternion.Quaternionf;
import vector.Vector3f;

public class CompoundObject extends RigidBody3 {
	List<SupportCalculator<Vector3f>> supportcalculators;

	public void addCollisionShape(CollisionShape3 collisionshape) {
		supportcalculators.add(collisionshape.createSupportCalculator(this));
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf> cs) {
		return new CompoundSupport(cs);
	}

	protected class CompoundSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf> collisionshape;

		public CompoundSupport(CollisionShape<Vector3f, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			Vector3f closest = new Vector3f();
			float distance = Float.MAX_VALUE;
			for (SupportCalculator<Vector3f> sc : supportcalculators) {

			}
			return closest;
		}
	}
}