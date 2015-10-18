package curves;

import vector.Vector;

public abstract class BezierCurve<L extends Vector> extends Curve<L> {
	L p0, p1, p2, p3;

	public BezierCurve(L p0, L p1, L p2, L p3) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	public L getP0() {
		return p0;
	}

	public L getP1() {
		return p1;
	}

	public L getP2() {
		return p2;
	}

	public L getP3() {
		return p3;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("BezierCurve[");
		sb.append(p0);
		sb.append(", ");
		sb.append(p1);
		sb.append(", ");
		sb.append(p2);
		sb.append(", ");
		sb.append(p3);
		sb.append(']');
		return sb.toString();
	}
}
