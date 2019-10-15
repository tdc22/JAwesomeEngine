package resolution;

import manifold.CollisionManifold;
import vector.Vector2f;
import vector.Vector3f;

public class NullResolution implements CollisionResolution {

	@Override
	public void resolve(CollisionManifold<Vector3f, ?> manifold) {
	}

	@Override
	public void resolve2(CollisionManifold<Vector2f, ?> manifold) {
	}
}