package objects;

import quaternion.Rotation;
import vector.Vector;

public abstract class Constraint<L extends Vector, A1 extends Vector, A2 extends Rotation, A3 extends Rotation> {
	protected float[][] A;
	protected RigidBody<L, A1, A2, A3> bodyA, bodyB;
	
	public Constraint(RigidBody<L, A1, A2, A3> bodyA, RigidBody<L, A1, A2, A3> bodyB) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
	}
	
	// A = J(M^-1)(J^T)
	protected abstract void initializeConstraint();
	
	public abstract void solve(float delta);
}