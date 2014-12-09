package resolution;

import manifold.CollisionManifold;
import vector.Vector2f;
import vector.Vector3f;

public interface CollisionResolution {
	public abstract void resolve(CollisionManifold<Vector3f> manifold);

	public abstract void resolve2(CollisionManifold<Vector2f> manifold);
}
