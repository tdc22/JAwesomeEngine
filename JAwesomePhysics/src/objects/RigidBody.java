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
		extends CollisionObject<L> {
	float invMass, staticfriction, dynamicfriction, rollingfriction,
			restitution, lineardamping, angulardamping;
	L linearfactor, linearvelocity, forceaccumulator;
	A1 angularfactor, angularvelocity, torqueaccumulator;
	A2 invrotation;
	A3 invinertia;
	AABB<L> aabb;

	public RigidBody() {
		invMass = 0;
		restitution = 0.5f;
		staticfriction = 0.3f;
		dynamicfriction = 0.2f;
		rollingfriction = 0.01f;
		lineardamping = 0.05f;
		angulardamping = 0.05f;
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

	public AABB<L> getAABB() {
		return aabb;
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

	public abstract L getGlobalMaxAABB();

	public abstract L getGlobalMinAABB();

	public A3 getInverseInertia() {
		return invinertia;
	}

	public float getInverseMass() {
		return invMass;
	}

	public A2 getInverseRotation() {
		return invrotation;
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

	public L getMaxAABB() {
		return aabb.getMax();
	}

	public L getMinAABB() {
		return aabb.getMin();
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

	public void setAABB(AABB<L> aabb) {
		this.aabb = aabb;
	}

	public void setAABB(L minAABB, L maxAABB) {
		aabb.set(minAABB, maxAABB);
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

	public void setMaxAABB(L maxAABB) {
		aabb.setMax(maxAABB);
	}

	public void setMinAABB(L minAABB) {
		aabb.setMin(minAABB);
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

	public abstract void updateInverseRotation();
}