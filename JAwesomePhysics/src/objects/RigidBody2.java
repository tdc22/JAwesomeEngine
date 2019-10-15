package objects;

import math.ComplexMath;
import matrix.Matrix1f;
import matrix.Matrix4f;
import quaternion.Complexf;
import utils.RotationMath;
import vector.Vector1f;
import vector.Vector2f;

public class RigidBody2 extends RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> implements InstancedBaseObject2 {
	public RigidBody2() {
		super(new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
		aabb = new AABB2(new Vector2f(), new Vector2f());
		invrotation = new Complexf();
		init();
	}

	public RigidBody2(CollisionShape2 cs) {
		super(cs, new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
		init();
	}

	@Override
	public void applyCentralForce(Vector2f force) {
		forceaccumulator.x += force.x * linearfactor.x;
		forceaccumulator.y += force.y * linearfactor.y;
	}

	@Override
	public void applyCentralImpulse(Vector2f impulse) {
		linearvelocity.x += impulse.x * linearfactor.x * invMass;
		linearvelocity.y += impulse.y * linearfactor.y * invMass;
	}

	@Override
	public void applyForce(Vector2f force, Vector2f rel_pos) {
		applyCentralForce(force);
		applyTorque(new Vector1f(rel_pos.x * force.y * linearfactor.y - rel_pos.y * force.x * linearfactor.x));
	}

	@Override
	public void applyImpulse(Vector2f impulse, Vector2f rel_pos) {
		if (invMass != 0) {
			applyCentralImpulse(impulse);
			applyTorqueImpulse(
					new Vector1f(rel_pos.x * impulse.y * linearfactor.y - rel_pos.y * impulse.x * linearfactor.x));
		}
	}

	@Override
	public void applyTorque(Vector1f torque) {
		torqueaccumulator.x += torque.x * angularfactor.x;
	}

	public void applyTorque(float torque) {
		torqueaccumulator.x += torque * angularfactor.x;
	}

	@Override
	public void applyTorqueImpulse(Vector1f torque) {
		angularvelocity.x += invinertia.getf(0, 0) * torque.x * angularfactor.x;
	}

	public void applyTorqueImpulse(float torque) {
		angularvelocity.x += invinertia.getf(0, 0) * torque * angularfactor.x;
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
		return null;
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
	public Matrix1f getInertia() {
		Matrix1f inertia = new Matrix1f(invinertia);
		inertia.invert();
		return inertia;
	}

	private void init() {
		linearfactor = new Vector2f(1, 1);
		linearvelocity = new Vector2f();
		forceaccumulator = new Vector2f();
		angularfactor = new Vector1f(1);
		angularvelocity = new Vector1f();
		torqueaccumulator = new Vector1f();
		invinertia = new Matrix1f(0);
		dynamicfriction = 0.2f;
		rollingfriction = 0.01f;
	}

	public void setAngularVelocity(float velocity) {
		angularvelocity.set(velocity);
	}

	public void setLinearVelocity(float velocityX, float velocityY) {
		linearvelocity.set(velocityX, velocityY);
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
	public void updateInverseRotation() {
		invrotation = ComplexMath.invert(getRotation());
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
}