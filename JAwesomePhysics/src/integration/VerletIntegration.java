package integration;

import math.QuatMath;
import math.VecMath;
import objects.RigidBody2;
import objects.RigidBody3;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

public class VerletIntegration implements IntegrationSolver {

	@Override
	public void integrate2(RigidBody2 obj, float delta) {
		Vector2f oldlinearvel = obj.getLinearVelocity();
		Vector1f oldangularvel = obj.getAngularVelocity();
		obj.setLinearVelocity(VecMath.scale(
				VecMath.addition(obj.getLinearVelocity(), VecMath.scale(
						VecMath.scale(obj.getForceAccumulator(), delta),
						obj.getInverseMass())), 1 / (1 + obj.getLinearDamping()
						* delta)));
		obj.setAngularVelocity(VecMath.scale(VecMath.addition(
				obj.getAngularVelocity(),
				VecMath.transformVector(obj.getInverseInertia(),
						VecMath.scale(obj.getTorqueAccumulator(), delta))),
				1 / (1 + obj.getAngularDamping() * delta)));

		// System.out.println(obj.getLinearVelocity() + "; " +
		// obj.getAngularVelocity());
		obj.clearForces();

		obj.translate(VecMath.scale(
				VecMath.addition(oldlinearvel, obj.getLinearVelocity()),
				delta * 0.5f));
		obj.rotate(VecMath.scale(
				VecMath.addition(oldangularvel, obj.getAngularVelocity()),
				delta * 0.5f));

		// System.out.println(oldlinearvel + "; " + oldangularvel + "; " +
		// obj.getLinearVelocity() + "; " + obj.getAngularVelocity());
	}

	@Override
	public void integrate3(RigidBody3 obj, float delta) {
		System.out.println(obj.getInverseMass() + "; " + VecMath.scale(
				VecMath.scale(obj.getForceAccumulator(), delta),
				obj.getInverseMass()));
		Vector3f oldlinearvel = obj.getLinearVelocity();
		Vector3f oldangularvel = obj.getAngularVelocity();
		obj.setLinearVelocity(VecMath.scale(
				VecMath.addition(obj.getLinearVelocity(), VecMath.scale(
						VecMath.scale(obj.getForceAccumulator(), delta),
						obj.getInverseMass())), 1 / (1 + obj.getLinearDamping()
						* delta)));
		obj.setAngularVelocity(VecMath.scale(VecMath.addition(
				obj.getAngularVelocity(),
				QuatMath.transform(obj.getInverseInertia(),
						VecMath.scale(obj.getTorqueAccumulator(), delta))),
				1 / (1 + obj.getAngularDamping() * delta)));

		obj.clearForces();

		obj.translate(VecMath.scale(
				VecMath.addition(oldlinearvel, obj.getLinearVelocity()),
				delta * 0.5f));
		obj.rotate(VecMath.scale(
				VecMath.addition(oldangularvel, obj.getAngularVelocity()),
				delta * 0.5f));
	}
}