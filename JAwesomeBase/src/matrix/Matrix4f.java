package matrix;

import java.nio.FloatBuffer;

import math.FastMath;
import vector.Vector2;
import vector.Vector2f;
import vector.Vector3;
import vector.Vector3f;
import vector.Vector4;
import vector.Vector4f;

/**
 * Holds a 4-dimensional float matrix.
 * 
 * @author Oliver Schall
 * 
 */

public class Matrix4f extends Matrix4 {
	public float[][] matrix;

	public Matrix4f() {
		matrix = new float[4][4];
		setIdentity();
	}

	public Matrix4f(float setAll) {
		this.matrix = new float[4][4];
		setAll(setAll);
	}

	public Matrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20,
			float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		matrix = new float[4][4];
		matrix[0][0] = m00;
		matrix[0][1] = m01;
		matrix[0][2] = m02;
		matrix[0][3] = m03;
		matrix[1][0] = m10;
		matrix[1][1] = m11;
		matrix[1][2] = m12;
		matrix[1][3] = m13;
		matrix[2][0] = m20;
		matrix[2][1] = m21;
		matrix[2][2] = m22;
		matrix[2][3] = m23;
		matrix[3][0] = m30;
		matrix[3][1] = m31;
		matrix[3][2] = m32;
		matrix[3][3] = m33;
	}

	public Matrix4f(float[][] matrix) {
		this.matrix = matrix;
	}

	public Matrix4f(Matrix4 matrix) {
		this.matrix = new float[4][4];
		set(matrix);
	}

	public Matrix4f(Matrix4f matrix) {
		this.matrix = new float[4][4];
		setArray(matrix.matrix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double determinant() {
		return determinantf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double determinant3() {
		return determinant3f();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float determinant3f() {
		return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
				+ matrix[0][1] * (matrix[1][2] * matrix[2][0] - matrix[1][0] * matrix[2][2])
				+ matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
	}

	/**
	 * {@inheritDoc}
	 */
	private float determinant3x3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21,
			float t22) {
		return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float determinantf() {
		return matrix[0][0]
				* (matrix[1][1] * (matrix[2][2] * matrix[3][3] - matrix[2][3] * matrix[3][2])
						+ matrix[1][2] * (matrix[2][3] * matrix[3][1] - matrix[2][1] * matrix[3][3])
						+ matrix[1][3] * (matrix[2][1] * matrix[3][2] - matrix[2][2] * matrix[3][1]))
				- matrix[0][1] * (matrix[1][0] * (matrix[2][2] * matrix[3][3] - matrix[2][3] * matrix[3][2])
						+ matrix[1][2] * (matrix[2][3] * matrix[3][0] - matrix[2][0] * matrix[3][3])
						+ matrix[1][3] * (matrix[2][0] * matrix[3][2] - matrix[2][2] * matrix[3][0]))
				+ matrix[0][2] * (matrix[1][0] * (matrix[2][1] * matrix[3][3] - matrix[2][3] * matrix[3][1])
						+ matrix[1][1] * (matrix[2][3] * matrix[3][0] - matrix[2][0] * matrix[3][3])
						+ matrix[1][3] * (matrix[2][0] * matrix[3][1] - matrix[2][1] * matrix[3][0]))
				- matrix[0][3] * (matrix[1][0] * (matrix[2][1] * matrix[3][2] - matrix[2][2] * matrix[3][2])
						+ matrix[1][1] * (matrix[2][2] * matrix[3][0] - matrix[2][0] * matrix[3][2])
						+ matrix[1][2] * (matrix[2][0] * matrix[3][1] - matrix[2][1] * matrix[3][0]));
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
		double[][] array = new double[4][4];
		array[0][0] = matrix[0][0];
		array[0][1] = matrix[0][1];
		array[0][2] = matrix[0][2];
		array[0][3] = matrix[0][3];
		array[1][0] = matrix[1][0];
		array[1][1] = matrix[1][1];
		array[1][2] = matrix[1][2];
		array[1][3] = matrix[1][3];
		array[2][0] = matrix[2][0];
		array[2][1] = matrix[2][1];
		array[2][2] = matrix[2][2];
		array[2][3] = matrix[2][3];
		array[3][0] = matrix[3][0];
		array[3][1] = matrix[3][1];
		array[3][2] = matrix[3][2];
		array[3][3] = matrix[3][3];
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
	public Vector4 getColumn(int column) {
		return new Vector4f(matrix[column][0], matrix[column][1], matrix[column][2], matrix[column][3]);
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
	public Vector4 getRow(int row) {
		return new Vector4f(matrix[0][row], matrix[1][row], matrix[2][row], matrix[3][row]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix3 getSubMatrix() {
		return new Matrix3f(matrix[0][0], matrix[0][1], matrix[0][2], matrix[1][0], matrix[1][1], matrix[1][2],
				matrix[2][0], matrix[2][1], matrix[2][2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix2 getSubMatrix2() {
		return new Matrix2f(matrix[0][0], matrix[0][1], matrix[1][0], matrix[1][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3 getTranslation() {
		return new Vector3f(matrix[3][0], matrix[3][1], matrix[3][2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2 getTranslation2() {
		return new Vector2f(matrix[3][0], matrix[3][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		float determinant = determinantf();
		if (determinant != 0) {
			float determinant_inv = 1 / determinant;

			float t00 = determinant3x3(matrix[1][1], matrix[1][2], matrix[1][3], matrix[2][1], matrix[2][2],
					matrix[2][3], matrix[3][1], matrix[3][2], matrix[3][3]) * determinant_inv;
			float t10 = -determinant3x3(matrix[1][0], matrix[1][2], matrix[1][3], matrix[2][0], matrix[2][2],
					matrix[2][3], matrix[3][0], matrix[3][2], matrix[3][3]) * determinant_inv;
			float t20 = determinant3x3(matrix[1][0], matrix[1][1], matrix[1][3], matrix[2][0], matrix[2][1],
					matrix[2][3], matrix[3][0], matrix[3][1], matrix[3][3]) * determinant_inv;
			float t30 = -determinant3x3(matrix[1][0], matrix[1][1], matrix[1][2], matrix[2][0], matrix[2][1],
					matrix[2][2], matrix[3][0], matrix[3][1], matrix[3][2]) * determinant_inv;

			float t01 = -determinant3x3(matrix[0][1], matrix[0][2], matrix[0][3], matrix[2][1], matrix[2][2],
					matrix[2][3], matrix[3][1], matrix[3][2], matrix[3][3]) * determinant_inv;
			float t11 = determinant3x3(matrix[0][0], matrix[0][2], matrix[0][3], matrix[2][0], matrix[2][2],
					matrix[2][3], matrix[3][0], matrix[3][2], matrix[3][3]) * determinant_inv;
			float t21 = -determinant3x3(matrix[0][0], matrix[0][1], matrix[0][3], matrix[2][0], matrix[2][1],
					matrix[2][3], matrix[3][0], matrix[3][1], matrix[3][3]) * determinant_inv;
			float t31 = determinant3x3(matrix[0][0], matrix[0][1], matrix[0][2], matrix[2][0], matrix[2][1],
					matrix[2][2], matrix[3][0], matrix[3][1], matrix[3][2]) * determinant_inv;

			float t02 = determinant3x3(matrix[0][1], matrix[0][2], matrix[0][3], matrix[1][1], matrix[1][2],
					matrix[1][3], matrix[3][1], matrix[3][2], matrix[3][3]) * determinant_inv;
			float t12 = -determinant3x3(matrix[0][0], matrix[0][2], matrix[0][3], matrix[1][0], matrix[1][2],
					matrix[1][3], matrix[3][0], matrix[3][2], matrix[3][3]) * determinant_inv;
			float t22 = determinant3x3(matrix[0][0], matrix[0][1], matrix[0][3], matrix[1][0], matrix[1][1],
					matrix[1][3], matrix[3][0], matrix[3][1], matrix[3][3]) * determinant_inv;
			float t32 = -determinant3x3(matrix[0][0], matrix[0][1], matrix[0][2], matrix[1][0], matrix[1][1],
					matrix[1][2], matrix[3][0], matrix[3][1], matrix[3][2]) * determinant_inv;

			float t03 = -determinant3x3(matrix[0][1], matrix[0][2], matrix[0][3], matrix[1][1], matrix[1][2],
					matrix[1][3], matrix[2][1], matrix[2][2], matrix[2][3]) * determinant_inv;
			float t13 = determinant3x3(matrix[0][0], matrix[0][2], matrix[0][3], matrix[1][0], matrix[1][2],
					matrix[1][3], matrix[2][0], matrix[2][2], matrix[2][3]) * determinant_inv;
			float t23 = -determinant3x3(matrix[0][0], matrix[0][1], matrix[0][3], matrix[1][0], matrix[1][1],
					matrix[1][3], matrix[2][0], matrix[2][1], matrix[2][3]) * determinant_inv;
			float t33 = determinant3x3(matrix[0][0], matrix[0][1], matrix[0][2], matrix[1][0], matrix[1][1],
					matrix[1][2], matrix[2][0], matrix[2][1], matrix[2][2]) * determinant_inv;

			matrix[0][0] = t00;
			matrix[1][0] = t10;
			matrix[2][0] = t20;
			matrix[3][0] = t30;

			matrix[0][1] = t01;
			matrix[1][1] = t11;
			matrix[2][1] = t21;
			matrix[3][1] = t31;

			matrix[0][2] = t02;
			matrix[1][2] = t12;
			matrix[2][2] = t22;
			matrix[3][2] = t32;

			matrix[0][3] = t03;
			matrix[1][3] = t13;
			matrix[2][3] = t23;
			matrix[3][3] = t33;
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
		matrix[0][3] = buf.get();
		matrix[1][0] = buf.get();
		matrix[1][1] = buf.get();
		matrix[1][2] = buf.get();
		matrix[1][3] = buf.get();
		matrix[2][0] = buf.get();
		matrix[2][1] = buf.get();
		matrix[2][2] = buf.get();
		matrix[2][3] = buf.get();
		matrix[3][0] = buf.get();
		matrix[3][1] = buf.get();
		matrix[3][2] = buf.get();
		matrix[3][3] = buf.get();
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
		matrix[0][3] = -matrix[0][3];
		matrix[1][0] = -matrix[1][0];
		matrix[1][1] = -matrix[1][1];
		matrix[1][2] = -matrix[1][2];
		matrix[1][3] = -matrix[1][3];
		matrix[2][0] = -matrix[2][0];
		matrix[2][1] = -matrix[2][1];
		matrix[2][2] = -matrix[2][2];
		matrix[2][3] = -matrix[2][3];
		matrix[3][0] = -matrix[3][0];
		matrix[3][1] = -matrix[3][1];
		matrix[3][2] = -matrix[3][2];
		matrix[3][3] = -matrix[3][3];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(double angle, Vector3 axis) {
		rotate((float) angle, axis);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(float angle, Vector3 axis) {
		angle = (float) Math.toRadians(angle);
		float c = FastMath.cos(angle);
		float s = FastMath.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.getXf() * axis.getYf();
		float yz = axis.getYf() * axis.getZf();
		float xz = axis.getXf() * axis.getZf();
		float xs = axis.getXf() * s;
		float ys = axis.getYf() * s;
		float zs = axis.getZf() * s;

		float f00 = axis.getXf() * axis.getXf() * oneminusc + c;
		float f01 = xy * oneminusc + zs;
		float f02 = xz * oneminusc - ys;
		float f10 = xy * oneminusc - zs;
		float f11 = axis.getYf() * axis.getYf() * oneminusc + c;
		float f12 = yz * oneminusc + xs;
		float f20 = xz * oneminusc + ys;
		float f21 = yz * oneminusc - xs;
		float f22 = axis.getZf() * axis.getZf() * oneminusc + c;

		float t00 = matrix[0][0] * f00 + matrix[1][0] * f01 + matrix[2][0] * f02;
		float t01 = matrix[0][1] * f00 + matrix[1][1] * f01 + matrix[2][1] * f02;
		float t02 = matrix[0][2] * f00 + matrix[1][2] * f01 + matrix[2][2] * f02;
		float t03 = matrix[0][3] * f00 + matrix[1][3] * f01 + matrix[2][3] * f02;
		float t10 = matrix[0][0] * f10 + matrix[1][0] * f11 + matrix[2][0] * f12;
		float t11 = matrix[0][1] * f10 + matrix[1][1] * f11 + matrix[2][1] * f12;
		float t12 = matrix[0][2] * f10 + matrix[1][2] * f11 + matrix[2][2] * f12;
		float t13 = matrix[0][3] * f10 + matrix[1][3] * f11 + matrix[2][3] * f12;
		matrix[2][0] = matrix[0][0] * f20 + matrix[1][0] * f21 + matrix[2][0] * f22;
		matrix[2][1] = matrix[0][1] * f20 + matrix[1][1] * f21 + matrix[2][1] * f22;
		matrix[2][2] = matrix[0][2] * f20 + matrix[1][2] * f21 + matrix[2][2] * f22;
		matrix[2][3] = matrix[0][3] * f20 + matrix[1][3] * f21 + matrix[2][3] * f22;
		matrix[0][0] = t00;
		matrix[0][1] = t01;
		matrix[0][2] = t02;
		matrix[0][3] = t03;
		matrix[1][0] = t10;
		matrix[1][1] = t11;
		matrix[1][2] = t12;
		matrix[1][3] = t13;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Matrix2 mat) {
		float[][] m1 = mat.getArrayf();
		float t00 = matrix[0][0] * m1[0][0] + matrix[1][0] * m1[0][1];
		float t01 = matrix[0][0] * m1[1][0] + matrix[1][0] * m1[1][1];
		matrix[1][0] = matrix[0][1] * m1[0][0] + matrix[1][1] * m1[0][1];
		matrix[1][1] = matrix[0][1] * m1[1][0] + matrix[1][1] * m1[1][1];
		matrix[0][0] = t00;
		matrix[0][1] = t01;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Matrix3 mat) {
		float[][] m1 = mat.getArrayf();
		float t00 = matrix[0][0] * m1[0][0] + matrix[1][0] * m1[0][1] + matrix[2][0] * m1[0][2];
		float t01 = matrix[0][0] * m1[1][0] + matrix[1][0] * m1[1][1] + matrix[2][0] * m1[1][2];
		float t02 = matrix[0][0] * m1[2][0] + matrix[1][0] * m1[2][1] + matrix[2][0] * m1[2][2];
		float t10 = matrix[0][1] * m1[0][0] + matrix[1][1] * m1[0][1] + matrix[2][1] * m1[0][2];
		float t11 = matrix[0][1] * m1[1][0] + matrix[1][1] * m1[1][1] + matrix[2][1] * m1[1][2];
		float t12 = matrix[0][1] * m1[2][0] + matrix[1][1] * m1[2][1] + matrix[2][1] * m1[2][2];
		matrix[2][0] = matrix[0][2] * m1[0][0] + matrix[1][2] * m1[0][1] + matrix[2][2] * m1[0][2];
		matrix[2][1] = matrix[0][2] * m1[1][0] + matrix[1][2] * m1[1][1] + matrix[2][2] * m1[1][2];
		matrix[2][2] = matrix[0][2] * m1[2][0] + matrix[1][2] * m1[2][1] + matrix[2][2] * m1[2][2];
		matrix[0][0] = t00;
		matrix[0][1] = t01;
		matrix[0][2] = t02;
		matrix[1][0] = t10;
		matrix[1][1] = t11;
		matrix[1][2] = t12;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double scale) {
		matrix[0][0] *= scale;
		matrix[0][1] *= scale;
		matrix[0][2] *= scale;
		matrix[0][3] *= scale;
		matrix[1][0] *= scale;
		matrix[1][1] *= scale;
		matrix[1][2] *= scale;
		matrix[1][3] *= scale;
		matrix[2][0] *= scale;
		matrix[2][1] *= scale;
		matrix[2][2] *= scale;
		matrix[2][3] *= scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(float scale) {
		matrix[0][0] *= scale;
		matrix[0][1] *= scale;
		matrix[0][2] *= scale;
		matrix[0][3] *= scale;
		matrix[1][0] *= scale;
		matrix[1][1] *= scale;
		matrix[1][2] *= scale;
		matrix[1][3] *= scale;
		matrix[2][0] *= scale;
		matrix[2][1] *= scale;
		matrix[2][2] *= scale;
		matrix[2][3] *= scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(Vector2 v) {
		matrix[0][0] *= v.getXf();
		matrix[0][1] *= v.getXf();
		matrix[0][2] *= v.getXf();
		matrix[0][3] *= v.getXf();
		matrix[1][0] *= v.getYf();
		matrix[1][1] *= v.getYf();
		matrix[1][2] *= v.getYf();
		matrix[1][3] *= v.getYf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(Vector3 v) {
		matrix[0][0] *= v.getXf();
		matrix[0][1] *= v.getXf();
		matrix[0][2] *= v.getXf();
		matrix[0][3] *= v.getXf();
		matrix[1][0] *= v.getYf();
		matrix[1][1] *= v.getYf();
		matrix[1][2] *= v.getYf();
		matrix[1][3] *= v.getYf();
		matrix[2][0] *= v.getZf();
		matrix[2][1] *= v.getZf();
		matrix[2][2] *= v.getZf();
		matrix[2][3] *= v.getZf();
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
	public void set(Matrix4 mat) {
		setArray(mat.getArrayf());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double set) {
		matrix[0][0] = (float) set;
		matrix[0][1] = (float) set;
		matrix[0][2] = (float) set;
		matrix[0][3] = (float) set;
		matrix[1][0] = (float) set;
		matrix[1][1] = (float) set;
		matrix[1][2] = (float) set;
		matrix[1][3] = (float) set;
		matrix[2][0] = (float) set;
		matrix[2][1] = (float) set;
		matrix[2][2] = (float) set;
		matrix[2][3] = (float) set;
		matrix[3][0] = (float) set;
		matrix[3][1] = (float) set;
		matrix[3][2] = (float) set;
		matrix[3][3] = (float) set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(float set) {
		matrix[0][0] = set;
		matrix[0][1] = set;
		matrix[0][2] = set;
		matrix[0][3] = set;
		matrix[1][0] = set;
		matrix[1][1] = set;
		matrix[1][2] = set;
		matrix[1][3] = set;
		matrix[2][0] = set;
		matrix[2][1] = set;
		matrix[2][2] = set;
		matrix[2][3] = set;
		matrix[3][0] = set;
		matrix[3][1] = set;
		matrix[3][2] = set;
		matrix[3][3] = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(double[][] array) {
		matrix[0][0] = (float) array[0][0];
		matrix[0][1] = (float) array[0][1];
		matrix[0][2] = (float) array[0][2];
		matrix[0][3] = (float) array[0][3];
		matrix[1][0] = (float) array[1][0];
		matrix[1][1] = (float) array[1][1];
		matrix[1][2] = (float) array[1][2];
		matrix[1][3] = (float) array[1][3];
		matrix[2][0] = (float) array[2][0];
		matrix[2][1] = (float) array[2][1];
		matrix[2][2] = (float) array[2][2];
		matrix[2][3] = (float) array[2][3];
		matrix[3][0] = (float) array[3][0];
		matrix[3][1] = (float) array[3][1];
		matrix[3][2] = (float) array[3][2];
		matrix[3][3] = (float) array[3][3];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArray(float[][] array) {
		matrix[0][0] = array[0][0];
		matrix[0][1] = array[0][1];
		matrix[0][2] = array[0][2];
		matrix[0][3] = array[0][3];
		matrix[1][0] = array[1][0];
		matrix[1][1] = array[1][1];
		matrix[1][2] = array[1][2];
		matrix[1][3] = array[1][3];
		matrix[2][0] = array[2][0];
		matrix[2][1] = array[2][1];
		matrix[2][2] = array[2][2];
		matrix[2][3] = array[2][3];
		matrix[3][0] = array[3][0];
		matrix[3][1] = array[3][1];
		matrix[3][2] = array[3][2];
		matrix[3][3] = array[3][3];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentity() {
		matrix[0][0] = 1;
		matrix[0][1] = 0;
		matrix[0][2] = 0;
		matrix[0][3] = 0;
		matrix[1][0] = 0;
		matrix[1][1] = 1;
		matrix[1][2] = 0;
		matrix[1][3] = 0;
		matrix[2][0] = 0;
		matrix[2][1] = 0;
		matrix[2][2] = 1;
		matrix[2][3] = 0;
		matrix[3][0] = 0;
		matrix[3][1] = 0;
		matrix[3][2] = 0;
		matrix[3][3] = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSubMatrix(Matrix3 mat) {
		double[][] tmp = mat.getArray();
		matrix[0][0] = (float) tmp[0][0];
		matrix[0][1] = (float) tmp[0][1];
		matrix[0][2] = (float) tmp[0][2];
		matrix[1][0] = (float) tmp[1][0];
		matrix[1][1] = (float) tmp[1][1];
		matrix[1][2] = (float) tmp[1][2];
		matrix[2][0] = (float) tmp[2][0];
		matrix[2][1] = (float) tmp[2][1];
		matrix[2][2] = (float) tmp[2][2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSubMatrix(Matrix3f mat) {
		float[][] tmp = mat.getArrayf();
		matrix[0][0] = tmp[0][0];
		matrix[0][1] = tmp[0][1];
		matrix[0][2] = tmp[0][2];
		matrix[1][0] = tmp[1][0];
		matrix[1][1] = tmp[1][1];
		matrix[1][2] = tmp[1][2];
		matrix[2][0] = tmp[2][0];
		matrix[2][1] = tmp[2][1];
		matrix[2][2] = tmp[2][2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSubMatrix2(Matrix2 mat) {
		double[][] tmp = mat.getArray();
		matrix[0][0] = (float) tmp[0][0];
		matrix[0][1] = (float) tmp[0][1];
		matrix[1][0] = (float) tmp[1][0];
		matrix[1][1] = (float) tmp[1][1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSubMatrix2(Matrix2f mat) {
		float[][] tmp = mat.getArrayf();
		matrix[0][0] = tmp[0][0];
		matrix[0][1] = tmp[0][1];
		matrix[1][0] = tmp[1][0];
		matrix[1][1] = tmp[1][1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSubMatrixIdentity() {
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
	public void setSubMatrixIdentity2() {
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
		buf.put(matrix[0][2]);
		buf.put(matrix[0][3]);
		buf.put(matrix[1][0]);
		buf.put(matrix[1][1]);
		buf.put(matrix[1][2]);
		buf.put(matrix[1][3]);
		buf.put(matrix[2][0]);
		buf.put(matrix[2][1]);
		buf.put(matrix[2][2]);
		buf.put(matrix[2][3]);
		buf.put(matrix[3][0]);
		buf.put(matrix[3][1]);
		buf.put(matrix[3][2]);
		buf.put(matrix[3][3]);
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
		sb.append("Matrix4f[");
		sb.append(matrix[0][0]).append(' ').append(matrix[1][0]).append(' ').append(matrix[2][0]).append(' ')
				.append(matrix[3][0]).append('\n');
		sb.append(matrix[0][1]).append(' ').append(matrix[1][1]).append(' ').append(matrix[2][1]).append(' ')
				.append(matrix[3][1]).append('\n');
		sb.append(matrix[0][2]).append(' ').append(matrix[1][2]).append(' ').append(matrix[2][2]).append(' ')
				.append(matrix[3][2]).append('\n');
		sb.append(matrix[0][3]).append(' ').append(matrix[1][3]).append(' ').append(matrix[2][3]).append(' ')
				.append(matrix[3][3]);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector2 v) {
		matrix[3][0] += v.getXf();
		matrix[3][1] += v.getYf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3 v) {
		matrix[3][0] += v.getXf();
		matrix[3][1] += v.getYf();
		matrix[3][2] += v.getZf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateRelative(Vector2 v) {
		matrix[3][0] += matrix[0][0] * v.getXf() + matrix[1][0] * v.getYf();
		matrix[3][1] += matrix[0][1] * v.getXf() + matrix[1][1] * v.getYf();
		matrix[3][2] += matrix[0][2] * v.getXf() + matrix[1][2] * v.getYf();
		matrix[3][3] += matrix[0][3] * v.getXf() + matrix[1][3] * v.getYf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateRelative(Vector3 v) {
		matrix[3][0] += matrix[0][0] * v.getXf() + matrix[1][0] * v.getYf() + matrix[2][0] * v.getZf();
		matrix[3][1] += matrix[0][1] * v.getXf() + matrix[1][1] * v.getYf() + matrix[2][1] * v.getZf();
		matrix[3][2] += matrix[0][2] * v.getXf() + matrix[1][2] * v.getYf() + matrix[2][2] * v.getZf();
		matrix[3][3] += matrix[0][3] * v.getXf() + matrix[1][3] * v.getYf() + matrix[2][3] * v.getZf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateTo(Vector2 v) {
		matrix[3][0] = v.getXf();
		matrix[3][1] = v.getYf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateTo(Vector3 v) {
		matrix[3][0] = v.getXf();
		matrix[3][1] = v.getYf();
		matrix[3][2] = v.getZf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transpose() {
		float m01 = matrix[0][1];
		float m02 = matrix[0][2];
		float m03 = matrix[0][3];
		float m12 = matrix[1][2];
		float m13 = matrix[1][3];
		float m23 = matrix[2][3];

		matrix[0][1] = matrix[1][0];
		matrix[0][2] = matrix[2][0];
		matrix[0][3] = matrix[3][0];
		matrix[1][2] = matrix[2][1];
		matrix[1][3] = matrix[3][1];
		matrix[2][3] = matrix[3][2];
		matrix[1][0] = m01;
		matrix[2][0] = m02;
		matrix[2][1] = m12;
		matrix[3][0] = m03;
		matrix[3][1] = m13;
		matrix[3][2] = m23;
	}

	// @Override
	// public void normalizeRotation() {
	// float l1 = (float) Math.sqrt(matrix[0][0] * matrix[0][0] + matrix[0][1] *
	// matrix[0][1] + matrix[0][2] * matrix[0][2]);
	// float l2 = (float) Math.sqrt(matrix[1][0] * matrix[1][0] + matrix[1][1] *
	// matrix[1][1] + matrix[1][2] * matrix[1][2]);
	// float l3 = (float) Math.sqrt(matrix[2][0] * matrix[2][0] + matrix[2][1] *
	// matrix[2][1] + matrix[2][2] * matrix[2][2]);
	//
	// matrix[0][0] /= l1;
	// matrix[0][1] /= l1;
	// matrix[0][2] /= l1;
	// matrix[1][0] /= l2;
	// matrix[1][1] /= l2;
	// matrix[1][2] /= l2;
	// matrix[2][0] /= l3;
	// matrix[2][1] /= l3;
	// matrix[2][2] /= l3;
	// }

	// @Override
	// public void normalizeRotation2() {
	// float l1 = (float) Math.sqrt(matrix[0][0] * matrix[0][0] + matrix[1][0] *
	// matrix[1][0]);
	// float l2 = (float) Math.sqrt(matrix[0][1] * matrix[0][1] + matrix[1][1] *
	// matrix[1][1]);
	// System.out.println("normalize mat");
	// matrix[0][0] /= l1;
	// matrix[0][1] /= l2;
	// matrix[1][0] /= l1;
	// matrix[1][1] /= l2;
	// }
}