package integration;

import objects.RigidBody2;
import objects.RigidBody3;

public interface IntegrationSolver {
	public void integrate2(RigidBody2 obj, float delta);

	public void integrate3(RigidBody3 obj, float delta);
}
