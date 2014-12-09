package integration;

import math.QuatMath;
import math.VecMath;
import objects.RigidBody2;
import objects.RigidBody3;

/**
 * Symplectic euler integration method implementation.
 * 
 * @author Oliver Schall
 * 
 */

public class EulerIntegration implements IntegrationSolver {
	@Override
	public void integrate2(RigidBody2 obj, float delta) {
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

		obj.clearForces();

		obj.translate(VecMath.scale(obj.getLinearVelocity(), delta));
		obj.rotate(VecMath.scale(obj.getAngularVelocity(), delta));
	}

	@Override
	public void integrate3(RigidBody3 obj, float delta) {
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

		obj.translate(VecMath.scale(obj.getLinearVelocity(), delta));
		obj.rotate(VecMath.scale(obj.getAngularVelocity(), delta));
	}
}
