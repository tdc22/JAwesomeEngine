package manifold;

import java.util.ArrayList;
import java.util.List;

import vector.Vector;

public class SimpleManifoldManager<L extends Vector> extends ManifoldManager<L> {
	final List<CollisionManifold<L>> collisionmanifolds;
	final List<CollisionManifold<L>> collisionmanifoldsnoghosts;

	public SimpleManifoldManager() {
		collisionmanifolds = new ArrayList<CollisionManifold<L>>();
		collisionmanifoldsnoghosts = new ArrayList<CollisionManifold<L>>();
	}

	@Override
	public void add(CollisionManifold<L> cm) {
		collisionmanifolds.add(cm);
		if (!cm.getObjects().getFirst().isGhost()
				&& !cm.getObjects().getSecond().isGhost()) {
			collisionmanifoldsnoghosts.add(cm);
		}
	}

	@Override
	public void clear() {
		collisionmanifolds.clear();
		collisionmanifoldsnoghosts.clear();
	}

	@Override
	public List<CollisionManifold<L>> getManifolds() {
		return collisionmanifolds;
	}

	@Override
	public List<CollisionManifold<L>> getManifoldsNoGhosts() {
		return collisionmanifoldsnoghosts;
	}
}
