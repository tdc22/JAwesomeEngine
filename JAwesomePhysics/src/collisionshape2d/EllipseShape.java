package collisionshape2d;

import math.ComplexMath;
import matrix.Matrix1f;
import objects.CollisionShape;
import objects.CollisionShape2;
import objects.SupportCalculator;
import quaternion.Complexf;
import shapedata2d.EllipseStructure;
import vector.Vector2f;

public class EllipseShape extends CollisionShape2 implements EllipseStructure {
	protected class EllipseSupport implements SupportCalculator<Vector2f> {
		private CollisionShape<Vector2f, Complexf, Matrix1f> collisionshape;

		public EllipseSupport(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector2f supportPointLocal(Vector2f direction) {
			if (direction.lengthSquared() == 0)
				direction = new Vector2f(0, 1);
			direction.normalize();
			Vector2f v = ComplexMath.transform(collisionshape.getInverseRotation(), direction);
			return new Vector2f(v.x * radius, v.y * halfheight);
		}

		@Override
		public Vector2f supportPointLocalNegative(Vector2f direction) {
			if (direction.lengthSquared() == 0)
				direction = new Vector2f(0, 1);
			direction.normalize();
			Vector2f v = ComplexMath.transform(collisionshape.getInverseRotation(), direction);
			return new Vector2f(v.x * -radius, v.y * -halfheight);
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	float radius, halfheight;

	public EllipseShape(float x, float y, float radius, float halfheight) {
		super();
		translate(x, y);
		this.radius = radius;
		this.halfheight = halfheight;
		init();
	}

	public EllipseShape(Vector2f pos, float radius, float halfheight) {
		super();
		translate(pos);
		this.radius = radius;
		this.halfheight = halfheight;
		init();
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
		return new EllipseSupport(cs);
	}

	@Override
	public float getHeight() {
		return 2 * halfheight;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	private void init() {
		float longest = radius > halfheight ? radius : halfheight;
		setAABB(new Vector2f(-longest, -longest), new Vector2f(longest, longest));
		supportcalculator = createSupportCalculator(this);
	}

	@Override
	public float getHalfHeight() {
		return halfheight;
	}
}