package space;

import integration.IntegrationSolver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import manifold.CollisionManifold;
import manifold.ContactManifold;
import manifold.ManifoldManager;
import narrowphase.Narrowphase;
import objects.Constraint;
import objects.RigidBody;
import objects.Updateable;
import positionalcorrection.PositionalCorrection;
import quaternion.Rotation;
import resolution.CollisionResolution;
import utils.Pair;
import vector.Vector;
import broadphase.Broadphase;

public abstract class Space<L extends Vector, A1 extends Vector, A2 extends Rotation, A3 extends Rotation>
		implements Updateable {
	final IntegrationSolver integrationsolver;
	final Broadphase<L> broadphase;
	final Narrowphase<L> narrowphase;
	final CollisionResolution collisionresolution;
	final PositionalCorrection positionalcorrection;
	final ManifoldManager<L> manifoldmanager;
	protected List<RigidBody<L, A1, A2, A3>> objects;
	protected Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> overlaps;
	protected List<Constraint<L>> constraints;
	protected L globalForce;
	protected L globalGravitation;
	protected int resolutionIterations = 25;
	protected int constraintResolutionIterations = 25;
	protected boolean cullStaticOverlaps = true;

	public Space(IntegrationSolver integrationsolver, Broadphase<L> broadphase,
			Narrowphase<L> narrowphase,
			CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection,
			ManifoldManager<L> manifoldmanager) {
		this.integrationsolver = integrationsolver;
		this.broadphase = broadphase;
		this.narrowphase = narrowphase;
		this.collisionresolution = collisionresolution;
		this.positionalcorrection = positionalcorrection;
		this.manifoldmanager = manifoldmanager;
		objects = new ArrayList<RigidBody<L, A1, A2, A3>>();
		overlaps = new LinkedHashSet<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>>();
		constraints = new ArrayList<Constraint<L>>();
	}

	public void addConstraint(Constraint<L> constraint) {
		constraints.add(constraint);
	}

	public void addRigidBody(RigidBody<L, A1, A2, A3> body) {
		broadphase.add(body);
		objects.add(body);
	}

	private void applyGlobalForce() {
		for (RigidBody<L, ?, ?, ?> rb : objects)
			rb.applyCentralForce(globalForce);
	}

	public void applyGlobalForce(L force) {
		for (RigidBody<L, ?, ?, ?> rb : objects)
			rb.applyCentralForce(force);
	}

	protected abstract void correct();

	public Broadphase<L> getBroadphase() {
		return broadphase;
	}

	// public void addObject(CollisionObject obj) {
	// objects.add(obj);
	// }

	public List<CollisionManifold<L>> getCollisionManifolds() {
		return manifoldmanager.getManifolds();
	}

	public CollisionResolution getCollsionResolution() {
		return collisionresolution;
	}

	public List<Constraint<L>> getConstraints() {
		return constraints;
	}

	public IntegrationSolver getIntegrationSolver() {
		return integrationsolver;
	}

	public Narrowphase<L> getNarrowphase() {
		return narrowphase;
	}

	public List<RigidBody<L, A1, A2, A3>> getObjects() {
		return objects;
	}

	public Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> getOverlaps() {
		return overlaps;
	}

	public PositionalCorrection getPositionalCorrection() {
		return positionalcorrection;
	}

	public int getResolutionIterations() {
		return resolutionIterations;
	}

	public boolean hasCollision(RigidBody<L, ?, ?, ?> object) {
		for (CollisionManifold<L> manifold : manifoldmanager.getManifolds())
			if (manifold.getObjects().contains(object))
				return true;
		return false;
	}

	public boolean hasCollision(RigidBody<L, ?, ?, ?> objectA,
			RigidBody<L, ?, ?, ?> objectB) {
		for (CollisionManifold<L> manifold : manifoldmanager.getManifolds())
			if (manifold.getObjects().contains(objectA)
					&& manifold.getObjects().contains(objectB))
				return true;
		return false;
	}

	public boolean hasOverlap(RigidBody<L, ?, ?, ?> object) {
		for (Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> overlap : overlaps)
			if (overlap.contains(object))
				return true;
		return false;
	}

	public boolean hasOverlap(RigidBody<L, ?, ?, ?> objectA,
			RigidBody<L, ?, ?, ?> objectB) {
		for (Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> overlap : overlaps)
			if (overlap.contains(objectA) && overlap.contains(objectB))
				return true;
		return false;
	}

	protected abstract void integrate(float delta);

	public boolean isCullStaticOverlaps() {
		return cullStaticOverlaps;
	}

	public void removeRigidBody(RigidBody<L, A1, A2, A3> body) {
		objects.remove(body);
		broadphase.remove(body);
	}

	protected abstract void resolve();

	protected void resolveConstraints(float delta) {
		for (Constraint<L> c : constraints)
			c.solve(delta);
	}

	public void setCullStaticOverlaps(boolean cull) {
		cullStaticOverlaps = cull;
	}

	public void setGlobalForce(L force) {
		globalForce = force;
	}

	public void setResolutionIterations(int count) {
		resolutionIterations = count;
	}

	@Override
	public void update(int delta) {
		updateTimestep(delta / 1000f);
	}

	public void updateTimestep(float delta) {
		for (RigidBody<?, ?, ?, ?> o : objects)
			o.updateInverseRotation();

		broadphase.update();
		overlaps = broadphase.getOverlaps();

		manifoldmanager.clear();
		for (Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> overlap : overlaps) {
			if (overlap.getFirst().getMass() != 0
					|| overlap.getSecond().getMass() != 0
					|| !cullStaticOverlaps) // TODO: check if
				// there's a better
				// way or make this
				// optional
				if (narrowphase.isColliding(overlap.getFirst(),
						overlap.getSecond())) {
					ContactManifold<L> contactManifold = narrowphase
							.computeCollision(overlap.getFirst(),
									overlap.getSecond());
					manifoldmanager.add(new CollisionManifold<L>(overlap,
							contactManifold));
				}
		}
		for (int i = 0; i < resolutionIterations; i++)
			resolve();
		applyGlobalForce();
		for (int i = 0; i < constraintResolutionIterations; i++)
			resolveConstraints(delta);
		integrate(delta);
		correct();
	}
}
