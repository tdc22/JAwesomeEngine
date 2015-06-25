package resolution;

import manifold.CollisionManifold;
import math.VecMath;
import objects.RigidBody2;
import objects.RigidBody3;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

/**
 * Collision resolution with friction and angular movement.
 * 
 * @author oliver
 */
public class ImpulseResolution implements CollisionResolution {

	private float pythagoreanSolve(float a, float b) {
		return (float) Math.sqrt(a * a + b * b);
	}

	@Override
	public void resolve(CollisionManifold<Vector3f> manifold) {
		// Linear resolution (like in SimpleLinearResolution)
		RigidBody3 A = (RigidBody3) manifold.getObjects().getFirst();
		RigidBody3 B = (RigidBody3) manifold.getObjects().getSecond();
		Vector3f normal = manifold.getCollisionNormal();
		Vector3f contactA = manifold.getRelativeContactPointA();
		Vector3f contactB = manifold.getRelativeContactPointB();

		Vector3f rv = VecMath.subtraction(B.getLinearVelocity(),
				A.getLinearVelocity());

		float velAlongNormal = VecMath.dotproduct(rv, normal);

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		Vector3f ca = VecMath.scale(VecMath.crossproduct(
				VecMath.crossproduct(contactA, normal), contactA), A
				.getInverseInertia().toMatrixf().getf(0, 0)); // TODO: compute
																// that matrix
																// value
																// manually
		Vector3f cb = VecMath.scale(VecMath.crossproduct(
				VecMath.crossproduct(contactB, normal), contactB), B
				.getInverseInertia().toMatrixf().getf(0, 0)); // TODO: same
		float j = (-(1 + e) * velAlongNormal)
				/ (A.getInverseMass() + B.getInverseMass() + VecMath
						.dotproduct(VecMath.addition(ca, cb), normal));

		Vector3f impulse = VecMath.scale(normal, j);
		A.applyImpulse(VecMath.negate(impulse), contactA);
		B.applyImpulse(impulse, contactB);

		// Friction
		// Re-calculate rv after normal impulse!
		rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		Vector3f tangent = VecMath.subtraction(rv,
				VecMath.scale(normal, VecMath.dotproduct(rv, normal)));
		if (tangent.length() > 0)
			tangent.normalize();

		float jt = (-VecMath.dotproduct(rv, tangent))
				/ (A.getInverseMass() + B.getInverseMass());

		float mu = pythagoreanSolve(A.getStaticFriction(),
				B.getStaticFriction());

		Vector3f frictionImpulse = null;
		if (Math.abs(jt) < j * mu)
			frictionImpulse = VecMath.scale(tangent, jt);
		else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(),
					B.getDynamicFriction());
			frictionImpulse = VecMath.scale(tangent, -j * dynamicFriction);
		}

		A.applyImpulse(VecMath.negate(frictionImpulse), contactA);
		B.applyImpulse(frictionImpulse, contactB);

		// Rolling friction
		float rollingFriction = pythagoreanSolve(A.getRollingFriction(),
				B.getRollingFriction());
		Vector3f rav = VecMath.subtraction(B.getAngularVelocity(),
				A.getAngularVelocity());
		rav.scale(rollingFriction);
		A.applyTorqueImpulse(rav);
		B.applyTorqueImpulse(VecMath.negate(rav));

		// TODO: change!!!
		/*
		 * Vector3f directionA = VecMath.crossproduct(A.getAngularVelocity(),
		 * VecMath.normalize(contactA)); Vector3f directionB =
		 * VecMath.crossproduct(B.getAngularVelocity(),
		 * VecMath.normalize(contactB)); //System.out.println(rollingFriction +
		 * "; " + directionA + "; " + directionB + "; " + contactA + "; " +
		 * contactB); A.applyImpulse(VecMath.scale(directionA,
		 * -rollingFriction), contactA);
		 * B.applyImpulse(VecMath.scale(directionB, rollingFriction), contactB);
		 */

		// directionA.scale(rollingFriction);
		// directionB.scale(-rollingFriction);
		// A.applyTorqueImpulse(directionA);
		// B.applyTorqueImpulse(directionB);
	}

	@Override
	public void resolve2(CollisionManifold<Vector2f> manifold) {
		// Linear resolution (like in SimpleLinearResolution)
		RigidBody2 A = (RigidBody2) manifold.getObjects().getFirst();
		RigidBody2 B = (RigidBody2) manifold.getObjects().getSecond();
		Vector2f normal = manifold.getCollisionNormal();
		Vector2f contactA = manifold.getRelativeContactPointA();
		Vector2f contactB = manifold.getRelativeContactPointB();

		Vector2f rv = VecMath.subtraction(B.getLinearVelocity(),
				A.getLinearVelocity());

		float velAlongNormal = VecMath.dotproduct(rv, normal);

		System.out.println("manifold: " + normal + "; " + contactA + "; "
				+ contactB);

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		float crossA = VecMath.crossproduct(contactA, normal);
		float ca = crossA * crossA * A.getInverseInertia().getf(0, 0);
		float crossB = VecMath.crossproduct(contactB, normal);
		float cb = crossB * crossB * B.getInverseInertia().getf(0, 0);
		// Vector2f ca =
		// VecMath.scale(VecMath.crossproduct(VecMath.crossproduct(contactA,
		// normal), contactA), A.getInverseInertia().getf(0, 0));
		// Vector2f cb =
		// VecMath.scale(VecMath.crossproduct(VecMath.crossproduct(contactB,
		// normal), contactB), B.getInverseInertia().getf(0, 0));
		float j = (-(1 + e) * velAlongNormal)
				/ (A.getInverseMass() + B.getInverseMass() + ca + cb); // +
																		// VecMath.dotproduct(VecMath.addition(ca,
																		// cb),
																		// normal));

		Vector2f impulse = VecMath.scale(normal, j);
		A.applyImpulse(VecMath.negate(impulse), contactA);
		B.applyImpulse(impulse, contactB);

		// Friction
		// Re-calculate rv after normal impulse!
		rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		Vector2f tangent = VecMath.subtraction(rv,
				VecMath.scale(normal, VecMath.dotproduct(rv, normal)));
		if (tangent.length() > 0)
			tangent.normalize();

		float jt = (-VecMath.dotproduct(rv, tangent))
				/ (A.getInverseMass() + B.getInverseMass());

		float mu = pythagoreanSolve(A.getStaticFriction(),
				B.getStaticFriction());

		Vector2f frictionImpulse = null;
		if (Math.abs(jt) < j * mu)
			frictionImpulse = VecMath.scale(tangent, jt);
		else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(),
					B.getDynamicFriction());
			frictionImpulse = VecMath.scale(tangent, -j * dynamicFriction);
		}

		A.applyImpulse(VecMath.negate(frictionImpulse), contactA);
		B.applyImpulse(frictionImpulse, contactB);

		// Rolling friction (reduces difference in angular movement)
		float rollingFriction = pythagoreanSolve(A.getRollingFriction(),
				B.getRollingFriction());
		Vector1f rav = VecMath.subtraction(B.getAngularVelocity(),
				A.getAngularVelocity());
		rav.scale(rollingFriction);
		A.applyTorqueImpulse(rav);
		B.applyTorqueImpulse(VecMath.negate(rav));

		// TODO: change!!!
		// Vector3f directionA = VecMath.crossproduct(new Vector3f(A
		// .getAngularVelocity().getXf(), 0, 0), VecMath
		// .normalize(new Vector3f(contactA)));
		// Vector3f directionB = VecMath.crossproduct(new Vector3f(B
		// .getAngularVelocity().getXf(), 0, 0), VecMath
		// .normalize(new Vector3f(contactB)));
		// A.applyImpulse(VecMath.scale(new Vector2f(directionA.x,
		// directionA.y),
		// rollingFriction), contactA);
		// B.applyImpulse(VecMath.scale(new Vector2f(directionB.x,
		// directionB.y),
		// rollingFriction), contactB);

		// System.out.println(impulse.x);
		// if(new Float(impulse.x).equals(Float.NaN)) System.exit(1);
		// System.out.println(impulse + "; " + frictionImpulse + "; " + rav +
		// "; "
		// + rollingFriction);
	}
}