package matrix;

import java.nio.FloatBuffer;

import vector.Vector2;
import vector.Vector2d;

/**
 * Holds a 2-dimensional double matrix.
 * 
 * @author Oliver Schall
 * 
 */

public class Matrix2d extends Matrix2 {
	double[][] matrix;

	public Matrix2d() {
		matrix = new double[2][2];
		setIdentity();
	}

	public Matrix2d(double setAll) {
		this.matrix = new double[2][2];
		setAll(setAll);
	}

	public Matrix2d(double m00, double m01, double m10, double m11) {
		matrix = new double[2][2];
		matrix[0][0] = m00;
		matrix[0][1] = m01;
		matrix[1][0] = m10;
		matrix[1][1] = m11;
	}

	public Matrix2d(double[][] matrix) {
		this.matrix = matrix;
	}

	public Matrix2d(Matrix2 matrix) {
		this.matrix = new double[2][2];
		set(matrix);
	}

	public Matrix2d(Matrix2d matrix) {
		this.matrix = new double[2][2];
		setArray(matrix.matrix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double determinant() {
		return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float determinantf() {
		return (float) (matrix[0][0] * matrix[1][1] - matrix[0][1]
				* matrix[1][0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double get(int x, int y) {
		return matrix[x][y];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[][] getArray() {
		return matrix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float[][] getArrayf() {
		float[][] array = new float[2][2];
		array[0][0] = (float) matrix[0][0];
		array[0][1] = (float) matrix[0][1];
		array[1][0] = (float) matrix[1][0];
		array[1][1] = (float) matrix[1][1];
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2 getColumn(int column) {
		return new Vector2d(matrix[0][column], matrix[1][column]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getf(int x, int y) {
		return (float) matrix[x][y];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2 getRow(int row) {
		return new Vector2d(matrix[row][0], matrix[row][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		double determinant = determinant();
		if (determinant != 0) {
			double determinant_inv = 1 / determinant;

			double t00 = matrix[1][1] * determinant_inv;
			double t01 = -matrix[0][1] * determinant_inv;
			double t10 = -matrix[1][0] * determinant_inv;
			double t11 = matrix[0][0] * determinant_inv;

			matrix[0][0] = t00;
			matrix[0][1] = t01;
			matrix[1][0] = t10;
			matrix[1][1] = t11;
		} else
			throw new IllegalStateException("Determinant is 0");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(FloatBuffer buf) {
		matrix[0][0] = buf.get();
		matrix[0][1] = buf.get();
		matrix[1][0] = buf.get();
		matrix[1][1] = buf.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadTranspose(FloatBuffer buf) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void negate() {
		matrix[0][0] = -matrix[0][0];
		matrix[0][1] = -matrix[0][1];
		matrix[1][0] = -matrix[1][0];
		matrix[1][1] = -matrix[1][1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int x, int y, double value) {
		matrix[x][y] = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int x, int y, float value) {
		matrix[x][y] = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Matrix2 mat) {
		setArray(mat.getArray());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double set) {
		matrix[0][0] = set;
		matrix[0][1] = set;
		matrix[1][0] = set;
		matrix[1][1] = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(float set) {
		matrix[0][0] = set;
		matrix[0][1] = set;
		matrix[1][0] = set;
		matrix[1][1] = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(double[][] array) {
		matrix[0][0] = array[0][0];
		matrix[0][1] = array[0][1];
		matrix[1][0] = array[1][0];
		matrix[1][1] = array[1][1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(float[][] array) {
		matrix[0][0] = array[0][0];
		matrix[0][1] = array[0][1];
		matrix[1][0] = array[1][0];
		matrix[1][1] = array[1][1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentity() {
		matrix[0][0] = 1;
		matrix[0][1] = 0;
		matrix[1][0] = 0;
		matrix[1][1] = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void store(FloatBuffer buf) {
		buf.put((float) matrix[0][0]);
		buf.put((float) matrix[0][1]);
		buf.put((float) matrix[1][0]);
		buf.put((float) matrix[1][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeTranspose(FloatBuffer buf) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Matrix2d[");
		sb.append(matrix[0][0]).append(' ').append(matrix[0][1]).append('\n');
		sb.append(matrix[1][0]).append(' ').append(matrix[1][1]);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transpose() {
		double t = matrix[0][1];
		matrix[0][1] = matrix[1][0];
		matrix[1][0] = t;
	}
}
