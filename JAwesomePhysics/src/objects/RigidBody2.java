package objects;

import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;

public class RigidBody2 extends
		RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> {
	public RigidBody2() {
		super();
		aabb = new AABB2(new Vector2f(), new Vector2f());
		invrotation = new Complexf();
		init();
	}

	public RigidBody2(CollisionShape2 cs) {
		super(cs);
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
	public InertiaCalculator<Matrix1f> createInertiaCalculator() {
		return null;
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
		return VecMath.addition(aabb.getMax(), getTranslation2());
	}

	@Override
	public Vector2f getGlobalMinAABB() {
		return VecMath.addition(aabb.getMin(), getTranslation2());
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
}