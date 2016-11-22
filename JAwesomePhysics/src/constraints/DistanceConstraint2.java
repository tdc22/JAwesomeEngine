package constraints;

import math.VecMath;
import matrix.Matrix1f;
import objects.Constraint2;
import objects.RigidBody;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;

public class DistanceConstraint2 extends Constraint2 {
	Vector2f localAnchorA, localAnchorB;
	float distance;

	public DistanceConstraint2(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB, Vector2f localAnchorA, Vector2f localAnchorB,
			float distance) {
		super(bodyA, bodyB);
		this.localAnchorA = localAnchorA;
		this.localAnchorB = localAnchorB;
		this.distance = distance;
	}

	@Override
	public float[][] getJacobian() {
		// http://myselph.de/gamePhysics/equalityConstraints.html
		Vector2f anchorA = VecMath.transformVector(bodyA.getMatrix(), localAnchorA);
		Vector2f anchorB = VecMath.transformVector(bodyB.getMatrix(), localAnchorB);
		return new float[][] {
				{ 2 * (anchorA.x - anchorB.x), 2 * (anchorA.y - anchorB.y),
						-((anchorA.x - anchorB.x) * (anchorA.y - bodyA.getTranslation().y)
								- (anchorA.y - anchorB.y) * (anchorA.x - bodyA.getTranslation().x)) },
				{ 2 * (anchorB.x - anchorA.x), 2 * (anchorB.y - anchorA.y),
						(anchorA.x - anchorB.x) * (anchorB.y - bodyB.getTranslation().y)
								- (anchorA.x - anchorB.y) * (anchorB.x - bodyB.getTranslation().x) } };
	}
}