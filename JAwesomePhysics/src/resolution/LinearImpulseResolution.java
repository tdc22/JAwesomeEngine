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

	private final Vector3f impulse3 = new Vector3f(), frictionImpulse3 = new Vector3f();

	@Override
	public void resolve(CollisionManifold<Vector3f, ?> manifold) {
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

		impulse3.set(normal);
		impulse3.scale(j);
		B.applyCentralImpulse(impulse3);
		impulse3.negate();
		A.applyCentralImpulse(impulse3);

		// Friction
		// Re-calculate rv after normal impulse!
		float rvx = B.getLinearVelocity().x - A.getLinearVelocity().x;
		float rvy = B.getLinearVelocity().y - A.getLinearVelocity().y;
		float rvz = B.getLinearVelocity().z - A.getLinearVelocity().z;

		float rvDotNormal = VecMath.dotproduct(rvx, rvy, rvz, normal.x, normal.y, normal.z);
		frictionImpulse3.set(rvx - normal.x * rvDotNormal, rvy - normal.y * rvDotNormal, rvz - normal.z * rvDotNormal);
		if (frictionImpulse3.lengthSquared() > 0)
			frictionImpulse3.normalize();

		float jt = (-VecMath.dotproduct(rvx, rvy, rvz, frictionImpulse3.x, frictionImpulse3.y, frictionImpulse3.z))
				/ ((A.getInverseMass() + B.getInverseMass()));

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse3.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse3.scale(-j * dynamicFriction);
		}

		B.applyCentralImpulse(frictionImpulse3);
		frictionImpulse3.negate();
		A.applyCentralImpulse(frictionImpulse3);
	}

	private final Vector2f impulse2 = new Vector2f(), frictionImpulse2 = new Vector2f();

	@Override
	public void resolve2(CollisionManifold<Vector2f, ?> manifold) {
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

		impulse2.set(normal);
		impulse2.scale(j);
		B.applyCentralImpulse(impulse2);
		impulse2.negate();
		A.applyCentralImpulse(impulse2);

		// Friction
		// Re-calculate rv after normal impulse!
		float rvx = B.getLinearVelocity().x - A.getLinearVelocity().x;
		float rvy = B.getLinearVelocity().y - A.getLinearVelocity().y;

		float rvDotNormal = VecMath.dotproduct(rvx, rvy, normal.x, normal.y);
		frictionImpulse2.set(rvx - normal.x * rvDotNormal, rvy - normal.y * rvDotNormal);

		if (frictionImpulse2.lengthSquared() > 0)
			frictionImpulse2.normalize();

		float jt = (-VecMath.dotproduct(rvx, rvy, frictionImpulse2.x, frictionImpulse2.y))
				/ ((A.getInverseMass() + B.getInverseMass()));

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse2.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse2.scale(-j * dynamicFriction);
		}

		B.applyCentralImpulse(frictionImpulse2);
		frictionImpulse2.negate();
		A.applyCentralImpulse(frictionImpulse2);
	}
}