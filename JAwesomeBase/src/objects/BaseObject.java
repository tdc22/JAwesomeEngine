package objects;

import quaternion.Quaternion;
import quaternion.Quaternionf;
import vector.Vector1;
import vector.Vector2;
import vector.Vector2f;
import vector.Vector3;
import vector.Vector3d;
import vector.Vector3f;

/**
 * Base object that handles transformations for all objects of the engine.
 * 
 * @author Oliver Schall
 * 
 */

public class BaseObject {
	protected Vector3f rotcenter;
	protected Vector3f translation;
	protected Quaternionf rotation;
	protected Vector3f scale;

	public BaseObject() {
		rotcenter = new Vector3f();
		translation = new Vector3f();
		rotation = new Quaternionf();
		scale = new Vector3f(1, 1, 1);
	}

	// /**
	// * Gets the transformation matrix of the object.
	// *
	// * @return transformation matrix
	// */
	// public Matrix4f getMatrix() {
	// return matrix;
	// }
	//
	// /**
	// * Gets the rotation sub matrix of the transformation matrix.
	// *
	// * @return rotation matrix
	// */
	// public Matrix3f getRotation() {
	// return (Matrix3f) matrix.getSubMatrix();
	// }
	//
	// /**
	// * Gets the 2d rotation sub matrix of the transformation matrix.
	// *
	// * @return 2d rotation matrix
	// */
	// public Matrix2f getRotation2() {
	// return (Matrix2f) matrix.getSubMatrix2();
	// }

	/**
	 * Gets the rotation represented as a quaternion.
	 * 
	 * @return rotation as quaternion
	 */
	public Quaternionf getRotation() {
		return rotation;
	}

	/**
	 * Gets the rotation center of the object.
	 * 
	 * @return rotation center
	 */
	public Vector3f getRotationCenter() {
		return rotcenter;
	}

	/**
	 * Gets the object translation.
	 * 
	 * @return object translation
	 */
	public Vector3f getTranslation() {
		return translation;
	}

	/**
	 * Gets the 2d object translation.
	 * 
	 * @return 2d object translation
	 */
	public Vector2f getTranslation2() {
		return new Vector2f(translation.x, translation.y);
	}

	// /**
	// * Resets the transformation matrix.
	// */
	// protected void resetMatrix() {
	// matrix.setIdentity();
	// }

	/**
	 * Resets the rotation.
	 */
	public void resetRotation() {
		rotation.setIdentity();
	}

	/**
	 * Rotates the object by an angle around an axis.
	 * 
	 * @param angle
	 *            rotation angle
	 * @param axis
	 *            rotation axis
	 */
	public void rotate(double angle, Vector3 axis) {
		rotation.rotate(angle, axis);
	}

	/**
	 * Rotates the object by an angle around the z-axis. (for 2d)
	 * 
	 * @param angle
	 *            rotation angle
	 */
	public void rotate(float angle) {
		rotation.rotate(angle, new Vector3f(0.0f, 0.0f, 1.0f));
	}

	/**
	 * Rotates the object by roll, pitch and yaw angles.
	 * 
	 * @param roll
	 *            x-axis angle
	 * @param pitch
	 *            y-axis angle
	 * @param yaw
	 *            z-axis angle
	 */
	public void rotate(float roll, float pitch, float yaw) {
		rotation.rotate(yaw, new Vector3f(0.0f, 0.0f, 1.0f));
		rotation.rotate(pitch, new Vector3f(0.0f, 1.0f, 0.0f));
		rotation.rotate(roll, new Vector3f(1.0f, 0.0f, 0.0f));
	}

	/**
	 * @see BaseObject#rotate(double, Vector3)
	 */
	public void rotate(float angle, Vector3 axis) {
		rotation.rotate(angle, axis);
	}

	// /**
	// * Rotates the object by a rotation matrix.
	// *
	// * @param mat
	// * matrix to rotate by
	// */
	// public void rotate(Matrix2 mat) {
	// matrix.rotate(mat);
	// }
	//
	// /**
	// * @see BaseObject#rotate(Matrix2)
	// */
	// public void rotate(Matrix3 mat) {
	// matrix.rotate(mat);
	// }

	/**
	 * Rotates the object by a quaternion.
	 * 
	 * @param quat
	 *            quaternion to rotate by
	 */
	public void rotate(Quaternion quat) {
		rotation.rotate(quat);
	}

	/**
	 * Rotates the object by a vector.
	 * 
	 * @param rot
	 *            vector to rotate by
	 */
	public void rotate(Vector1 rot) {
		rotation.rotate(rot.getX(), new Vector3f(0.0f, 0.0f, 1.0f));
	}

	/**
	 * @see BaseObject#rotate(Vector1)
	 */
	public void rotate(Vector3 rot) {
		rotation.rotate(rot.getZ(), new Vector3d(0.0d, 0.0d, 1.0d));
		rotation.rotate(rot.getY(), new Vector3d(0.0d, 1.0d, 0.0d));
		rotation.rotate(rot.getX(), new Vector3d(1.0d, 0.0d, 0.0d));
	}

	/**
	 * Sets the object rotation to an angle.
	 * 
	 * @param angle
	 *            new rotation angle
	 */
	public void rotateTo(float angle) {
		rotation.setIdentity();
		rotate(angle);
	}

	/**
	 * @see BaseObject#rotateTo(float)
	 */
	public void rotateTo(float roll, float pitch, float yaw) {
		rotation.setIdentity();
		rotate(roll, pitch, yaw);
	}

