package objects;

import quaternion.Rotation;
import vector.Vector;

/**
 * A RigidBody implementation.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class RigidBody<L extends Vector, A1 extends Vector, A2 extends Rotation, A3 extends Rotation>
		extends CollisionShape<L, A2, A3> {
	float invMass, staticfriction, dynamicfriction, rollingfriction,
			restitution, lineardamping, angulardamping;
	L linearfactor, linearvelocity, forceaccumulator;
	A1 angularfactor, angularvelocity, torqueaccumulator;
	A3 invinertia;

	public RigidBody(L rotationcenter, L translation, A2 rotation, L scale) {
		super(rotationcenter, translation, rotation, scale);
		init();
	}

	public RigidBody(CollisionShape<L, A2, A3> cs, L rotationcenter,
			L translation, A2 rotation, L scale) {
		super(cs, rotationcenter, translation, rotation, scale);
		init();
	}

	public abstract void applyCentralForce(L force);

	public abstract void applyCentralImpulse(L impulse);

	public abstract void applyForce(L force, L rel_pos);

	public abstract void applyImpulse(L impulse, L rel_pos);

	public abstract void applyTorque(A1 torque);

	public abstract void applyTorqueImpulse(A1 torque);

	public void clearForces() {
		forceaccumulator.setAll(0);
		torqueaccumulator.setAll(0);
	}

	public float getAngularDamping() {
		return angulardamping;
	}

	public A1 getAngularFactor() {
		return angularfactor;
	}

	public A1 getAngularVelocity() {
		return angularvelocity;
	}

	public float getDynamicFriction() {
		return dynamicfriction;
	}

	public L getForceAccumulator() {
		return forceaccumulator;
	}

	public abstract A3 getInertia();

	public A3 getInverseInertia() {
		return invinertia;
	}

	public float getInverseMass() {
		return invMass;
	}

	public float getLinearDamping() {
		return lineardamping;
	}

	public L getLinearFactor() {
		return linearfactor;
	}

	public L getLinearVelocity() {
		return linearvelocity;
	}

	public float getMass() {
		if (invMass != 0)
			return 1 / invMass;
		return 0;
	}

	public float getRestitution() {
		return restitution;
	}

	public float getRollingFriction() {
		return rollingfriction;
	}

	public float getStaticFriction() {
		return staticfriction;
	}

	public A1 getTorqueAccumulator() {
		return torqueaccumulator;
	}

	private void init() {
		invMass = 0;
		restitution = 0.2f;// 0.5f;
		staticfriction = 0.3f;
		// Dynamic and rolling friction in RigidBody2 and RigidBody3
		lineardamping = 0.05f;
		angulardamping = 0.05f;
		/*
		 * 2d: invMass = 0; restitution = 0.5f; staticfriction = 0.3f;
		 * dynamicfriction = 0.2f; rollingfriction = 0.01f; lineardamping =
		 * 0.05f; angulardamping = 0.05f;
		 */
	}

	public void setAngularDamping(float angulardamping) {
		this.angulardamping = angulardamping;
	}

	public void setAngularFactor(A1 angularfactor) {
		this.angularfactor = angularfactor;
	}

	public void setAngularVelocity(A1 angularvelocity) {
		this.angularvelocity = angularvelocity;
	}

	public void setDynamicFriction(float dynamicfriction) {
		this.dynamicfriction = dynamicfriction;
	}

	public void setInertia(A3 inertia) {
		inertia.invert();
		invinertia = inertia;
	}

	public void setInverseInertia(A3 inverseInertia) {
		invinertia = inverseInertia;
	}

	public void setInverseMass(float inverseMass) {
		invMass = inverseMass;
	}

	public void setLinearDamping(float lineardamping) {
		this.lineardamping = lineardamping;
	}

	public void setLinearFactor(L linearfactor) {
		this.linearfactor = linearfactor;
	}

	public void setLinearVelocity(L linearvelocity) {
		this.linearvelocity = linearvelocity;
	}

	public void setMass(float mass) {
		if (mass != 0)
			invMass = 1 / mass;
		else
			invMass = 0;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	public void setRollingFriction(float rollingfriction) {
		this.rollingfriction = rollingfriction;
	}

	public void setStaticFriction(float staticfriction) {
		this.staticfriction = staticfriction;
	}

	public boolean isGhost() {
		return false;
	}
}