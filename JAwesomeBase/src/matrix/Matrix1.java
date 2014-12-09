package matrix;

import vector.Vector1;

/**
 * Superclass for 1-dimensional matrices.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Matrix1 extends Matrix {
	/**
	 * Gets the vector of a column.
	 * 
	 * @param column
	 *            number of the column
	 * @return column vector
	 */
	public abstract Vector1 getColumn(int column);

	/**
	 * Gets the vector of a row.
	 * 
	 * @param row
	 *            number of the row
	 * @return row vector
	 */
	public abstract Vector1 getRow(int row);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeX() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeY() {
		return 1;
	}

	/**
	 * Sets the matrix to the given one.
	 * 
	 * @param mat
	 *            matrix to change to
	 */
	public abstract void set(Matrix1 mat);
}