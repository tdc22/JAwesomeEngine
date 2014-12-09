package resolution;

import manifold.CollisionManifold;
import math.VecMath;
import objects.RigidBody2;
import objects.RigidBody3;
import vector.Vector2f;
import vector.Vector3f;

/**
 * Linear collision resolution with friction.
 * 
 * @author oliver
 */
public class LinearImpulseResolution implements CollisionResolution {

	private float pythagoreanSolve(float a, float b) {
		return (float) Math.sqrt(a * a + b * b);
	}

	@Override
	public void resolve(CollisionManifold<Vector3f> manifold) {
		// Linear resolution (like in SimpleLinearResolution)
		RigidBody3 A = (RigidBody3) manifold.getObjects().getFirst();
		RigidBody3 B = (RigidBody3) manifold.getObjects().getSecond();
		Vector3f normal = manifold.getCollisionNormal();

		Vector3f rv = VecMath.subtraction(B.getLinearVelocity(),
				A.getLinearVelocity());

		float velAlongNormal = VecMath.dotproduct(rv, normal);

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		float j = (-(1 + e) * velAlongNormal)
				/ (A.getInverseMass() + B.getInverseMass());

		Vector3f impulse = VecMath.scale(normal, j);
		A.applyCentralImpulse(VecMath.negate(impulse));
		B.applyCentralImpulse(impulse);

		// Friction
		// Re-calculate rv after normal impulse!
		rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		Vector3f tangent = VecMath.subtraction(rv,
				VecMath.scale(normal, VecMath.dotproduct(rv, normal)));
		if (tangent.length() > 0)
			tangent.normalize();

		float jt = (-VecMath.dotproduct(rv, tangent))
				/ ((A.getInverseMass() + B.getInverseMass()));

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

		A.applyCentralImpulse(VecMath.negate(frictionImpulse));
		B.applyCentralImpulse(frictionImpulse);
	}

	@Override
	public void resolve2(CollisionManifold<Vector2f> manifold) {
		// Linear resolution (like in SimpleLinearResolution)
		RigidBody2 A = (RigidBody2) manifold.getObjects().getFirst();
		RigidBody2 B = (RigidBody2) manifold.getObjects().getSecond();
		Vector2f normal = manifold.getCollisionNormal();

		Vector2f rv = VecMath.subtraction(B.getLinearVelocity(),
				A.getLinearVelocity());

		float velAlongNormal = VecMath.dotproduct(rv, normal);

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		float j = (-(1 + e) * velAlongNormal)
				/ (A.getInverseMass() + B.getInverseMass());

		Vector2f impulse = VecMath.scale(normal, j);
		A.applyCentralImpulse(VecMath.negate(impulse));
		B.applyCentralImpulse(impulse);

		// Friction
		// Re-calculate rv after normal impulse!
		rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		Vector2f tangent = VecMath.subtraction(rv,
				VecMath.scale(normal, VecMath.dotproduct(rv, normal)));
		if (tangent.length() > 0)
			tangent.normalize();

		float jt = (-VecMath.dotproduct(rv, tangent))
				/ ((A.getInverseMass() + B.getInverseMass()));

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

		A.applyCentralImpulse(VecMath.negate(frictionImpulse));
		B.applyCentralImpulse(frictionImpulse);
	}
}