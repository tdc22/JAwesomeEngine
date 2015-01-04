package manifold;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.RigidBody;
import utils.Pair;
import vector.Vector2f;

public class PersistentCollisionManifold2 extends CollisionManifold<Vector2f> {
	List<Vector2f> storedpointsA, storedpointsB;
	List<Integer> pointagesA, pointagesB;
	float lastdistA = 0, lastdistB = 0;
	int maxstoretime;
	float distthreshold;

	public PersistentCollisionManifold2(int maxstoretime, float distthreshold, CollisionManifold<Vector2f> cm) {
		super(cm);
		this.maxstoretime = maxstoretime;
		this.distthreshold = distthreshold;
		init();
		addPoint(storedpointsA, pointagesA, cm.getContactPointA(), lastdistA);
		addPoint(storedpointsB, pointagesB, cm.getContactPointB(), lastdistB);
	}

	public PersistentCollisionManifold2(int maxstoretime, float distthreshold, 
			Pair<RigidBody<Vector2f, ?, ?, ?>, RigidBody<Vector2f, ?, ?, ?>> objs,
			ContactManifold<Vector2f> manifold) {
		super(objs, manifold);
		this.maxstoretime = maxstoretime;
		this.distthreshold = distthreshold;
		init();
		addPoint(storedpointsA, pointagesA, manifold.getContactPointA(), lastdistA);
		addPoint(storedpointsB, pointagesB, manifold.getContactPointB(), lastdistB);
	}

	public PersistentCollisionManifold2(int maxstoretime, float distthreshold, RigidBody<Vector2f, ?, ?, ?> obj1,
			RigidBody<Vector2f, ?, ?, ?> obj2,
			ContactManifold<Vector2f> manifold) {
		super(obj1, obj2, manifold);
		this.maxstoretime = maxstoretime;
		this.distthreshold = distthreshold;
		init();
		addPoint(storedpointsA, pointagesA, manifold.getContactPointA(), lastdistA);
		addPoint(storedpointsB, pointagesB, manifold.getContactPointB(), lastdistB);
	}

	// TODO: Threshold for the dist so that spheres only have 1 contact point
	public PersistentCollisionManifold2(int maxstoretime, float distthreshold, RigidBody<Vector2f, ?, ?, ?> obj1,
			RigidBody<Vector2f, ?, ?, ?> obj2, float penetrationdepth,
			Vector2f collisionnormal, Vector2f contactA, Vector2f contactB,
			Vector2f relativecontactA, Vector2f relativecontactB,
			Vector2f localcontactA, Vector2f localcontactB, Vector2f tangentA,
			Vector2f tangentB) {
		super(obj1, obj2, penetrationdepth, collisionnormal, contactA,
				contactB, relativecontactA, relativecontactB, localcontactA,
				localcontactB, tangentA, tangentB);
		this.maxstoretime = maxstoretime;
		this.distthreshold = distthreshold;
		init();
		addPoint(storedpointsA, pointagesA, contactA, lastdistA);
		addPoint(storedpointsB, pointagesB, contactB, lastdistB);
	}
	
	private void init() {
		storedpointsA = new ArrayList<Vector2f>();
		storedpointsB = new ArrayList<Vector2f>();
		pointagesA = new ArrayList<Integer>();
		pointagesB = new ArrayList<Integer>();
	}

	public void add(ContactManifold<Vector2f> cm) {
		penetrationdepth = cm.getPenetrationDepth();
		lastdistA = addPoint(storedpointsA, pointagesA, cm.getContactPointA(), lastdistA);
		lastdistB = addPoint(storedpointsB, pointagesB, cm.getContactPointB(), lastdistB);
		contactA = computeCenter(storedpointsA);
		contactB = computeCenter(storedpointsB);
		relativecontactA = VecMath.subtraction(contactA, getObjects()
				.getFirst().getTranslation2());
		relativecontactB = VecMath.subtraction(contactB, getObjects()
				.getSecond().getTranslation2());
		collisionnormal = cm.getCollisionNormal();
	}
	
	public void increaseAge() {
		for(int i = pointagesA.size() - 1; i >= 0; i--) {
			int a = pointagesA.get(i) + 1;
			if(a > maxstoretime) {
				storedpointsA.remove(i);
				pointagesA.remove(i);
			}
			else
				pointagesA.set(i, a);
		}
		for(int i = pointagesB.size() - 1; i >= 0; i--) {
			int a = pointagesB.get(i) + 1;
			if(a > maxstoretime) {
				storedpointsB.remove(i);
				pointagesB.remove(i);
			}
			else
				pointagesB.set(i, a);
		}
	}

	private float addPoint(List<Vector2f> list, List<Integer> agelist, Vector2f point, float lastdist) {
		if (list.size() == 2) {
			float dist = VecMath
					.length(VecMath.subtraction(point, list.get(1)));
			if (dist > lastdist) {
				list.add(point);
				agelist.add(0);
				lastdist = dist;
				list.remove(0);
				agelist.remove(0);
			}
		} else {
			if (list.size() == 1) {
				lastdist = VecMath.length(VecMath.subtraction(point,
						list.get(0)));
				if(lastdist > distthreshold) {
					list.add(point);
					agelist.add(0);
				}
			} else {
				list.add(point);
				agelist.add(0);
			}
		}
		return lastdist;
	}

	public void clear() {
		storedpointsA.clear();
		storedpointsB.clear();
		pointagesA.clear();
		pointagesB.clear();
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