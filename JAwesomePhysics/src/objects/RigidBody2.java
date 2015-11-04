package objects;

import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import matrix.Matrix4f;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

public class RigidBody2 extends
		RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>  implements InstancedBaseObject2 {
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
		forceaccumulator = VecMath.addition(forceaccumulator,
				VecMath.multiplication(force, linearfactor));
	}

	@Override
	public void applyCentralImpulse(Vector2f impulse) {
		linearvelocity = VecMath.addition(linearvelocity, VecMath.scale(
				VecMath.multiplication(impulse, linearfactor), invMass));
	}

	@Override
	public void applyForce(Vector2f force, Vector2f rel_pos) {
		applyCentralForce(force);
		applyTorque(new Vector1f(VecMath.crossproduct(new Vector3f(rel_pos),
				new Vector3f(VecMath.multiplication(force, linearfactor)))
				.getZf()));
	}

	@Override
	public void applyImpulse(Vector2f impulse, Vector2f rel_pos) {
		if (invMass != 0) {
			applyCentralImpulse(impulse);
			applyTorqueImpulse(new Vector1f(VecMath
					.crossproduct(
							new Vector3f(rel_pos),
							new Vector3f(VecMath.multiplication(impulse,
									linearfactor))).getZf()));
		}
	}

	@Override
	public void applyTorque(Vector1f torque) {
		torqueaccumulator = VecMath.addition(torqueaccumulator,
				VecMath.multiplication(torque, angularfactor));
	}

	@Override
	public void applyTorqueImpulse(Vector1f torque) {
		angularvelocity = VecMath.addition(
				angularvelocity,
				VecMath.transformVector(invinertia,
						VecMath.multiplication(torque, angularfactor)));
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(
			CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
		return null;
	}

	@Override
	public AABB<Vector2f> getGlobalAABB() {
		return new AABB2(getGlobalMinAABB(), getGlobalMaxAABB());
	}

	@Override
	public Vector2f getGlobalMaxAABB() {
		return VecMath.addition(aabb.getMax(), getTranslation());
	}

	@Override
	public Vector2f getGlobalMinAABB() {
		return VecMath.addition(aabb.getMin(), getTranslation());
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
		return VecMath.addition(supportPointRelative(direction),
				getTranslation());
	}

	@Override
	public Vector2f supportPointNegative(Vector2f direction) {
		return VecMath.addition(supportPointRelativeNegative(direction),
				getTranslation());
	}

	@Override
	public Vector2f supportPointRelative(Vector2f direction) {
		return ComplexMath.transform(this.getRotation(),
				supportcalculator.supportPointLocal(direction));
	}

	@Override
	public Vector2f supportPointRelativeNegative(Vector2f direction) {
		return ComplexMath.transform(this.getRotation(),
				supportcalculator.supportPointLocalNegative(direction));
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
		// TODO Auto-generated method stub
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
}