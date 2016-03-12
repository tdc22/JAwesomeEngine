package constraints;

import matrix.Matrix1f;
import objects.Constraint2;
import objects.RigidBody;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;

public class DistanceConstraint2 extends Constraint2 {
	float distance;

	public DistanceConstraint2(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB, float distance) {
		super(bodyA, bodyB);
		this.distance = distance;
	}

	@Override
	public float[][] getJacobian() {
		// http://myselph.de/gamePhysics/equalityConstraints.html
		return new float[][] {{0f, 0f, 0f}, {0f, 0f, 0f}};
	}

}
