package collisionshape2d;

import java.util.HashMap;
import java.util.List;

import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import objects.CollisionShape;
import objects.CollisionShape2;
import objects.SupportCalculator;
import quaternion.Complexf;
import vector.Vector2f;

public class ConvexShape2 extends CollisionShape2 {
	protected class ConvexSupport2 implements SupportCalculator<Vector2f> {
		private CollisionShape<Vector2f, Complexf, Matrix1f> collisionshape;

		public ConvexSupport2(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector2f supportPointLocal(Vector2f direction) {
			Vector2f v = ComplexMath.transform(
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
		public Vector2f supportPointLocalNegative(Vector2f direction) {
			Vector2f v = ComplexMath.transform(
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

	List<Vector2f> vertices;
	HashMap<Integer, Integer[]> adjacentsMap;

	public ConvexShape2(float x, float y, List<Vector2f> vertices,
			HashMap<Integer, Integer[]> adjacentsMap) {
		super();
		translate(x, y);
		this.vertices = vertices;
		this.adjacentsMap = adjacentsMap;
		init();
	}

	public ConvexShape2(Vector2f pos, List<Vector2f> vertices,
			HashMap<Integer, Integer[]> adjacentsMap) {
		super();
		translate(pos);
		this.vertices = vertices;
		this.adjacentsMap = adjacentsMap;
		init();
	}

	public List<Vector2f> getVertices() {
		return vertices;
	}

	public HashMap<Integer, Integer[]> getAdjacentsMap() {
		return adjacentsMap;
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(
			CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
		return new ConvexSupport2(cs);
	}

	private void init() {
		float maxLength = 0;
		for (Vector2f v : vertices) {
			float l = (float) v.lengthSquared();
			if (l > maxLength)
				maxLength = l;
		}
		maxLength = (float) Math.sqrt(maxLength);

		setAABB(new Vector2f(-maxLength, -maxLength), new Vector2f(maxLength,
				maxLength));
		supportcalculator = createSupportCalculator(this);
	}
}
