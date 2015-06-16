package objects;

import java.util.List;

import vector.Vector;

public interface SupportCalculator<L extends Vector> {
	public L supportPointLocal(L direction);

	public L supportPointLocalNegative(L direction);

	public List<L> supportPointLocalList(L direction);

	public List<L> supportPointLocalNegativeList(L direction);

	public boolean hasMultipleSupportPoints();
}