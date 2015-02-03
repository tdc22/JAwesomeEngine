package manifold;

import java.util.List;

import vector.Vector;

public abstract class ManifoldManager<L extends Vector> {
	public abstract void add(CollisionManifold<L> cm);

	public abstract List<CollisionManifold<L>> getManifolds();

	public abstract void clear();
}