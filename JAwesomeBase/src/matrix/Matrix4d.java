package matrix;

import java.nio.FloatBuffer;

import math.FastMath;
import vector.Vector2;
import vector.Vector2d;
import vector.Vector3;
import vector.Vector3d;
import vector.Vector4;
import vector.Vector4d;

/**
 * Holds a 4-dimensional double matrix.
 * 
 * @author Oliver Schall
 * 
 */

public class Matrix4d extends Matrix4 {
	public double[][] matrix;

	public Matrix4d() {
		matrix = new double[4][4];
		setIdentity();
	}

	public Matrix4d(double setAll) {
		this.matrix = new double[4][4];
		setAll(setAll);
	}

	public Matrix4d(double m00, double m01, double m02, double m03, double m10,
			double m11, double m12, double m13, double m20, double m21,
			double m22, double m23, double m30, double m31, double m32,
			double m33) {
		matrix = new double[4][4];
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

	public Matrix4d(double[][] matrix) {
		this.matrix = matrix;
	}

	public Matrix4d(Matrix4 matrix) {
		this.matrix = new double[4][4];
		set(matrix);
	}

	public Matrix4d(Matrix4d matrix) {
		this.matrix = new double[4][4];
		setArray(matrix.matrix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double determinant() {
		return matrix[0][0]
				* (matrix[1][1]
						* (matrix[2][2] * matrix[3][3] - matrix[2][3]
								* matrix[3][2])
						+ matrix[1][2]
						* (matrix[2][3] * matrix[3][1] - matrix[2][1]
								* matrix[3][3]) + matrix[1][3]
						* (matrix[2][1] * matrix[3][2] - matrix[2][2]
								* matrix[3][1]))
				- matrix[0][1]
				* (matrix[1][0]
						* (matrix[2][2] * matrix[3][3] - matrix[2][3]
								* matrix[3][2])
						+ matrix[1][2]
						* (matrix[2][3] * matrix[3][0] - matrix[2][0]
								* matrix[3][3]) + matrix[1][3]
						* (matrix[2][0] * matrix[3][2] - matrix[2][2]
								* matrix[3][0]))
				+ matrix[0][2]
				* (matrix[1][0]
						* (matrix[2][1] * matrix[3][3] - matrix[2][3]
								* matrix[3][1])
						+ matrix[1][1]
						* (matrix[2][3] * matrix[3][0] - matrix[2][0]
								* matrix[3][3]) + matrix[1][3]
						* (matrix[2][0] * matrix[3][1] - matrix[2][1]
								* matrix[3][0]))
				- matrix[0][3]
				* (matrix[1][0]
						* (matrix[2][1] * matrix[3][2] - matrix[2][2]
								* matrix[3][2])
						+ matrix[1][1]
						* (matrix[2][2] * matrix[3][0] - matrix[2][0]
								* matrix[3][2]) + matrix[1][2]
						* (matrix[2][0] * matrix[3][1] - matrix[2][1]
								* matrix[3][0]));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double determinant3() {
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
	public float determinant3f() {
		return (float) determinant3();
	}

	/**
	 * {@inheritDoc}
	 */
	private double determinant3x3(double t00, double t01, double t02,
			double t10, double t11, double t12, double t20, double t21,
			double t22) {
		return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22)
				+ t02 * (t10 * t21 - t11 * t20);
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
		float[][] array = new float[4][4];
		array[0][0] = (float) matrix[0][0];
		array[0][1] = (float) matrix[0][1];
		array[0][2] = (float) matrix[0][2];
		array[0][3] = (float) matrix[0][3];
		array[1][0] = (float) matrix[1][0];
		array[1][1] = (float) matrix[1][1];
		array[1][2] = (float) matrix[1][2];
		array[1][3] = (float) matrix[1][3];
		array[2][0] = (float) matrix[2][0];
		array[2][1] = (float) matrix[2][1];
		array[2][2] = (float) matrix[2][2];
		array[2][3] = (float) matrix[2][3];
		array[3][0] = (float) matrix[3][0];
		array[3][1] = (float) matrix[3][1];
		array[3][2] = (float) matrix[3][2];
		array[3][3] = (float) matrix[3][3];
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector4 getColumn(int column) {
		return new Vector4d(matrix[column][0], matrix[column][1],
				matrix[column][2], matrix[column][3]);
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
	public Vector4 getRow(int row) {
		return new Vector4d(matrix[0][row], matrix[1][row], matrix[2][row],
				matrix[3][row]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix3 getSubMatrix() {
		return new Matrix3d(matrix[0][0], matrix[0][1], matrix[0][2],
				matrix[1][0], matrix[1][1], matrix[1][2], matrix[2][0],
				matrix[2][1], matrix[2][2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix2 getSubMatrix2() {
		return new Matrix2d(matrix[0][0], matrix[0][1], matrix[1][0],
				matrix[1][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3 getTranslation() {
		return new Vector3d(matrix[3][0], matrix[3][1], matrix[3][2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2 getTranslation2() {
		return new Vector2d(matrix[3][0], matrix[3][1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		double determinant = determinant();
		if (determinant != 0) {
			double determinant_inv = 1 / determinant;

			double t00 = determinant3x3(matrix[1][1], matrix[1][2],
					matrix[1][3], matrix[2][1], matrix[2][2], matrix[2][3],
					matrix[3][1], matrix[3][2], matrix[3][3])
					* determinant_inv;
			double t10 = -determinant3x3(matrix[1][0], matrix[1][2],
					matrix[1][3], matrix[2][0], matrix[2][2], matrix[2][3],
					matrix[3][0], matrix[3][2], matrix[3][3])
					* determinant_inv;
			double t20 = determinant3x3(matrix[1][0], matrix[1][1],
					matrix[1][3], matrix[2][0], matrix[2][1], matrix[2][3],
					matrix[3][0], matrix[3][1], matrix[3][3])
					* determinant_inv;
			double t30 = -determinant3x3(matrix[1][0], matrix[1][1],
					matrix[1][2], matrix[2][0], matrix[2][1], matrix[2][2],
					matrix[3][0], matrix[3][1], matrix[3][2])
					* determinant_inv;

			double t01 = -determinant3x3(matrix[0][1], matrix[0][2],
					matrix[0][3], matrix[2][1], matrix[2][2], matrix[2][3],
					matrix[3][1], matrix[3][2], matrix[3][3])
					* determinant_inv;
			double t11 = determinant3x3(matrix[0][0], matrix[0][2],
					matrix[0][3], matrix[2][0], matrix[2][2], matrix[2][3],
					matrix[3][0], matrix[3][2], matrix[3][3])
					* determinant_inv;
			double t21 = -determinant3x3(matrix[0][0], matrix[0][1],
					matrix[0][3], matrix[2][0], matrix[2][1], matrix[2][3],
					matrix[3][0], matrix[3][1], matrix[3][3])
					* determinant_inv;
			double t31 = determinant3x3(matrix[0][0], matrix[0][1],
					matrix[0][2], matrix[2][0], matrix[2][1], matrix[2][2],
					matrix[3][0], matrix[3][1], matrix[3][2])
					* determinant_inv;

			double t02 = determinant3x3(matrix[0][1], matrix[0][2],
					matrix[0][3], matrix[1][1], matrix[1][2], matrix[1][3],
					matrix[3][1], matrix[3][2], matrix[3][3])
					* determinant_inv;
			double t12 = -determinant3x3(matrix[0][0], matrix[0][2],
					matrix[0][3], matrix[1][0], matrix[1][2], matrix[1][3],
					matrix[3][0], matrix[3][2], matrix[3][3])
					* determinant_inv;
			double t22 = determinant3x3(matrix[0][0], matrix[0][1],
					matrix[0][3], matrix[1][0], matrix[1][1], matrix[1][3],
					matrix[3][0], matrix[3][1], matrix[3][3])
					* determinant_inv;
			double t32 = -determinant3x3(matrix[0][0], matrix[0][1],
					matrix[0][2], matrix[1][0], matrix[1][1], matrix[1][2],
					matrix[3][0], matrix[3][1], matrix[3][2])
					* determinant_inv;

			double t03 = -determinant3x3(matrix[0][1], matrix[0][2],
					matrix[0][3], matrix[1][1], matrix[1][2], matrix[1][3],
					matrix[2][1], matrix[2][2], matrix[2][3])
					* determinant_inv;
			double t13 = determinant3x3(matrix[0][0], matrix[0][2],
					matrix[0][3], matrix[1][0], matrix[1][2], matrix[1][3],
					matrix[2][0], matrix[2][2], matrix[2][3])
					* determinant_inv;
			double t23 = -determinant3x3(matrix[0][0], matrix[0][1],
					matrix[0][3], matrix[1][0], matrix[1][1], matrix[1][3],
					matrix[2][0], matrix[2][1], matrix[2][3])
					* determinant_inv;
			double t33 = determinant3x3(matrix[0][0], matrix[0][1],
					matrix[0][2], matrix[1][0], matrix[1][1], matrix[1][2],
					matrix[2][0], matrix[2][1], matrix[2][2])
					* determinant_inv;

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
		angle = Math.toRadians(angle);
		double c = FastMath.cos(angle);
		double s = FastMath.sin(angle);
		double oneminusc = 1.0d - c;
		double xy = axis.getX() * axis.getY();
		double yz = axis.getY() * axis.getZ();
		double xz = axis.getX() * axis.getZ();
		double xs = axis.getX() * s;
		double ys = axis.getY() * s;
		double zs = axis.getZ() * s;

		double f00 = axis.getX() * axis.getX() * oneminusc + c;
		double f01 = xy * oneminusc + zs;
		double f02 = xz * oneminusc - ys;
		double f10 = xy * oneminusc - zs;
		double f11 = axis.getY() * axis.getY() * oneminusc + c;
		double f12 = yz * oneminusc + xs;
		double f20 = xz * oneminusc + ys;
		double f21 = yz * oneminusc - xs;
		double f22 = axis.getZ() * axis.getZ() * oneminusc + c;

		double t00 = matrix[0][0] * f00 + matrix[1][0] * f01 + matrix[2][0]
				* f02;
		double t01 = matrix[0][1] * f00 + matrix[1][1] * f01 + matrix[2][1]
				* f02;
		double t02 = matrix[0][2] * f00 + matrix[1][2] * f01 + matrix[2][2]
				* f02;
		double t03 = matrix[0][3] * f00 + matrix[1][3] * f01 + matrix[2][3]
				* f02;
		double t10 = matrix[0][0] * f10 + matrix[1][0] * f11 + matrix[2][0]
				* f12;
		double t11 = matrix[0][1] * f10 + matrix[1][1] * f11 + matrix[2][1]
				* f12;
		double t12 = matrix[0][2] * f10 + matrix[1][2] * f11 + matrix[2][2]
				* f12;
		double t13 = matrix[0][3] * f10 + matrix[1][3] * f11 + matrix[2][3]
				* f12;
		matrix[2][0] = matrix[0][0] * f20 + matrix[1][0] * f21 + matrix[2][0]
				* f22;
		matrix[2][1] = matrix[0][1] * f20 + matrix[1][1] * f21 + matrix[2][1]
				* f22;
		matrix[2][2] = matrix[0][2] * f20 + matrix[1][2] * f21 + matrix[2][2]
				* f22;
		matrix[2][3] = matrix[0][3] * f20 + matrix[1][3] * f21 + matrix[2][3]
				* f22;
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
	public void rotate(float angle, Vector3 axis) {
		rotate(angle, axis);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Matrix2 mat) {
		double[][] m1 = mat.getArray();
		double t00 = matrix[0][0] * m1[0][0] + matrix[1][0] * m1[0][1];
		double t01 = matrix[0][0] * m1[1][0] + matrix[1][0] * m1[1][1];
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
		double[][] m1 = mat.getArray();
		double t00 = matrix[0][0] * m1[0][0] + matrix[1][0] * m1[0][1]
				+ matrix[2][0] * m1[0][2];
		double t01 = matrix[0][0] * m1[1][0] + matrix[1][0] * m1[1][1]
				+ matrix[2][0] * m1[1][2];
		double t02 = matrix[0][0] * m1[2][0] + matrix[1][0] * m1[2][1]
				+ matrix[2][0] * m1[2][2];
		double t10 = matrix[0][1] * m1[0][0] + matrix[1][1] * m1[0][1]
				+ matrix[2][1] * m1[0][2];
		double t11 = matrix[0][1] * m1[1][0] + matrix[1][1] * m1[1][1]
				+ matrix[2][1] * m1[1][2];
		double t12 = matrix[0][1] * m1[2][0] + matrix[1][1] * m1[2][1]
				+ matrix[2][1] * m1[2][2];
		matrix[2][0] = matrix[0][2] * m1[0][0] + matrix[1][2] * m1[0][1]
				+ matrix[2][2] * m1[0][2];
		matrix[2][1] = matrix[0][2] * m1[1][0] + matrix[1][2] * m1[1][1]
				+ matrix[2][2] * m1[1][2];
		matrix[2][2] = matrix[0][2] * m1[2][0] + matrix[1][2] * m1[2][1]
				+ matrix[2][2] * m1[2][2];
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
		matrix[0][0] *= v.getX();
		matrix[0][1] *= v.getX();
		matrix[0][2] *= v.getX();
		matrix[0][3] *= v.getX();
		matrix[1][0] *= v.getY();
		matrix[1][1] *= v.getY();
		matrix[1][2] *= v.getY();
		matrix[1][3] *= v.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(Vector3 v) {
		matrix[0][0] *= v.getX();
		matrix[0][1] *= v.getX();
		matrix[0][2] *= v.getX();
		matrix[0][3] *= v.getX();
		matrix[1][0] *= v.getY();
		matrix[1][1] *= v.getY();
		matrix[1][2] *= v.getY();
		matrix[1][3] *= v.getY();
		matrix[2][0] *= v.getZ();
		matrix[2][1] *= v.getZ();
		matrix[2][2] *= v.getZ();
		matrix[2][3] *= v.getZ();
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
	public void set(Matrix4 mat) {
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
		matrix[0][0] = tmp[0][0];
		matrix[0][1] = tmp[0][1];
		matrix[1][0] = tmp[1][0];
		matrix[1][1] = tmp[1][1];
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
		buf.put((float) matrix[0][0]);
		buf.put((float) matrix[0][1]);
		buf.put((float) matrix[0][2]);
		buf.put((float) matrix[0][3]);
		buf.put((float) matrix[1][0]);
		buf.put((float) matrix[1][1]);
		buf.put((float) matrix[1][2]);
		buf.put((float) matrix[1][3]);
		buf.put((float) matrix[2][0]);
		buf.put((float) matrix[2][1]);
		buf.put((float) matrix[2][2]);
		buf.put((float) matrix[2][3]);
		buf.put((float) matrix[3][0]);
		buf.put((float) matrix[3][1]);
		buf.put((float) matrix[3][2]);
		buf.put((float) matrix[3][3]);
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
		sb.append("Matrix4d[");
		sb.append(matrix[0][0]).append(' ').append(matrix[1][0]).append(' ')
				.append(matrix[2][0]).append(' ').append(matrix[3][0])
				.append('\n');
		sb.append(matrix[0][1]).append(' ').append(matrix[1][1]).append(' ')
				.append(matrix[2][1]).append(' ').append(matrix[3][1])
				.append('\n');
		sb.append(matrix[0][2]).append(' ').append(matrix[1][2]).append(' ')
				.append(matrix[2][2]).append(' ').append(matrix[3][2])
				.append('\n');
		sb.append(matrix[0][3]).append(' ').append(matrix[1][3]).append(' ')
				.append(matrix[2][3]).append(' ').append(matrix[3][3]);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector2 v) {
		matrix[3][0] += v.getX();
		matrix[3][1] += v.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3 v) {
		matrix[3][0] += v.getX();
		matrix[3][1] += v.getY();
		matrix[3][2] += v.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateRelative(Vector2 v) {
		matrix[3][0] += matrix[0][0] * v.getX() + matrix[1][0] * v.getY();
		matrix[3][1] += matrix[0][1] * v.getX() + matrix[1][1] * v.getY();
		matrix[3][2] += matrix[0][2] * v.getX() + matrix[1][2] * v.getY();
		matrix[3][3] += matrix[0][3] * v.getX() + matrix[1][3] * v.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateRelative(Vector3 v) {
		matrix[3][0] += matrix[0][0] * v.getX() + matrix[1][0] * v.getY()
				+ matrix[2][0] * v.getZ();
		matrix[3][1] += matrix[0][1] * v.getX() + matrix[1][1] * v.getY()
				+ matrix[2][1] * v.getZ();
		matrix[3][2] += matrix[0][2] * v.getX() + matrix[1][2] * v.getY()
				+ matrix[2][2] * v.getZ();
		matrix[3][3] += matrix[0][3] * v.getX() + matrix[1][3] * v.getY()
				+ matrix[2][3] * v.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateTo(Vector2 v) {
		matrix[3][0] = v.getX();
		matrix[3][1] = v.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateTo(Vector3 v) {
		matrix[3][0] = v.getX();
		matrix[3][1] = v.getY();
		matrix[3][2] = v.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transpose() {
		double[][] tmp = matrix;
		matrix[0][1] = tmp[1][0];
		matrix[0][2] = tmp[2][0];
		matrix[0][3] = tmp[3][0];
		matrix[1][0] = tmp[0][1];
		matrix[1][2] = tmp[2][1];
		matrix[1][3] = tmp[3][1];
		matrix[2][0] = tmp[0][2];
		matrix[2][1] = tmp[1][2];
		matrix[2][3] = tmp[3][2];
		matrix[3][0] = tmp[0][3];
		matrix[3][1] = tmp[1][3];
		matrix[3][2] = tmp[2][3];
	}

	// @Override
	// public void normalizeRotation() {
	//
	// }
	//
	// @Override
	// public void normalizeRotation2() {
	//
	// }
}