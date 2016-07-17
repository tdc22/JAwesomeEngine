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

	final List<ObjectType> objects;

	final List<Pair<ObjectType, ObjectType>> overlaps;

	final Map<Pair<ObjectType, ObjectType>, Counter> counters;

	final List<BroadphaseListener<L, ObjectType>> listeners;

	public SweepAndPrune() {
		objects = new ArrayList<ObjectType>();
		overlaps = new ArrayList<Pair<ObjectType, ObjectType>>();
		counters = new LinkedHashMap<Pair<ObjectType, ObjectType>, Counter>();
		listeners = new ArrayList<BroadphaseListener<L, ObjectType>>();
	}

	public void addListener(BroadphaseListener<L, ObjectType> listener) {
		listeners.add(listener);
	}

	public void removeListener(BroadphaseListener<L, ObjectType> listener) {
		listeners.remove(listener);
	}

	@Override
	public Set<Pair<ObjectType, ObjectType>> getOverlaps() {
		return new LinkedHashSet<Pair<ObjectType, ObjectType>>(overlaps);
	}

	public List<ObjectType> getObjects() {
		return objects;
	}

	public boolean contains(ObjectType obj) {
		return objects.contains(obj);
	}

	final Pair<ObjectType, ObjectType> tempPair = new Pair<ObjectType, ObjectType>(null, null);

	protected void sortAxis(List<SweepPoint> axis) {
		for (int j = 1; j < axis.size(); j++) {
			SweepPoint keyelement = axis.get(j);
			float key = keyelement.value();

			int i = j - 1;

			while (i >= 0 && axis.get(i).value() > key) {
				SweepPoint swapper = axis.get(i);

				if (keyelement.begin && !swapper.begin) {
					tempPair.set(keyelement.object, swapper.object);
					if (counters.containsKey(tempPair)) {
						counters.get(tempPair).overlaps++;
					} else {
						Counter counter = new Counter();
						counter.overlaps = 1;
						counters.put(new Pair<ObjectType, ObjectType>(tempPair), counter);
					}
				} else if (!keyelement.begin && swapper.begin) {
					tempPair.set(keyelement.object, swapper.object);
					if (counters.containsKey(tempPair)) {
						counters.get(tempPair).overlaps--;
					}
				}

				axis.set(i + 1, swapper);
				i--;
			}
			axis.set(i + 1, keyelement);
		}
	}
}