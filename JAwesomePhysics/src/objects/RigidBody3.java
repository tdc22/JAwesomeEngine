package objects;

import math.QuatMath;
import math.VecMath;
import matrix.Matrix4f;
import quaternion.Quaternionf;
import utils.RotationMath;
import utils.VectorConstants;
import vector.Vector3f;

public class RigidBody3 extends RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>
		implements InstancedBaseObject3 {
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
		forceaccumulator.x += force.x * linearfactor.x;
		forceaccumulator.y += force.y * linearfactor.y;
		forceaccumulator.z += force.z * linearfactor.z;
	}

	@Override
	public void applyCentralImpulse(Vector3f impulse) {
		linearvelocity.x += impulse.x * linearfactor.x * invMass;
		linearvelocity.y += impulse.y * linearfactor.y * invMass;
		linearvelocity.z += impulse.z * linearfactor.z * invMass;
	}

	@Override
	public void applyForce(Vector3f force, Vector3f rel_pos) {
		applyCentralForce(force);
		applyTorque(VecMath.crossproduct(rel_pos, VecMath.multiplication(force, linearfactor)));
	}

	@Override
	public void applyImpulse(Vector3f impulse, Vector3f rel_pos) {
		if (invMass != 0) {
			applyCentralImpulse(impulse);
			float ilX = impulse.x * linearfactor.x;
			float ilY = impulse.y * linearfactor.y;
			float ilZ = impulse.z * linearfactor.z;
			applyTorqueImpulse(new Vector3f(rel_pos.y * ilZ - rel_pos.z * ilY, rel_pos.z * ilX - rel_pos.x * ilZ,
					rel_pos.x * ilY - rel_pos.y * ilX));
		}
	}

	@Override
	public void applyTorque(Vector3f torque) {
		torqueaccumulator.x += torque.x * angularfactor.x;
		torqueaccumulator.y += torque.y * angularfactor.y;
		torqueaccumulator.z += torque.z * angularfactor.z;
	}

	@Override
	public void applyTorqueImpulse(Vector3f torque) {
		Vector3f transformed = QuatMath.transform(invinertia, torque);
		transformed.scale(angularfactor);
		angularvelocity.x += transformed.x;
		angularvelocity.y += transformed.y;
		angularvelocity.z += transformed.z;
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return null;
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

	public void setAngularVelocity(float velocityX, float velocityY, float velocityZ) {
		angularvelocity.set(velocityX, velocityY, velocityZ);
	}

	public void setLinearVelocity(float velocityX, float velocityY, float velocityZ) {
		linearvelocity.set(velocityX, velocityY, velocityZ);
	}

	@Override
	public Vector3f supportPoint(Vector3f direction) {
		Vector3f support = supportPointRelative(direction);
		support.x += getTranslation().x;
		support.y += getTranslation().y;
		support.z += getTranslation().z;
		return support;
	}

	@Override
	public Vector3f supportPointNegative(Vector3f direction) {
		Vector3f supportNeg = supportPointRelativeNegative(direction);
		supportNeg.x += getTranslation().x;
		supportNeg.y += getTranslation().y;
		supportNeg.z += getTranslation().z;
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
	public void setRotationCenter(float x, float y, float z) {
		rotationcenter.set(x, y, z);
	}
}