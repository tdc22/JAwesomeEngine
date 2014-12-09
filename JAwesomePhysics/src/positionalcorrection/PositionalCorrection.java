package positionalcorrection;

import manifold.CollisionManifold;
import vector.Vector2f;
import vector.Vector3f;

public interface PositionalCorrection {
	public abstract void correct(CollisionManifold<Vector3f> manifold);

	public abstract void correct2(CollisionManifold<Vector2f> manifold);
}
