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
}
