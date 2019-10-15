package objects;

import matrix.Matrix1f;
import matrix.Matrix4f;
import quaternion.Complexf;
import utils.RotationMath;
import vector.Vector2f;

public abstract class CollisionShape2 extends CollisionShape<Vector2f, Complexf, Matrix1f>
		implements InstancedBaseObject2 {

	public CollisionShape2() {
		super(new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
		aabb = new AABB2(new Vector2f(), new Vector2f());
		invrotation = new Complexf();
	}

	public CollisionShape2(CollisionShape2 cs) {
		super(cs, new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
	}

	@Override
	public AABB<Vector2f> getGlobalAABB() {
		AABB2 result = new AABB2();
		RotationMath.calculateRotationOffsetAABB(this, result);
		return result;
	}

	@Override
	public Vector2f getGlobalMaxAABB() {
		return RotationMath.calculateRotationOffsetAABBMax(this);
	}

	@Override
	public Vector2f getGlobalMinAABB() {
		return RotationMath.calculateRotationOffsetAABBMin(this);
	}

	@Override
	public Vector2f supportPoint(Vector2f direction) {
		Vector2f support = supportPointRelative(direction);
		support.translate(getTranslation());
		return support;
	}

	@Override
	public Vector2f supportPointNegative(Vector2f direction) {
		Vector2f supportNeg = supportPointRelativeNegative(direction);
		supportNeg.translate(getTranslation());
		return supportNeg;
	}

	@Override
	public Vector2f supportPointRelative(Vector2f direction) {
		Vector2f supportRel = supportcalculator.supportPointLocal(direction);
		supportRel.transform(getRotation());
		return supportRel;
	}

	@Override
	public Vector2f supportPointRelativeNegative(Vector2f direction) {
		Vector2f supportRelNeg = supportcalculator.supportPointLocalNegative(direction);
		supportRelNeg.transform(getRotation());
		return supportRelNeg;
	}

	@Override
	public void updateInverseRotation() {
		Complexf c = new Complexf(getRotation());
		c.invert();
		invrotation = c;
	}

	@Override
	public void translate(float x, float y) {
		translation.translate(x, y);
	}

	@Override
	public void translateTo(float x, float y) {
		translation.set(x, y);
	}

	@Override
	public void rotate(float rotX) {
		rotation.rotate(rotX);
	}

	@Override
	public void rotateTo(float rotX) {
		resetRotation();
		rotation.rotate(rotX);
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		scale.scale(scaleX, scaleY);
	}

	@Override
	public void scaleTo(float scaleX, float scaleY) {
		scale.set(scaleX, scaleY);
	}

	@Override
	public void translate(Vector2f translate) {
		translation.translate(translate);
	}

	@Override
	public void translateTo(Vector2f translate) {
		translation.set(translate);
	}

	@Override
	public void rotate(Complexf rotate) {
		rotation.rotate(rotate);
	}

	@Override
	public void rotateTo(Complexf rotate) {
		rotation.set(rotate);
	}

	@Override
	public void scale(Vector2f scale) {
		this.scale.scale(scale);
	}

	@Override
	public void scaleTo(Vector2f scale) {
		this.scale.set(scale);
	}

	@Override
	public void scale(float scale) {
		this.scale.scale(scale);
	}

	@Override
	public void scaleTo(float scale) {
		this.scale.set(scale, scale);
	}

	@Override
	public Matrix4f getMatrix() {
		float[][] mat = rotation.toMatrixf().getArrayf();
		return new Matrix4f(mat[0][0] * scale.x, mat[0][1] * scale.x, 0, 0, mat[1][0] * scale.y, mat[1][1] * scale.y, 0,
				0, 0, 0, 0, 0, translation.getXf(), translation.getYf(), 0, 1);
	}
}