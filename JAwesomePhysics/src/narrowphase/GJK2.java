package narrowphase;

import math.VecMath;
import objects.SupportMap;
import vector.Vector2f;

public class GJK2 extends GilbertJohnsonKeerthi<Vector2f> {
	private final int MAX_ITERATIONS = 50;

	public GJK2(ManifoldGenerator<Vector2f> manifoldgeneration) {
		super(manifoldgeneration, 3);
	}

	final Vector2f startdirection = new Vector2f(1, 1);

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb) {
		simplex.clear();
		// S = Support(?)
		direction = support(Sa, Sb, startdirection);
		// [] = S
		simplex.add(direction);
		// D = -S
		direction = VecMath.negate(direction);
		// Loop:
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			// A = Support(D)
			Vector2f a = support(Sa, Sb, direction);
			// if AtD < 0 No Intersection
			if (VecMath.dotproduct(a, direction) < 0)
				return false;
			// [] += A
			simplex.add(a);
			// if DoSimplex([], D) Intersection
			if (GJK2Util.doSimplex(simplex, direction))
				return true;
		}
		return false;
	}

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb, Vector2f dir) {
		Vector2f suppA = Sa.supportPoint(dir);
		Vector2f suppB = Sb.supportPointNegative(dir);
		suppB.negate();
		suppA.translate(suppB);
		return suppA;
	}
}