package constraints;

import math.VecMath;
import objects.Constraint2;
import objects.RigidBody2;
import vector.Vector2f;

public class DistanceConstraint2 extends Constraint2 {
	float distance;

	public DistanceConstraint2(RigidBody2 bodyA, RigidBody2 bodyB,
			float distance) {
		super(bodyA, bodyB);
		this.distance = distance;
	}

	@Override
	public void solve(float delta) {
		if (delta == 0)
			return;
		Vector2f axis = VecMath.subtraction(bodyB.getTranslation2(),
				bodyA.getTranslation2());
		float dist = VecMath.length(axis);
		if (dist == 0)
			return;
		axis.scale(1 / dist);

		float relVel = VecMath.dotproduct(
				VecMath.subtraction(bodyB.getLinearVelocity(),
						bodyA.getLinearVelocity()), axis);
		float relDist = dist - distance;

		float remove = relVel + relDist / delta;
		float impulse = remove
				/ (bodyA.getInverseMass() + bodyB.getInverseMass());

		axis.scale(impulse);
		applyCentralImpulse(axis);
	}
}