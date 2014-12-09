package broadphase;

import java.util.Set;

import objects.RigidBody;
import utils.Pair;
import vector.Vector;

public interface Broadphase<L extends Vector> {
	public void add(RigidBody<L, ?, ?, ?> object);

	public Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> getOverlaps();

	public Set<RigidBody<L, ?, ?, ?>> raycast();

	public void remove(RigidBody<L, ?, ?, ?> object);

	public void update();
}