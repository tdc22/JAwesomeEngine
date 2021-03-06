package integration;

import objects.RigidBody2;
import objects.RigidBody3;
import vector.Vector2f;
import vector.Vector3f;

public interface IntegrationSolver {
	public void integrate2(RigidBody2 obj, float delta, Vector2f gravitation);

	public void integrate3(RigidBody3 obj, float delta, Vector3f gravitation);
}
