package narrowphase;

import manifold.ContactManifold;
import objects.SupportMap;
import vector.Vector;

public interface Narrowphase<L extends Vector> {
	public boolean isColliding(SupportMap<L> Sa, SupportMap<L> Sb);

	public ContactManifold<L> computeCollision(SupportMap<L> Sa,
			SupportMap<L> Sb);
}