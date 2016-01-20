package space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import broadphase.Broadphase;
import broadphase.BroadphaseListener;
import integration.IntegrationSolver;
import manifold.CollisionManifold;
import manifold.ContactManifold;
import manifold.ManifoldManager;
import narrowphase.Narrowphase;
import objects.CollisionShape;
import objects.CompoundObject;
import objects.Constraint;
import objects.GhostObject;
import objects.RigidBody;
import objects.Updateable;
import positionalcorrection.PositionalCorrection;
import quaternion.Rotation;
import resolution.CollisionResolution;
import utils.Pair;
import vector.Vector;

public abstract class Space<L extends Vector, A1 extends Vector, A2 extends Rotation, A3 extends Rotation>
		implements Updateable {
	final IntegrationSolver integrationsolver;
	final Broadphase<L, RigidBody<L, ?, ?, ?>> broadphase;
	final Narrowphase<L> narrowphase;
	final CollisionResolution collisionresolution;
	final PositionalCorrection positionalcorrection;
	final ManifoldManager<L> manifoldmanager;
	protected final List<RigidBody<L, A1, A2, A3>> objects;
	protected final List<CompoundObject<L, A2>> compoundObjects;
	protected final List<GhostObject<L, A1, A2, A3>> ghostobjects;
	protected final Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> collisionfilter;
	protected final List<Constraint<L, A1, A2, A3>> constraints;
	protected Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> overlaps;
	protected L globalForce;
	protected L globalGravitation;
	protected int resolutionIterations = 25;
	protected int constraintResolutionIterations = 25;
	protected boolean cullStaticOverlaps = true;
	protected PhysicsProfiler profiler;

	protected class CompoundListener implements BroadphaseListener<L, RigidBody<L, ?, ?, ?>> {

		@Override
		public void overlapStarted(RigidBody<L, ?, ?, ?> objA, RigidBody<L, ?, ?, ?> objB) {
		}

		@Override
		public void overlapEnded(RigidBody<L, ?, ?, ?> objA, RigidBody<L, ?, ?, ?> objB) {
			if (objA.isCompound()) {
				if (objB.isCompound()) {
					for (CollisionShape<L, ?, ?> cs : objB.getCompound().getCollisionShapes()) {
						objA.getCompound().getCompoundBroadphase().remove(cs);
					}
				} else {
					objA.getCompound().getCompoundBroadphase().remove(objB);
				}
			} else {
				if (objB.isCompound()) {
					objB.getCompound().getCompoundBroadphase().remove(objA);
				} else {

				}
			}
		}

	}

	public Space(IntegrationSolver integrationsolver, Broadphase<L, RigidBody<L, ?, ?, ?>> broadphase,
			Narrowphase<L> narrowphase, CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection, ManifoldManager<L> manifoldmanager) {
		this.integrationsolver = integrationsolver;
		this.broadphase = broadphase;
		this.narrowphase = narrowphase;
		this.collisionresolution = collisionresolution;
		this.positionalcorrection = positionalcorrection;
		this.manifoldmanager = manifoldmanager;
		objects = new ArrayList<RigidBody<L, A1, A2, A3>>();
		compoundObjects = new ArrayList<CompoundObject<L, A2>>();
		ghostobjects = new ArrayList<GhostObject<L, A1, A2, A3>>();
		overlaps = new LinkedHashSet<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>>();
		collisionfilter = new HashSet<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>>();
		constraints = new ArrayList<Constraint<L, A1, A2, A3>>();
		profiler = new NullPhysicsProfiler();
		broadphase.addListener(new CompoundListener());
	}

	public void addConstraint(Constraint<L, A1, A2, A3> constraint) {
		constraints.add(constraint);
	}

	public void addRigidBody(RigidBody<L, A1, A2, A3> body) {
		broadphase.add(body);
		objects.add(body);
	}

	public void addGhostObject(GhostObject<L, A1, A2, A3> ghostobject) {
		broadphase.add(ghostobject);
		ghostobjects.add(ghostobject);
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

	public Broadphase<L, RigidBody<L, ?, ?, ?>> getBroadphase() {
		return broadphase;
	}

	// public void addObject(CollisionObject obj) {
	// objects.add(obj);
	// }

	public List<CollisionManifold<L>> getCollisionManifolds() {
		return manifoldmanager.getManifolds();
	}

	public List<CollisionManifold<L>> getCollisionManifoldsNoGhosts() {
		return manifoldmanager.getManifoldsNoGhosts();
	}

	public CollisionResolution getCollsionResolution() {
		return collisionresolution;
	}

	public List<Constraint<L, A1, A2, A3>> getConstraints() {
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

	public List<CompoundObject<L, A2>> getCompoundObjects() {
		return compoundObjects;
	}

	public List<GhostObject<L, A1, A2, A3>> getGhostObjects() {
		return ghostobjects;
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

	public boolean hasCollision(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
		for (CollisionManifold<L> manifold : manifoldmanager.getManifolds())
			if (manifold.getObjects().contains(objectA) && manifold.getObjects().contains(objectB))
				return true;
		return false;
	}

	public boolean hasOverlap(RigidBody<L, ?, ?, ?> object) {
		for (Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> overlap : overlaps)
			if (overlap.contains(object))
				return true;
		return false;
	}

	public boolean hasOverlap(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
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

	public void removeGhostObject(GhostObject<L, A1, A2, A3> ghostobject) {
		ghostobjects.remove(ghostobject);
		broadphase.remove(ghostobject);
	}

	protected abstract void resolve();

	public void resolveConstraints(float delta) {
		for (Constraint<L, A1, A2, A3> c : constraints)
			c.solve(delta);
	}

	public void setCullStaticOverlaps(boolean cull) {
		cullStaticOverlaps = cull;
	}

	public void setGlobalForce(L force) {
		globalForce = force;
	}

	public void setGlobalGravitation(L gravitation) {
		globalGravitation = gravitation;
	}

	public void setResolutionIterations(int count) {
		resolutionIterations = count;
	}

	public void setProfiler(PhysicsProfiler profiler) {
		this.profiler = profiler;
	}

	@Override
	public void update(int delta) {
		updateTimestep(delta / 1000f);
	}

	public void addCollisionFilter(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
		addCollisionFilter(new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(objectA, objectB));
	}

	public void addCollisionFilter(Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> collisionPair) {
		collisionfilter.add(collisionPair);
	}

	public boolean isCollisionFiltered(Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> collisionPair) {
		return collisionfilter.contains(collisionPair);
	}

	public void updateTimestep(float delta) {
		profiler.physicsStart();

		for (RigidBody<?, ?, ?, ?> o : objects)
			o.updateInverseRotation();
		for (CompoundObject<L, A2> co : compoundObjects)
			co.updateTransformations();
		for (GhostObject<?, ?, ?, ?> g : ghostobjects)
			g.updateInverseRotation();

		broadphase.update();
		overlaps = broadphase.getOverlaps();
		for (CompoundObject<L, A2> co : compoundObjects)
			co.getCompoundBroadphase().update();

		profiler.boradphaseNarrowphase();

		manifoldmanager.clear();
		for (Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> overlap : overlaps) {
			if ((overlap.getFirst().getMass() != 0 || overlap.getSecond().getMass() != 0 || !cullStaticOverlaps)
					&& !isCollisionFiltered(overlap)
					&& !(overlap.getFirst().isGhost() && overlap.getSecond().isGhost())) {
				if (!overlap.getFirst().isCompound()) {
					if (!overlap.getSecond().isCompound()) {
						if (narrowphase.isColliding(overlap.getFirst(), overlap.getSecond())) {
							ContactManifold<L> contactManifold = narrowphase.computeCollision(overlap.getFirst(),
									overlap.getSecond());
							if (contactManifold != null) {
								manifoldmanager.add(new CollisionManifold<L>(overlap, contactManifold));
							}
						}
					} else {
						handleCompoundAndNonCompound(overlap.getSecond().getCompound(), overlap.getFirst());
					}
				} else {
					if (!overlap.getSecond().isCompound()) {
						handleCompoundAndNonCompound(overlap.getFirst().getCompound(), overlap.getSecond());
					} else {
						CompoundObject<L, ?> co1 = overlap.getFirst().getCompound();
						CompoundObject<L, ?> co2 = overlap.getSecond().getCompound();
						Broadphase<L, CollisionShape<L, ?, ?>> compoundBroadphase = co1.getCompoundBroadphase();
						for (CollisionShape<L, ?, ?> cs : co2.getCollisionShapes()) {
							if (!compoundBroadphase.contains(cs)) {
								compoundBroadphase.add(cs);
							}
						}
						for (Pair<CollisionShape<L, ?, ?>, CollisionShape<L, ?, ?>> compoundoverlap : compoundBroadphase
								.getOverlaps()) {
							// first in first && second in second
							if (co1.getCollisionShapes().contains(compoundoverlap.getFirst())
									&& co2.getCollisionShapes().contains(compoundoverlap.getSecond())) {
								if (narrowphase.isColliding(compoundoverlap.getFirst(), compoundoverlap.getSecond())) {
									ContactManifold<L> contactManifold = narrowphase
											.computeCollision(compoundoverlap.getFirst(), compoundoverlap.getSecond());
									if (contactManifold != null) {
										manifoldmanager
												.add(new CollisionManifold<L>(
														new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(
																co1.getRigidBody(), co2.getRigidBody()),
														contactManifold));
									}
								}
							} else {
								// first in second && second in first
								if (co2.getCollisionShapes().contains(compoundoverlap.getFirst())
										&& co1.getCollisionShapes().contains(compoundoverlap.getSecond())) {
									if (narrowphase.isColliding(compoundoverlap.getFirst(),
											compoundoverlap.getSecond())) {
										ContactManifold<L> contactManifold = narrowphase.computeCollision(
												compoundoverlap.getFirst(), compoundoverlap.getSecond());
										if (contactManifold != null) {
											manifoldmanager
													.add(new CollisionManifold<L>(
															new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(
																	co2.getRigidBody(), co1.getRigidBody()),
															contactManifold));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		profiler.narrowphaseResolution();

		for (int i = 0; i < resolutionIterations; i++)
			resolve();
		applyGlobalForce();
		// for (int i = 0; i < constraintResolutionIterations; i++)
		// resolveConstraints(delta);

		profiler.resolutionIntegration();

		integrate(delta);
		correct();

		for (CompoundObject<L, A2> co : compoundObjects)
			co.updateTransformations();

		profiler.physicsEnd();
	}

	private void handleCompoundAndNonCompound(CompoundObject<L, ?> co, RigidBody<L, ?, ?, ?> rb) {
		Broadphase<L, CollisionShape<L, ?, ?>> compoundBroadphase = co.getCompoundBroadphase();
		if (!compoundBroadphase.contains(rb)) {
			compoundBroadphase.add(rb);
		}
		for (Pair<CollisionShape<L, ?, ?>, CollisionShape<L, ?, ?>> overlap : compoundBroadphase.getOverlaps()) {
			if (overlap.contains(rb)) {
				if ((co.getCollisionShapes().contains(overlap.getFirst()) && rb.equals(overlap.getSecond())
						|| (co.getCollisionShapes().contains(overlap.getSecond()) && rb.equals(overlap.getFirst())))) {
					if (narrowphase.isColliding(overlap.getFirst(), overlap.getSecond())) {
						ContactManifold<L> contactManifold = narrowphase.computeCollision(overlap.getFirst(),
								overlap.getSecond());
						if (contactManifold != null) {
							if (overlap.getFirst().equals(rb)) {
								manifoldmanager.add(new CollisionManifold<L>(
										new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(rb, co.getRigidBody()),
										contactManifold));
							} else {
								manifoldmanager.add(new CollisionManifold<L>(
										new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(co.getRigidBody(), rb),
										contactManifold));
							}
						}
					}
				}
			}
		}
	}
}