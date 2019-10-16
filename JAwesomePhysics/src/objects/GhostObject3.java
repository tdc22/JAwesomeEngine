package objects;

import matrix.Matrix4f;
import quaternion.Quaternionf;
import utils.RotationMath;
import utils.VectorConstants;
import vector.Vector3f;

public class GhostObject3 extends GhostObject<Vector3f, Vector3f, Quaternionf, Quaternionf>
		implements InstancedBaseObject3 {

	public GhostObject3() {
		super(new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
	}

	public GhostObject3(CollisionShape3 cs) {
		super(cs, new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
	}

	public GhostObject3(CollisionShape3 cs, Vector3f rotationcenter, Vector3f translation, Quaternionf rotation,
			Vector3f scale) {
		super(cs, rotationcenter, translation, rotation, scale);
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
	public Vector3f getSupportCenter() {
		return getTranslation();
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
	public void applyCentralForce(Vector3f force) {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyCentralImpulse(Vector3f impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyForce(Vector3f force, Vector3f rel_pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyImpulse(Vector3f impulse, Vector3f rel_pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyTorque(Vector3f torque) {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyTorqueImpulse(Vector3f torque) {
		// TODO Auto-generated method stub

	}

	@Override
	public Quaternionf getInertia() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		// TODO Auto-generated method stub
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
		return null;
	}
}
