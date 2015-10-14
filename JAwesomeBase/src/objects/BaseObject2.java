package objects;

import matrix.Matrix4f;
import quaternion.Complexf;
import vector.Vector2f;

/**
 * Base object that handles transformations for all objects of the engine.
 * 
 * @author Oliver Schall
 * 
 */

public class BaseObject2 extends BaseObject<Vector2f, Complexf> implements InstancedBaseObject2 {

	public BaseObject2() {
		super(new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
	}

	public void translate(float transx, float transy) {
		translation.translate(transx, transy);
	}

	public void translateTo(float x, float y) {
		translation.set(x, y);
	}

	public void translateTo(Vector2f pos) {
		translation.set(pos);
	}

	@Override
	public void translate(Vector2f translate) {
		translation.translate(translate);
	}

	public void rotateTo(float rotX) {
		resetRotation();
		rotation.rotate(rotX);
	}

	@Override
	public void rotate(Complexf rotate) {
		rotation.rotate(rotate);
	}

	@Override
	public void scale(Vector2f scale) {
		scale.scale(scale);
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
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		scale.scale(scaleX, scaleY);
	}

	@Override
	public void scaleTo(float scaleX, float scaleY) {
		scale.set(scaleX, scaleY);
	}

	@Override
	public void rotateTo(Complexf rotate) {
		resetRotation();
		rotation.rotate(rotate);
	}

	@Override
	public void scaleTo(Vector2f scale) {
		this.scale.set(scale);
	}

	@Override
	public void scale(float scale) {
		this.scale.scale(scale);
	}

	@Override
	public void scaleTo(float scale) {
		this.scale.set(scale, scale);
	}
}