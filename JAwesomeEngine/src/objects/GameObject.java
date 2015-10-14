package objects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import quaternion.Rotation;
import vector.Vector;

public abstract class GameObject<L extends Vector, A extends Rotation> extends DataObject<L, A> {
	protected FloatBuffer buf;

	public GameObject(L rotcenter, L translation, A rotation, L scale) {
		super(rotcenter, translation, rotation, scale);

		buf = BufferUtils.createFloatBuffer(16);
		updateBuffer();
	}

	public void delete() {
		buf.clear();
	}

	public FloatBuffer getMatrixBuffer() {
		return buf;
	}

	public abstract void updateBuffer();

	// @Override
	// protected void resetMatrix() {
	// matrix.setIdentity();
	// updateBuffer();
	// }

	// @Override
	// public void resetRotation() {
	// rotation.setIdentity();
	// updateBuffer();
	// }
	//
	// @Override
	// public void rotate(double angle, Vector3 axis) {
	// rotation.rotate(angle, axis);
	// updateBuffer();
	// }
	//
	// @Override
	// public void rotate(float angle) {
	// rotation.rotate(angle, new Vector3f(0.0f, 0.0f, 1.0f));
	// updateBuffer();
	// }
	//
	// @Override
	// public void rotate(float roll, float pitch, float yaw) {
	// rotation.rotate(yaw, new Vector3f(0.0f, 0.0f, 1.0f));
	// rotation.rotate(pitch, new Vector3f(0.0f, 1.0f, 0.0f));
	// rotation.rotate(roll, new Vector3f(1.0f, 0.0f, 0.0f));
	// updateBuffer();
	// }
	//
	// @Override
	// public void rotate(float angle, Vector3 axis) {
	// rotation.rotate(angle, axis);
	// updateBuffer();
	// }
	//
	// // @Override
	// // public void rotate(Matrix2 mat) {
	// // matrix.rotate(mat);
	// // updateBuffer();
	// // }
	// //
	// // @Override
	// // public void rotate(Matrix3 mat) {
	// // matrix.rotate(mat);
	// // updateBuffer();
	// // }
	//
	// @Override
	// public void rotate(Vector1 rot) {
	// rotation.rotate(rot.getX(), new Vector3f(0.0f, 0.0f, 1.0f));
	// updateBuffer();
	// }
	//
	// @Override
	// public void rotate(Vector3 rot) {
	// rotation.rotate(rot.getZ(), new Vector3d(0.0d, 0.0d, 1.0d));
	// rotation.rotate(rot.getY(), new Vector3d(0.0d, 1.0d, 0.0d));
	// rotation.rotate(rot.getX(), new Vector3d(1.0d, 0.0d, 0.0d));
	// updateBuffer();
	// }
	//
	// @Override
	// public void scale(float factor) {
	// scale.scale(factor);
	// updateBuffer();
	// }
	//
	// @Override
	// public void scale(float scalex, float scaley, float scalez) {
	// scale.scale(scalex, scaley, scalez);
	// updateBuffer();
	// }
	//
	// @Override
	// public void scale(Vector3 factors) {
	// scale.scale(factors);
	// updateBuffer();
	// }
	//
	// @Override
	// public void scaleTo(float scalex, float scaley) {
	// scale.setX(scalex);
	// scale.setY(scaley);
	// updateBuffer();
	// }
	//
	// @Override
	// public void scaleTo(float scalex, float scaley, float scalez) {
	// scale.set(scalex, scaley, scalez);
	// updateBuffer();
	// }
	//
	// @Override
	// public void scaleTo(Vector2 scalevec) {
	// scale.setX(scalevec.getX());
	// scale.setY(scalevec.getY());
	// updateBuffer();
	// }
	//
	// @Override
	// public void scaleTo(Vector3 scalevec) {
	// scale.set(scalevec);
	// updateBuffer();
	// }
	//
	// // @Override
	// // public void setMatrix(Matrix4f mat) {
	// // matrix = mat;
	// // updateBuffer();
	// // }
	// //
	// // @Override
	// // public void transform(Matrix4f mat) {
	// // matrix = VecMath.transformMatrix(matrix, mat);
	// // updateBuffer();
	// // }
	//
	// @Override
	// public void translate(float x, float y) {
	// translation.translate(x, y, 0);
	// updateBuffer();
	// }
	//
	// @Override
	// public void translate(float x, float y, float z) {
	// translation.translate(x, y, z);
	// updateBuffer();
	// }
	//
	// @Override
	// public void translate(Vector2 trans) {
	// translation.translate(trans.getX(), trans.getY(), 0);
	// updateBuffer();
	// }
	//
	// @Override
	// public void translate(Vector3 trans) {
	// translation.translate(trans);
	// updateBuffer();
	// }
	//
	// // @Override
	// // public void translateRelative(float x, float y) {
	// // matrix.translateRelative(new Vector3f(x, y, 0));
	// // updateBuffer();
	// // }
	// //
	// // @Override
	// // public void translateRelative(float x, float y, float z) {
	// // matrix.translateRelative(new Vector3f(x, y, z));
	// // updateBuffer();
	// // }
	// //
	// // @Override
	// // public void translateRelative(Vector2 trans) {
	// // matrix.translateRelative(trans);
	// // updateBuffer();
	// // }
	// //
	// // @Override
	// // public void translateRelative(Vector3 trans) {
	// // matrix.translateRelative(trans);
	// // updateBuffer();
	// // }
	//
	// @Override
	// public void translateTo(float x, float y) {
	// translation.setX(x);
	// translation.setY(y);
	// updateBuffer();
	// }
	//
	// @Override
	// public void translateTo(float x, float y, float z) {
	// translation.set(x, y, z);
	// updateBuffer();
	// }
	//
	// @Override
	// public void translateTo(Vector2 pos) {
	// translation.setX(pos.getX());
	// translation.setY(pos.getY());
	// updateBuffer();
	// }
	//
	// @Override
	// public void translateTo(Vector3 pos) {
	// translation.set(pos);
	// updateBuffer();
	// }
	//
	// public void updateBuffer() {
	// // matrix.store(buf);
	// float[][] mat = rotation.toMatrixf().getArrayf();
	// buf.put(mat[0][0] * scale.x);
	// buf.put(mat[0][1] * scale.x);
	// buf.put(mat[0][2] * scale.x);
	// buf.put(0);
	// buf.put(mat[1][0] * scale.y);
	// buf.put(mat[1][1] * scale.y);
	// buf.put(mat[1][2] * scale.y);
	// buf.put(0);
	// buf.put(mat[2][0] * scale.z);
	// buf.put(mat[2][1] * scale.z);
	// buf.put(mat[2][2] * scale.z);
	// buf.put(0);
	// buf.put(translation.getXf());
	// buf.put(translation.getYf());
	// buf.put(translation.getZf());
	// buf.put(1);
	// buf.flip();
	// }
	//
	// // public void attachTo(GameObject obj) {
	// // attachedTo = obj;
	// // isAttached = true;
	// // }
	// //
	// // public void detach() {
	// // isAttached = false;
	// // attachedTo = null;
	// // }
	// //
	// // public void attach(GameObject obj) {
	// // obj.attachTo(this);
	// // }
	// //
	// // public boolean isAttached() {
	// // return isAttached;
	// // }
	// //
	// // public GameObject getObjectAttachedTo() {
	// // return attachedTo;
	// // }
}
