package objects;

import vector.Vector;

public abstract class AABB<L extends Vector> {
	L min, max;

	public AABB(L min, L max) {
		this.min = min;
		this.max = max;
	}

	public abstract boolean contains(L point);

	public boolean contains(AABB<L> aabb) {
		return contains(aabb.getMin()) && contains(aabb.getMax());
	}

	public abstract float volume();

	public L getMax() {
		return max;
	}

	public L getMin() {
		return min;
	}

	public abstract boolean intersects(AABB<L> aabb);

	public void set(L min, L max) {
		this.min = min;
		this.max = max;
	}

	public void setMax(L max) {
		this.max = max;
	}

	public void setMin(L min) {
		this.min = min;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("AABB[");
		sb.append(min.toString());
		sb.append(", ");
		sb.append(max.toString());
		sb.append("]");
		return sb.toString();
	}

	public abstract AABB<L> union(AABB<L> aabb);
}
