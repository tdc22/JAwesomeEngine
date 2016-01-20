package manifold;

import objects.RigidBody;
import utils.Pair;
import vector.Vector;

public class CollisionManifold<L extends Vector> extends ContactManifold<L> {
	Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> objects;

	public CollisionManifold(CollisionManifold<L> manifold) {
		super(manifold);
		objects = manifold.getObjects();
	}

	public CollisionManifold(Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> objs, ContactManifold<L> manifold) {
		super(manifold);
		objects = objs;
	}

	public CollisionManifold(RigidBody<L, ?, ?, ?> obj1, RigidBody<L, ?, ?, ?> obj2, ContactManifold<L> manifold) {
		super(manifold);
		objects = new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(obj1, obj2);
	}

	public CollisionManifold(RigidBody<L, ?, ?, ?> obj1, RigidBody<L, ?, ?, ?> obj2, float penetrationdepth,
			L collisionnormal, L contactA, L contactB, L relativecontactA, L relativecontactB, L localcontactA,
			L localcontactB, L tangentA, L tangentB) {
		super(penetrationdepth, collisionnormal, contactA, contactB, relativecontactA, relativecontactB, localcontactA,
				localcontactB, tangentA, tangentB);
		objects = new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(obj1, obj2);
	}

	public Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> getObjects() {
		return objects;
	}
}