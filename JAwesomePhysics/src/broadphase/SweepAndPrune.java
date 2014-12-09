package broadphase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import objects.RigidBody;
import utils.Pair;
import vector.Vector;

public abstract class SweepAndPrune<L extends Vector> implements Broadphase<L> {
	protected final class Counter {
		public boolean wasOverlapping = false;
		public int overlaps;
	}

	protected class SweepPoint {
		public boolean begin;
		int axis;
		public RigidBody<L, ?, ?, ?> object;

		public SweepPoint(RigidBody<L, ?, ?, ?> obj, boolean begin, int axis) {
			this.object = obj;
			this.begin = begin;
			this.axis = axis;
		}

		public float value() {
			if (begin) {
				return object.getGlobalMinAABB().getf(axis);
			}
			return object.getGlobalMaxAABB().getf(axis);
		}
	}

	List<RigidBody<L, ?, ?, ?>> objects;

	List<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> overlaps;

	Map<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>, Counter> counters;

	public SweepAndPrune() {
		objects = new ArrayList<RigidBody<L, ?, ?, ?>>();
		overlaps = new ArrayList<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>>();
		counters = new LinkedHashMap<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>, Counter>();
	}

	@Override
	public Set<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>> getOverlaps() {
		return new LinkedHashSet<Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>>(
				overlaps);
	}

	protected void sortAxis(List<SweepPoint> axis) {
		for (int j = 1; j < axis.size(); j++) {
			SweepPoint keyelement = axis.get(j);
			float key = keyelement.value();

			int i = j - 1;

			while (i >= 0 && axis.get(i).value() > key) {
				SweepPoint swapper = axis.get(i);

				if (keyelement.begin && !swapper.begin) {
					Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>> pair = new Pair<RigidBody<L, ?, ?, ?>, RigidBody<L, ?, ?, ?>>(
							keyelement.object, swapper.object);
					if (counters.containsKey(pair)) {
						counters.get(pair).overlaps++;
					} else {
						Counter counter = new Counter();
						counter.overlaps = 1;
						counters.put(pair, counter);
					}
				}

				if (!keyelement.begin && swapper.begin) {
					Pair<RigidBody<?, ?, ?, ?>, RigidBody<?, ?, ?, ?>> pair = new Pair<RigidBody<?, ?, ?, ?>, RigidBody<?, ?, ?, ?>>(
							keyelement.object, swapper.object);
					if (counters.containsKey(pair)) {
						counters.get(pair).overlaps--;
					}
				}

				axis.set(i + 1, swapper);
				i--;
			}
			axis.set(i + 1, keyelement);
		}
	}
}