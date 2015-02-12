package objects;

import vector.Vector;

public abstract class Constraint<L extends Vector> {
	protected RigidBody<L, ?, ?, ?> bodyA, bodyB;

	// good source:
	// http://www.wildbunny.co.uk/blog/2011/04/06/physics-engines-for-dummies/
	public Constraint(RigidBody<L, ?, ?, ?> bodyA, RigidBody<L, ?, ?, ?> bodyB) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
	}

	public abstract void applyCentralImpulse(L impulse);

	public abstract void solve(float delta);
}