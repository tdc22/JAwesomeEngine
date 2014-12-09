package matrix;

import vector.Vector3;

/**
 * Superclass for 3-dimensional matrices.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Matrix3 extends Matrix {
	/**
	 * @see Matrix1#getColumn(int)
	 */
	public abstract Vector3 getColumn(int column);

	/**
	 * @see Matrix1#getRow(int)
	 */
	public abstract Vector3 getRow(int row);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeX() {
		return 3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeY() {
		return 3;
	}

	/**
	 * @see Matrix1#set(Matrix1)
	 */
	public abstract void set(Matrix3 mat);
}
