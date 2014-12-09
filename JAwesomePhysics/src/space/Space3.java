package space;

import integration.IntegrationSolver;
import manifold.CollisionManifold;
import manifold.ManifoldManager;
import narrowphase.Narrowphase;
import objects.RigidBody;
import objects.RigidBody3;
import positionalcorrection.PositionalCorrection;
import quaternion.Quaternionf;
import resolution.CollisionResolution;
import vector.Vector3f;
import broadphase.Broadphase;

public class Space3 extends Space<Vector3f, Vector3f, Quaternionf, Quaternionf> {

	public Space3(IntegrationSolver integrationsolver,
			Broadphase<Vector3f> broadphase, Narrowphase<Vector3f> narrowphase,
			CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection,
			ManifoldManager<Vector3f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution,
				positionalcorrection, manifoldmanager);
		globalForce = new Vector3f();
	}

	@Override
	protected void correct() {
		for (CollisionManifold<Vector3f> manifold : getCollisionManifolds())
			positionalcorrection.correct(manifold);
	}

	@Override
	protected void integrate(float delta) {
		for (RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> o : objects)
			integrationsolver.integrate3((RigidBody3) o, delta);
	}

	@Override
	protected void resolve() {
		for (CollisionManifold<Vector3f> manifold : getCollisionManifolds())
			collisionresolution.resolve(manifold);
	}
}
