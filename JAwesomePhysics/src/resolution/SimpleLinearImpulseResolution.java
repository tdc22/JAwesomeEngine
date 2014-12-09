package resolution;

import manifold.CollisionManifold;
import math.VecMath;
import objects.RigidBody2;
import objects.RigidBody3;
import vector.Vector2f;
import vector.Vector3f;

/**
 * Linear collision resolution without friction.
 * 
 * @author oliver
 */
public class SimpleLinearImpulseResolution implements CollisionResolution {

	@Override
	public void resolve(CollisionManifold<Vector3f> manifold) {
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
	}

	@Override
	public void resolve2(CollisionManifold<Vector2f> manifold) {
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
	}
}