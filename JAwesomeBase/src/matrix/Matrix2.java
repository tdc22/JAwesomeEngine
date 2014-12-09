package matrix;

import vector.Vector2;

/**
 * Superclass for 2-dimensional matrices.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Matrix2 extends Matrix {
	/**
	 * @see Matrix1#getColumn(int)
	 */
	public abstract Vector2 getColumn(int column);

	/**
	 * @see Matrix1#getRow(int)
	 */
	public abstract Vector2 getRow(int row);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeX() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeY() {
		return 2;
	}

	/**
	 * @see Matrix1#set(Matrix1)
	 */
	public abstract void set(Matrix2 mat);
}
