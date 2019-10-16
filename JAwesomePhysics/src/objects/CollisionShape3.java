package objects;

import matrix.Matrix4f;
import quaternion.Quaternionf;
import utils.RotationMath;
import utils.VectorConstants;
import vector.Vector3f;

public abstract class CollisionShape3 extends CollisionShape<Vector3f, Quaternionf, Quaternionf>
		implements InstancedBaseObject3 {

	public CollisionShape3() {
		super(new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
		aabb = new AABB3(new Vector3f(), new Vector3f());
		invrotation = new Quaternionf();
	}

	public CollisionShape3(CollisionShape3 cs) {
		super(cs, new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
	}

	@Override
	public AABB<Vector3f> getGlobalAABB() {
		AABB3 result = new AABB3();
		RotationMath.calculateRotationOffsetAABB3(this, result);
		return result;
	}

	@Override
	public Vector3f getGlobalMaxAABB() {
		return RotationMath.calculateRotationOffsetAABBMax3(this);
	}

	@Override
	public Vector3f getGlobalMinAABB() {
		return RotationMath.calculateRotationOffsetAABBMin3(this);
	}

	@Override
	public Vector3f supportPoint(Vector3f direction) {
		Vector3f support = supportPointRelative(direction);
		support.translate(getTranslation());
		return support;
	}

	@Override
	public Vector3f supportPointNegative(Vector3f direction) {
		Vector3f supportNeg = supportPointRelativeNegative(direction);
		supportNeg.translate(getTranslation());
		return supportNeg;
	}

	@Override
	public Vector3f supportPointRelative(Vector3f direction) {
		Vector3f supportRel = supportcalculator.supportPointLocal(direction);
		supportRel.translate(getRotationCenter());
		supportRel.transform(getRotation());
		return supportRel;
	}

	@Override
	public Vector3f supportPointRelativeNegative(Vector3f direction) {
		Vector3f supportRelNeg = supportcalculator.supportPointLocalNegative(direction);
		supportRelNeg.translate(getRotationCenter());
		supportRelNeg.transform(getRotation());
		return supportRelNeg;
	}

	@Override
	public void updateInverseRotation() {
		invrotation.set(getRotation());
		invrotation.invert();
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
		rotation.rotate(rotZ, VectorConstants.AXIS_Z);
		rotation.rotate(rotY, VectorConstants.AXIS_Y);
		rotation.rotate(rotX, VectorConstants.AXIS_X);
	}

	@Override
	public void rotateTo(float rotX, float rotY, float rotZ) {
		resetRotation();
		rotation.rotate(rotZ, VectorConstants.AXIS_Z);
		rotation.rotate(rotY, VectorConstants.AXIS_Y);
		rotation.rotate(rotX, VectorConstants.AXIS_X);
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
		float[][] mat = rotation.toMatrixf().getArrayf();
		return new Matrix4f(mat[0][0] * scale.x, mat[0][1] * scale.x, mat[0][2] * scale.x, 0, mat[1][0] * scale.y,
				mat[1][1] * scale.y, mat[1][2] * scale.y, 0, mat[2][0] * scale.z, mat[2][1] * scale.z,
				mat[2][2] * scale.z, 0, translation.getXf(), translation.getYf(), translation.getZf(), 1);
	}
}