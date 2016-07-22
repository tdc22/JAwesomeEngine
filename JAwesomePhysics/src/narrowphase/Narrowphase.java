package narrowphase;

import manifold.ContactManifold;
import objects.Ray;
import objects.SupportMap;
import vector.Vector;

public interface Narrowphase<L extends Vector> {
	public boolean isColliding(SupportMap<L> Sa, SupportMap<L> Sb);

	public boolean isColliding(SupportMap<L> Sa, Ray<L> ray);

	public ContactManifold<L> computeCollision(SupportMap<L> Sa, SupportMap<L> Sb);

	public L computeCollision(SupportMap<L> Sa, Ray<L> ray);
}