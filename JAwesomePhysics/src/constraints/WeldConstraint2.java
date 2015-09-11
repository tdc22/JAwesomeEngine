package constraints;

import math.VecMath;
import matrix.Matrix1f;
import matrix.Matrix3f;
import objects.Constraint2;
import objects.RigidBody;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

public class WeldConstraint2 extends Constraint2 {
	Vector2f anchorA, anchorB;
	float invMassA, invMassB, invInertiaA, invInertiaB;
	float bias, gamma, frequencyHz, referenceAngle, dampingRatio;
	Matrix3f mass;
	Vector2f rA, rB;
	
	// source1: http://www.dyn4j.org/2010/07/equality-constraints/
	// source2: http://www.dyn4j.org/2010/07/point-to-point-constraint/
	// source3: http://www.dyn4j.org/2010/09/distance-constraint/
	public WeldConstraint2(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB, Vector2f anchorA, Vector2f anchorB) {
		super(bodyA, bodyB);
		this.anchorA = anchorA;
		this.anchorB = anchorB;
		referenceAngle = (float) (1/bodyB.getInverseRotation().angle() - 1/bodyA.getInverseRotation().angle());
	}

	@Override
	protected void initializeConstraint() {
		invMassA = bodyA.getInverseMass();
		invMassB = bodyB.getInverseMass();
		invInertiaA = bodyA.getInverseInertia().getf(0, 0);
		invInertiaB = bodyB.getInverseInertia().getf(0, 0);

		float aA = (float) bodyA.getTranslation2().length();
		Vector2f vA = bodyA.getLinearVelocity();
		float wA = (float) vA.length();

		float aB = (float) bodyB.getTranslation2().length();
		Vector2f vB = bodyB.getLinearVelocity();
		float wB = (float) vB.length();

		Complexf qA = new Complexf(Math.sin(aA), Math.cos(aA));
		Complexf qB = new Complexf(Math.sin(aB), Math.cos(aB));

		rA = b2Mul(qA, m_localAnchorA - m_localCenterA);
		rB = b2Mul(qB, m_localAnchorB - m_localCenterB);

		// J = [-I -r1_skew I r2_skew]
		//     [ 0       -1 0       1]
		// r_skew = [-ry; rx]

		// Matlab
		// K = [ mA+r1y^2*iA+mB+r2y^2*iB,  -r1y*iA*r1x-r2y*iB*r2x,          -r1y*iA-r2y*iB]
		//     [  -r1y*iA*r1x-r2y*iB*r2x, mA+r1x^2*iA+mB+r2x^2*iB,           r1x*iA+r2x*iB]
		//     [          -r1y*iA-r2y*iB,           r1x*iA+r2x*iB,                   iA+iB]

		float mA = invMassA, mB = invMassB;
		float iA = invInertiaA, iB = invInertiaB;

		Matrix3f K;
		K.ex.x = mA + mB + m_rA.y * m_rA.y * iA + m_rB.y * m_rB.y * iB;
		K.ey.x = -m_rA.y * m_rA.x * iA - m_rB.y * m_rB.x * iB;
		K.ez.x = -m_rA.y * iA - m_rB.y * iB;
		K.ex.y = K.ey.x;
		K.ey.y = mA + mB + m_rA.x * m_rA.x * iA + m_rB.x * m_rB.x * iB;
		K.ez.y = m_rA.x * iA + m_rB.x * iB;
		K.ex.z = K.ez.x;
		K.ey.z = K.ez.y;
		K.ez.z = iA + iB;

		if (frequencyHz > 0.0f)
		{
			K.GetInverse22(&m_mass);

			float invM = iA + iB;
			float m = invM > 0.0f ? 1.0f / invM : 0.0f;

			float C = aB - aA - referenceAngle;

			// Frequency
			float omega = 2.0f * b2_pi * frequencyHz;

			// Damping coefficient
			float d = 2.0f * m * dampingRatio * omega;

			// Spring stiffness
			float k = m * omega * omega;

			// magic formulas
			float h = data.step.dt;
			gamma = h * (d + h * k);
			gamma = gamma != 0.0f ? 1.0f / gamma : 0.0f;
			bias = C * h * k * gamma;

			invM += gamma;
			mass.ez.z = invM != 0.0f ? 1.0f / invM : 0.0f;
		}
		else if (K.ez.z == 0.0f)
		{
			K.GetInverse22(mass);
			gamma = 0.0f;
			bias = 0.0f;
		}
		else
		{
			K.GetSymInverse33(mass);
			gamma = 0.0f;
			bias = 0.0f;
		}
	}