	/**
	 * Sets the object rotation.
	 * 
	 * @param rot
	 *            vector containing new angles
	 */
	public void rotateTo(Vector1 rot) {
		rotation.setIdentity();
		rotate(rot);
	}

	/**
	 * @see BaseObject#rotateTo(Vector1)
	 */
	public void rotateTo(Vector3 rot) {
		rotation.setIdentity();
		rotate(rot);
	}

	/**
	 * Scales the object by a factor.
	 * 
	 * @param factor
	 *            scaling factor
	 */
	public void scale(float factor) {
		scale.scale(factor);
	}

	/**
	 * Scales the object by different factors.
	 * 
	 * @param scalex
	 *            x-axis scaling factor
	 * @param scaley
	 *            y-axis scaling factor
	 * @param scalez
	 *            z-axis scaling factor
	 */
	public void scale(float scalex, float scaley, float scalez) {
		scale.scale(scalex, scaley, scalez);
	}

	/**
	 * @see BaseObject#scale(float, float, float)
	 */
	public void scale(Vector3 factors) {
		scale.scale(factors);
	}

	// /**
	// * Sets the object matrix.
	// *
	// * @param mat
	// * new matrix
	// */
	// public void setMatrix(Matrix4f mat) {
	// matrix = mat;
	// }

	/**
	 * Sets the object rotation.
	 * 
	 * @param rot
	 *            rotation to set to
	 */
	public void setRotation(Quaternionf rot) {
		this.rotation = rot;
	}

	/**
	 * Sets the rotation center relative to the object position.
	 * 
	 * @param centerx
	 *            x-position of the rotation center
	 * @param centery
	 *            y-position of the rotation center
	 * @param centerz
	 *            z-position of the rotation center
	 */
	public void setRotationCenter(float centerx, float centery, float centerz) {
		setRotationCenter(new Vector3f(centerx, centery, centerz));
	}

	/**
	 * Sets the rotation center relative to the object position.
	 * 
	 * @param center
	 *            new rotation center
	 */
	public void setRotationCenter(Vector3f center) {
		rotcenter = center;
	}

	/**
	 * Sets the object translation.
	 * 
	 * @param trans
	 *            translation to set to
	 */
	public void setTranslation(Vector3f trans) {
		this.translation = trans;
	}

	// /**
	// * Transforms the object matrix by a given one.
	// *
	// * @param mat
	// * matrix to transform by
	// */
	// public void transform(Matrix4f mat) {
	// matrix = VecMath.transformMatrix(matrix, mat);
	// }

	/**
	 * Translates the object.
	 * 
	 * @param x
	 *            x-axis translation
	 * @param y
	 *            y-axis translation
	 */
	public void translate(float x, float y) {
		translation.translate(x, y, 0);
	}

	/**
	 * Translates the object.
	 * 
	 * @param x
	 *            x-axis translation
	 * @param y
	 *            y-axis translation
	 * @param z
	 *            z-axis translation
	 */
	public void translate(float x, float y, float z) {
		translation.translate(x, y, z);
	}

	/**
	 * Translates the object by a vector.
	 * 
	 * @param trans
	 *            translation vector
	 */
	public void translate(Vector2 trans) {
		translation.translate(trans.getX(), trans.getY(), 0);
	}

	/**
	 * @see BaseObject#translate(Vector2)
	 * @param trans
	 */
	public void translate(Vector3 trans) {
		translation.translate(trans);
	}

	// /**
	// * Translates the object relative to its rotation.
	// *
	// * @param x
	// * relative x-axis translation
	// * @param y
	// * relative y-axis translation
	// */
	// public void translateRelative(float x, float y) {
	//
	// matrix.translateRelative(new Vector3f(x, y, 0));
	// }
	//
	// /**
	// * Translates the object relative to its rotation.
	// *
	// * @param x
	// * relative x-axis translation
	// * @param y
	// * relative y-axis translation
	// * @param z
	// * relative z-axis translation
	// */
	// public void translateRelative(float x, float y, float z) {
	// matrix.translateRelative(new Vector3f(x, y, z));
	// }
	//
	// /**
	// * Translates the object by a vector relative to its rotation.
	// *
	// * @param trans
	// * relative translation vector
	// */
	// public void translateRelative(Vector2 trans) {
	// matrix.translateRelative(trans);
	// }
	//
	// /**
	// * @see BaseObject#translateRelative(Vector2)
	// */
	// public void translateRelative(Vector3 trans) {
	// matrix.translateRelative(trans);
	// }

	/**
	 * Sets the object translation.
	 * 
	 * @param x
	 *            new x-axis object translation
	 * @param y
	 *            new y-axis object translation
	 */
	public void translateTo(float x, float y) {
		translation.setX(x);
		translation.setY(y);
	}

	/**
	 * Sets the object translation.
	 * 
	 * @param x
	 *            new x-axis object translation
	 * @param y
	 *            new y-axis object translation
	 * @param z
	 *            new z-axis object translation
	 */
	public void translateTo(float x, float y, float z) {
		translation.set(x, y, z);
	}

	/**
	 * Sets the object translation to a vector.
	 * 
	 * @param pos
	 *            new translation vector
	 */
	public void translateTo(Vector2 pos) {
		translation.setX(pos.getX());
		translation.setY(pos.getY());
	}

	/**
	 * @see BaseObject#translateTo(Vector2)
	 */
	public void translateTo(Vector3 pos) {
		translation.set(pos);
	}
}