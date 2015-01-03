package manifold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objects.RigidBody;
import utils.Pair;
import vector.Vector2f;

public class PersistentManifoldManager2 extends ManifoldManager<Vector2f> {
	protected class StoredManifold extends PersistentCollisionManifold2 {
		boolean modified;

		public StoredManifold(int maxstoretime, float distthreshold, CollisionManifold<Vector2f> manifold) {
			super(maxstoretime, distthreshold, manifold);
			modified = true;
		}

		public boolean isModified() {
			return modified;
		}

		public void setModified(boolean m) {
			modified = m;
		}
	}
	
	int maxstoretime;
	float distthreshold;

	HashMap<Pair<RigidBody<Vector2f, ?, ?, ?>, RigidBody<Vector2f, ?, ?, ?>>, Integer> manifoldids;

	List<StoredManifold> manifolds;

	public PersistentManifoldManager2() {
		init(6, 5);
	}
	
	public PersistentManifoldManager2(int maxstoretime, float distthreshold) {
		init(maxstoretime, distthreshold);
	}
	
	private void init(int maxstoretime, float distthreshold) {
		this.maxstoretime = maxstoretime;
		this.distthreshold = distthreshold;
		manifoldids = new HashMap<Pair<RigidBody<Vector2f, ?, ?, ?>, RigidBody<Vector2f, ?, ?, ?>>, Integer>();
		manifolds = new ArrayList<StoredManifold>();
	}

	@Override
	public void add(CollisionManifold<Vector2f> cm) {
		if (manifoldids.containsKey(cm.getObjects())) {
			StoredManifold s = manifolds.get(manifoldids.get(cm.getObjects()));
			s.add(cm);
			s.setModified(true);
		} else {
			manifoldids.put(cm.getObjects(), manifolds.size());
			manifolds.add(new StoredManifold(maxstoretime, distthreshold, cm));
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
	public List<CollisionManifold<Vector2f>> getManifolds() {
		return new ArrayList<CollisionManifold<Vector2f>>(manifolds);
	}

	@Override
	public void start() {
	}
}
