package narrowphase;

import java.util.List;

import manifold.ContactManifold;
import math.VecMath;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class EPA2 implements ManifoldGenerator<Vector2f> {
	private class Edge {
		Vector2f normal;
		float distance;
		int index;

		public Edge() {
			normal = new Vector2f();
		}
	}

	private final float TOLERANCE = 0.05f;
	private final int MAX_ITERATIONS = 50;

	@Override
	public ContactManifold<Vector2f> computeCollision(SupportMap<Vector2f> Sa,
			SupportMap<Vector2f> Sb, List<Vector2f> simplex) {
		Vector2f normal = new Vector2f();
		float depth = 0;
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			Edge e = findClosestEdge(simplex);
			Vector2f p = support(Sa, Sb, e.normal);
			double d = VecMath.dotproduct(p, e.normal);
			if (d - e.distance < TOLERANCE) {
				normal = e.normal;
				depth = (float) d;
				break;
			} else {
				simplex.add(e.index, p);
			}
		}

		if (normal.lengthSquared() == 0)
			return null;

		Vector3f tmp = VecMath.crossproduct(new Vector3f(normal), new Vector3f(
				0, 0, 1));
		Vector2f tangentA = new Vector2f(tmp.x, tmp.y);
		Vector2f tangentB = VecMath.negate(tangentA);

		Vector2f negnormal = VecMath.negate(normal);
		return new ContactManifold<Vector2f>(depth, normal,
				Sa.supportPoint(normal), Sb.supportPoint(negnormal),
				Sa.supportPointRelative(normal),
				Sb.supportPointRelative(normal), Sa.supportPointLocal(normal),
				Sb.supportPointLocal(negnormal), tangentA, tangentB);
	}

	private Vector2f edgeDirection(Vector2f edge, Vector2f origin) {
		Vector2f a = new Vector2f(-edge.y, edge.x);
		if (VecMath.dotproduct(a, origin) > 0)
			return a;
		a.negate();
		return a;
	}

	/**
	 * Finds the closest feature to the origin on the Minkowski Difference.
	 */
	private Edge findClosestEdge(List<Vector2f> simplex) {
		Edge closest = new Edge();
		closest.distance = Float.MAX_VALUE;
		for (int i = 0; i < simplex.size(); i++) {
			int j = i + 1 == simplex.size() ? 0 : i + 1;
			Vector2f a = simplex.get(i);
			Vector2f b = simplex.get(j);
			Vector2f e = VecMath.subtraction(b, a);
			Vector2f n = edgeDirection(e, a);
			n.normalize(); // TODO: Zero length vector
			double d = VecMath.dotproduct(n, a);
			if (d < closest.distance) {
				closest.distance = (float) d;
				closest.normal = n;
				closest.index = j;
			}
		}
		return closest;
	}

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb,
			Vector2f dir) {
		return VecMath.subtraction(Sa.supportPoint(dir),
				Sb.supportPointNegative(dir));
	}
}