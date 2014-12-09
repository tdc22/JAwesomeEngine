package broadphase;

import java.util.Set;

import objects.RigidBody;
import utils.Pair;
import vector.Vector3f;

public class DynamicAABBTree implements Broadphase<Vector3f> {

	@Override
	public void add(RigidBody<Vector3f, ?, ?, ?> object) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>> getOverlaps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<RigidBody<Vector3f, ?, ?, ?>> raycast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(RigidBody<Vector3f, ?, ?, ?> object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
}