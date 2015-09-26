package constraints;

import matrix.Matrix1f;
import objects.Constraint2;
import objects.RigidBody;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;

public class DistanceConstraint2 extends Constraint2 {
	Vector2f anchorA, anchorB;
	// ??? http://www.gamedev.net/topic/540730-constraints-and-linear-programming/
	// source1: http://www.dyn4j.org/2010/07/equality-constraints/
	// source2: http://www.dyn4j.org/2010/07/point-to-point-constraint/
	// source3: http://www.dyn4j.org/2010/09/distance-constraint/
	// source: https://github.com/erincatto/Box2D/blob/master/Box2D/Box2D/Dynamics/Joints/b2WeldJoint.cpp
	public DistanceConstraint2(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB, Vector2f anchorA, Vector2f anchorB) {
		super(bodyA, bodyB);
		this.anchorA = anchorA;
		this.anchorB = anchorB;
	}

	@Override
	protected float[][] calculateJacobian() {
		Vector2f d = new Vector2f((bodyB.getTranslation2().x + anchorB.x) - (bodyA.getTranslation2().x + anchorA.x), (bodyB.getTranslation2().y + anchorB.y) - (bodyA.getTranslation2().y + anchorA.y));
		return new float[][] {{-d.x, 0, 0, 0, 0, 0}, 
								{0, -d.y, 0, 0, 0, 0}, 
								{0, 0, (float) -bodyA.getRotation().get2dRotation().angle(), 0, 0, 0}, 
								{0, 0, 0, d.x, 0, 0}, 
								{0, 0, 0, 0, d.y, 0}, 
								{0, 0, 0, 0, 0, (float) bodyB.getRotation().get2dRotation().angle()}};
	}
	// doesn't work, TRY THIS: http://www.bulletphysics.com/ftp/pub/test/physics/papers/IterativeDynamics.pdf <<<<<<<<<<<<<<<<<<<<<<<<<<<
}