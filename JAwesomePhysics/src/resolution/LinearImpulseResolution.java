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

		float velAlongNormal = (B.getLinearVelocity().x - A.getLinearVelocity().x) * normal.x
				+ (B.getLinearVelocity().y - A.getLinearVelocity().y) * normal.y
				+ (B.getLinearVelocity().z - A.getLinearVelocity().z) * normal.z;

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		float j = (-(1 + e) * velAlongNormal) / (A.getInverseMass() + B.getInverseMass());

		Vector3f impulse = VecMath.scale(normal, j);
		B.applyCentralImpulse(impulse);
		impulse.negate();
		A.applyCentralImpulse(impulse);

		// Friction
		// Re-calculate rv after normal impulse!
		Vector3f rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		float rvDotNormal = VecMath.dotproduct(rv, normal);
		Vector3f frictionImpulse = new Vector3f(rv.x - normal.x * rvDotNormal, rv.y - normal.y * rvDotNormal,
				rv.z - normal.z * rvDotNormal);
		if (frictionImpulse.lengthSquared() > 0)
			frictionImpulse.normalize();

		float jt = (-VecMath.dotproduct(rv, frictionImpulse)) / ((A.getInverseMass() + B.getInverseMass()));

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse.scale(-j * dynamicFriction);
		}

		B.applyCentralImpulse(frictionImpulse);
		frictionImpulse.negate();
		A.applyCentralImpulse(frictionImpulse);
	}

	@Override
	public void resolve2(CollisionManifold<Vector2f> manifold) {
		// Linear resolution (like in SimpleLinearResolution)
		RigidBody2 A = (RigidBody2) manifold.getObjects().getFirst();
		RigidBody2 B = (RigidBody2) manifold.getObjects().getSecond();
		Vector2f normal = manifold.getCollisionNormal();

		float velAlongNormal = (B.getLinearVelocity().x - A.getLinearVelocity().x) * normal.x
				+ (B.getLinearVelocity().y - A.getLinearVelocity().y) * normal.y;

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		float j = (-(1 + e) * velAlongNormal) / (A.getInverseMass() + B.getInverseMass());

		Vector2f impulse = VecMath.scale(normal, j);
		B.applyCentralImpulse(impulse);
		impulse.negate();
		A.applyCentralImpulse(impulse);

		// Friction
		// Re-calculate rv after normal impulse!
		Vector2f rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		float rvDotNormal = VecMath.dotproduct(rv, normal);
		Vector2f frictionImpulse = new Vector2f(rv.x - normal.x * rvDotNormal, rv.y - normal.y * rvDotNormal);

		if (frictionImpulse.lengthSquared() > 0)
			frictionImpulse.normalize();

		float jt = (-VecMath.dotproduct(rv, frictionImpulse)) / ((A.getInverseMass() + B.getInverseMass()));

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse.scale(-j * dynamicFriction);
		}

		B.applyCentralImpulse(frictionImpulse);
		frictionImpulse.negate();
		A.applyCentralImpulse(frictionImpulse);
	}
}