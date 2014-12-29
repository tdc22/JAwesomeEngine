package manifold;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.RigidBody;
import utils.Pair;
import vector.Vector3f;

public class PersistentCollisionManifold extends CollisionManifold<Vector3f> {
	List<Vector3f> storedpointsA, storedpointsB;
	float lastareaA = 0, lastareaB = 0;

	public PersistentCollisionManifold(CollisionManifold<Vector3f> cm) {
		super(cm);
		storedpointsA = new ArrayList<Vector3f>();
		storedpointsB = new ArrayList<Vector3f>();
		addPoint(storedpointsA, cm.getContactPointA(), lastareaA);
		addPoint(storedpointsB, cm.getContactPointB(), lastareaB);
	}

	public PersistentCollisionManifold(
			Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>> objs,
			ContactManifold<Vector3f> manifold) {
		super(objs, manifold);
		storedpointsA = new ArrayList<Vector3f>();
		storedpointsB = new ArrayList<Vector3f>();
		addPoint(storedpointsA, manifold.getContactPointA(), lastareaA);
		addPoint(storedpointsB, manifold.getContactPointB(), lastareaB);
	}

	public PersistentCollisionManifold(RigidBody<Vector3f, ?, ?, ?> obj1,
			RigidBody<Vector3f, ?, ?, ?> obj2,
			ContactManifold<Vector3f> manifold) {
		super(obj1, obj2, manifold);
		storedpointsA = new ArrayList<Vector3f>();
		storedpointsB = new ArrayList<Vector3f>();
		addPoint(storedpointsA, manifold.getContactPointA(), lastareaA);
		addPoint(storedpointsB, manifold.getContactPointB(), lastareaB);
	}

	public PersistentCollisionManifold(RigidBody<Vector3f, ?, ?, ?> obj1,
			RigidBody<Vector3f, ?, ?, ?> obj2, float penetrationdepth,
			Vector3f collisionnormal, Vector3f contactA, Vector3f contactB,
			Vector3f relativecontactA, Vector3f relativecontactB,
			Vector3f localcontactA, Vector3f localcontactB, Vector3f tangentA,
			Vector3f tangentB) {
		super(obj1, obj2, penetrationdepth, collisionnormal, contactA,
				contactB, relativecontactA, relativecontactB, localcontactA,
				localcontactB, tangentA, tangentB);
		storedpointsA = new ArrayList<Vector3f>();
		storedpointsB = new ArrayList<Vector3f>();
		addPoint(storedpointsA, contactA, lastareaA);
		addPoint(storedpointsB, contactB, lastareaB);
	}

	public void add(ContactManifold<Vector3f> cm) {
		penetrationdepth = cm.getPenetrationDepth();
		lastareaA = addPoint(storedpointsA, cm.getContactPointA(), lastareaA);
		lastareaB = addPoint(storedpointsB, cm.getContactPointB(), lastareaB);
		contactA = computeCenter(storedpointsA);
		contactB = computeCenter(storedpointsB);
		relativecontactA = VecMath.subtraction(contactA, getObjects()
				.getFirst().getTranslation());
		relativecontactB = VecMath.subtraction(contactB, getObjects()
				.getSecond().getTranslation());
		collisionnormal = cm.getCollisionNormal();
	}

	private float addPoint(List<Vector3f> list, Vector3f point, float lastarea) {
		if (list.size() == 3) {
			float area = triangleSize(list);
			if (area > lastarea) {
				list.add(point);
				lastarea = area;
				list.remove(0);
			}
		} else {
			if (list.size() == 2) {
				list.add(point);
				lastarea = triangleSize(list);
			} else {
				list.add(point);
			}
		}
		return lastarea;
	}

	public void clear() {
		storedpointsA.clear();
		storedpointsB.clear();
	}

	private Vector3f computeCenter(List<Vector3f> list) {
		if (list.size() == 3) {
			return VecMath.scale(VecMath.addition(
					VecMath.addition(list.get(0), list.get(1)), list.get(2)),
					1 / 3f);
		}
		if (list.size() == 2) {
			return VecMath.scale(VecMath.addition(list.get(0), list.get(1)),
					0.5f);
		}
		// size == 1
		return list.get(0);
	}

	private float triangleSize(List<Vector3f> list) {
		return 0.5f * VecMath.length(VecMath.crossproduct(
				VecMath.subtraction(list.get(1), list.get(0)),
				VecMath.subtraction(list.get(2), list.get(0))));
	}
}