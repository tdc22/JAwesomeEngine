package objects;

import matrix.Matrix4f;
import quaternion.Quaternionf;
import utils.VectorConstants;
import vector.Vector3f;

/**
 * Base object that handles transformations for all objects of the engine.
 * 
 * @author Oliver Schall
 * 
 */

public class BaseObject3 extends BaseObject<Vector3f, Quaternionf> implements InstancedBaseObject3 {

	public BaseObject3() {
		super(new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
	}

	public void translate(float transx, float transy, float transz) {
		translation.translate(transx, transy, transz);
	}

	public void translateTo(float x, float y, float z) {
		translation.set(x, y, z);
	}

	public void translateTo(Vector3f pos) {
		translation.set(pos);
	}

	@Override
	public void translate(Vector3f translate) {
		translation.translate(translate);
	}

	public void rotateTo(float rotX, float rotY, float rotZ) {
		resetRotation();
		rotation.rotate(rotZ, VectorConstants.AXIS_Z);
		rotation.rotate(rotY, VectorConstants.AXIS_Y);
		rotation.rotate(rotX, VectorConstants.AXIS_X);
	}

	@Override
	public void rotate(Quaternionf rotate) {
		rotation.rotate(rotate);
	}

	@Override
	public void scale(Vector3f scale) {
		scale.scale(scale);
	}

	@Override
	public Matrix4f getMatrix() {
		float[][] mat = rotation.toMatrixf().getArrayf();
		return new Matrix4f(mat[0][0] * scale.x, mat[0][1] * scale.x, mat[0][2] * scale.x, 0, mat[1][0] * scale.y,
				mat[1][1] * scale.y, mat[1][2] * scale.y, 0, mat[2][0] * scale.z, mat[2][1] * scale.z,
				mat[2][2] * scale.z, 0, translation.getXf(), translation.getYf(), translation.getZf(), 1);
	}

	@Override
	public void rotate(float rotX, float rotY, float rotZ) {
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
	public void rotateTo(Quaternionf rotate) {
		resetRotation();
		rotation.rotate(rotate);
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
	public void setRotationCenter(float x, float y, float z) {
		rotationcenter.set(x, y, z);
	}
}