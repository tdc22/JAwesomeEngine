package objects;

import math.QuatMath;
import math.VecMath;
import matrix.Matrix4f;
import quaternion.Quaternionf;
import vector.Vector3f;

public abstract class CollisionShape3 extends
		CollisionShape<Vector3f, Quaternionf, Quaternionf> implements InstancedBaseObject3 {

	public CollisionShape3() {
		super(new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
		aabb = new AABB3(new Vector3f(), new Vector3f());
		invrotation = new Quaternionf();
	}

	public CollisionShape3(CollisionShape3 cs) {
		super(cs, new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
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

	@Override
	public void translate(float x, float y, float z) {
		translation.translate(x, y, z);
	}

	@Override
	public void translateTo(float x, float y, float z) {
		translation.set(x, y, z);
	}

	@Override
	public void rotate(float rotX, float rotY, float rotZ) {
		rotation.rotate(rotZ, new Vector3f(0.0d, 0.0d, 1.0d));
		rotation.rotate(rotY, new Vector3f(0.0d, 1.0d, 0.0d));
		rotation.rotate(rotX, new Vector3f(1.0d, 0.0d, 0.0d));
	}

	@Override
	public void rotateTo(float rotX, float rotY, float rotZ) {
		resetRotation();
		rotation.rotate(rotZ, new Vector3f(0.0d, 0.0d, 1.0d));
		rotation.rotate(rotY, new Vector3f(0.0d, 1.0d, 0.0d));
		rotation.rotate(rotX, new Vector3f(1.0d, 0.0d, 0.0d));
	}

	@Override
	public void scale(float scaleX, float scaleY, float scaleZ) {
		scale.scale(scaleX, scaleY, scaleZ);
	}

	@Override
	public void scaleTo(float scaleX, float scaleY, float scaleZ) {
		scale.set(scaleX, scaleY, scaleZ);
	}

	@Override
	public void translate(Vector3f translate) {
		translation.translate(translate);
	}

	@Override
	public void translateTo(Vector3f translate) {
		translation.set(translate);
	}

	@Override
	public void rotate(Quaternionf rotate) {
		rotation.rotate(rotate);
	}

	@Override
	public void rotateTo(Quaternionf rotate) {
		rotation.set(rotate);
	}

	@Override
	public void scale(Vector3f scale) {
		this.scale.scale(scale);
	}

	@Override
	public void scaleTo(Vector3f scale) {
		this.scale.set(scale);
	}

	@Override
	public void scale(float scale) {
		this.scale.scale(scale);
	}

	@Override
	public void scaleTo(float scale) {
		this.scale.set(scale, scale, scale);
	}

	@Override
	public Matrix4f getMatrix() {
		// TODO Auto-generated method stub
		return null;
	}
}