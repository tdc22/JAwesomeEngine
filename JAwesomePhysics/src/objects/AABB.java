package objects;

import vector.Vector;

public class AABB<L extends Vector> {
	L min, max;

	public AABB(L min, L max) {
		this.min = min;
		this.max = max;
	}

	public L getMax() {
		return max;
	}

	public L getMin() {
		return min;
	}

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
}
