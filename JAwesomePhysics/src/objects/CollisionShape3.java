package objects;

import math.QuatMath;
import math.VecMath;
import quaternion.Quaternionf;
import vector.Vector3f;

public abstract class CollisionShape3 extends
		CollisionShape<Vector3f, Quaternionf, Quaternionf> {

	public CollisionShape3() {
		super();
		aabb = new AABB3(new Vector3f(), new Vector3f());
		invrotation = new Quaternionf();
	}

	public CollisionShape3(CollisionShape3 cs) {
		super(cs);
	}

	@Override
	public AABB3 getGlobalAABB() {
		return new AABB3(getGlobalMinAABB(), getGlobalMaxAABB());
	}

	@Override
	public Vector3f getGlobalMaxAABB() {
		return VecMath.addition(aabb.getMax(), getTranslation());
	}

	@Override
	public Vector3f getGlobalMinAABB() {
		return VecMath.addition(aabb.getMin(), getTranslation());
	}

	@Override
	public Vector3f supportPoint(Vector3f direction) {
		return VecMath.addition(supportPointRelative(direction),
				getTranslation());
	}

	@Override
	public Vector3f supportPointNegative(Vector3f direction) {
		return VecMath.addition(supportPointRelativeNegative(direction),
				getTranslation());
	}

	@Override
	public Vector3f supportPointRelative(Vector3f direction) {
		return QuatMath.transform(this.getRotation(),
				supportcalculator.supportPointLocal(direction));
	}

	@Override
	public Vector3f supportPointRelativeNegative(Vector3f direction) {
		return QuatMath.transform(this.getRotation(),
				supportcalculator.supportPointLocalNegative(direction));
	}

	@Override
	public void updateInverseRotation() {
		Quaternionf q = new Quaternionf(this.getRotation());
		q.invert();
		invrotation = q;
	}
}