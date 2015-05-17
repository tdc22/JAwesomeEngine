package integration;

import math.QuatMath;
import matrix.Matrix1f;
import objects.RigidBody2;
import objects.RigidBody3;
import quaternion.Quaternionf;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

public class VerletIntegration implements IntegrationSolver {

	@Override
	public void integrate2(RigidBody2 obj, float delta, Vector2f gravitation) {
		Vector2f oldlinearvel = obj.getLinearVelocity();
		Vector1f oldangularvel = obj.getAngularVelocity();

		if (obj.getInverseMass() != 0) {
			Vector2f lv = obj.getLinearVelocity();
			Vector2f fa = obj.getForceAccumulator();
			float linearDampingValue = 1 / (1 + obj.getLinearDamping() * delta);
			obj.setLinearVelocity(
					(lv.x + fa.x * delta * obj.getInverseMass() + gravitation.x)
							* linearDampingValue,
					(lv.y + fa.y * delta * obj.getInverseMass() + gravitation.y)
							* linearDampingValue);
		}

		Vector1f av = obj.getAngularVelocity();
		Vector1f ta = obj.getTorqueAccumulator();
		Matrix1f ii = obj.getInverseInertia();
		float angularDampingValue = 1 / (1 + obj.getAngularDamping() * delta);
		obj.setAngularVelocity((av.x + ii.getf(0, 0) * ta.x * delta)
				* angularDampingValue);

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

		obj.translate((oldlinearvel.x + obj.getLinearVelocity().x) * delta
				* 0.5f, (oldlinearvel.y + obj.getLinearVelocity().y) * delta
				* 0.5f);
		obj.rotate((oldangularvel.x + obj.getAngularVelocity().x) * delta
				* 0.5f);
		// obj.translate(VecMath.scale(
		// VecMath.addition(oldlinearvel, obj.getLinearVelocity()),
		// delta * 0.5f));
		// obj.rotate(VecMath.scale(
		// VecMath.addition(oldangularvel, obj.getAngularVelocity()),
		// delta * 0.5f));
	}

	@Override
	public void integrate3(RigidBody3 obj, float delta, Vector3f gravitation) {
		Vector3f oldlinearvel = obj.getLinearVelocity();
		Vector3f oldangularvel = obj.getAngularVelocity();

		if (obj.getInverseMass() != 0) {
			Vector3f lv = obj.getLinearVelocity();
			Vector3f fa = obj.getForceAccumulator();
			float linearDampingValue = 1 / (1 + obj.getLinearDamping() * delta);
			obj.setLinearVelocity(
					(lv.x + fa.x * delta * obj.getInverseMass() + gravitation.x)
							* linearDampingValue,
					(lv.y + fa.y * delta * obj.getInverseMass() + gravitation.y)
							* linearDampingValue,
					(lv.z + fa.z * delta * obj.getInverseMass() + gravitation.z)
							* linearDampingValue);
		}

		Vector3f av = obj.getAngularVelocity();
		Vector3f ta = obj.getTorqueAccumulator();
		Quaternionf ii = obj.getInverseInertia();
		float angularDampingValue = 1 / (1 + obj.getAngularDamping() * delta);
		Vector3f transformedTorque = QuatMath.transform(ii, ta);
		obj.setAngularVelocity((av.x + transformedTorque.x * delta)
				* angularDampingValue, (av.y + transformedTorque.y * delta)
				* angularDampingValue, (av.z + transformedTorque.z * delta)
				* angularDampingValue);

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

		// if(obj.getLinearVelocity().length() != 0)
		// System.out.println(obj.getLinearVelocity() + "; " +
		// obj.getForceAccumulator());
		obj.clearForces();

		obj.translate((oldlinearvel.x + obj.getLinearVelocity().x) * delta
				* 0.5f, (oldlinearvel.y + obj.getLinearVelocity().y) * delta
				* 0.5f, (oldlinearvel.z + obj.getLinearVelocity().z) * delta
				* 0.5f);
		obj.rotate((oldangularvel.x + obj.getAngularVelocity().x) * delta
				* 0.5f, (oldangularvel.y + obj.getAngularVelocity().y) * delta
				* 0.5f, (oldangularvel.z + obj.getAngularVelocity().z) * delta
				* 0.5f);
		// obj.translate(VecMath.scale(
		// VecMath.addition(oldlinearvel, obj.getLinearVelocity()),
		// delta * 0.5f));
		// obj.rotate(VecMath.scale(
		// VecMath.addition(oldangularvel, obj.getAngularVelocity()),
		// delta * 0.5f));
	}
}