	// source: https://github.com/erincatto/Box2D/blob/master/Box2D/Box2D/Dynamics/Joints/b2WeldJoint.cpp
	@Override
	public void solve(float delta) {
		Vector2f vA = bodyA.getLinearVelocity();
		float wA = bodyA.getAngularVelocity().getXf();
		Vector2f vB = bodyB.getLinearVelocity();
		float wB = bodyB.getAngularVelocity().getXf();

		float mA = invMassA;
		float mB = invMassB;
		float iA = invInertiaA;
		float iB = invInertiaB;
		Vector3f impulse = new Vector3f(); // TODO: check if maybe not clear every loop

		if (frequencyHz > 0.0f)
		{
			float Cdot2 = wB - wA;

			float impulse2 = -mass.getf(3, 3) * (Cdot2 + bias + gamma * impulse.z);
			impulse.z += impulse2;

			wA -= iA * impulse2;
			wB += iB * impulse2;
			
			Vector2f Cdot1 = new Vector2f(vB.x + -wB * rB.y - vA.x - -wA * rA.y, vB.y + wB * rB.x - vA.y - wA * rA.x);

			/*
			 * /// Multiply a matrix times a vector.
				inline b2Vec2 b2Mul22(const b2Mat33& A, const b2Vec2& v)
				{
					return b2Vec2(A.ex.x * v.x + A.ey.x * v.y, A.ex.y * v.x + A.ey.y * v.y);
				}
			 */
			float[][] masses = mass.getArrayf();
			Vector2f impulse1 = new Vector2f(-masses[0][0] * Cdot1.x - masses[1][0] * Cdot1.y, -masses[0][1] * Cdot1.x - masses[1][1] * Cdot1.y);
			impulse.x += impulse1.x;
			impulse.y += impulse1.y;

			Vector2f P = impulse1;

			vA.x -= P.x * mA;
			vA.y -= P.y * mA;
			wA -= iA * VecMath.crossproduct(rA, P);

			vB.x += P.x * mB;
			vB.y += P.y * mB;
			wB += iB * VecMath.crossproduct(rB, P);
		}
		else
		{
			/*
			 * /// Perform the cross product on a vector and a scalar. In 2D this produces
				/// a vector.
				inline b2Vec2 b2Cross(const b2Vec2& a, float32 s)
				{
					return b2Vec2(s * a.y, -s * a.x);
				}
				
				/// Perform the cross product on a scalar and a vector. In 2D this produces
				/// a vector.
				inline b2Vec2 b2Cross(float32 s, const b2Vec2& a)
				{
					return b2Vec2(-s * a.y, s * a.x);
				}
			 */
			
			Vector2f Cdot1 = new Vector2f(vB.x + -wB * rB.y - vA.x - -wA * rA.y, vB.y + wB * rB.x - vA.y - wA * rA.x);
			float Cdot2 = wB - wA;
			Vector3f Cdot = new Vector3f(Cdot1.x, Cdot1.y, Cdot2);

			Vector3f timpulse = VecMath.negate(VecMath.transformVector(mass, Cdot));
			impulse.x += timpulse.x;
			impulse.y += timpulse.y;
			impulse.z += timpulse.z;

			Vector2f P = new Vector2f(impulse.x, impulse.y);

			vA.x -= P.x * mA;
			vA.y -= P.y * mA;
			wA -= iA * (VecMath.crossproduct(rA, P) + impulse.z);

			vB.x += P.x * mB;
			vB.y += P.y * mB;
			wB += iB * (VecMath.crossproduct(rB, P) + impulse.z);
		}
		
		bodyA.setLinearVelocity(vA);
		bodyA.setAngularVelocity(new Vector1f(wA));
		bodyB.setLinearVelocity(vB);
		bodyB.setAngularVelocity(new Vector1f(wB));
//		data.velocities[m_indexA].v = vA;
//		data.velocities[m_indexA].w = wA;
//		data.velocities[m_indexB].v = vB;
//		data.velocities[m_indexB].w = wB;
	}
}