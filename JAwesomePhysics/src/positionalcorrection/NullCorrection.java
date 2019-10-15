package positionalcorrection;

import manifold.CollisionManifold;
import vector.Vector2f;
import vector.Vector3f;

public class NullCorrection implements PositionalCorrection {

	@Override
	public void correct(CollisionManifold<Vector3f, ?> manifold) {
	}

	@Override
	public void correct2(CollisionManifold<Vector2f, ?> manifold) {
	}
}
