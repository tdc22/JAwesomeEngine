package objects;

import quaternion.Quaternionf;
import vector.Vector3f;

public abstract class Constraint3 extends Constraint<Vector3f, Vector3f, Quaternionf, Quaternionf> {

	public Constraint3(RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> bodyA, RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> bodyB) {
		super(bodyA, bodyB);
	}
}