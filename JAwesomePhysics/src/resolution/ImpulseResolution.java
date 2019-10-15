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
		Vector3f cxnA = VecMath.crossproduct(contactA, normal);
		Vector3f ca = new Vector3f((cxnA.y * contactA.z - cxnA.z * contactA.y) * iiA,
				(cxnA.z * contactA.x - cxnA.x * contactA.y) * iiA, (cxnA.x * contactA.y - cxnA.y * contactA.x) * iiA);

		ii = B.getInverseInertia();
		float iiB = 1 - 2 * ii.getQ2f() * ii.getQ2f() - 2 * ii.getQ3f() * ii.getQ3f(); // Quaternion
																						// to
																						// Matrix(0,
																						// 0)
		// cb = (contactB x normal) x contactB * B(0, 0)
		Vector3f cxnB = VecMath.crossproduct(contactB, normal);
		Vector3f cb = new Vector3f((cxnB.y * contactB.z - cxnB.z * contactB.y) * iiB,
				(cxnB.z * contactB.x - cxnB.x * contactB.y) * iiB, (cxnB.x * contactB.y - cxnB.y * contactB.x) * iiB);

		// j = (-(1 + e) * velAlongNormal / (A.inverseMass + B.inverseMass + (ca
		// + cb) dot normal)
		float j = (-(1 + e) * velAlongNormal) / (A.getInverseMass() + B.getInverseMass()
				+ ((ca.x + cb.x) * normal.x + (ca.y + cb.y) * normal.y + (ca.z + cb.z) * normal.z));

		Vector3f impulse = VecMath.scale(normal, j);
		B.applyImpulse(impulse, contactB);
		impulse.negate();
		A.applyImpulse(impulse, contactA);

		// Friction
		// Re-calculate rv after normal impulse!
		Vector3f rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		// tangent = rv - normal * (rv dot normal)
		float rvDotNormal = VecMath.dotproduct(rv, normal);
		Vector3f frictionImpulse = new Vector3f(rv.x - normal.x * rvDotNormal, rv.y - normal.y * rvDotNormal,
				rv.z - normal.z * rvDotNormal);
		if (frictionImpulse.lengthSquared() > 0)
			frictionImpulse.normalize();

		float jt = (-VecMath.dotproduct(rv, frictionImpulse)) / (A.getInverseMass() + B.getInverseMass());

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse.scale(-j * dynamicFriction);
		}

		B.applyImpulse(frictionImpulse, contactB);
		frictionImpulse.negate();
		A.applyImpulse(frictionImpulse, contactA);

		// Rolling friction
		float rollingFriction = pythagoreanSolve(A.getRollingFriction(), B.getRollingFriction());
		Vector3f rav = VecMath.subtraction(B.getAngularVelocity(), A.getAngularVelocity());
		rav.scale(rollingFriction);
		A.applyTorqueImpulse(rav);
		rav.negate();
		B.applyTorqueImpulse(rav);
	}

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

		Vector2f impulse = VecMath.scale(normal, j);
		B.applyImpulse(impulse, contactB);
		impulse.negate();
		A.applyImpulse(impulse, contactA);

		// Friction
		// Re-calculate rv after normal impulse!
		Vector2f rv = VecMath.subtraction(B.getLinearVelocity(), A.getLinearVelocity());

		// tangent = rv - normal * (rv dot normal)
		float rvDotNormal = VecMath.dotproduct(rv, normal);
		Vector2f frictionImpulse = new Vector2f(rv.x - normal.x * rvDotNormal, rv.y - normal.y * rvDotNormal);
		if (frictionImpulse.lengthSquared() > 0)
			frictionImpulse.normalize();

		float jt = (-VecMath.dotproduct(rv, frictionImpulse)) / (A.getInverseMass() + B.getInverseMass());

		float mu = pythagoreanSolve(A.getStaticFriction(), B.getStaticFriction());

		if (Math.abs(jt) < j * mu) {
			frictionImpulse.scale(jt);
		} else {
			float dynamicFriction = pythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction());
			frictionImpulse.scale(-j * dynamicFriction);
		}

		B.applyImpulse(frictionImpulse, contactB);
		frictionImpulse.negate();
		A.applyImpulse(frictionImpulse, contactA);

		// Rolling friction (reduces difference in angular movement)
		float rav = (B.getAngularVelocity().x - A.getAngularVelocity().x)
				* pythagoreanSolve(A.getRollingFriction(), B.getRollingFriction());
		A.applyTorqueImpulse(rav);
		rav = -rav;
		B.applyTorqueImpulse(rav);
	}
}