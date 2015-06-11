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

		public ConvexSupport(
				CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);

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
			Vector3f v = QuatMath.transform(
					collisionshape.getInverseRotation(), direction);

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
	}

	List<Vector3f> vertices;
	HashMap<Integer, Integer[]> adjacentsMap;

	public ConvexShape(float x, float y, float z, List<Vector3f> vertices,
			HashMap<Integer, Integer[]> adjacentsMap) {
		super();
		translate(x, y, z);
		this.vertices = vertices;
		this.adjacentsMap = adjacentsMap;
		init();
	}

	public ConvexShape(Vector3f pos, List<Vector3f> vertices,
			HashMap<Integer, Integer[]> adjacentsMap) {
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
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new ConvexSupport(cs);
	}

	private void init() {
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float minZ = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		float maxZ = -Float.MAX_VALUE;
		for (Vector3f v : vertices) {
			if (v.x < minX)
				minX = v.x;
			if (v.y < minY)
				minY = v.y;
			if (v.z < minZ)
				minZ = v.z;
			if (v.x > maxX)
				maxX = v.x;
			if (v.y > maxY)
				maxY = v.y;
			if (v.z > maxZ)
				maxZ = v.z;
		}

		setAABB(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));
		supportcalculator = createSupportCalculator(this);
	}
}
