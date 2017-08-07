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
					v.set(vertices.get(currentVertex));
					return v;
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
					v.set(vertices.get(currentVertex));
					return v;
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

		setAABB(new Vector3f(-maxLength, -maxLength, -maxLength), new Vector3f(maxLength, maxLength, maxLength));
		supportcalculator = createSupportCalculator(this);
	}
}
