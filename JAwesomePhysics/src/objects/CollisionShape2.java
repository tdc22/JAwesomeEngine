package objects;

import math.ComplexMath;
import math.VecMath;
import quaternion.Complexf;
import vector.Vector2f;

public abstract class CollisionShape2 extends
		CollisionShape<Vector2f, Complexf> {

	public CollisionShape2() {
		super();
		aabb = new AABB2(new Vector2f(), new Vector2f());
		invrotation = new Complexf();
	}

	public CollisionShape2(CollisionShape2 cs) {
		super(cs);
	}

	@Override
	public AABB2 getGlobalAABB() {
		return new AABB2(getGlobalMinAABB(), getGlobalMaxAABB());
	}

	@Override
	public Vector2f getGlobalMaxAABB() {
		return VecMath.addition(aabb.getMax(), getTranslation2());
	}

	@Override
	public Vector2f getGlobalMinAABB() {
		return VecMath.addition(aabb.getMin(), getTranslation2());
	}

	@Override
	public Vector2f supportPoint(Vector2f direction) {
		return VecMath.addition(supportPointRelative(direction),
				getTranslation2());
	}

	@Override
	public Vector2f supportPointRelative(Vector2f direction) {
		return ComplexMath.transform(this.getRotation().get2dRotationf(),
				supportcalculator.supportPointLocal(direction));
	}

	@Override
	public void updateInverseRotation() {
		Complexf c = new Complexf(getRotation().get2dRotation());
		c.invert();
		invrotation = c;
	}

}
