package broadphase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import objects.RigidBody;
import utils.Pair;
import vector.Vector3f;

public class SAP extends SweepAndPrune<Vector3f> {
	List<SweepPoint> axisX, axisY, axisZ;

	public SAP() {
		axisX = new ArrayList<SweepPoint>();
		axisY = new ArrayList<SweepPoint>();
		axisZ = new ArrayList<SweepPoint>();
	}

	@Override
	public void add(RigidBody<Vector3f, ?, ?, ?> object) {
		objects.add(object);
		axisX.add(new SweepPoint(object, true, 0));
		axisX.add(new SweepPoint(object, false, 0));
		axisY.add(new SweepPoint(object, true, 1));
		axisY.add(new SweepPoint(object, false, 1));
		axisZ.add(new SweepPoint(object, true, 2));
		axisZ.add(new SweepPoint(object, false, 2));
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
		sortAxis(axisX);
		sortAxis(axisY);
		sortAxis(axisZ);

		Iterator<Entry<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>, Counter>> iter = counters
				.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>, Counter> entry = iter
					.next();
			Counter c = entry.getValue();
			Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>> pair = entry
					.getKey();

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