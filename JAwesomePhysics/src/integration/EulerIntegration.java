package integration;

import math.QuatMath;
import matrix.Matrix1f;
import objects.RigidBody2;
import objects.RigidBody3;
import quaternion.Quaternionf;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

/**
 * Symplectic euler integration method implementation.
 * 
 * @author Oliver Schall
 * 
 */

public class EulerIntegration implements IntegrationSolver {
	@Override
	public void integrate2(RigidBody2 obj, float delta) {
		Vector2f lv = obj.getLinearVelocity();
		Vector2f fa = obj.getForceAccumulator();
		float linearDampingValue = 1 / (1 + obj.getLinearDamping() * delta);
		obj.setLinearVelocity(new Vector2f((lv.x + fa.x * delta
				* obj.getInverseMass())
				* linearDampingValue, (lv.y + fa.y * delta
				* obj.getInverseMass())
				* linearDampingValue));

		Vector1f av = obj.getAngularVelocity();
		Vector1f ta = obj.getTorqueAccumulator();
		Matrix1f ii = obj.getInverseInertia();
		float angularDampingValue = 1 / (1 + obj.getAngularDamping() * delta);
		obj.setAngularVelocity(new Vector1f((av.x + ii.getf(0, 0) * ta.x
				* delta)
				* angularDampingValue));

		// obj.setLinearVelocity(VecMath.scale(
		// VecMath.addition(obj.getLinearVelocity(), VecMath.scale(
		// VecMath.scale(obj.getForceAccumulator(), delta),
		// obj.getInverseMass())), 1 / (1 + obj.getLinearDamping()
		// * delta)));
		// obj.setAngularVelocity(VecMath.scale(VecMath.addition(
		// obj.getAngularVelocity(),
		// VecMath.transformVector(obj.getInverseInertia(),
		// VecMath.scale(obj.getTorqueAccumulator(), delta))),
		// 1 / (1 + obj.getAngularDamping() * delta)));

		obj.clearForces();

		obj.translate(obj.getLinearVelocity().x * delta,
				obj.getLinearVelocity().y * delta);
		obj.rotate(obj.getAngularVelocity().x * delta);
	}

	@Override
	public void integrate3(RigidBody3 obj, float delta) {
		Vector3f lv = obj.getLinearVelocity();
		Vector3f fa = obj.getForceAccumulator();
		float linearDampingValue = 1 / (1 + obj.getLinearDamping() * delta);
		obj.setLinearVelocity(new Vector3f((lv.x + fa.x * delta
				* obj.getInverseMass())
				* linearDampingValue, (lv.y + fa.y * delta
				* obj.getInverseMass())
				* linearDampingValue, (lv.z + fa.z * delta
				* obj.getInverseMass())
				* linearDampingValue));

		Vector3f av = obj.getAngularVelocity();
		Vector3f ta = obj.getTorqueAccumulator();
		Quaternionf ii = obj.getInverseInertia();
		float angularDampingValue = 1 / (1 + obj.getAngularDamping() * delta);
		Vector3f transformedTorque = QuatMath.transform(ii, ta);
		obj.setAngularVelocity(new Vector3f(
				(av.x + transformedTorque.x * delta) * angularDampingValue,
				(av.y + transformedTorque.y * delta) * angularDampingValue,
				(av.z + transformedTorque.z * delta) * angularDampingValue));

		// obj.setLinearVelocity(VecMath.scale(
		// VecMath.addition(obj.getLinearVelocity(), VecMath.scale(
		// VecMath.scale(obj.getForceAccumulator(), delta),
		// obj.getInverseMass())), 1 / (1 + obj.getLinearDamping()
		// * delta)));
		// obj.setAngularVelocity(VecMath.scale(VecMath.addition(
		// obj.getAngularVelocity(),
		// QuatMath.transform(obj.getInverseInertia(),
		// VecMath.scale(obj.getTorqueAccumulator(), delta))),
		// 1 / (1 + obj.getAngularDamping() * delta)));

		obj.clearForces();

		obj.translate(obj.getLinearVelocity().x * delta,
				obj.getLinearVelocity().y * delta, obj.getLinearVelocity().z
						* delta);
		obj.rotate(obj.getAngularVelocity().x * delta,
				obj.getAngularVelocity().y * delta, obj.getAngularVelocity().z
						* delta);
	}
}
