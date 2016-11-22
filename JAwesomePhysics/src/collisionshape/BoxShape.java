package collisionshape;

import math.QuatMath;
import math.VecMath;
import objects.CollisionShape;
import objects.CollisionShape3;
import objects.SupportCalculator;
import quaternion.Quaternionf;
import shapedata.BoxStructure;
import vector.Vector3f;

public class BoxShape extends CollisionShape3 implements BoxStructure {
	protected class BoxSupport implements SupportCalculator<Vector3f> {
		private CollisionShape<Vector3f, Quaternionf, Quaternionf> collisionshape;

		public BoxSupport(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
			collisionshape = cs;
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);
			v.set((v.x < 0 ? -halfsize.x : halfsize.x), (v.y < 0 ? -halfsize.y : halfsize.y),
					(v.z < 0 ? -halfsize.z : halfsize.z));
			return v;
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			Vector3f v = QuatMath.transform(collisionshape.getInverseRotation(), direction);
			v.set((v.x < 0 ? halfsize.x : -halfsize.x), (v.y < 0 ? halfsize.y : -halfsize.y),
					(v.z < 0 ? halfsize.z : -halfsize.z));
			return v;
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}

	Vector3f halfsize;

	public BoxShape(float x, float y, float z, float halfsizex, float halfsizey, float halfsizez) {
		super();
		translate(x, y, z);
		halfsize = new Vector3f(halfsizex, halfsizey, halfsizez);
		init();
	}

	public BoxShape(float x, float y, float z, Vector3f halfsize) {
		super();
		translate(x, y, z);
		this.halfsize = halfsize;
		init();
	}

	public BoxShape(Vector3f pos, float halfsizex, float halfsizey, float halfsizez) {
		super();
		translate(pos);
		halfsize = new Vector3f(halfsizex, halfsizey, halfsizez);
		init();
	}

	public BoxShape(Vector3f pos, Vector3f halfsize) {
		super();
		translate(pos);
		this.halfsize = halfsize;
		init();
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new BoxSupport(cs);
	}

	@Override
	public Vector3f getHalfSize() {
		return halfsize;
	}

	@Override
	public Vector3f getSize() {
		return VecMath.scale(halfsize, 2);
	}

	private void init() {
		float diag = (float) Math.sqrt(halfsize.x * halfsize.x + halfsize.y * halfsize.y + halfsize.z * halfsize.z);
		setAABB(new Vector3f(-diag, -diag, -diag), new Vector3f(diag, diag, diag));
		supportcalculator = createSupportCalculator(this);
	}
}