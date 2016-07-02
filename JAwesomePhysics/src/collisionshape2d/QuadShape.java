package collisionshape2d;

import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import objects.CollisionShape;
import objects.CollisionShape2;
import objects.SupportCalculator;
import quaternion.Complexf;
import shapedata2d.QuadStructure;
import vector.Vector2f;

public class QuadShape extends CollisionShape2 implements QuadStructure {
	protected class QuadSupport implements SupportCalculator<Vector2f> {
		private CollisionShape<Vector2f, Complexf, Matrix1f> collisionshape;

		public QuadSupport(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector2f supportPointLocal(Vector2f direction) {
			Vector2f v = ComplexMath.transform(collisionshape.getInverseRotation(), direction);
			return new Vector2f((v.x < 0 ? -1 : 1) * halfsize.x, (v.y < 0 ? -1 : 1) * halfsize.y);
		}

		@Override
		public Vector2f supportPointLocalNegative(Vector2f direction) {
			Vector2f v = ComplexMath.transform(collisionshape.getInverseRotation(), direction);
			return new Vector2f((v.x < 0 ? 1 : -1) * halfsize.x, (v.y < 0 ? 1 : -1) * halfsize.y);
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	Vector2f halfsize;

	public QuadShape(float x, float y, float halfsizex, float halfsizey) {
		super();
		translate(x, y);
		halfsize = new Vector2f(halfsizex, halfsizey);
		init();
	}

	public QuadShape(float x, float y, Vector2f halfsize) {
		super();
		translate(x, y);
		this.halfsize = halfsize;
		init();
	}

	public QuadShape(Vector2f pos, float halfsizex, float halfsizey) {
		super();
		translate(pos);
		halfsize = new Vector2f(halfsizex, halfsizey);
		init();
	}

	public QuadShape(Vector2f pos, Vector2f halfsize) {
		super();
		translate(pos);
		this.halfsize = halfsize;
		init();
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
		return new QuadSupport(cs);
	}

	@Override
	public Vector2f getHalfSize() {
		return halfsize;
	}

	@Override
	public Vector2f getSize() {
		return VecMath.scale(halfsize, 2);
	}

	private void init() {
		float diag = (float) Math.sqrt(halfsize.x * halfsize.x + halfsize.y * halfsize.y);
		setAABB(new Vector2f(-diag, -diag), new Vector2f(diag, diag));
		supportcalculator = createSupportCalculator(this);
	}
}