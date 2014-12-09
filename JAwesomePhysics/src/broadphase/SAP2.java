package broadphase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import objects.RigidBody;
import utils.Pair;
import vector.Vector2f;

public class SAP2 extends SweepAndPrune<Vector2f> {
	List<SweepPoint> axisX, axisY;

	public SAP2() {
		axisX = new ArrayList<SweepPoint>();
		axisY = new ArrayList<SweepPoint>();
	}

	@Override
	public void add(RigidBody<Vector2f, ?, ?, ?> object) {
		objects.add(object);
		axisX.add(new SweepPoint(object, true, 0));
		axisX.add(new SweepPoint(object, false, 0));
		axisY.add(new SweepPoint(object, true, 1));
		axisY.add(new SweepPoint(object, false, 1));
	}

	@Override
	public Set<RigidBody<Vector2f, ?, ?, ?>> raycast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(RigidBody<Vector2f, ?, ?, ?> object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		sortAxis(axisX);
		sortAxis(axisY);

		Iterator<Entry<Pair<RigidBody<Vector2f, ?, ?, ?>, RigidBody<Vector2f, ?, ?, ?>>, Counter>> iter = counters
				.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Pair<RigidBody<Vector2f, ?, ?, ?>, RigidBody<Vector2f, ?, ?, ?>>, Counter> entry = iter
					.next();
			Counter c = entry.getValue();
			Pair<RigidBody<Vector2f, ?, ?, ?>, RigidBody<Vector2f, ?, ?, ?>> pair = entry
					.getKey();

			if (c.wasOverlapping) {
				// report separation
				if (c.overlaps < 2) {
					overlaps.remove(pair);
					c.wasOverlapping = false;
				}
			} else {
				// report overlap
				if (c.overlaps > 1) {
					overlaps.add(pair);
					c.wasOverlapping = true;
				}
			}

			if (c.overlaps < 1) {
				iter.remove();
			}
		}

	}
}