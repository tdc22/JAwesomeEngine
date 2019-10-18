package objects;

import matrix.Matrix4f;
import quaternion.Complexf;
import vector.Vector2f;

public class GameObject2 extends GameObject<Vector2f, Complexf> implements InstancedBaseObject2 {

	public GameObject2() {
		super(new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
	}

	public GameObject2(float x, float y) {
		super(new Vector2f(), new Vector2f(x, y), new Complexf(), new Vector2f(1, 1));
	}

	public GameObject2(Vector2f pos) {
		super(new Vector2f(), pos, new Complexf(), new Vector2f(1, 1));
	}

	public void updateBuffer() {
		float real = getRotation().getRealf();
		float imaginary = getRotation().getImaginaryf();
		buf.put(real * scale.x);
		buf.put(-imaginary * scale.x);
		buf.put(0);
		buf.put(0);
		buf.put(imaginary * scale.y);
		buf.put(real * scale.y);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(translation.getXf());
		buf.put(translation.getYf());
		buf.put(0);
		buf.put(1);
		buf.flip();
	}

	public void translate(float transx, float transy) {
		translation.translate(transx, transy);
		updateBuffer();
	}

	public void translateTo(float x, float y) {
		translation.set(x, y);
		updateBuffer();
	}

	public void translateTo(Vector2f pos) {
		translation.set(pos);
		updateBuffer();
	}

	@Override
	public void translate(Vector2f translate) {
		translation.translate(translate);
		updateBuffer();
	}

	public void rotateTo(float rotX) {
		resetRotation();
		rotation.rotate(rotX);
		updateBuffer();
	}

	@Override
	public void rotate(Complexf rotate) {
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scale(Vector2f scale) {
		this.scale.scale(scale);
		updateBuffer();
	}

	@Override
	public Matrix4f getMatrix() {
		float[][] mat = rotation.toMatrixf().getArrayf();
		return new Matrix4f(mat[0][0] * scale.x, mat[0][1] * scale.x, 0, 0, mat[1][0] * scale.y, mat[1][1] * scale.y, 0,
				0, 0, 0, 0, 0, translation.getXf(), translation.getYf(), 0, 1);
	}

	@Override
	public void rotate(float rotX) {
		rotation.rotate(rotX);
		updateBuffer();
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		scale.scale(scaleX, scaleY);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scaleX, float scaleY) {
		scale.set(scaleX, scaleY);
		updateBuffer();
	}

	@Override
	public void rotateTo(Complexf rotate) {
		resetRotation();
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scaleTo(Vector2f scale) {
		this.scale.set(scale);
		updateBuffer();
	}

	@Override
	public void scale(float scale) {
		this.scale.scale(scale);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scale) {
		this.scale.set(scale, scale);
		updateBuffer();
	}

	@Override
	public void setRotationCenter(float x, float y) {
		rotationcenter.set(x, y);
		updateBuffer();
	}
}
