package matrix;

import java.nio.FloatBuffer;

import vector.Vector3;
import vector.Vector3d;

/**
 * Holds a 3-dimensional double matrix.
 * 
 * @author Oliver Schall
 * 
 */

public class Matrix3d extends Matrix3 {
	double[][] matrix;

	public Matrix3d() {
		matrix = new double[3][3];
		setIdentity();
	}

	public Matrix3d(double setAll) {
		this.matrix = new double[3][3];
		setAll(setAll);
	}

	public Matrix3d(double m00, double m01, double m02, double m10, double m11,
			double m12, double m20, double m21, double m22) {
		matrix = new double[3][3];
		matrix[0][0] = m00;
		matrix[0][1] = m01;
		matrix[0][2] = m02;
		matrix[1][0] = m10;
		matrix[1][1] = m11;
		matrix[1][2] = m12;
		matrix[2][0] = m20;
		matrix[2][1] = m21;
		matrix[2][2] = m22;
	}

	public Matrix3d(double[][] matrix) {
		this.matrix = matrix;
	}

	public Matrix3d(Matrix3 matrix) {
		this.matrix = new double[3][3];
		set(matrix);
	}

	public Matrix3d(Matrix3d matrix) {
		this.matrix = new double[3][3];
		setArray(matrix.matrix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double determinant() {
		return matrix[0][0]
				* (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
				+ matrix[0][1]
				* (matrix[1][2] * matrix[2][0] - matrix[1][0] * matrix[2][2])
				+ matrix[0][2]
				* (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float determinantf() {
		return (float) determinant();
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
		float[][] array = new float[3][3];
		array[0][0] = (float) matrix[0][0];
		array[0][1] = (float) matrix[0][1];
		array[0][2] = (float) matrix[0][2];
		array[1][0] = (float) matrix[1][0];
		array[1][1] = (float) matrix[1][1];
		array[1][2] = (float) matrix[1][2];
		array[2][0] = (float) matrix[2][0];
		array[2][1] = (float) matrix[2][1];
		array[2][2] = (float) matrix[2][2];
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3 getColumn(int column) {
		return new Vector3d(matrix[0][column], matrix[1][column],
				matrix[2][column]);
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
	public Vector3 getRow(int row) {
		return new Vector3d(matrix[row][0], matrix[row][1], matrix[row][2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		double determinant = determinant();
		if (determinant != 0) {
			double determinant_inv = 1 / determinant;

			double t00 = determinant_inv
					* (matrix[1][1] * matrix[2][2] - matrix[1][2]
							* matrix[2][1]);
			double t01 = determinant_inv
					* (matrix[0][2] * matrix[2][1] - matrix[0][1]
							* matrix[2][2]);
			double t02 = determinant_inv
					* (matrix[0][1] * matrix[1][2] - matrix[0][2]
							* matrix[1][1]);
			double t10 = determinant_inv
					* (matrix[1][2] * matrix[2][0] - matrix[1][0]
							* matrix[2][2]);
			double t11 = determinant_inv
					* (matrix[0][0] * matrix[2][2] - matrix[0][2]
							* matrix[2][0]);
			double t12 = determinant_inv
					* (matrix[0][2] * matrix[1][0] - matrix[0][0]
							* matrix[1][2]);
			double t20 = determinant_inv
					* (matrix[1][0] * matrix[2][1] - matrix[1][1]
							* matrix[2][0]);
			double t21 = determinant_inv
					* (matrix[0][1] * matrix[2][0] - matrix[0][0]
							* matrix[2][1]);
			double t22 = determinant_inv
					* (matrix[0][0] * matrix[1][1] - matrix[0][1]
							* matrix[1][0]);

			matrix[0][0] = t00;
			matrix[1][0] = t10;
			matrix[2][0] = t20;

			matrix[0][1] = t01;
			matrix[1][1] = t11;
			matrix[2][1] = t21;

			matrix[0][2] = t02;
			matrix[1][2] = t12;
			matrix[2][2] = t22;
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
		matrix[0][2] = buf.get();
		matrix[1][0] = buf.get();
		matrix[1][1] = buf.get();
		matrix[1][2] = buf.get();
		matrix[2][0] = buf.get();
		matrix[2][1] = buf.get();
		matrix[2][2] = buf.get();
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
		matrix[0][2] = -matrix[0][2];
		matrix[1][0] = -matrix[1][0];
		matrix[1][1] = -matrix[1][1];
		matrix[1][2] = -matrix[1][2];
		matrix[2][0] = -matrix[2][0];
		matrix[2][1] = -matrix[2][1];
		matrix[2][2] = -matrix[2][2];
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
	public void set(Matrix3 mat) {
		setArray(mat.getArray());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double set) {
		matrix[0][0] = set;
		matrix[0][1] = set;
		matrix[0][2] = set;
		matrix[1][0] = set;
		matrix[1][1] = set;
		matrix[1][2] = set;
		matrix[2][0] = set;
		matrix[2][1] = set;
		matrix[2][2] = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(float set) {
		matrix[0][0] = set;
		matrix[0][1] = set;
		matrix[0][2] = set;
		matrix[1][0] = set;
		matrix[1][1] = set;
		matrix[1][2] = set;
		matrix[2][0] = set;
		matrix[2][1] = set;
		matrix[2][2] = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(double[][] array) {
		matrix[0][0] = array[0][0];
		matrix[0][1] = array[0][1];
		matrix[0][2] = array[0][2];
		matrix[1][0] = array[1][0];
		matrix[1][1] = array[1][1];
		matrix[1][2] = array[1][2];
		matrix[2][0] = array[2][0];
		matrix[2][1] = array[2][1];
		matrix[2][2] = array[2][2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(float[][] array) {
		matrix[0][0] = array[0][0];
		matrix[0][1] = array[0][1];
		matrix[0][2] = array[0][2];
		matrix[1][0] = array[1][0];
		matrix[1][1] = array[1][1];
		matrix[1][2] = array[1][2];
		matrix[2][0] = array[2][0];
		matrix[2][1] = array[2][1];
		matrix[2][2] = array[2][2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentity() {
		matrix[0][0] = 1;
		matrix[0][1] = 0;
		matrix[0][2] = 0;
		matrix[1][0] = 0;
		matrix[1][1] = 1;
		matrix[1][2] = 0;
		matrix[2][0] = 0;
		matrix[2][1] = 0;
		matrix[2][2] = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void store(FloatBuffer buf) {
		buf.put((float) matrix[0][0]);
		buf.put((float) matrix[0][1]);
		buf.put((float) matrix[0][2]);
		buf.put((float) matrix[1][0]);
		buf.put((float) matrix[1][1]);
		buf.put((float) matrix[1][2]);
		buf.put((float) matrix[2][0]);
		buf.put((float) matrix[2][1]);
		buf.put((float) matrix[2][2]);
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
		sb.append("Matrix3d[");
		sb.append(matrix[0][0]).append(' ').append(matrix[1][0]).append(' ')
				.append(matrix[2][0]).append('\n');
		sb.append(matrix[0][1]).append(' ').append(matrix[1][1]).append(' ')
				.append(matrix[2][1]).append('\n');
		sb.append(matrix[0][2]).append(' ').append(matrix[1][2]).append(' ')
				.append(matrix[2][2]);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transpose() {
		double[][] tmp = matrix;
		matrix[0][1] = tmp[1][0];
		matrix[0][2] = tmp[2][0];
		matrix[1][0] = tmp[0][1];
		matrix[1][2] = tmp[2][1];
		matrix[2][0] = tmp[0][2];
		matrix[2][1] = tmp[1][2];
	}
}