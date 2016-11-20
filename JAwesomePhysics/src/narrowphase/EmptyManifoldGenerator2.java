package narrowphase;

import java.util.List;

import manifold.ContactManifold;
import objects.SupportMap;
import vector.Vector2f;

public class EmptyManifoldGenerator2 implements ManifoldGenerator<Vector2f> {

	@Override
	public ContactManifold<Vector2f> computeCollision(SupportMap<Vector2f> Sa,
			SupportMap<Vector2f> Sb, List<Vector2f> simplex) {
		return new ContactManifold<Vector2f>();
	}
}
