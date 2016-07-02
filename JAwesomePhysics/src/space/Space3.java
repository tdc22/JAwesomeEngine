package space;

import broadphase.Broadphase;
import integration.IntegrationSolver;
import manifold.CollisionManifold;
import manifold.ManifoldManager;
import narrowphase.Narrowphase;
import objects.CompoundObject3;
import objects.RigidBody;
import objects.RigidBody3;
import positionalcorrection.PositionalCorrection;
import quaternion.Quaternionf;
import resolution.CollisionResolution;
import vector.Vector3f;

public class Space3 extends Space<Vector3f, Vector3f, Quaternionf, Quaternionf> {

	public Space3(IntegrationSolver integrationsolver, Broadphase<Vector3f, RigidBody<Vector3f, ?, ?, ?>> broadphase,
			Narrowphase<Vector3f> narrowphase, CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection, ManifoldManager<Vector3f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution, positionalcorrection, manifoldmanager);
		globalForce = new Vector3f();
		globalGravitation = new Vector3f();
	}

	@Override
	protected void correct() {
		for (CollisionManifold<Vector3f> manifold : getCollisionManifoldsNoGhosts())
			positionalcorrection.correct(manifold);
	}

	@Override
	protected void integrate(float delta) {
		for (RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> o : objects)
			integrationsolver.integrate3((RigidBody3) o, delta, globalGravitation);
	}

	@Override
	protected void resolve() {
		for (CollisionManifold<Vector3f> manifold : getCollisionManifoldsNoGhosts())
			collisionresolution.resolve(manifold);
	}

	public void addCompoundObject(CompoundObject3 compoundobject) {
		addRigidBody(compoundobject);
		compoundObjects.add(compoundobject);
	}

	public void setGlobalGravitation(float x, float y, float z) {
		globalGravitation.set(x, y, z);
	}
}
