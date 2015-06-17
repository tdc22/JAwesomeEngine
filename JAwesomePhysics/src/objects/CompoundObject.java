package objects;

import vector.Vector;
import broadphase.Broadphase;

public interface CompoundObject<L extends Vector> {
	public Broadphase<L, CollisionShape<L, ?, ?>> getCompoundBroadphase();

	public RigidBody<L, ?, ?, ?> getRigidBody();
}