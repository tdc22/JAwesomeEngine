package objects;

import vector.Vector;

public interface SupportMap<L extends Vector> {
	public L supportPoint(L direction);

	public L supportPointLocal(L direction);

	public L supportPointLocalNegative(L direction);

	public L supportPointNegative(L direction);

	public L supportPointRelative(L direction);

	public L supportPointRelativeNegative(L direction);

	public boolean isCompound();
}