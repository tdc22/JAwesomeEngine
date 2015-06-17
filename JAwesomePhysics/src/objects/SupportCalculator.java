package objects;

import vector.Vector;

public interface SupportCalculator<L extends Vector> {
	public L supportPointLocal(L direction);

	public L supportPointLocalNegative(L direction);

	public boolean isCompound();
}