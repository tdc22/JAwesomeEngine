package manifold;

import java.util.ArrayList;
import java.util.List;

import quaternion.Rotation;
import vector.Vector;

public class SimpleManifoldManager<L extends Vector, A extends Rotation> extends ManifoldManager<L, A> {
	final List<CollisionManifold<L, A>> collisionmanifolds;
	final List<CollisionManifold<L, A>> collisionmanifoldsnoghosts;

	public SimpleManifoldManager() {
		collisionmanifolds = new ArrayList<CollisionManifold<L, A>>();
		collisionmanifoldsnoghosts = new ArrayList<CollisionManifold<L, A>>();
	}

	@Override
	public void add(CollisionManifold<L, A> cm) {
		collisionmanifolds.add(cm);
		if (!cm.getObjects().getFirst().isGhost() && !cm.getObjects().getSecond().isGhost()) {
			collisionmanifoldsnoghosts.add(cm);
		}
	}

	@Override
	public void clear() {
		collisionmanifolds.clear();
		collisionmanifoldsnoghosts.clear();
	}

	@Override
	public List<CollisionManifold<L, A>> getManifolds() {
		return collisionmanifolds;
	}

	@Override
	public List<CollisionManifold<L, A>> getManifoldsNoGhosts() {
		return collisionmanifoldsnoghosts;
	}
}
