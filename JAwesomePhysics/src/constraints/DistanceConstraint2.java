package constraints;

import math.ComplexMath;
import math.VecMath;
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
	// doesn't work, TRY THIS: http://www.bulletphysics.com/ftp/pub/test/physics/papers/IterativeDynamics.pdf <<<<<<<<<<<<<<<<<<<<<<<<<<<
	Vector2f rA, rB, u;
	float mass, gamma, bias, impulse, frequencyHz, dampingRatio, linearSlop, length;
	
	public DistanceConstraint2(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB, Vector2f anchorA, Vector2f anchorB, float length) {
		super(bodyA, bodyB);
		this.anchorA = anchorA;
		this.anchorB = anchorB;
		this.length = length;
		
		frequencyHz = 1f;
		dampingRatio = 1;
		linearSlop = 0.1f;
	}
	
	@Override
	public void initStep(float delta) {
		Vector2f cA = bodyA.getTranslation();
		float aA = (float) bodyA.getRotation().angle();
		Vector2f vA = bodyA.getLinearVelocity();
		float wA = bodyA.getAngularVelocity().x;

		Vector2f cB = bodyB.getTranslation();
		float aB = (float) bodyB.getRotation().angle();
		Vector2f vB = bodyB.getLinearVelocity();
		float wB = bodyB.getAngularVelocity().x;

		Complexf qA = bodyA.getRotation();
		Complexf qB = bodyB.getRotation();

		System.out.println(qA + "; " + qB);
		rA = ComplexMath.transform(qA, VecMath.subtraction(anchorA, bodyA.getTranslation()));
		rB = ComplexMath.transform(qB, VecMath.subtraction(anchorB, bodyB.getTranslation()));
		u = new Vector2f(cB.x + rB.x - cA.x - rA.x, cB.y + rB.y - cA.y - rA.y);//cB + rB - cA - rA;

		// Handle singularity.
		float dist = (float) u.length();
		if (dist > linearSlop)
		{
			u.scale(1.0f / dist);
		}
		else
		{
			u.set(0.0f, 0.0f);
		}

		float crAu = VecMath.crossproduct(rA, u);
		float crBu = VecMath.crossproduct(rB, u);
		float invMass = (float) (bodyA.getInverseMass() + bodyA.getInverseInertia().get(0, 0) * crAu * crAu + bodyB.getInverseMass() + bodyB.getInverseInertia().get(0, 0) * crBu * crBu);

		// Compute the effective mass matrix.
		mass = invMass != 0.0f ? 1.0f / invMass : 0.0f;

		if (frequencyHz > 0.0f)
		{
			float C = dist - length;

			// Frequency
			float omega = (float) (2.0f * Math.PI * frequencyHz);

			// Damping coefficient
			float d = 2.0f * mass * dampingRatio * omega;

			// Spring stiffness
			float k = mass * omega * omega;

			// magic formulas
			float h = delta;
			gamma = h * (d + h * k);
			gamma = gamma != 0.0f ? 1.0f / gamma : 0.0f;
			bias = C * h * k * gamma;

			invMass += gamma;
			mass = invMass != 0.0f ? 1.0f / invMass : 0.0f;
		}
		else
		{
			gamma = 0.0f;
			bias = 0.0f;
		}

		impulse = 0;

		bodyA.setLinearVelocity(vA);
		bodyA.setAngularVelocity(new Vector1f(wA));
		bodyB.setLinearVelocity(vB);
		bodyB.setAngularVelocity(new Vector1f(wB));
	}
	
	@Override
	public void solve(float delta) {
		Vector2f vA = bodyA.getLinearVelocity();
		float wA = bodyA.getAngularVelocity().x;
		Vector2f vB = bodyB.getLinearVelocity();
		float wB = bodyB.getAngularVelocity().x;

		// Cdot = dot(u, v + cross(w, r))
		Vector2f vpA = new Vector2f(-wA * rA.y, wA * rA.x);//vA + b2Cross(wA, rA);
		Vector2f vpB = new Vector2f(-wB * rB.y, wB * rB.x);//vB + b2Cross(wB, rB); return b2Vec2(-s * a.y, s * a.x);
		float Cdot = u.x * (vpB.x - vpA.x) + u.y * (vpB.y - vpA.y);//b2Dot(m_u, vpB - vpA);
//		System.out.println(Cdot);

		float impulsef = -mass * (Cdot + bias + gamma * impulse);
		impulse += impulsef;

		Vector2f P = VecMath.scale(u, impulsef);
		vA.x -= P.x * bodyA.getInverseMass();
		vA.y -= P.y * bodyA.getInverseMass();
		wA -= bodyA.getInverseInertia().get(0, 0) * VecMath.crossproduct(rA, P);
		vB.x += P.x * bodyB.getInverseMass();
		vB.y += P.y * bodyB.getInverseMass();
		wB += bodyB.getInverseInertia().get(0, 0) * VecMath.crossproduct(rB, P);
//		System.out.println(vA + "; " + wA + "; " + vB + "; " + wB);
		
		bodyA.setLinearVelocity(vA);
		bodyA.setAngularVelocity(new Vector1f(wA));
		bodyB.setLinearVelocity(vB);
		bodyB.setAngularVelocity(new Vector1f(wB));
	}
}