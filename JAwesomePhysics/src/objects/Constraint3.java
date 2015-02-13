package objects;

import math.VecMath;
import vector.Vector3f;

public abstract class Constraint3 extends Constraint<Vector3f> {

	public Constraint3(RigidBody3 bodyA, RigidBody3 bodyB) {
		super(bodyA, bodyB);
	}

	@Override
	public void applyCentralImpulse(Vector3f impulse) {
		bodyA.applyCentralImpulse(impulse);
		bodyB.applyCentralImpulse(VecMath.negate(impulse));
	}
}