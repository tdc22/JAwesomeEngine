package objects;

import matrix.Matrix4f;
import quaternion.Quaternionf;
import vector.Vector3f;

public class GameObject3 extends GameObject<Vector3f, Quaternionf> implements InstancedBaseObject3 {

	public GameObject3() {
		super(new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
	}

	public GameObject3(float x, float y, float z) {
		super(new Vector3f(), new Vector3f(x, y, z), new Quaternionf(), new Vector3f(1, 1, 1));
	}

	public GameObject3(Vector3f pos) {
		super(new Vector3f(), pos, new Quaternionf(), new Vector3f(1, 1, 1));
	}

	public void updateBuffer() {
		float q0 = rotation.getQ0f();
		float q1 = rotation.getQ1f();
		float q2 = rotation.getQ2f();
		float q3 = rotation.getQ3f();
		buf.put((1 - 2 * q2 * q2 - 2 * q3 * q3) * scale.x);
		buf.put((2 * q1 * q2 + 2 * q3 * q0) * scale.x);
		buf.put((2 * q1 * q3 - 2 * q2 * q0) * scale.x);
		buf.put(0);
		buf.put((2 * q1 * q2 - 2 * q3 * q0) * scale.y);
		buf.put((1 - 2 * q1 * q1 - 2 * q3 * q3) * scale.y);
		buf.put((2 * q2 * q3 + 2 * q1 * q0) * scale.y);
		buf.put(0);
		buf.put((2 * q1 * q3 + 2 * q2 * q0) * scale.z);
		buf.put((2 * q2 * q3 - 2 * q1 * q0) * scale.z);
		buf.put((1 - 2 * q1 * q1 - 2 * q2 * q2) * scale.z);
		buf.put(0);
		buf.put(translation.getXf());
		buf.put(translation.getYf());
		buf.put(translation.getZf());
		buf.put(1);
		buf.flip();
	}

	public void translate(float transx, float transy, float transz) {
		translation.translate(transx, transy, transz);
		updateBuffer();
	}

	public void translateTo(float x, float y, float z) {
		translation.set(x, y, z);
		updateBuffer();
	}

	public void translateTo(Vector3f pos) {
		translation.set(pos);
		updateBuffer();
	}

	@Override
	public void translate(Vector3f translate) {
		translation.translate(translate);
		updateBuffer();
	}

	public void rotateTo(float rotX, float rotY, float rotZ) {
		resetRotation();
		rotation.rotate(rotZ, new Vector3f(0.0d, 0.0d, 1.0d));
		rotation.rotate(rotY, new Vector3f(0.0d, 1.0d, 0.0d));
		rotation.rotate(rotX, new Vector3f(1.0d, 0.0d, 0.0d));
		updateBuffer();
	}

	@Override
	public void rotate(Quaternionf rotate) {
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scale(Vector3f scale) {
		scale.scale(scale);
		updateBuffer();
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
		rotation.rotate(rotZ, new Vector3f(0.0d, 0.0d, 1.0d));
		rotation.rotate(rotY, new Vector3f(0.0d, 1.0d, 0.0d));
		rotation.rotate(rotX, new Vector3f(1.0d, 0.0d, 0.0d));
		updateBuffer();
	}

	@Override
	public void scale(float scaleX, float scaleY, float scaleZ) {
		scale.scale(scaleX, scaleY, scaleZ);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scaleX, float scaleY, float scaleZ) {
		scale.set(scaleX, scaleY, scaleZ);
		updateBuffer();
	}

	@Override
	public void rotateTo(Quaternionf rotate) {
		resetRotation();
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scaleTo(Vector3f scale) {
		this.scale.set(scale);
		updateBuffer();
	}

	@Override
	public void scale(float scale) {
		this.scale.scale(scale, scale, scale);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scale) {
		this.scale.set(scale, scale, scale);
		updateBuffer();
	}
}
