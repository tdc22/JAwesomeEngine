package objects;

import matrix.Matrix1f;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;

public abstract class Constraint2 extends
		Constraint<Vector2f, Vector1f, Complexf, Matrix1f> {

	public Constraint2(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB) {
		super(bodyA, bodyB);
	}
}