package objects;

import matrix.Matrix1f;
import matrix.Matrix4f;
import quaternion.Complexf;
import utils.RotationMath;
import vector.Vector1f;
import vector.Vector2f;

public class GhostObject2 extends GhostObject<Vector2f, Vector1f, Complexf, Matrix1f> implements InstancedBaseObject2 {

	public GhostObject2() {
		super(new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
	}

	public GhostObject2(CollisionShape2 cs) {
		super(cs, new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
	}

	public GhostObject2(CollisionShape2 cs, Vector2f rotationcenter, Vector2f translation, Complexf rotation,
			Vector2f scale) {
		super(cs, rotationcenter, translation, rotation, scale);
	}

	@Override
	public Vector2f supportPoint(Vector2f direction) {
		Vector2f support = supportPointRelative(direction);
		support.x += getTranslation().x;
		support.y += getTranslation().y;
		return support;
	}

	@Override
	public Vector2f supportPointNegative(Vector2f direction) {
		Vector2f supportNeg = supportPointRelativeNegative(direction);
		supportNeg.x += getTranslation().x;
		supportNeg.y += getTranslation().y;
		return supportNeg;
	}

	@Override
	public Vector2f supportPointRelative(Vector2f direction) {
		Vector2f supportRel = supportcalculator.supportPointLocal(direction);
		supportRel.translate(getRotationCenter());
		supportRel.transform(getRotation());
		return supportRel;
	}

	@Override
	public Vector2f supportPointRelativeNegative(Vector2f direction) {
		Vector2f supportRelNeg = supportcalculator.supportPointLocalNegative(direction);
		supportRelNeg.translate(getRotationCenter());
		supportRelNeg.transform(getRotation());
		return supportRelNeg;
	}

	@Override
	public void applyCentralForce(Vector2f force) {

	}

	@Override
	public void applyCentralImpulse(Vector2f impulse) {

	}

	@Override
	public void applyForce(Vector2f force, Vector2f rel_pos) {

	}

	@Override
	public void applyImpulse(Vector2f impulse, Vector2f rel_pos) {

	}

	@Override
	public void applyTorque(Vector1f torque) {

	}

	@Override
	public void applyTorqueImpulse(Vector1f torque) {

	}

	@Override
	public Matrix1f getInertia() {
		return null;
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
		return null;
	}

	@Override
	public AABB<Vector2f> getGlobalAABB() {
		AABB2 result = new AABB2();
		RotationMath.calculateRotationOffsetAABB2(this, result);
		return result;
	}

	@Override
	public Vector2f getGlobalMaxAABB() {
		return RotationMath.calculateRotationOffsetAABBMax2(this);
	}

	@Override
	public Vector2f getGlobalMinAABB() {
		return RotationMath.calculateRotationOffsetAABBMin2(this);
	}

	@Override
	public void updateInverseRotation() {
		invrotation.set(getRotation());
		invrotation.invert();
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
		return null;
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
	public void setRotationCenter(float x, float y) {
		rotationcenter.set(x, y);
	}
}
