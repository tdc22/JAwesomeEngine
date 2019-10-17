package integration;

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
	public void integrate2(RigidBody2 obj, float delta, Vector2f gravitation) {
		if (obj.getInverseMass() != 0) {
			Vector2f lv = obj.getLinearVelocity();
			Vector2f fa = obj.getForceAccumulator();
			float linearDampingValue = 1 / (1 + obj.getLinearDamping() * delta);
			obj.setLinearVelocity(
					(lv.x + delta * (fa.x * obj.getInverseMass() + gravitation.x * obj.getLinearFactor().x))
							* linearDampingValue,
					(lv.y + delta * (fa.y * obj.getInverseMass() + gravitation.y * obj.getLinearFactor().y))
							* linearDampingValue);
		}

		Vector1f av = obj.getAngularVelocity();
		Vector1f ta = obj.getTorqueAccumulator();
		Matrix1f ii = obj.getInverseInertia();
		float angularDampingValue = 1 / (1 + obj.getAngularDamping() * delta);
		obj.setAngularVelocity((av.x + ii.getf(0, 0) * ta.x * delta) * angularDampingValue);

		obj.clearForces();

		obj.translate(obj.getLinearVelocity().x * delta, obj.getLinearVelocity().y * delta);
		obj.rotate(obj.getAngularVelocity().x * delta);
	}

	private final Vector3f transformedTorque = new Vector3f();

	@Override
	public void integrate3(RigidBody3 obj, float delta, Vector3f gravitation) {
		if (obj.getInverseMass() != 0) {
			Vector3f lv = obj.getLinearVelocity();
			Vector3f fa = obj.getForceAccumulator();
			float linearDampingValue = 1 / (1 + obj.getLinearDamping() * delta);
			obj.setLinearVelocity(
					(lv.x + delta * (fa.x * obj.getInverseMass() + gravitation.x * obj.getLinearFactor().x))
							* linearDampingValue,
					(lv.y + delta * (fa.y * obj.getInverseMass() + gravitation.y * obj.getLinearFactor().y))
							* linearDampingValue,
					(lv.z + delta * (fa.z * obj.getInverseMass() + gravitation.z * obj.getLinearFactor().z))
							* linearDampingValue);
		}

		Vector3f av = obj.getAngularVelocity();
		Vector3f ta = obj.getTorqueAccumulator();
		Quaternionf ii = obj.getInverseInertia();
		float angularDampingValue = 1 / (1 + obj.getAngularDamping() * delta);
		transformedTorque.set(ta);
		transformedTorque.transform(ii);
		obj.setAngularVelocity((av.x + transformedTorque.x * delta) * angularDampingValue,
				(av.y + transformedTorque.y * delta) * angularDampingValue,
				(av.z + transformedTorque.z * delta) * angularDampingValue);

		obj.clearForces();

		obj.translate(obj.getLinearVelocity().x * delta, obj.getLinearVelocity().y * delta,
				obj.getLinearVelocity().z * delta);
		obj.rotate(obj.getAngularVelocity().x * delta, obj.getAngularVelocity().y * delta,
				obj.getAngularVelocity().z * delta);
	}
}
