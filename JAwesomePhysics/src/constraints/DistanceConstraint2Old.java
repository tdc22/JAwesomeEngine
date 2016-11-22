package constraints;

import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import objects.RigidBody;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;

public class DistanceConstraint2Old {
	protected RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA, bodyB;
	Vector2f anchorA, anchorB;
	// ???
	// http://www.gamedev.net/topic/540730-constraints-and-linear-programming/
	// source1: http://www.dyn4j.org/2010/07/equality-constraints/
	// source2: http://www.dyn4j.org/2010/07/point-to-point-constraint/
	// source3: http://www.dyn4j.org/2010/09/distance-constraint/
	// source:
	// https://github.com/erincatto/Box2D/blob/master/Box2D/Box2D/Dynamics/Joints/b2WeldJoint.cpp
	// doesn't work, TRY THIS:
	// http://www.bulletphysics.com/ftp/pub/test/physics/papers/IterativeDynamics.pdf
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<
	Vector2f rA, rB, u;
	float mass, gamma, bias, impulse, frequencyHz, dampingRatio, length;

	final float maxLinearCorrection = 0.2f;
	final float linearSlop = 0.005f;

	public DistanceConstraint2Old(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB, Vector2f anchorA, Vector2f anchorB, float length) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
		this.anchorA = anchorA;
		this.anchorB = anchorB;
		this.length = length;

		frequencyHz = 1f;
		dampingRatio = 0f;
	}

	public void initStep(float delta) {
		Vector2f cA = bodyA.getTranslation();
		Vector2f vA = bodyA.getLinearVelocity();
		float wA = bodyA.getAngularVelocity().x;

		Vector2f cB = bodyB.getTranslation();
		Vector2f vB = bodyB.getLinearVelocity();
		float wB = bodyB.getAngularVelocity().x;

		Complexf qA = bodyA.getRotation();
		Complexf qB = bodyB.getRotation();

		rA = ComplexMath.transform(qA, anchorA);
		rB = ComplexMath.transform(qB, anchorB);
		u = new Vector2f(cB.x + rB.x - cA.x - rA.x, cB.y + rB.y - cA.y - rA.y);// cB
																				// +
																				// rB
																				// -
																				// cA
																				// -
																				// rA;
		System.out.println(u + "; " + cA + "; " + vA + "; " + wA + "; " + cB + "; " + vB + "; " + wB);

		// Handle singularity.
		float dist = (float) u.length();
		if (dist > linearSlop) {
			u.scale(1.0f / dist);
		} else {
			u.set(0.0f, 0.0f);
		}

		float crAu = VecMath.crossproduct(rA, u);
		float crBu = VecMath.crossproduct(rB, u);
		float invMass = (float) (bodyA.getInverseMass() + bodyA.getInverseInertia().get(0, 0) * crAu * crAu
				+ bodyB.getInverseMass() + bodyB.getInverseInertia().get(0, 0) * crBu * crBu);

		// Compute the effective mass matrix.
		mass = invMass != 0.0f ? 1.0f / invMass : 0.0f;

		if (frequencyHz > 0.0f) {
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
		} else {
			gamma = 0.0f;
			bias = 0.0f;
		}

		impulse = 0;
		System.out.println(u + "; " + vA + "; " + wA + "; " + vB + "; " + wB);

		bodyA.setLinearVelocity(vA);
		bodyA.setAngularVelocity(new Vector1f(wA));
		bodyB.setLinearVelocity(vB);
		bodyB.setAngularVelocity(new Vector1f(wB));
	}

	public void solve(float delta) {
		Vector2f vA = bodyA.getLinearVelocity();
		float wA = bodyA.getAngularVelocity().x;
		Vector2f vB = bodyB.getLinearVelocity();
		float wB = bodyB.getAngularVelocity().x;

		// Cdot = dot(u, v + cross(w, r))
		Vector2f vpA = new Vector2f(-wA * rA.y, wA * rA.x);// vA + b2Cross(wA,
															// rA);
		Vector2f vpB = new Vector2f(-wB * rB.y, wB * rB.x);// vB + b2Cross(wB,
															// rB); return
															// b2Vec2(-s * a.y,
															// s * a.x);
		float Cdot = u.x * (vpB.x - vpA.x) + u.y * (vpB.y - vpA.y);// b2Dot(m_u,
																	// vpB -
																	// vpA);
		// System.out.println(Cdot);

		float impulsef = -mass * (Cdot + bias + gamma * impulse);
		impulse += impulsef;

		Vector2f P = VecMath.scale(u, impulsef);
		vA.x -= P.x * bodyA.getInverseMass();
		vA.y -= P.y * bodyA.getInverseMass();
		wA -= bodyA.getInverseInertia().get(0, 0) * VecMath.crossproduct(rA, P);
		vB.x += P.x * bodyB.getInverseMass();
		vB.y += P.y * bodyB.getInverseMass();
		wB += bodyB.getInverseInertia().get(0, 0) * VecMath.crossproduct(rB, P);
		// System.out.println(vA + "; " + wA + "; " + vB + "; " + wB);

		bodyA.setLinearVelocity(vA);
		bodyA.setAngularVelocity(new Vector1f(wA));
		bodyB.setLinearVelocity(vB);
		bodyB.setAngularVelocity(new Vector1f(wB));
	}

	public boolean solvePosition(float delta) {
		if (frequencyHz > 0) {
			return false;
		}

		Vector2f cA = bodyA.getTranslation();
		float aA = (float) bodyA.getRotation().angle();
		Vector2f cB = bodyB.getTranslation();
		float aB = (float) bodyB.getRotation().angle();

		Complexf qA = bodyA.getRotation();
		Complexf qB = bodyB.getRotation();

		Vector2f rA = ComplexMath.transform(qA, anchorA);
		Vector2f rB = ComplexMath.transform(qB, anchorB);
		u = new Vector2f(cB.x + rB.x - cA.x - rA.x, cB.y + rB.y - cA.y - rA.y);// cB
																				// +
																				// rB
																				// -
																				// cA
																				// -
																				// rA;

		float dist = (float) u.length();
		if (dist > 0 && dist != 1)
			u.normalize();
		float C = dist - length;
		C = Math.min(Math.max(C, -maxLinearCorrection), maxLinearCorrection);

		float impulse = -mass * C;
		Vector2f P = VecMath.scale(u, impulse);

		cA.x -= bodyA.getInverseMass() * P.x;
		cA.y -= bodyA.getInverseMass() * P.y;
		aA -= bodyA.getInverseInertia().getf(0, 0) * VecMath.crossproduct(rA, P);
		cB.x += bodyB.getInverseMass() * P.x;
		cB.y += bodyB.getInverseMass() * P.y;
		aB += bodyB.getInverseInertia().getf(0, 0) * VecMath.crossproduct(rB, P);

		bodyA.setTranslation(cA);
		Complexf rotA = new Complexf();
		rotA.rotate(aA);
		bodyA.setRotation(rotA);
		bodyB.setTranslation(cB);
		Complexf rotB = new Complexf();
		rotB.rotate(aB);
		bodyB.setRotation(rotB);

		return Math.abs(C) < linearSlop;
	}
}