package objects;

import math.QuatMath;
import math.VecMath;
import quaternion.Quaternionf;
import vector.Vector3f;

public abstract class RigidBody3 extends
		RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> {
	public RigidBody3() {
		super();
		linearfactor = new Vector3f(1, 1, 1);
		linearvelocity = new Vector3f();
		forceaccumulator = new Vector3f();
		angularfactor = new Vector3f(1, 1, 1);
		angularvelocity = new Vector3f();
		torqueaccumulator = new Vector3f();
		invinertia = new Quaternionf(0);
		invrotation = new Quaternionf();
		aabb = new AABB<Vector3f>(new Vector3f(), new Vector3f());
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
	public Vector3f supportPointRelative(Vector3f direction) {
		return QuatMath.transform(this.getRotation(),
				supportPointLocal(direction));
	}

	@Override
	public void updateInverseRotation() {
		Quaternionf q = new Quaternionf(this.getRotation());
		q.invert();
		invrotation = q;
		// Matrix3f m = new Matrix3f(matrix.getSubMatrix());
		// m.invert();
		// invrotation = m;
	}
}