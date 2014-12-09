package matrix;

import java.nio.FloatBuffer;

import vector.Vector1;
import vector.Vector1d;

/**
 * Holds a 1-dimensional double matrix.
 * 
 * @author Oliver Schall
 * 
 */

public class Matrix1d extends Matrix1 {
	double m00;

	public Matrix1d() {
		m00 = 1;
	}

	public Matrix1d(double m00) {
		this.m00 = m00;
	}

	public Matrix1d(double[][] matrix) {
		m00 = matrix[0][0];
	}

	public Matrix1d(Matrix1 matrix) {
		set(matrix);
	}

	public Matrix1d(Matrix1d matrix) {
		m00 = matrix.m00;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double determinant() {
		return m00;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float determinantf() {
		return (float) m00;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double get(int x, int y) {
		if (x == 0 && y == 0)
			return m00;
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[][] getArray() {
		return new double[][] { { m00 } };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float[][] getArrayf() {
		return new float[][] { { (float) m00 } };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector1 getColumn(int column) {
		if (column == 0)
			return new Vector1d(m00);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getf(int x, int y) {
		if (x == 0 && y == 0)
			return (float) m00;
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector1 getRow(int row) {
		if (row == 0)
			return new Vector1d(m00);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		m00 = 1 / m00;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(FloatBuffer buf) {
		m00 = buf.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadTranspose(FloatBuffer buf) {
		m00 = buf.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void negate() {
		m00 = -m00;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int x, int y, double value) {
		if (x == 0 && y == 0)
			m00 = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int x, int y, float value) {
		if (x == 0 && y == 0)
			m00 = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Matrix1 mat) {
		m00 = mat.getArray()[0][0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double set) {
		m00 = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(float set) {
		m00 = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(double[][] array) {
		m00 = array[0][0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(float[][] array) {
		m00 = array[0][0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentity() {
		m00 = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void store(FloatBuffer buf) {
		buf.put((float) m00);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeTranspose(FloatBuffer buf) {
		buf.put((float) m00);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Matrix1d[");
		sb.append(m00);
		sb.append(']');
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transpose() {
	}
}