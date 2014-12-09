package manifold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objects.RigidBody;
import utils.Pair;
import vector.Vector3f;

public class PersistentManifoldManager extends ManifoldManager<Vector3f> {
	protected class StoredManifold extends PersistentCollisionManifold {
		boolean modified;

		public StoredManifold(PersistentCollisionManifold manifold) {
			super(manifold);
			modified = true;
		}

		public boolean isModified() {
			return modified;
		}

		public void setModified(boolean m) {
			modified = m;
		}
	}

	HashMap<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>, Integer> manifoldids;

	List<StoredManifold> manifolds;

	public PersistentManifoldManager() {
		manifoldids = new HashMap<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>, Integer>();
		manifolds = new ArrayList<StoredManifold>();
	}

	@Override
	public void add(CollisionManifold<Vector3f> cm) {
		if (manifoldids.containsKey(cm.getObjects())) {
			StoredManifold s = manifolds.get(manifoldids.get(cm.getObjects()));
			s.add(cm);
			s.setModified(true);
		} else {
			manifoldids.put(cm.getObjects(), manifolds.size());
			manifolds.add(new StoredManifold(
					new PersistentCollisionManifold(cm)));
		}
	}

	@Override
	public void end() {
		// remove unupdated manifolds
		boolean deleted = false;
		int minindex = 0;
		int s = manifolds.size();
		for (int i = s - 1; i >= 0; i--) {
			StoredManifold sm = manifolds.get(i);
			if (!sm.isModified()) {
				sm.clear();
				manifoldids.remove(sm.getObjects());
				manifolds.remove(i);
				deleted = true;
				minindex = i;
			} else
				sm.setModified(false);
		}
		if (deleted) {
			s = manifolds.size();
			for (int i = minindex; i < s; i++) {
				StoredManifold sm = manifolds.get(i);
				manifoldids.put(sm.getObjects(), i);
			}
		}
	}

	@Override
	public List<CollisionManifold<Vector3f>> getManifolds() {
		return new ArrayList<CollisionManifold<Vector3f>>(manifolds);
	}

	@Override
	public void start() {
	}
}
