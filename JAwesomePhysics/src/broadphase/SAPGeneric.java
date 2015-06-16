package broadphase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import objects.CollisionShape;
import utils.Pair;
import vector.Vector3f;

public class SAPGeneric<ObjectType extends CollisionShape<Vector3f, ?, ?>>
		extends SweepAndPrune<Vector3f, ObjectType> {
	List<SweepPoint> axisX, axisY, axisZ;

	public SAPGeneric() {
		axisX = new ArrayList<SweepPoint>();
		axisY = new ArrayList<SweepPoint>();
		axisZ = new ArrayList<SweepPoint>();
	}

	@Override
	public void add(ObjectType object) {
		objects.add(object);
		axisX.add(new SweepPoint(object, true, 0));
		axisX.add(new SweepPoint(object, false, 0));
		axisY.add(new SweepPoint(object, true, 1));
		axisY.add(new SweepPoint(object, false, 1));
		axisZ.add(new SweepPoint(object, true, 2));
		axisZ.add(new SweepPoint(object, false, 2));
	}

	@Override
	public Set<ObjectType> raycast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(ObjectType object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		sortAxis(axisX);
		sortAxis(axisY);
		sortAxis(axisZ);

		Iterator<Entry<Pair<ObjectType, ObjectType>, Counter>> iter = counters
				.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Pair<ObjectType, ObjectType>, Counter> entry = iter.next();
			Counter c = entry.getValue();
			Pair<ObjectType, ObjectType> pair = entry.getKey();

			if (c.wasOverlapping) {
				// report separation
				if (c.overlaps < 3) {
					overlaps.remove(pair);
					c.wasOverlapping = false;

					// notify handlers
					// for (Handler h: handlers) {
					// h.separation(pair);
					// }
				}
			} else {
				// report overlap
				if (c.overlaps > 2) {
					overlaps.add(pair);
					c.wasOverlapping = true;

					// notify handlers
					// for (Handler h: handlers) {
					// h.overlap(pair);
					// }

				}
			}

			if (c.overlaps < 1) {
				iter.remove();
			}
		}
	}
}