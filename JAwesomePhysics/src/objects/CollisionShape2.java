package objects;

import java.util.List;

import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import quaternion.Complexf;
import vector.Vector2f;

public abstract class CollisionShape2 extends
		CollisionShape<Vector2f, Complexf, Matrix1f> {

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
	public Vector2f supportPointNegative(Vector2f direction) {
		return VecMath.addition(supportPointRelativeNegative(direction),
				getTranslation2());
	}

	@Override
	public Vector2f supportPointRelative(Vector2f direction) {
		return ComplexMath.transform(this.getRotation().get2dRotationf(),
				supportcalculator.supportPointLocal(direction));
	}

	@Override
	public Vector2f supportPointRelativeNegative(Vector2f direction) {
		return ComplexMath.transform(this.getRotation().get2dRotationf(),
				supportcalculator.supportPointLocalNegative(direction));
	}

	@Override
	public void updateInverseRotation() {
		Complexf c = new Complexf(getRotation().get2dRotation());
		c.invert();
		invrotation = c;
	}

	@Override
	public List<Vector2f> supportPointList(Vector2f direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vector2f> supportPointNegativeList(Vector2f direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vector2f> supportPointRelativeList(Vector2f direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vector2f> supportPointRelativeNegativeList(Vector2f direction) {
		// TODO Auto-generated method stub
		return null;
	}
}
