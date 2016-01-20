package narrowphase;

import java.util.List;

import manifold.ContactManifold;
import objects.SupportMap;
import vector.Vector3f;

public class EmptyManifoldGenerator implements ManifoldGenerator<Vector3f> {

	@Override
	public ContactManifold<Vector3f> computeCollision(SupportMap<Vector3f> Sa, SupportMap<Vector3f> Sb,
			List<Vector3f> simplex) {
		return new ContactManifold<Vector3f>();
	}
}
