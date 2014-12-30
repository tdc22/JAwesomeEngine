package manifold;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.RigidBody;
import utils.Pair;
import vector.Vector2f;

public class PersistentCollisionManifold2 extends CollisionManifold<Vector2f> {
	List<Vector2f> storedpointsA, storedpointsB;
	float lastdistA = 0, lastdistB = 0;
	int timetreshold, disttreshold; // TODO

	public PersistentCollisionManifold2(CollisionManifold<Vector2f> cm) {
		super(cm);
		storedpointsA = new ArrayList<Vector2f>();
		storedpointsB = new ArrayList<Vector2f>();
		addPoint(storedpointsA, cm.getContactPointA(), lastdistA);
		addPoint(storedpointsB, cm.getContactPointB(), lastdistB);
	}

	public PersistentCollisionManifold2(
			Pair<RigidBody<Vector2f, ?, ?, ?>, RigidBody<Vector2f, ?, ?, ?>> objs,
			ContactManifold<Vector2f> manifold) {
		super(objs, manifold);
		storedpointsA = new ArrayList<Vector2f>();
		storedpointsB = new ArrayList<Vector2f>();
		addPoint(storedpointsA, manifold.getContactPointA(), lastdistA);
		addPoint(storedpointsB, manifold.getContactPointB(), lastdistB);
	}

	public PersistentCollisionManifold2(RigidBody<Vector2f, ?, ?, ?> obj1,
			RigidBody<Vector2f, ?, ?, ?> obj2,
			ContactManifold<Vector2f> manifold) {
		super(obj1, obj2, manifold);
		storedpointsA = new ArrayList<Vector2f>();
		storedpointsB = new ArrayList<Vector2f>();
		addPoint(storedpointsA, manifold.getContactPointA(), lastdistA);
		addPoint(storedpointsB, manifold.getContactPointB(), lastdistB);
	}

	// TODO: Threshold for the dist so that spheres only have 1 contact point
	public PersistentCollisionManifold2(RigidBody<Vector2f, ?, ?, ?> obj1,
			RigidBody<Vector2f, ?, ?, ?> obj2, float penetrationdepth,
			Vector2f collisionnormal, Vector2f contactA, Vector2f contactB,
			Vector2f relativecontactA, Vector2f relativecontactB,
			Vector2f localcontactA, Vector2f localcontactB, Vector2f tangentA,
			Vector2f tangentB) {
		super(obj1, obj2, penetrationdepth, collisionnormal, contactA,
				contactB, relativecontactA, relativecontactB, localcontactA,
				localcontactB, tangentA, tangentB);
		storedpointsA = new ArrayList<Vector2f>();
		storedpointsB = new ArrayList<Vector2f>();
		addPoint(storedpointsA, contactA, lastdistA);
		addPoint(storedpointsB, contactB, lastdistB);
	}

	public void add(ContactManifold<Vector2f> cm) {
		penetrationdepth = cm.getPenetrationDepth();
		lastdistA = addPoint(storedpointsA, cm.getContactPointA(), lastdistA);
		lastdistB = addPoint(storedpointsB, cm.getContactPointB(), lastdistB);
		contactA = computeCenter(storedpointsA);
		contactB = computeCenter(storedpointsB);
		relativecontactA = VecMath.subtraction(contactA, getObjects()
				.getFirst().getTranslation2());
		relativecontactB = VecMath.subtraction(contactB, getObjects()
				.getSecond().getTranslation2());
		collisionnormal = cm.getCollisionNormal();
	}

	private float addPoint(List<Vector2f> list, Vector2f point, float lastdist) {
		if (list.size() == 2) {
			float dist = VecMath
					.length(VecMath.subtraction(point, list.get(1)));
			if (dist > lastdist) {
				list.add(point);
				lastdist = dist;
				list.remove(0);
			}
		} else {
			if (list.size() == 1) {
				list.add(point);
				lastdist = VecMath.length(VecMath.subtraction(point,
						list.get(1)));
			} else
				list.add(point);
		}
		return lastdist;
	}

	public void clear() {
		storedpointsA.clear();
		storedpointsB.clear();
	}

	private Vector2f computeCenter(List<Vector2f> list) {
		if (list.size() == 2) {
			return VecMath.scale(VecMath.addition(list.get(0), list.get(1)),
					0.5f);
		}
		// size == 1
		return list.get(0);
	}
}