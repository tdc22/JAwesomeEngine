package manifold;

import objects.RigidBody;
import quaternion.Rotation;
import utils.Pair;
import vector.Vector;

public class CollisionManifold<L extends Vector, A extends Rotation> extends ContactManifold<L> {
	Pair<RigidBody<L, ?, A, ?>, RigidBody<L, ?, A, ?>> objects;

	public CollisionManifold(CollisionManifold<L, A> manifold) {
		super(manifold);
		objects = manifold.getObjects();
	}

	public CollisionManifold(Pair<RigidBody<L, ?, A, ?>, RigidBody<L, ?, A, ?>> objs, ContactManifold<L> manifold) {
		super(manifold);
		objects = objs;
	}

	public CollisionManifold(RigidBody<L, ?, A, ?> obj1, RigidBody<L, ?, A, ?> obj2, ContactManifold<L> manifold) {
		super(manifold);
		objects = new Pair<RigidBody<L, ?, A, ?>, RigidBody<L, ?, A, ?>>(obj1, obj2);
	}

	public CollisionManifold(RigidBody<L, ?, A, ?> obj1, RigidBody<L, ?, A, ?> obj2, float penetrationdepth,
			L collisionnormal, L contactA, L contactB, L relativecontactA, L relativecontactB, L localcontactA,
			L localcontactB, L tangentA, L tangentB) {
		super(penetrationdepth, collisionnormal, contactA, contactB, relativecontactA, relativecontactB, localcontactA,
				localcontactB, tangentA, tangentB);
		objects = new Pair<RigidBody<L, ?, A, ?>, RigidBody<L, ?, A, ?>>(obj1, obj2);
	}

	public Pair<RigidBody<L, ?, A, ?>, RigidBody<L, ?, A, ?>> getObjects() {
		return objects;
	}
}