package space;

import integration.IntegrationSolver;
import manifold.CollisionManifold;
import manifold.ManifoldManager;
import matrix.Matrix1f;
import narrowphase.Narrowphase;
import objects.CompoundObject2;
import objects.RigidBody;
import objects.RigidBody2;
import positionalcorrection.PositionalCorrection;
import quaternion.Complexf;
import resolution.CollisionResolution;
import vector.Vector1f;
import vector.Vector2f;
import broadphase.Broadphase;

public class Space2 extends Space<Vector2f, Vector1f, Complexf, Matrix1f> {

	public Space2(IntegrationSolver integrationsolver,
			Broadphase<Vector2f, RigidBody<Vector2f, ?, ?, ?>> broadphase,
			Narrowphase<Vector2f> narrowphase,
			CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection,
			ManifoldManager<Vector2f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution,
				positionalcorrection, manifoldmanager);
		globalForce = new Vector2f();
		globalGravitation = new Vector2f();
	}

	@Override
	protected void correct() {
		for (CollisionManifold<Vector2f> manifold : getCollisionManifolds())
			positionalcorrection.correct2(manifold);
	}

	@Override
	protected void integrate(float delta) {
		for (RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> o : objects) {
			integrationsolver.integrate2((RigidBody2) o, delta,
					globalGravitation);
		}
	}

	@Override
	protected void resolve() {
		for (CollisionManifold<Vector2f> manifold : getCollisionManifolds())
			collisionresolution.resolve2(manifold);
	}

	public void addCompoundObject(CompoundObject2 compoundobject) {
		addRigidBody(compoundobject);
		compoundObjects.add(compoundobject);
	}
}
