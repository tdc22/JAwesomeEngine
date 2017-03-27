package narrowphase;

import java.util.ArrayList;
import java.util.List;

import manifold.RaycastResult;
import math.VecMath;
import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;

public class GinoRaycast2 implements RaycastNarrowphase<Vector2f> {
	private final int MAX_ITERATIONS_RAYCAST = 50;
	final float EPSILON = 0.001f;
	final float EPSILON_SQR = EPSILON * EPSILON;

	// source:
	// http://www.bulletphysics.com/ftp/pub/test/physics/papers/jgt04raycast.pdf
	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		float lambda = 0;
		Vector2f x = new Vector2f(ray.getPosition());
		Vector2f n = new Vector2f();
		Vector2f v = VecMath.subtraction(x, Sa.supportPoint(ray.getDirection()));
		List<Vector2f> P = new ArrayList<Vector2f>();

		for (int i = 0; i < MAX_ITERATIONS_RAYCAST; i++) {
			if (v.lengthSquared() > EPSILON_SQR) {
				return true;
			}

			Vector2f p = Sa.supportPoint(v);
			Vector2f w = VecMath.subtraction(x, p);
			if (VecMath.dotproduct(v, w) > 0) {
				if (VecMath.dotproduct(v, ray.getDirection()) >= 0) {
					return false;
				}
				lambda -= VecMath.dotproduct(v, w) / VecMath.dotproduct(v, ray.getDirection());
				x = ray.getPosition();
				x.translate(VecMath.scale(ray.getDirection(), lambda));
				n = v;
			}

			// Y <- P u {p}
			List<Vector2f> Y = new ArrayList<Vector2f>(P);
			Y.add(p);
			// v <- v(conv({x} - Y))

			// P <- "smalles X <= Y such that v e conv({x] - X)

		}

		return false;
	}

	private float computeCollisionOnRay(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RaycastResult<Vector2f> computeCollision(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// TODO Auto-generated method stub
		return null;
	}

}
