package curves;

import vector.Vector;

public abstract class Curve<L extends Vector> {
	public abstract L getPoint(float t);

	public abstract void setStartPoint(L startpoint);
}
