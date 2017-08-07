package collisionshape;

import java.util.HashMap;
import java.util.List;

import math.QuatMath;
import math.VecMath;
import objects.CollisionShape;
import objects.CollisionShape3;
import objects.SupportCalculator;
import quaternion.Quaternionf;
import vector.Vector3f;

public class ConvexShape extends CollisionShape3 {
	protected class ConvexSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf, Quaternionf> collisionshape;

		public ConvexSupport(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);

			int lastVertex = 0;
			int currentVertex = 0;
			float bestDot = VecMath.dotproduct(vertices.get(currentVertex), v);
			while (true) {
				Integer[] adjIndices = adjacentsMap.get(currentVertex);
				for (Integer i : adjIndices) {
					float currentDot = VecMath.dotproduct(vertices.get(i), v);
					if (currentDot > bestDot) {
						bestDot = currentDot;
						currentVertex = i;
					}
				}
				if (currentVertex != lastVertex) {
					lastVertex = currentVertex;
				} else {
					return vertices.get(currentVertex);
				}
			}
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);

			int lastVertex = 0;
			int currentVertex = 0;
			float bestDot = VecMath.dotproduct(vertices.get(currentVertex), v);
			while (true) {
				Integer[] adjIndices = adjacentsMap.get(currentVertex);
				for (Integer i : adjIndices) {
					float currentDot = VecMath.dotproduct(vertices.get(i), v);
					if (currentDot < bestDot) {
						bestDot = currentDot;
						currentVertex = i;
					}
				}
				if (currentVertex != lastVertex) {
					lastVertex = currentVertex;
				} else {
					return vertices.get(currentVertex);
				}
			}
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	List<Vector3f> vertices;
	HashMap<Integer, Integer[]> adjacentsMap;

	public ConvexShape(float x, float y, float z, List<Vector3f> vertices, HashMap<Integer, Integer[]> adjacentsMap) {
		super();
		translate(x, y, z);
		this.vertices = vertices;
		this.adjacentsMap = adjacentsMap;
		init();
	}

	public ConvexShape(Vector3f pos, List<Vector3f> vertices, HashMap<Integer, Integer[]> adjacentsMap) {
		super();
		translate(pos);
		this.vertices = vertices;
		this.adjacentsMap = adjacentsMap;
		init();
	}

	public List<Vector3f> getVertices() {
		return vertices;
	}

	public HashMap<Integer, Integer[]> getAdjacentsMap() {
		return adjacentsMap;
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new ConvexSupport(cs);
	}

	private void init() {
		Vector3f min = new Vector3f();
		Vector3f max = new Vector3f();
		VecMath.minMaxVectors(vertices, min, max);
		Vector3f center = VecMath.addition(min, VecMath.scale(VecMath.subtraction(max, min), 0.5f));
		float maxLength = 0;
		for (Vector3f v : vertices) {
			float l = (float) VecMath.subtraction(v, center).lengthSquared();
			if (l > maxLength)
				maxLength = l;
		}
		maxLength = (float) Math.sqrt(maxLength);

		// TODO: either include rotation center in aabb calculation for broadphase or
		// translate all vertices here instead of setting the rotation center
		// TODO: translate vertices, then translate object in negative direction
		// TODO: do same in 2d convexshape
		setRotationCenter(center);
		/* Wir wollen hier doch nicht die vertices verschieben, da dies zu ungewünschten Effekten führen kann.
		 * Beispielsweise könnte ein nutzer hier ein Objekt reinladen und von einer gewissen Position ausgehen,
		 * daraufhin die Position absolut setzen. Dann ist hier die Ausrichtung futsch.
		 * Stattdessen soll hier das Rotation-Center reaktiviert werden. Das muss auch beim Rendering wieder
		 * aktiviert werden. Der Performance-Penalty ist vernachlässigbar. Auch bei der Berechnung der AABB-Maxima/
		 * Minima soll dieser als referenzwert verwendet werden.
		 * Außerdem sollte ein entsprechender rendering-test für das rotationcenter angelegt werden. (TransRot) */
		setAABB(new Vector3f(-maxLength, -maxLength, -maxLength), new Vector3f(maxLength, maxLength, maxLength));
		System.out.println("Center: " + center + "; " + maxLength);
		supportcalculator = createSupportCalculator(this);
	}
}
