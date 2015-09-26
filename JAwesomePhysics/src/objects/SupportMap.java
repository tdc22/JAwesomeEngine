package objects;

import vector.Vector;

public interface SupportMap<L extends Vector> extends SupportCalculator<L> {
	public L supportPoint(L direction);

	public L supportPointNegative(L direction);

	public L supportPointRelative(L direction);

	public L supportPointRelativeNegative(L direction);
}