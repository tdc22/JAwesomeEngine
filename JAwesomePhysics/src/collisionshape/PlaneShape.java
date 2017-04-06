package collisionshape;

import math.QuatMath;
import math.VecMath;
import objects.CollisionShape;
import objects.CollisionShape3;
import objects.SupportCalculator;
import quaternion.Quaternionf;
import shapedata.PlaneStructure;
import vector.Vector2f;
import vector.Vector3f;

public class PlaneShape extends CollisionShape3 implements PlaneStructure {
	protected class PlaneSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf, Quaternionf> collisionshape;

		public PlaneSupport(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);
			v.set((v.x < 0 ? -halfsize.x : halfsize.x), 0, (v.z < 0 ? -halfsize.y : halfsize.y));
			return v;
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);
			v.set((v.x < 0 ? halfsize.x : -halfsize.x), 0, (v.z < 0 ? halfsize.y : -halfsize.y));
			return v;
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	Vector2f halfsize;

	public PlaneShape(float x, float y, float z, float halfsizex, float halfsizey) {
		super();
		translate(x, y, z);
		halfsize = new Vector2f(halfsizex, halfsizey);
		init();
	}

	public PlaneShape(float x, float y, float z, Vector2f halfsize) {
		super();
		translate(x, y, z);
		this.halfsize = halfsize;
		init();
	}

	public PlaneShape(Vector3f pos, float halfsizex, float halfsizey) {
		super();
		translate(pos);
		halfsize = new Vector2f(halfsizex, halfsizey);
		init();
	}

	public PlaneShape(Vector3f pos, Vector2f halfsize) {
		super();
		translate(pos);
		this.halfsize = halfsize;
		init();
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new PlaneSupport(cs);
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
		setAABB(new Vector3f(-diag, -diag, -diag), new Vector3f(diag, diag, diag));
		supportcalculator = createSupportCalculator(this);
	}
}