package objects;

import math.VecMath;
import vector.Vector2f;

public abstract class Constraint2 extends Constraint<Vector2f> {

	public Constraint2(RigidBody2 bodyA,
			RigidBody2 bodyB) {
		super(bodyA, bodyB);
	}

	@Override
	public void applyCentralImpulse(Vector2f impulse) {
		bodyA.applyCentralImpulse(impulse);
		bodyB.applyCentralImpulse(VecMath.negate(impulse));
	}
}