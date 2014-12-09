package matrix;

import java.nio.FloatBuffer;

import quaternion.Rotation;

/**
 * The base class for all matrices. The matrices are stored in float or double
 * arrays in the form of array[column][row].
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Matrix implements Rotation {

	/**
	 * Calculates the determinant of the matrix.
	 * 
	 * @return determinant of the matrix
	 */
	public abstract double determinant();

	/**
	 * @see Matrix#determinant()
	 */
	public abstract float determinantf();

	/**
	 * Gets a matrix value at in a row and a column.
	 * 
	 * @param x
	 *            column
	 * @param y
	 *            row
	 * @return value at that position
	 */
	public abstract double get(int x, int y);

	/**
	 * Gets the backing array for the matrix.
	 * 
	 * @return array containing matrix values
	 */
	public abstract double[][] getArray();

	/**
	 * @see Matrix#getArray()
	 */
	public abstract float[][] getArrayf();

	/**
	 * @see Matrix#get(int, int)
	 */
	public abstract float getf(int x, int y);

	/**
	 * Number of columns of the matrix.
	 * 
	 * @return number of columns
	 */
	public abstract int getSizeX();

	/**
	 * Number of rows of the matrix.
	 * 
	 * @return number of rows
	 */
	public abstract int getSizeY();

	/**
	 * Inverts the matrix.
	 */
	public abstract void invert();

	/**
	 * Loads the matrix from a FloatBuffer.
	 * 
	 * @param buf
	 *            FloatBuffer to load from
	 */
	public abstract void load(FloatBuffer buf);

	/**
	 * Loads the transposed matrix from a FloatBuffer.
	 * 
	 * @param buf
	 *            FloatBuffer to load from
	 */
	public abstract void loadTranspose(FloatBuffer buf);

	/**
	 * Negates the matrix values.
	 */
	public abstract void negate();

	/**
	 * Sets a certain matrix entry to a value.
	 * 
	 * @param x
	 *            column position
	 * @param y
	 *            row position
	 * @param value
	 *            value to set the entry to
	 */
	public abstract void set(int x, int y, double value);

	/**
	 * @see Matrix#set(int, int, double)
	 */
	public abstract void set(int x, int y, float value);

	/**
	 * Sets all matrix entries to a value.
	 * 
	 * @param set
	 *            value to set the entries to
	 */
	public abstract void setAll(double set);

	/**
	 * @see Matrix#setAll(double)
	 */
	public abstract void setAll(float set);

	/**
	 * Sets the backing array.
	 * 
	 * @param array
	 *            contains the new matrix values
	 */
	public abstract void setArray(double[][] array);

	/**
	 * @see Matrix#setArray(double[][])
	 */
	public abstract void setArray(float[][] array);

	/**
	 * Sets the matrix to the identity.
	 */
	public abstract void setIdentity();

	/**
	 * Stores the matrix in a FloatBuffer.
	 * 
	 * @param buf
	 *            the FloatBuffer the matrix gets stored in
	 */
	public abstract void store(FloatBuffer buf);

	/**
	 * Stores the transposed matrix in a FloatBuffer.
	 * 
	 * @param buf
	 *            the FloatBuffer the transposed matrix gets stored in
	 */
	public abstract void storeTranspose(FloatBuffer buf);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();

	/**
	 * Transposes the matrix.
	 */
	public abstract void transpose();
}