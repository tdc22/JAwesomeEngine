package matrix;

import java.nio.FloatBuffer;

import quaternion.Complex;
import quaternion.Complexf;
import vector.Vector2;
import vector.Vector2f;

/**
 * Holds a 2-dimensional float matrix.
 * 
 * @author Oliver Schall
 * 
 */

public class Matrix2f extends Matrix2 {
	public float[][] matrix;

	public Matrix2f() {
		matrix = new float[2][2];
		setIdentity();
	}

	public Matrix2f(float setAll) {
		this.matrix = new float[2][2];
		setAll(setAll);
	}

	public Matrix2f(float m00, float m01, float m10, float m11) {
		matrix = new float[2][2];
		matrix[0][0] = m00;
		matrix[0][1] = m01;
		matrix[1][0] = m10;
		matrix[1][1] = m11;
	}

	public Matrix2f(float[][] matrix) {
		this.matrix = matrix;
	}

	public Matrix2f(Matrix2 matrix) {
		this.matrix = new float[2][2];
		set(matrix);
	}

	public Matrix2f(Matrix2f matrix) {
		this.matrix = new float[2][2];
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
		return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
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
		double[][] array = new double[2][2];
		array[0][0] = matrix[0][0];
		array[0][1] = matrix[0][1];
		array[1][0] = matrix[1][0];
		array[1][1] = matrix[1][1];
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float[][] getArrayf() {
		return matrix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2 getColumn(int column) {
		return new Vector2f(matrix[0][column], matrix[0][column]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getf(int x, int y) {
		return matrix[x][y];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2 getRow(int row) {
		return new Vector2f(matrix[row][0], matrix[row][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		float determinant = determinantf();
		if (determinant != 0) {
			float determinant_inv = 1 / determinant;

			float t00 = matrix[1][1] * determinant_inv;
			float t01 = -matrix[0][1] * determinant_inv;
			float t10 = -matrix[1][0] * determinant_inv;
			float t11 = matrix[0][0] * determinant_inv;

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
		matrix[x][y] = (float) value;
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
		setArray(mat.getArrayf());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double set) {
		matrix[0][0] = (float) set;
		matrix[0][1] = (float) set;
		matrix[1][0] = (float) set;
		matrix[1][1] = (float) set;
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
		matrix[0][0] = (float) array[0][0];
		matrix[0][1] = (float) array[0][1];
		matrix[1][0] = (float) array[1][0];
		matrix[1][1] = (float) array[1][1];
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
		buf.put(matrix[0][0]);
		buf.put(matrix[0][1]);
		buf.put(matrix[1][0]);
		buf.put(matrix[1][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeTranspose(FloatBuffer buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public Complex toComplex() {
		return toComplexf();
	}

	@Override
	public Complex toComplexDiagonal() {
		return toComplexDiagonalf();
	}

	@Override
	public Complexf toComplexDiagonalf() {
		return new Complexf(Math.sqrt(1 + matrix[0][0] + matrix[1][1]) / 2, 0);
	}

	@Override
	public Complexf toComplexf() {
		float tr = matrix[0][0] + matrix[1][1];
		if (tr > 0) {
			float S = (float) (Math.sqrt(tr + 1.0) * 2);
			return new Complexf(0.25 * S, (matrix[1][0] - matrix[0][1]) / S);
		} else {
			float S = (float) (Math.sqrt(1.0 + matrix[0][0] - matrix[1][1] - 0) * 2);
			return new Complexf((matrix[0][1] + matrix[1][0]) / S, 0.25 * S);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Matrix2f[");
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
		float t = matrix[0][1];
		matrix[0][1] = matrix[1][0];
		matrix[1][0] = t;
	}
}