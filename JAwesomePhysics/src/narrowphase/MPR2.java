package narrowphase;

import java.util.List;

import manifold.ContactManifold;
import objects.SupportMap;
import vector.Vector2f;

public class MPR2 extends MinkowskiPortalRefinement<Vector2f> {

	@Override
	public ContactManifold<Vector2f> computeCollision(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContactManifold<Vector2f> computeCollision(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb,
			List<Vector2f> simplex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb) {
		// 1b
		// Vector2f deep =
		return false;
	}
}
