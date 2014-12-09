package narrowphase;

import java.util.List;

import manifold.ContactManifold;
import objects.SupportMap;
import vector.Vector;

public interface ManifoldGenerator<L extends Vector> {
	public abstract ContactManifold<L> computeCollision(SupportMap<L> Sa,
			SupportMap<L> Sb, List<L> simplex);
}
