package matrix;

import java.nio.FloatBuffer;

import vector.Vector3;
import vector.Vector3f;

/**
 * Holds a 3-dimensional float matrix.
 * 
 * @author Oliver Schall
 * 
 */

public class Matrix3f extends Matrix3 {
	public float[][] matrix;

	public Matrix3f() {
		matrix = new float[3][3];
		setIdentity();
	}

	public Matrix3f(float setAll) {
		this.matrix = new float[3][3];
		setAll(setAll);
	}

	public Matrix3f(float m00, float m01, float m02, float m10, float m11,
			float m12, float m20, float m21, float m22) {
		matrix = new float[3][3];
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

	public Matrix3f(float[][] matrix) {
		this.matrix = matrix;
	}

	public Matrix3f(Matrix3 matrix) {
		this.matrix = new float[3][3];
		set(matrix);
	}

	public Matrix3f(Matrix3f matrix) {
		this.matrix = new float[3][3];
		setArray(matrix.matrix);
	}

	@Override
	public double determinant() {
		return determinantf();
	}

	@Override
	public float determinantf() {
		return matrix[0][0]
				* (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
				+ matrix[0][1]
				* (matrix[1][2] * matrix[2][0] - matrix[1][0] * matrix[2][2])
				+ matrix[0][2]
				* (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
	}

	@Override
	public double get(int x, int y) {
		return matrix[x][y];
	}

	@Override
	public double[][] getArray() {
		double[][] array = new double[3][3];
		array[0][0] = matrix[0][0];
		array[0][1] = matrix[0][1];
		array[0][2] = matrix[0][2];
		array[1][0] = matrix[1][0];
		array[1][1] = matrix[1][1];
		array[1][2] = matrix[1][2];
		array[2][0] = matrix[2][0];
		array[2][1] = matrix[2][1];
		array[2][2] = matrix[2][2];
		return array;
	}

	@Override
	public float[][] getArrayf() {
		return matrix;
	}

	@Override
	public Vector3 getColumn(int column) {
		return new Vector3f(matrix[0][column], matrix[1][column],
				matrix[2][column]);
	}

	@Override
	public float getf(int x, int y) {
		return matrix[x][y];
	}

	@Override
	public Vector3 getRow(int row) {
		return new Vector3f(matrix[row][0], matrix[row][1], matrix[row][2]);
	}

	@Override
	public void invert() {
		float determinant = determinantf();
		if (determinant != 0) {
			float determinant_inv = 1 / determinant;

			float t00 = determinant_inv
					* (matrix[1][1] * matrix[2][2] - matrix[1][2]
							* matrix[2][1]);
			float t01 = determinant_inv
					* (matrix[0][2] * matrix[2][1] - matrix[0][1]
							* matrix[2][2]);
			float t02 = determinant_inv
					* (matrix[0][1] * matrix[1][2] - matrix[0][2]
							* matrix[1][1]);
			float t10 = determinant_inv
					* (matrix[1][2] * matrix[2][0] - matrix[1][0]
							* matrix[2][2]);
			float t11 = determinant_inv
					* (matrix[0][0] * matrix[2][2] - matrix[0][2]
							* matrix[2][0]);
			float t12 = determinant_inv
					* (matrix[0][2] * matrix[1][0] - matrix[0][0]
							* matrix[1][2]);
			float t20 = determinant_inv
					* (matrix[1][0] * matrix[2][1] - matrix[1][1]
							* matrix[2][0]);
			float t21 = determinant_inv
					* (matrix[0][1] * matrix[2][0] - matrix[0][0]
							* matrix[2][1]);
			float t22 = determinant_inv
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

	@Override
	public void loadTranspose(FloatBuffer buf) {
		// TODO Auto-generated method stub

	}

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

	@Override
	public void set(int x, int y, double value) {
		matrix[x][y] = (float) value;
	}

	@Override
	public void set(int x, int y, float value) {
		matrix[x][y] = value;
	}

	@Override
	public void set(Matrix3 mat) {
		setArray(mat.getArrayf());
	}

	@Override
	public void setAll(double set) {
		matrix[0][0] = (float) set;
		matrix[0][1] = (float) set;
		matrix[0][2] = (float) set;
		matrix[1][0] = (float) set;
		matrix[1][1] = (float) set;
		matrix[1][2] = (float) set;
		matrix[2][0] = (float) set;
		matrix[2][1] = (float) set;
		matrix[2][2] = (float) set;
	}

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

	@Override
	public void setArray(double[][] array) {
		matrix[0][0] = (float) array[0][0];
		matrix[0][1] = (float) array[0][1];
		matrix[0][2] = (float) array[0][2];
		matrix[1][0] = (float) array[1][0];
		matrix[1][1] = (float) array[1][1];
		matrix[1][2] = (float) array[1][2];
		matrix[2][0] = (float) array[2][0];
		matrix[2][1] = (float) array[2][1];
		matrix[2][2] = (float) array[2][2];
	}

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

	@Override
	public void store(FloatBuffer buf) {
		buf.put(matrix[0][0]);
		buf.put(matrix[0][1]);
		buf.put(matrix[0][2]);
		buf.put(matrix[1][0]);
		buf.put(matrix[1][1]);
		buf.put(matrix[1][2]);
		buf.put(matrix[2][0]);
		buf.put(matrix[2][1]);
		buf.put(matrix[2][2]);
	}

	@Override
	public void storeTranspose(FloatBuffer buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Matrix3f[");
		sb.append(matrix[0][0]).append(' ').append(matrix[1][0]).append(' ')
				.append(matrix[2][0]).append('\n');
		sb.append(matrix[0][1]).append(' ').append(matrix[1][1]).append(' ')
				.append(matrix[2][1]).append('\n');
		sb.append(matrix[0][2]).append(' ').append(matrix[1][2]).append(' ')
				.append(matrix[2][2]);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public void transpose() {
		float[][] tmp = matrix;
		matrix[0][1] = tmp[1][0];
		matrix[0][2] = tmp[2][0];
		matrix[1][0] = tmp[0][1];
		matrix[1][2] = tmp[2][1];
		matrix[2][0] = tmp[0][2];
		matrix[2][1] = tmp[1][2];
	}

}
