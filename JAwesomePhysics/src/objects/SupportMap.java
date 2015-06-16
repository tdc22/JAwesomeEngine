package objects;

import java.util.List;

import vector.Vector;

public interface SupportMap<L extends Vector> {
	public L supportPoint(L direction);

	public L supportPointLocal(L direction);

	public L supportPointLocalNegative(L direction);

	public L supportPointNegative(L direction);

	public L supportPointRelative(L direction);

	public L supportPointRelativeNegative(L direction);

	public List<L> supportPointList(L direction);

	public List<L> supportPointLocalList(L direction);

	public List<L> supportPointLocalNegativeList(L direction);

	public List<L> supportPointNegativeList(L direction);

	public List<L> supportPointRelativeList(L direction);

	public List<L> supportPointRelativeNegativeList(L direction);

	public boolean hasMultipleSupportPoints();
}
