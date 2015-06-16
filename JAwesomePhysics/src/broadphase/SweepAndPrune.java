package broadphase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import objects.CollisionShape;
import utils.Pair;
import vector.Vector;

public abstract class SweepAndPrune<L extends Vector, ObjectType extends CollisionShape<L, ?, ?>>
		implements Broadphase<L, ObjectType> {
	protected final class Counter {
		public boolean wasOverlapping = false;
		public int overlaps;
	}

	protected class SweepPoint {
		public boolean begin;
		int axis;
		public ObjectType object;

		public SweepPoint(ObjectType obj, boolean begin, int axis) {
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

	List<ObjectType> objects;

	List<Pair<ObjectType, ObjectType>> overlaps;

	Map<Pair<ObjectType, ObjectType>, Counter> counters;

	public SweepAndPrune() {
		objects = new ArrayList<ObjectType>();
		overlaps = new ArrayList<Pair<ObjectType, ObjectType>>();
		counters = new LinkedHashMap<Pair<ObjectType, ObjectType>, Counter>();
	}

	@Override
	public Set<Pair<ObjectType, ObjectType>> getOverlaps() {
		return new LinkedHashSet<Pair<ObjectType, ObjectType>>(overlaps);
	}

	protected void sortAxis(List<SweepPoint> axis) {
		for (int j = 1; j < axis.size(); j++) {
			SweepPoint keyelement = axis.get(j);
			float key = keyelement.value();

			int i = j - 1;

			while (i >= 0 && axis.get(i).value() > key) {
				SweepPoint swapper = axis.get(i);

				if (keyelement.begin && !swapper.begin) {
					Pair<ObjectType, ObjectType> pair = new Pair<ObjectType, ObjectType>(
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
					Pair<ObjectType, ObjectType> pair = new Pair<ObjectType, ObjectType>(
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