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
	public List<CollisionManifold<L>> getManifolds() {
		return collisionmanifolds;
	}

	@Override
	public void start() {
		collisionmanifolds.clear();
	}
}
