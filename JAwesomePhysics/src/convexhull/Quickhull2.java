package convexhull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vector.Vector2f;
import collisionshape2d.ConvexShape2;

public class Quickhull2 {
	// source:
	// http://www.sanfoundry.com/java-program-implement-quick-hull-algorithm-find-convex-hull/
	public static ConvexShape2 computeConvexHull(List<Vector2f> points) {
		List<Vector2f> vertices = computeConvexHullVertices(points);

		HashMap<Integer, Integer[]> adjacentsMap = new HashMap<Integer, Integer[]>();
		for (int i = 0; i < vertices.size(); i++) {
			Integer[] adjs = new Integer[2];
			if (i == 0)
				adjs[0] = vertices.size() - 1;
			else
				adjs[0] = i - 1;
			if (i == vertices.size() - 1)
				adjs[1] = 0;
			else
				adjs[1] = i + 1;
			adjacentsMap.put(i, adjs);
		}

		ConvexShape2 shape = new ConvexShape2(0, 0, vertices, adjacentsMap);
		return shape;
	}

	public static List<Vector2f> computeConvexHullVertices(List<Vector2f> points) {
		points = new ArrayList<Vector2f>(points);
		List<Vector2f> vertices = new ArrayList<Vector2f>();

		if (points.size() > 0) {
			int minPointXID = 0;
			int maxPointXID = 0;

			float minX = points.get(0).x;
			float maxX = points.get(0).x;

			for (int i = 1; i < points.size(); i++) {
				if (points.get(i).x < minX) {
					minX = points.get(i).x;
					minPointXID = i;
				}
				if (points.get(i).x > maxX) {
					maxX = points.get(i).x;
					maxPointXID = i;
				}
			}

			Vector2f A = points.get(minPointXID);
			Vector2f B = points.get(maxPointXID);
			vertices.add(A);
			vertices.add(B);
			points.remove(A);
			points.remove(B);

			List<Vector2f> leftSet = new ArrayList<Vector2f>();
			List<Vector2f> rightSet = new ArrayList<Vector2f>();

			for (Vector2f p : points) {
				if (pointLocation(A, B, p) == -1)
					leftSet.add(p);
				else if (pointLocation(A, B, p) == 1)
					rightSet.add(p);
			}
			hullSet(A, B, rightSet, vertices);
			hullSet(B, A, leftSet, vertices);
		}
		return vertices;
	}

	private static float distance(Vector2f A, Vector2f B, Vector2f P) {
		// |(B - A) x (A - P)|
		return Math.abs((B.x - A.x) * (A.y - P.y) - (B.y - A.y) * (A.x - P.x));
	}

	private static int pointLocation(Vector2f A, Vector2f B, Vector2f P) {
		float d = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
		if (d > 0)
			return 1;
		else if (d == 0)
			return 0;
		else
			return -1;
	}

	private static void hullSet(Vector2f A, Vector2f B, List<Vector2f> set,
			List<Vector2f> hull) {
		int insertPosition = hull.indexOf(B);
		if (set.size() == 0)
			return;
		if (set.size() == 1) {
			Vector2f p = set.get(0);
			set.remove(p);
			hull.add(insertPosition, p);
			return;
		}
		float dist = Float.MIN_VALUE;
		int furthestPoint = -1;
		for (int i = 0; i < set.size(); i++) {
			Vector2f p = set.get(i);
			float distance = distance(A, B, p);
			if (distance > dist) {
				dist = distance;
				furthestPoint = i;
			}
		}
		Vector2f P = set.get(furthestPoint);
		set.remove(furthestPoint);
		hull.add(insertPosition, P);

		ArrayList<Vector2f> leftSetAP = new ArrayList<Vector2f>();
		ArrayList<Vector2f> leftSetPB = new ArrayList<Vector2f>();
		for (Vector2f M : set) {
			if (pointLocation(A, P, M) == 1) {
				leftSetAP.add(M);
			}
			if (pointLocation(P, B, M) == 1) {
				leftSetPB.add(M);
			}
		}

		hullSet(A, P, leftSetAP, hull);
		hullSet(P, B, leftSetPB, hull);
	}
}