package narrowphase;

import java.util.List;

import manifold.ContactManifold;
import objects.SupportMap;
import vector.Vector;

public abstract class GilbertJohnsonKeerthi<L extends Vector> implements
		Narrowphase<L> {
	List<L> simplex;
	L direction;
	ManifoldGenerator<L> manifoldgeneration;

	public GilbertJohnsonKeerthi(ManifoldGenerator<L> manifoldgeneration) {
		this.manifoldgeneration = manifoldgeneration;
	}

	@Override
	public ContactManifold<L> computeCollision(SupportMap<L> Sa,
			SupportMap<L> Sb) {
		return manifoldgeneration.computeCollision(Sa, Sb, simplex);
	}

	public List<L> getSimplex() {
		return simplex;
	}
}
