package narrowphase;

import java.util.ArrayList;
import java.util.List;

import manifold.ContactManifold;
import math.VecMath;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class EPA2 implements ManifoldGenerator<Vector2f> {
	private class Edge {
		Vector2f a, b, normal;
		float distance;

		public Edge(Vector2f a, Vector2f b) {
			this.a = a;
			this.b = b;

			normal = new Vector2f(a.y - b.y, b.x - a.x);
			if (VecMath.dotproduct(normal, a) < 0)
				normal.negate();

			if (normal.lengthSquared() > 0)
				normal.normalize();
		}
	}

	private final List<Edge> edges;
	private final float TOLERANCE = 0.001f;
	private final float EPSILON = 0;// 0.1f;
	private final int MAX_ITERATIONS = 50;

	public EPA2() {
		edges = new ArrayList<Edge>();
	}

	private final Vector3f tmpCross = new Vector3f();
	private final Vector2f negnormal = new Vector2f();

	@Override
	public ContactManifold<Vector2f> computeCollision(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb,
			List<Vector2f> simplex) {
		edges.clear();

		Vector2f A = simplex.get(0);
		Vector2f B = simplex.get(1);
		Vector2f C = simplex.get(2);

		edges.add(new Edge(A, B));
		edges.add(new Edge(B, C));
		edges.add(new Edge(C, A));

		Vector2f normal = new Vector2f();
		float depth = 0;
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			Edge e = findClosestEdge(edges);
			if (isOriginInsideEdgeArea(e)) {
				Vector2f p = support(Sa, Sb, e.normal);
				double d = VecMath.dotproduct(p, e.normal);
				if (d - e.distance < TOLERANCE) {
					normal = e.normal;
					depth = (float) d;
					break;
				} else {
					edges.add(new Edge(e.a, p));
					edges.add(new Edge(p, e.b));
				}
			}
			edges.remove(e);
		}

		if (normal.lengthSquared() == 0)
			return null;

		VecMath.crossproduct(normal.x, normal.y, 0, 0, 0, 1, tmpCross);
		Vector2f tangentA = new Vector2f(tmpCross.x, tmpCross.y);
		Vector2f tangentB = VecMath.negate(tangentA);

		VecMath.negate(normal, negnormal);
		return new ContactManifold<Vector2f>(depth, normal, Sa.supportPoint(normal), Sb.supportPoint(negnormal),
				Sa.supportPointRelative(normal), Sb.supportPointRelative(normal), Sa.supportPointLocal(normal),
				Sb.supportPointLocal(negnormal), tangentA, tangentB);
	}

	private boolean isOriginInsideEdgeArea(Edge e) {
		return (checkEdge(e.a, e.normal) && checkEdge(e.b, e.normal));
	}

	private boolean checkEdge(Vector2f a, Vector2f normal) {
		return (-a.x * normal.x + -a.y * normal.y <= EPSILON);
	}

	/**
	 * Finds the closest feature to the origin on the Minkowski Difference.
	 */
	private Edge findClosestEdge(List<Edge> edges) {
		Edge closest = null;
		float distance = Float.MAX_VALUE;
		for (Edge e : edges) {
			float dist = VecMath.dotproduct(e.normal, e.a);
			if (dist < distance) {
				closest = e;
				distance = dist;
				e.distance = distance;
			}
		}
		return closest;
	}

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb, Vector2f dir) {
		Vector2f suppA = Sa.supportPoint(dir);
		Vector2f suppB = Sb.supportPointNegative(dir);
		suppB.negate();
		suppA.translate(suppB);
		return suppA;
	}
}