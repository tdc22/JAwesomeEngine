package objects;

import java.util.List;

import broadphase.Broadphase;
import quaternion.Rotation;
import vector.Vector;

public interface CompoundObject<L extends Vector, A extends Rotation> {
	public Broadphase<L, CollisionShape<L, ?, ?>> getCompoundBroadphase();

	public RigidBody<L, ?, ?, ?> getRigidBody();

	public List<CollisionShape<L, A, ?>> getCollisionShapes();

	public void updateTransformations();
}