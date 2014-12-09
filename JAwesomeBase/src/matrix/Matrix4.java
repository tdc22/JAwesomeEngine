package matrix;

import vector.Vector2;
import vector.Vector3;
import vector.Vector4;

/**
 * Superclass for 4-dimensional matrices. Represents an OpenGL object matrix.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Matrix4 extends Matrix {
	/**
	 * Calculates the determinant of the rotation sub matrix.
	 * 
	 * @return determinant of the rotation matrix
	 */
	public abstract double determinant3();

	/**
	 * @see Matrix4#determinant3()
	 */
	public abstract float determinant3f();

	/**
	 * @see Matrix1#getColumn(int)
	 */
	public abstract Vector4 getColumn(int column);

	/**
	 * @see Matrix1#getRow(int)
	 */
	public abstract Vector4 getRow(int row);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeX() {
		return 4;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeY() {
		return 4;
	}

	/**
	 * Gets the rotation sub matrix.
	 * 
	 * @return 3d rotation sub matrix
	 */
	public abstract Matrix3 getSubMatrix();

	/**
	 * Gets the 2d-rotation sub matrix.
	 * 
	 * @return 3d rotation sub matrix
	 */
	public abstract Matrix2 getSubMatrix2();

	/**
	 * Gets the translation of the OpenGL-matrix.
	 * 
	 * @return translation
	 */
	public abstract Vector3 getTranslation();

	/**
	 * Gets the 2d-translation of the OpenGL-matrix.
	 * 
	 * @return 2d-translation
	 */
	public abstract Vector2 getTranslation2();

	/**
	 * Rotates the matrix by an angle around an axis.
	 * 
	 * @param angle
	 *            rotation angle
	 * @param axis
	 *            rotation axis
	 */
	public abstract void rotate(double angle, Vector3 axis);

	/**
	 * @see Matrix4#rotate(double, Vector3)
	 */
	public abstract void rotate(float angle, Vector3 axis);

	/**
	 * Rotates the matrix by another one.
	 * 
	 * @param matrix
	 *            matrix to rotate around
	 */
	public abstract void rotate(Matrix2 matrix);

	/**
	 * @see Matrix4#rotate(Matrix2)
	 */
	public abstract void rotate(Matrix3 matrix);

	/**
	 * Scales the matrix.
	 * 
	 * @param scale
	 *            factor to scale by
	 */
	public abstract void scale(double scale);

	/**
	 * @see Matrix4#scale(double)
	 */
	public abstract void scale(float scale);

	/**
	 * Scales the matrix by a Vector.
	 * 
	 * @param v
	 *            vector to scale by
	 */
	public abstract void scale(Vector2 v);

	/**
	 * @see Matrix4#scale(Vector2)
	 */
	public abstract void scale(Vector3 v);

	/**
	 * @see Matrix1#set(Matrix1)
	 */
	public abstract void set(Matrix4 mat);

	/**
	 * Sets the rotation sub matrix.
	 * 
	 * @param mat
	 *            new rotation matrix
	 */
	public abstract void setSubMatrix(Matrix3 mat);

	/**
	 * @see Matrix4#setSubMatrix(Matrix3)
	 */
	public abstract void setSubMatrix(Matrix3f mat);

	/**
	 * Sets the 2d-rotation sub matrix.
	 * 
	 * @param mat
	 *            new 2d-rotation matrix
	 */
	public abstract void setSubMatrix2(Matrix2 mat);

	/**
	 * @see Matrix4#setSubMatrix2(Matrix2)
	 */
	public abstract void setSubMatrix2(Matrix2f mat);

	/**
	 * Sets the rotation sub matrix to the identity.
	 */
	public abstract void setSubMatrixIdentity();

	/**
	 * Sets the 2d-rotation sub matrix to the identity.
	 */
	public abstract void setSubMatrixIdentity2();

	/**
	 * Translates the matrix by a vector.
	 * 
	 * @param v
	 *            vector to translate by
	 */
	public abstract void translate(Vector2 v);

	/**
	 * @see Matrix4#translate(Vector2)
	 */
	public abstract void translate(Vector3 v);

	/**
	 * Translates the matrix relative to its rotation by a vector.
	 * 
	 * @param v
	 *            vector to translate by
	 */
	public abstract void translateRelative(Vector2 v);

	/**
	 * @see Matrix4#translateRelative(Vector2)
	 */
	public abstract void translateRelative(Vector3 v);

	// public abstract void normalizeRotation();
	//
	// public abstract void normalizeRotation2();

	/**
	 * Sets the translation.
	 * 
	 * @param v
	 *            new translation position
	 */
	public abstract void translateTo(Vector2 v);

	/**
	 * @see Matrix4#translateTo(Vector2)
	 */
	public abstract void translateTo(Vector3 v);
}
