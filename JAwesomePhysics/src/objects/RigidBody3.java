package objects;

import math.QuatMath;
import math.VecMath;
import matrix.Matrix4f;
import quaternion.Quaternionf;
import vector.Vector3f;

public class RigidBody3 extends
		RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>  implements InstancedBaseObject3 {
	public RigidBody3() {
		super(new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
		aabb = new AABB3(new Vector3f(), new Vector3f());
		invrotation = new Quaternionf();
		init();
	}

	public RigidBody3(CollisionShape3 cs) {
		super(cs, new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
		init();
	}

	@Override
	public void applyCentralForce(Vector3f force) {
		forceaccumulator = VecMath.addition(forceaccumulator,
				VecMath.multiplication(force, linearfactor));
	}

	@Override
	public void applyCentralImpulse(Vector3f impulse) {
		linearvelocity = VecMath.addition(linearvelocity, VecMath.scale(
				VecMath.multiplication(impulse, linearfactor), invMass));
	}

	@Override
	public void applyForce(Vector3f force, Vector3f rel_pos) {
		applyCentralForce(force);
		applyTorque(VecMath.crossproduct(rel_pos,
				VecMath.multiplication(force, linearfactor)));
	}

	@Override
	public void applyImpulse(Vector3f impulse, Vector3f rel_pos) {
		if (invMass != 0) {
			applyCentralImpulse(impulse);
			applyTorqueImpulse(VecMath.crossproduct(rel_pos,
					VecMath.multiplication(impulse, linearfactor)));
		}
	}

	@Override
	public void applyTorque(Vector3f torque) {
		torqueaccumulator = VecMath.addition(torqueaccumulator,
				VecMath.multiplication(torque, angularfactor));
	}

	@Override
	public void applyTorqueImpulse(Vector3f torque) {
		angularvelocity = VecMath.addition(
				angularvelocity,
				QuatMath.transform(invinertia,
						VecMath.multiplication(torque, angularfactor)));
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return null;
	}

	@Override
	public AABB<Vector3f> getGlobalAABB() {
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
	public Quaternionf getInertia() {
		return QuatMath.invert(invinertia);
	}

	private void init() {
		linearfactor = new Vector3f(1, 1, 1);
		linearvelocity = new Vector3f();
		forceaccumulator = new Vector3f();
		angularfactor = new Vector3f(1, 1, 1);
		angularvelocity = new Vector3f();
		torqueaccumulator = new Vector3f();
		invinertia = new Quaternionf(0);
		dynamicfriction = 0.05f;
		rollingfriction = 0.000001f;
	}

	public void setAngularVelocity(float velocityX, float velocityY,
			float velocityZ) {
		angularvelocity.set(velocityX, velocityY, velocityZ);
	}

	public void setLinearVelocity(float velocityX, float velocityY,
			float velocityZ) {
		linearvelocity.set(velocityX, velocityY, velocityZ);
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
}