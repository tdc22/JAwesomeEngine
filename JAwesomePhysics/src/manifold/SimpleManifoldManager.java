package manifold;

import java.util.ArrayList;
import java.util.List;

import vector.Vector;

public class SimpleManifoldManager<L extends Vector> extends ManifoldManager<L> {
	List<CollisionManifold<L>> collisionmanifolds;

	public SimpleManifoldManager() {
		collisionmanifolds = new ArrayList<CollisionManifold<L>>();
	}

	@Override
	public void add(CollisionManifold<L> cm) {
		collisionmanifolds.add(cm);
	}

	@Override
	public void clear() {
		collisionmanifolds.clear();
	}

	@Override
	public List<CollisionManifold<L>> getManifolds() {
		return collisionmanifolds;
	}
}
