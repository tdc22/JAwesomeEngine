package space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import broadphase.Broadphase;
import broadphase.BroadphaseListener;
import constraints.ConstraintSolverErin2;
import integration.IntegrationSolver;
import manifold.CollisionManifold;
import manifold.ContactManifold;
import manifold.ManifoldManager;
import manifold.RaycastHitResult;
import manifold.RaycastResult;
import narrowphase.Narrowphase;
import narrowphase.RaycastNarrowphase;
import objects.CollisionShape;
import objects.CompoundObject;
import objects.Constraint;
import objects.Constraint2;
import objects.GhostObject;
import objects.Ray;
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
	final Broadphase<L, RigidBody<L, ?, A2, ?>> broadphase;
	final Narrowphase<L> narrowphase;
	final RaycastNarrowphase<L> raycastnarrowphase;
	final CollisionResolution collisionresolution;
	final PositionalCorrection positionalcorrection;
	final ManifoldManager<L, A2> manifoldmanager;
	final ConstraintSolverErin2 constraintsolver = new ConstraintSolverErin2();
	protected final List<RigidBody<L, A1, A2, A3>> objects;
	protected final List<CompoundObject<L, A2>> compoundObjects;
	protected final List<GhostObject<L, A1, A2, A3>> ghostobjects;
	protected final Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> collisionfilters;
	protected final List<Constraint<L, A1, A2, A3>> constraints;
	protected Set<Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>>> overlaps;
	protected L globalForce;
	protected L globalGravitation;
	protected int resolutionIterations = 25;
	protected int constraintResolutionIterations = 1;
	protected boolean cullStaticOverlaps = true;
	protected PhysicsProfiler profiler;

	protected class CompoundListener implements BroadphaseListener<L, RigidBody<L, ?, A2, ?>> {

		@Override
		public void overlapStarted(RigidBody<L, ?, A2, ?> objA, RigidBody<L, ?, A2, ?> objB) {
		}

		@Override
		public void overlapEnded(RigidBody<L, ?, A2, ?> objA, RigidBody<L, ?, A2, ?> objB) {
			if (objA.isCompound()) {
				if (objB.isCompound()) {
					for (CollisionShape<L, A2, ?> cs : objB.getCompound().getCollisionShapes()) {
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

	public Space(IntegrationSolver integrationsolver, Broadphase<L, RigidBody<L, ?, A2, ?>> broadphase,
			Narrowphase<L> narrowphase, RaycastNarrowphase<L> raycastnarrowphase,
			CollisionResolution collisionresolution, PositionalCorrection positionalcorrection,
			ManifoldManager<L, A2> manifoldmanager) {
		this.integrationsolver = integrationsolver;
		this.broadphase = broadphase;
		this.narrowphase = narrowphase;
		this.raycastnarrowphase = raycastnarrowphase;
		this.collisionresolution = collisionresolution;
		this.positionalcorrection = positionalcorrection;
		this.manifoldmanager = manifoldmanager;
		objects = new ArrayList<RigidBody<L, A1, A2, A3>>();
		compoundObjects = new ArrayList<CompoundObject<L, A2>>();
		ghostobjects = new ArrayList<GhostObject<L, A1, A2, A3>>();
		overlaps = new LinkedHashSet<Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>>>();
		collisionfilters = new HashSet<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>>();
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

	public Broadphase<L, RigidBody<L, ?, A2, ?>> getBroadphase() {
		return broadphase;
	}

	// public void addObject(CollisionObject obj) {
	// objects.add(obj);
	// }

	public List<CollisionManifold<L, A2>> getCollisionManifolds() {
		return manifoldmanager.getManifolds();
	}

	public List<CollisionManifold<L, A2>> getCollisionManifoldsNoGhosts() {
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

	public RaycastNarrowphase<L> getRaycastNarrowphase() {
		return raycastnarrowphase;
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

	public Set<Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>>> getOverlaps() {
		return overlaps;
	}

	public Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> getCollisionFilters() {
		return collisionfilters;
	}

	public PositionalCorrection getPositionalCorrection() {
		return positionalcorrection;
	}

	public int getResolutionIterations() {
		return resolutionIterations;
	}

	public boolean hasCollision(RigidBody<L, ?, ?, ?> object) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifolds())
			if (manifold.getObjects().contains(object))
				return true;
		return false;
	}

	public boolean hasCollision(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifolds())
			if (manifold.getObjects().contains(objectA) && manifold.getObjects().contains(objectB))
				return true;
		return false;
	}

	public boolean hasCollisionNoGhosts(RigidBody<L, ?, ?, ?> object) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifoldsNoGhosts())
			if (manifold.getObjects().contains(object))
				return true;
		return false;
	}

	public boolean hasCollisionNoGhosts(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifoldsNoGhosts())
			if (manifold.getObjects().contains(objectA) && manifold.getObjects().contains(objectB))
				return true;
		return false;
	}

	public CollisionManifold<L, ?> getFirstCollisionManifold(RigidBody<L, ?, ?, ?> object) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifolds())
			if (manifold.getObjects().contains(object))
				return manifold;
		return null;
	}

	public CollisionManifold<L, ?> getFirstCollisionManifold(RigidBody<L, ?, ?, ?> objectA,
			RigidBody<L, ?, ?, ?> objectB) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifolds())
			if (manifold.getObjects().contains(objectA) && manifold.getObjects().contains(objectB))
				return manifold;
		return null;
	}

	public CollisionManifold<L, ?> getFirstCollisionManifoldNoGhosts(RigidBody<L, ?, ?, ?> object) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifoldsNoGhosts())
			if (manifold.getObjects().contains(object))
				return manifold;
		return null;
	}

	public CollisionManifold<L, ?> getFirstCollisionManifoldNoGhosts(RigidBody<L, ?, ?, ?> objectA,
			RigidBody<L, ?, ?, ?> objectB) {
		for (CollisionManifold<L, ?> manifold : manifoldmanager.getManifoldsNoGhosts())
			if (manifold.getObjects().contains(objectA) && manifold.getObjects().contains(objectB))
				return manifold;
		return null;
	}

	public boolean hasOverlap(RigidBody<L, ?, ?, ?> object) {
		for (Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>> overlap : overlaps)
			if (overlap.contains(object))
				return true;
		return false;
	}

	public boolean hasOverlap(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
		for (Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>> overlap : overlaps)
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
		/*
		 * for (Constraint<L, A1, A2, A3> c : constraints) c.initStep(delta); for (int i
		 * = 0; i < constraintResolutionIterations; i++) for (Constraint<L, A1, A2, A3>
		 * c : constraints) c.solve(delta);
		 */
		// source: http://www.cs.cmu.edu/~baraff/papers/sig96.pdf
		// http://www.bulletphysics.com/ftp/pub/test/physics/papers/IterativeDynamics.pdf
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

	public void setConstraintResolutionIterations(int count) {
		constraintResolutionIterations = count;
	}

	public void setProfiler(PhysicsProfiler profiler) {
		this.profiler = profiler;
	}

	@Override
	public void update(int delta) {
		updateTimestep(delta / 1000f);
	}

	public RigidBody<L, ?, ?, ?> raycastBroadphase(Ray<L> ray) {
		return broadphase.raycast(ray);
	}

	public Set<RigidBody<L, ?, A2, ?>> raycastAllBroadphase(Ray<L> ray) {
		return broadphase.raycastAll(ray);
	}

	public RaycastResult<L> raycast(Ray<L> ray) {
		RaycastResult<L> result = null;
		float distance = Float.MAX_VALUE;

		Set<RigidBody<L, ?, A2, ?>> raycastOverlaps = raycastAllBroadphase(ray);
		for (RigidBody<L, ?, A2, ?> body : raycastOverlaps) {
			if (raycastnarrowphase.isColliding(body, ray)) {
				RaycastHitResult<L> rayhit = raycastnarrowphase.computeCollision(body, ray);
				if (rayhit.getHitDistance() < distance) {
					result = new RaycastResult<L>(body, rayhit);
					distance = result.getHitDistance();
				}
			}
		}

		return result;
	}

	public Set<RaycastResult<L>> raycastAll(Ray<L> ray) {
		Set<RaycastResult<L>> result = new HashSet<RaycastResult<L>>();

		Set<RigidBody<L, ?, A2, ?>> raycastOverlaps = raycastAllBroadphase(ray);
		for (RigidBody<L, ?, A2, ?> body : raycastOverlaps) {
			if (!body.isCompound()) {
				if (raycastnarrowphase.isColliding(body, ray)) {
					result.add(new RaycastResult<L>(body, raycastnarrowphase.computeCollision(body, ray)));
				}
			} else {
				Set<CollisionShape<L, A2, ?>> compoundRaycastOverlaps = body.getCompound().getCompoundBroadphase()
						.raycastAll(ray);
				for (CollisionShape<L, ?, ?> compoundbody : compoundRaycastOverlaps) {
					if (raycastnarrowphase.isColliding(compoundbody, ray)) {
						result.add(new RaycastResult<L>(body, raycastnarrowphase.computeCollision(compoundbody, ray)));
					}
				}
			}
		}

		return result;
	}

	public void addCollisionFilter(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
		addCollisionFilter(new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(objectA, objectB));
	}

	public void addCollisionFilter(Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> collisionPair) {
		collisionfilters.add(collisionPair);
	}

	public void removeCollisionFilter(RigidBody<L, ?, ?, ?> objectA, RigidBody<L, ?, ?, ?> objectB) {
		removeCollisionFilter(new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(objectA, objectB));
	}

	public void removeCollisionFilter(Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> collisionPair) {
		collisionfilters.remove(collisionPair);
	}

	public boolean isCollisionFiltered(Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>> overlap) {
		return collisionfilters.contains(overlap);
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
		for (Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>> overlap : overlaps) {
			if ((overlap.getFirst().getMass() != 0 || overlap.getSecond().getMass() != 0 || !cullStaticOverlaps)
					&& !isCollisionFiltered(overlap)
					&& !(overlap.getFirst().isGhost() && overlap.getSecond().isGhost())) {
				if (!overlap.getFirst().isCompound()) {
					if (!overlap.getSecond().isCompound()) {
						if (narrowphase.isColliding(overlap.getFirst(), overlap.getSecond())) {
							ContactManifold<L> contactManifold = narrowphase.computeCollision(overlap.getFirst(),
									overlap.getSecond());
							if (contactManifold != null) {
								manifoldmanager.add(new CollisionManifold<L, A2>(overlap, contactManifold));
							}
						}
					} else {
						handleCompoundAndNonCompound(overlap.getSecond().getCompound(), overlap.getFirst());
					}
				} else {
					if (!overlap.getSecond().isCompound()) {
						handleCompoundAndNonCompound(overlap.getFirst().getCompound(), overlap.getSecond());
					} else {
						CompoundObject<L, A2> co1 = overlap.getFirst().getCompound();
						CompoundObject<L, A2> co2 = overlap.getSecond().getCompound();
						Broadphase<L, CollisionShape<L, A2, ?>> compoundBroadphase = co1.getCompoundBroadphase();
						for (CollisionShape<L, A2, ?> cs : co2.getCollisionShapes()) {
							if (!compoundBroadphase.contains(cs)) {
								compoundBroadphase.add(cs);
							}
						}
						for (Pair<CollisionShape<L, A2, ?>, CollisionShape<L, A2, ?>> compoundoverlap : compoundBroadphase
								.getOverlaps()) {
							// first in first && second in second
							if (co1.getCollisionShapes().contains(compoundoverlap.getFirst())
									&& co2.getCollisionShapes().contains(compoundoverlap.getSecond())) {
								if (narrowphase.isColliding(compoundoverlap.getFirst(), compoundoverlap.getSecond())) {
									ContactManifold<L> contactManifold = narrowphase
											.computeCollision(compoundoverlap.getFirst(), compoundoverlap.getSecond());
									if (contactManifold != null) {
										manifoldmanager
												.add(new CollisionManifold<L, A2>(
														new Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>>(
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
													.add(new CollisionManifold<L, A2>(
															new Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>>(
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
		resolveConstraints(delta); // TODO: check position
		// for (int i = 0; i < constraintResolutionIterations; i++)
		// resolveConstraints(delta);

		profiler.resolutionIntegration();

		integrate(delta);
		correct();

		for (int i = 0; i < constraintResolutionIterations; i++) {
			for (Constraint<L, A1, A2, A3> c : constraints) {
				constraintsolver.solveVelocities((Constraint2) c);
			}
		}

		for (CompoundObject<L, A2> co : compoundObjects)
			co.updateTransformations();

		profiler.physicsEnd();
	}

	private void handleCompoundAndNonCompound(CompoundObject<L, A2> co, RigidBody<L, ?, A2, ?> rb) {
		Broadphase<L, CollisionShape<L, A2, ?>> compoundBroadphase = co.getCompoundBroadphase();
		if (!compoundBroadphase.contains(rb)) {
			compoundBroadphase.add(rb);
		}
		for (Pair<CollisionShape<L, A2, ?>, CollisionShape<L, A2, ?>> overlap : compoundBroadphase.getOverlaps()) {
			if (overlap.contains(rb)) {
				if ((co.getCollisionShapes().contains(overlap.getFirst()) && rb.equals(overlap.getSecond())
						|| (co.getCollisionShapes().contains(overlap.getSecond()) && rb.equals(overlap.getFirst())))) {
					if (narrowphase.isColliding(overlap.getFirst(), overlap.getSecond())) {
						ContactManifold<L> contactManifold = narrowphase.computeCollision(overlap.getFirst(),
								overlap.getSecond());
						if (contactManifold != null) {
							if (overlap.getFirst().equals(rb)) {
								manifoldmanager.add(new CollisionManifold<L, A2>(
										new Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>>(rb, co.getRigidBody()),
										contactManifold));
							} else {
								manifoldmanager.add(new CollisionManifold<L, A2>(
										new Pair<RigidBody<L, ?, A2, ?>, RigidBody<L, ?, A2, ?>>(co.getRigidBody(), rb),
										contactManifold));
							}
						}
					}
				}
			}
		}
	}
}