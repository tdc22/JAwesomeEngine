package resolution;

import manifold.CollisionManifold;
import math.VecMath;
import objects.RigidBody2;
import objects.RigidBody3;
import quaternion.Quaternionf;
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

	private final Vector3f impulse3 = new Vector3f(), frictionImpulse3 = new Vector3f(), cxnA3 = new Vector3f(),
			ca3 = new Vector3f(), cxnB3 = new Vector3f(), cb3 = new Vector3f(), rav3 = new Vector3f();

	@Override
	public void resolve(CollisionManifold<Vector3f, ?> manifold) {
		// Linear resolution (like in SimpleLinearResolution)
		RigidBody3 A = (RigidBody3) manifold.getObjects().getFirst();
		RigidBody3 B = (RigidBody3) manifold.getObjects().getSecond();
		Vector3f normal = manifold.getCollisionNormal();
		Vector3f contactA = manifold.getRelativeContactPointA();
		Vector3f contactB = manifold.getRelativeContactPointB();

		// (linVel(B) - linVel(A)) dot normal
		float velAlongNormal = (B.getLinearVelocity().x - A.getLinearVelocity().x) * normal.x
				+ (B.getLinearVelocity().y - A.getLinearVelocity().y) * normal.y
				+ (B.getLinearVelocity().z - A.getLinearVelocity().z) * normal.z;

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		Quaternionf ii = A.getInverseInertia();
		float iiA = 1 - 2 * ii.getQ2f() * ii.getQ2f() - 2 * ii.getQ3f() * ii.getQ3f(); // Quaternion
																						// to
																						// Matrix(0,
																						// 0)

		// ca = (contactA x normal) x contactA * A(0, 0)
		VecMath.crossproduct(contactA, normal, cxnA3);
		ca3.set((cxnA3.y * contactA.z - cxnA3.z * contactA.y) * iiA,
				(cxnA3.z * contactA.x - cxnA3.x * contactA.y) * iiA,
				(cxnA3.x * contactA.y - cxnA3.y * contactA.x) * iiA);

		ii = B.getInverseInertia();
		float iiB = 1 - 2 * ii.getQ2f() * ii.getQ2f() - 2 * ii.getQ3f() * ii.getQ3f(); // Quaternion
																						// to
																						// Matrix(0,
																						// 0)
		// cb = (contactB x normal) x contactB * B(0, 0)
		VecMath.crossproduct(contactB, normal, cxnB3);
		cb3.set((cxnB3.y * contactB.z - cxnB3.z * contactB.y) * iiB,
				(cxnB3.z * contactB.x - cxnB3.x * contactB.y) * iiB,
				(cxnB3.x * contactB.y - cxnB3.y * contactB.x) * iiB);

		// j = (-(1 + e) * velAlongNormal / (A.inverseMass + B.inverseMass + (ca
		// + cb) dot normal)
		float j = (-(1 + e) * velAlongNormal) / (A.getInverseMass() + B.getInverseMass()
				+ ((ca3.x + cb3.x) * normal.x + (ca3.y + cb3.y) * normal.y + (ca3.z + cb3.z) * normal.z));

		impulse3.set(normal);
		impulse3.scale(j);
		B.applyImpulse(impulse3, contactB);
		impulse3.negate();
		A.applyImpulse(impulse3, contactA);

		// Friction
		// Re-calculate rv after normal impulse!
		float rvx = B.getLinearVelocity().x - A.getLinearVelocity().x;
		float rvy = B.getLinearVelocity().y - A.getLinearVelocity().y;
		float rvz = B.getLinearVelocity().z - A.getLinearVelocity().z;

		// tangent = rv - normal * (rv dot normal)
		float rvDotNormal = VecMath.dotproduct(rvx, rvy, rvz, normal.x, normal.y, normal.z);
		frictionImpulse3.set(rvx - normal.x * rvDotNormal, rvy - normal.y * rvDotNormal, rvz - normal.z * rvDotNormal);
		if (frictionImpulse3.lengthSquared() > 0)
			frictionImpulse3.normalize();

		float jt = (-VecMath.dotproduct(rvx, rvy, rvz, frictionImpulse3.x, frictionImpulse3.y, frictionImpulse3.z))
				/ (A.getInverseMass() + B.getInverseMass());

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse3.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse3.scale(-j * dynamicFriction);
		}

		B.applyImpulse(frictionImpulse3, contactB);
		frictionImpulse3.negate();
		A.applyImpulse(frictionImpulse3, contactA);

		// Rolling friction
		float rollingFriction = pythagoreanSolve(A.getRollingFriction(), B.getRollingFriction());
		VecMath.subtraction(B.getAngularVelocity(), A.getAngularVelocity(), rav3);
		rav3.scale(rollingFriction);
		A.applyTorqueImpulse(rav3);
		rav3.negate();
		B.applyTorqueImpulse(rav3);
	}

	private final Vector2f impulse2 = new Vector2f(), frictionImpulse2 = new Vector2f();

	@Override
	public void resolve2(CollisionManifold<Vector2f, ?> manifold) {
		// Linear resolution (like in SimpleLinearResolution)
		RigidBody2 A = (RigidBody2) manifold.getObjects().getFirst();
		RigidBody2 B = (RigidBody2) manifold.getObjects().getSecond();
		Vector2f normal = manifold.getCollisionNormal();
		Vector2f contactA = manifold.getRelativeContactPointA();
		Vector2f contactB = manifold.getRelativeContactPointB();

		float velAlongNormal = (B.getLinearVelocity().x - A.getLinearVelocity().x) * normal.x
				+ (B.getLinearVelocity().y - A.getLinearVelocity().y) * normal.y;

		if (velAlongNormal > 0)
			return;

		float e = Math.min(A.getRestitution(), B.getRestitution());
		float crossA = VecMath.crossproduct(contactA, normal);
		float ca = crossA * crossA * A.getInverseInertia().getf(0, 0);
		float crossB = VecMath.crossproduct(contactB, normal);
		float cb = crossB * crossB * B.getInverseInertia().getf(0, 0);
		float j = (-(1 + e) * velAlongNormal) / (A.getInverseMass() + B.getInverseMass() + ca + cb);

		impulse2.set(normal);
		impulse2.scale(j);
		B.applyImpulse(impulse2, contactB);
		impulse2.negate();
		A.applyImpulse(impulse2, contactA);

		// Friction
		// Re-calculate rv after normal impulse!
		float rvx = B.getLinearVelocity().x - A.getLinearVelocity().x;
		float rvy = B.getLinearVelocity().y - A.getLinearVelocity().y;

		// tangent = rv - normal * (rv dot normal)
		float rvDotNormal = VecMath.dotproduct(rvx, rvy, normal.x, normal.y);
		frictionImpulse2.set(rvx - normal.x * rvDotNormal, rvy - normal.y * rvDotNormal);
		if (frictionImpulse2.lengthSquared() > 0)
			frictionImpulse2.normalize();

		float jt = (-VecMath.dotproduct(rvx, rvy, frictionImpulse2.x, frictionImpulse2.y))
				/ (A.getInverseMass() + B.getInverseMass());

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse2.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse2.scale(-j * dynamicFriction);
		}

		B.applyImpulse(frictionImpulse2, contactB);
		frictionImpulse2.negate();
		A.applyImpulse(frictionImpulse2, contactA);

		// Rolling friction (reduces difference in angular movement)
		float rav = (B.getAngularVelocity().x - A.getAngularVelocity().x)
				* pythagoreanSolve(A.getRollingFriction(), B.getRollingFriction());
		A.applyTorqueImpulse(rav);
		rav = -rav;
		B.applyTorqueImpulse(rav);
	}
}