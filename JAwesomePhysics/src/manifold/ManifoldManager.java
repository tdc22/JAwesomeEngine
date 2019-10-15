package manifold;

import java.util.List;

import quaternion.Rotation;
import vector.Vector;

public abstract class ManifoldManager<L extends Vector, A extends Rotation> {
	public abstract void add(CollisionManifold<L, A> cm);

	public abstract void clear();

	public abstract List<CollisionManifold<L, A>> getManifolds();

	public abstract List<CollisionManifold<L, A>> getManifoldsNoGhosts();
}