package math;

import matrix.Matrix1;
import matrix.Matrix1f;
import matrix.Matrix2;
import matrix.Matrix2f;
import matrix.Matrix3;
import matrix.Matrix3f;
import matrix.Matrix4f;
import vector.Vector1;
import vector.Vector1d;
import vector.Vector1f;
import vector.Vector2;
import vector.Vector2d;
import vector.Vector2f;
import vector.Vector3;
import vector.Vector3d;
import vector.Vector3f;
import vector.Vector4;
import vector.Vector4d;
import vector.Vector4f;

/**
 * Applies mathematical operations to n-dimensional Vectors.
 * 
 * @author Oliver Schall
 * 
 */

public class VecMath {

	/**
	 * Addition of two vectors.
	 * 
	 * @param v1
	 *            first vector
	 * @param v2
	 *            second vector
	 * @return sum in a new vector
	 */
	public static Vector1d addition(Vector1 v1, Vector1 v2) {
		return new Vector1d(v1.getX() + v2.getX());
	}

	/**
	 * @see math.VecMath#addition(Vector1, Vector1)
	 */
	public static Vector1f addition(Vector1f v1, Vector1f v2) {
		return new Vector1f(v1.x + v2.x);
	}

	/**
	 * @see math.VecMath#addition(Vector1, Vector1)
	 */
	public static Vector2d addition(Vector2 v1, Vector2 v2) {
		return new Vector2d(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}

	/**
	 * @see math.VecMath#addition(Vector1, Vector1)
	 */
	public static Vector2f addition(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x + v2.x, v1.y + v2.y);
	}

	/**
	 * @see math.VecMath#addition(Vector1, Vector1)
	 */
	public static Vector3d addition(Vector3 v1, Vector3 v2) {
		return new Vector3d(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
	}

	/**
	 * @see math.VecMath#addition(Vector1, Vector1)
	 */
	public static Vector3f addition(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	/**
	 * Computes the normal of a plane given by three points.
	 * 
	 * @param p1
	 *            first point
	 * @param p2
	 *            second point
	 * @param p3
	 *            third point
	 * @return normal of the plane
	 */
	public static Vector3d computeNormal(Vector3 p1, Vector3 p2, Vector3 p3) {
		double dif1x = p2.getX() - p1.getX();
		double dif1y = p2.getY() - p1.getY();
		double dif1z = p2.getZ() - p1.getZ();
		double dif2x = p3.getX() - p1.getX();
		double dif2y = p3.getY() - p1.getY();
		double dif2z = p3.getZ() - p1.getZ();

		return new Vector3d(dif1y * dif2z - dif1z * dif2y, dif1z * dif2x - dif1x * dif2z,
				dif1x * dif2y - dif1y * dif2x);
	}

	/**
	 * @see math.VecMath#computeNormal(Vector3, Vector3, Vector3)
	 */
	public static Vector3f computeNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
		float dif1x = p2.x - p1.x;
		float dif1y = p2.y - p1.y;
		float dif1z = p2.z - p1.z;
		float dif2x = p3.x - p1.x;
		float dif2y = p3.y - p1.y;
		float dif2z = p3.z - p1.z;

		return new Vector3f(dif1y * dif2z - dif1z * dif2y, dif1z * dif2x - dif1x * dif2z,
				dif1x * dif2y - dif1y * dif2x);
	}

	/**
	 * Calculates the cross product of two vectors.
	 * 
	 * @param v1
	 *            first vector
	 * @param v2
	 *            second vector
	 * @return cross product value
	 */
	public static double crossproduct(Vector2 v1, Vector2 v2) {
		return v1.getX() * v2.getY() - v1.getY() * v2.getX();
	}

	/**
	 * @see math.VecMath#crossproduct(Vector2, Vector2)
	 */
	public static float crossproduct(Vector2f v1, Vector2f v2) {
		return v1.getXf() * v2.getYf() - v1.getYf() * v2.getXf();
	}
	
	/**
	 * @see math.VecMath#crossproduct(Vector2, Vector2)
	 */
	public static double crossproduct(double v1x, double v1y, double v2x, double v2y) {
		return v1x * v2y - v1y * v2x;
	}
	
	/**
	 * @see math.VecMath#crossproduct(Vector2, Vector2)
	 */
	public static float crossproduct(float v1x, float v1y, float v2x, float v2y) {
		return v1x * v2y - v1y * v2x;
	}

	/**
	 * @see math.VecMath#crossproduct(Vector2, Vector2)
	 */
	public static Vector3d crossproduct(Vector3 v1, Vector3 v2) {
		return new Vector3d(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
				v1.getZ() * v2.getX() - v1.getX() * v2.getZ(), v1.getX() * v2.getY() - v1.getY() * v2.getX());
	}

	/**
	 * @see math.VecMath#crossproduct(Vector2, Vector2)
	 */
	public static Vector3f crossproduct(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.getYf() * v2.getZf() - v1.getZf() * v2.getYf(),
				v1.getZf() * v2.getXf() - v1.getXf() * v2.getZf(), v1.getXf() * v2.getYf() - v1.getYf() * v2.getXf());
	}
	
	/**
	 * @see math.VecMath#crossproduct(Vector2, Vector2)
	 */
	public static Vector3d crossproduct(double v1x, double v1y, double v1z, double v2x, double v2y, double v2z) {
		return new Vector3d(v1y * v2z - v1z * v2y, v1z * v2x - v1x * v2z, v1x * v2y - v1y * v2x);
	}
	
	/**
	 * @see math.VecMath#crossproduct(Vector2, Vector2)
	 */
	public static Vector3f crossproduct(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z) {
		return new Vector3f(v1y * v2z - v1z * v2y, v1z * v2x - v1x * v2z, v1x * v2y - v1y * v2x);
	}

	/**
	 * Dot product of two vectors.
	 * 
	 * @param v1
	 *            first vector
	 * @param v2
	 *            second vector
	 * @return result of the dot product
	 */
	public static double dotproduct(Vector1 v1, Vector1 v2) {
		return v1.getX() * v2.getX();
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(Vector1f v1, Vector1f v2) {
		return v1.getXf() * v2.getXf();
	}
	
	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static double dotproduct(double v1x, double v2x) {
		return v1x * v2x;
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(float v1x, float v2x) {
		return v1x * v2x;
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static double dotproduct(Vector2 v1, Vector2 v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY();
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(Vector2f v1, Vector2f v2) {
		return v1.getXf() * v2.getXf() + v1.getYf() * v2.getYf();
	}
	
	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static double dotproduct(double v1x, double v1y, double v2x, double v2y) {
		return v1x * v2x + v1y * v2y;
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(float v1x, float v1y, float v2x, float v2y) {
		return v1x * v2x + v1y * v2y;
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static double dotproduct(Vector3 v1, Vector3 v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(Vector3f v1, Vector3f v2) {
		return v1.getXf() * v2.getXf() + v1.getYf() * v2.getYf() + v1.getZf() * v2.getZf();
	}
	
	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static double dotproduct(double v1x, double v1y, double v1z, double v2x, double v2y, double v2z) {
		return v1x * v2x + v1y * v2y + v1z * v2z;
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z) {
		return v1x * v2x + v1y * v2y + v1z * v2z;
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static double dotproduct(Vector4 v1, Vector4 v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ() + v1.getW() * v2.getW();
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(Vector4f v1, Vector4f v2) {
		return v1.getXf() * v2.getXf() + v1.getYf() * v2.getYf() + v1.getZf() * v2.getZf() + v1.getWf() * v2.getWf();
	}
	
	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static double dotproduct(double v1x, double v1y, double v1z, double v1w, double v2x, double v2y, double v2z, double v2w) {
		return v1x * v2x + v1y * v2y + v1z * v2z + v1w * v2w;
	}

	/**
	 * @see math.VecMath#dotproduct(Vector1, Vector1)
	 */
	public static float dotproduct(float v1x, float v1y, float v1z, float v1w, float v2x, float v2y, float v2z, float v2w) {
		return v1x * v2x + v1y * v2y + v1z * v2z + v1w * v2w;
	}

	/**
	 * Static method to get the identity matrix in a new matrix object.
	 * 
	 * @return identity matrix
	 */
	public static Matrix4f identityMatrix() {
		Matrix4f result = new Matrix4f();
		return result;
	}

	/**
	 * Calculates the length of a vector.
	 * 
	 * @param v1
	 *            vector to get the length from
	 * @return length of the vector
	 */
	public static float length(Vector2f v1) {
		return (float) Math.sqrt(v1.x * v1.x + v1.y * v1.y);
	}

	/**
	 * @see math.VecMath#length(Vector2f)
	 */
	public static float length(Vector3f v1) {
		return (float) Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
	}

	/**
	 * Multiplication of two vectors.
	 * 
	 * @param v1
	 *            first vector
	 * @param v2
	 *            second vector
	 * @return product in a new vector
	 */
	public static Vector1d multiplication(Vector1 v1, Vector1 v2) {
		return new Vector1d(v1.getX() * v2.getX());
	}

	/**
	 * @see math.VecMath#multiplication(Vector1, Vector1)
	 */
	public static Vector1f multiplication(Vector1f v1, Vector1f v2) {
		return new Vector1f(v1.x * v2.x);
	}

	/**
	 * @see math.VecMath#multiplication(Vector1, Vector1)
	 */
	public static Vector2d multiplication(Vector2 v1, Vector2 v2) {
		return new Vector2d(v1.getX() * v2.getX(), v1.getY() * v2.getY());
	}

	/**
	 * @see math.VecMath#multiplication(Vector1, Vector1)
	 */
	public static Vector2f multiplication(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x * v2.x, v1.y * v2.y);
	}

	/**
	 * @see math.VecMath#multiplication(Vector1, Vector1)
	 */
	public static Vector3d multiplication(Vector3 v1, Vector3 v2) {
		return new Vector3d(v1.getX() * v2.getX(), v1.getY() * v2.getY(), v1.getZ() * v2.getZ());
	}

	/**
	 * @see math.VecMath#multiplication(Vector1, Vector1)
	 */
	public static Vector3f multiplication(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
	}

	/**
	 * Negates the vector.
	 * 
	 * @param v1
	 *            vector to negate
	 * @return resulting vector
	 */
	public static Vector1 negate(Vector1 v1) {
		return new Vector1d(-v1.getX());
	}

	/**
	 * @see math.VecMath#negate(Vector1)
	 */
	public static Vector1f negate(Vector1f v1) {
		return new Vector1f(-v1.x);
	}

	/**
	 * @see math.VecMath#negate(Vector1)
	 */
	public static Vector2 negate(Vector2 v1) {
		return new Vector2d(-v1.getX(), -v1.getY());
	}

	/**
	 * @see math.VecMath#negate(Vector1)
	 */
	public static Vector2f negate(Vector2f v1) {
		return new Vector2f(-v1.x, -v1.y);
	}

	/**
	 * @see math.VecMath#negate(Vector1)
	 */
	public static Vector3 negate(Vector3 v1) {
		return new Vector3d(-v1.getX(), -v1.getY(), -v1.getZ());
	}

	/**
	 * @see math.VecMath#negate(Vector1)
	 */
	public static Vector3f negate(Vector3f v1) {
		return new Vector3f(-v1.x, -v1.y, -v1.z);
	}

	/**
	 * @see math.VecMath#negate(Vector1)
	 */
	public static Vector4 negate(Vector4 v1) {
		return new Vector4d(-v1.getX(), -v1.getY(), -v1.getZ(), -v1.getW());
	}

	/**
	 * @see math.VecMath#negate(Vector1)
	 */
	public static Vector4f negate(Vector4f v1) {
		return new Vector4f(-v1.x, -v1.y, -v1.z, -v1.w);
	}

	/**
	 * Normalizes a vector.
	 * 
	 * @param v1
	 *            vector to normalize
	 * @return resulting vector
	 */
	public static Vector2d normalize(Vector2 v1) {
		double length = v1.length();
		return new Vector2d(v1.getX() / length, v1.getY() / length);
	}

	/**
	 * @see math.VecMath#normalize(Vector2)
	 */
	public static Vector2f normalize(Vector2f v1) {
		float length = (float) v1.length();
		return new Vector2f(v1.x / length, v1.y / length);
	}

	/**
	 * @see math.VecMath#normalize(Vector2)
	 */
	public static Vector3d normalize(Vector3 v1) {
		double length = v1.length();
		return new Vector3d(v1.getX() / length, v1.getY() / length, v1.getZ() / length);
	}

	/**
	 * @see math.VecMath#normalize(Vector2)
	 */
	public static Vector3f normalize(Vector3f v1) {
		float length = (float) v1.length();
		return new Vector3f(v1.x / length, v1.y / length, v1.z / length);
	}

	/**
	 * Scales a vector by a factor.
	 * 
	 * @param v
	 *            vector to scale
	 * @param factor
	 *            scales the vector
	 * @return result in a new vector
	 */
	public static Vector1d scale(Vector1 v, double factor) {
		return new Vector1d(v.getX() * factor);
	}

	/**
	 * @see math.VecMath#scale(Vector1, double)
	 */
	public static Vector1f scale(Vector1f v, float factor) {
		return new Vector1f(v.x * factor);
	}

	/**
	 * @see math.VecMath#scale(Vector1, double)
	 */
	public static Vector2d scale(Vector2 v, double factor) {
		return new Vector2d(v.getX() * factor, v.getY() * factor);
	}

	/**
	 * @see math.VecMath#scale(Vector1, double)
	 */
	public static Vector2f scale(Vector2f v, float factor) {
		return new Vector2f(v.x * factor, v.y * factor);
	}

	/**
	 * @see math.VecMath#scale(Vector1, double)
	 */
	public static Vector3d scale(Vector3 v, double factor) {
		return new Vector3d(v.getX() * factor, v.getY() * factor, v.getZ() * factor);
	}

	/**
	 * @see math.VecMath#scale(Vector1, double)
	 */
	public static Vector3f scale(Vector3f v, float factor) {
		return new Vector3f(v.x * factor, v.y * factor, v.z * factor);
	}

	/**
	 * Scales a vector to a length.
	 * 
	 * @param v1
	 *            vector the scale
	 * @param factor
	 *            length to scale to
	 * @return resulting vector
	 */
	public static Vector2d setScale(Vector2 v1, double factor) {
		Vector2d result = new Vector2d();
		result = normalize(v1);
		result = scale(result, factor);
		return result;
	}

	/**
	 * @see math.VecMath#setScale(Vector2, double)
	 */
	public static Vector2f setScale(Vector2f v1, float factor) {
		Vector2f result = new Vector2f();
		result = normalize(v1);
		result = scale(result, factor);
		return result;
	}

	/**
	 * @see math.VecMath#setScale(Vector2, double)
	 */
	public static Vector3d setScale(Vector3 v1, double factor) {
		Vector3d result = new Vector3d();
		result = normalize(v1);
		result = scale(result, factor);
		return result;
	}

	/**
	 * @see math.VecMath#setScale(Vector2, double)
	 */
	public static Vector3f setScale(Vector3f v1, float factor) {
		Vector3f result = new Vector3f();
		result = normalize(v1);
		result = scale(result, factor);
		return result;
	}

	/**
	 * Subtracts the vector v2 from v1. (v1 - v2)
	 * 
	 * @param v1
	 *            first vector
	 * @param v2
	 *            second vector
	 * @return difference in a new vector
	 */
	public static Vector1d subtraction(Vector1 v1, Vector1 v2) {
		return new Vector1d(v1.getX() - v2.getX());
	}

	/**
	 * @see math.VecMath#subtraction(Vector1, Vector1)
	 */
	public static Vector1f subtraction(Vector1f v1, Vector1f v2) {
		return new Vector1f(v1.x - v2.x);
	}

	/**
	 * @see math.VecMath#subtraction(Vector1, Vector1)
	 */
	public static Vector2d subtraction(Vector2 v1, Vector2 v2) {
		return new Vector2d(v1.getX() - v2.getX(), v1.getY() - v2.getY());
	}

	/**
	 * @see math.VecMath#subtraction(Vector1, Vector1)
	 */
	public static Vector2f subtraction(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x - v2.x, v1.y - v2.y);
	}

	/**
	 * @see math.VecMath#subtraction(Vector1, Vector1)
	 */
	public static Vector3d subtraction(Vector3 v1, Vector3 v2) {
		return new Vector3d(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
	}

	// public static Vector2f crossproduct(Vector2f v, float s) {
	// return new Vector2f(s * v.y, -s * v.x);
	// }
	//
	// public static Vector2d crossproduct(Vector2 v, double s) {
	// return new Vector2d(s * v.getY(), -s * v.getX());
	// }
	//
	// public static Vector2f crossproduct(float s, Vector2f v) {
	// return new Vector2f(-s * v.y, s * v.x);
	// }
	//
	// public static Vector2d crossproduct(double s, Vector2 v) {
	// return new Vector2d(-s * v.getY(), s * v.getX());
	// }

	/**
	 * @see math.VecMath#subtraction(Vector1, Vector1)
	 */
	public static Vector3f subtraction(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	/**
	 * Transforms a matrix by another one. (Matrix multiplication)
	 * 
	 * @param m1
	 *            first matrix
	 * @param m2
	 *            second matrix
	 * @return resulting matrix
	 */
	public static Matrix4f transformMatrix(Matrix4f m1, Matrix4f m2) {
		float[][] matrix1 = m1.matrix;
		float[][] matrix2 = m2.matrix;
		float[][] result = new float[4][4];

		result[0][0] = matrix1[0][0] * matrix2[0][0] + matrix1[0][1] * matrix2[1][0] + matrix1[0][2] * matrix2[2][0]
				+ matrix1[0][3] * matrix2[3][0];
		result[0][1] = matrix1[0][0] * matrix2[0][1] + matrix1[0][1] * matrix2[1][1] + matrix1[0][2] * matrix2[2][1]
				+ matrix1[0][3] * matrix2[3][1];
		result[0][2] = matrix1[0][0] * matrix2[0][2] + matrix1[0][1] * matrix2[1][2] + matrix1[0][2] * matrix2[2][2]
				+ matrix1[0][3] * matrix2[3][2];
		result[0][3] = matrix1[0][0] * matrix2[0][3] + matrix1[0][1] * matrix2[1][3] + matrix1[0][2] * matrix2[2][3]
				+ matrix1[0][3] * matrix2[3][3];

		result[1][0] = matrix1[1][0] * matrix2[0][0] + matrix1[1][1] * matrix2[1][0] + matrix1[1][2] * matrix2[2][0]
				+ matrix1[1][3] * matrix2[3][0];
		result[1][1] = matrix1[1][0] * matrix2[0][1] + matrix1[1][1] * matrix2[1][1] + matrix1[1][2] * matrix2[2][1]
				+ matrix1[1][3] * matrix2[3][1];
		result[1][2] = matrix1[1][0] * matrix2[0][2] + matrix1[1][1] * matrix2[1][2] + matrix1[1][2] * matrix2[2][2]
				+ matrix1[1][3] * matrix2[3][2];
		result[1][3] = matrix1[1][0] * matrix2[0][3] + matrix1[1][1] * matrix2[1][3] + matrix1[1][2] * matrix2[2][3]
				+ matrix1[1][3] * matrix2[3][3];

		result[2][0] = matrix1[2][0] * matrix2[0][0] + matrix1[2][1] * matrix2[1][0] + matrix1[2][2] * matrix2[2][0]
				+ matrix1[2][3] * matrix2[3][0];
		result[2][1] = matrix1[2][0] * matrix2[0][1] + matrix1[2][1] * matrix2[1][1] + matrix1[2][2] * matrix2[2][1]
				+ matrix1[2][3] * matrix2[3][1];
		result[2][2] = matrix1[2][0] * matrix2[0][2] + matrix1[2][1] * matrix2[1][2] + matrix1[2][2] * matrix2[2][2]
				+ matrix1[2][3] * matrix2[3][2];
		result[2][3] = matrix1[2][0] * matrix2[0][3] + matrix1[2][1] * matrix2[1][3] + matrix1[2][2] * matrix2[2][3]
				+ matrix1[2][3] * matrix2[3][3];

		result[3][0] = matrix1[3][0] * matrix2[0][0] + matrix1[3][1] * matrix2[1][0] + matrix1[3][2] * matrix2[2][0]
				+ matrix1[3][3] * matrix2[3][0];
		result[3][1] = matrix1[3][0] * matrix2[0][1] + matrix1[3][1] * matrix2[1][1] + matrix1[3][2] * matrix2[2][1]
				+ matrix1[3][3] * matrix2[3][1];
		result[3][2] = matrix1[3][0] * matrix2[0][2] + matrix1[3][1] * matrix2[1][2] + matrix1[3][2] * matrix2[2][2]
				+ matrix1[3][3] * matrix2[3][2];
		result[3][3] = matrix1[3][0] * matrix2[0][3] + matrix1[3][1] * matrix2[1][3] + matrix1[3][2] * matrix2[2][3]
				+ matrix1[3][3] * matrix2[3][3];

		return new Matrix4f(result);
	}

	/**
	 * Transforms a vector by a matrix.
	 * 
	 * @param m
	 *            transforming matrix
	 * @param v
	 *            vector to be transformed
	 * @return transformed vector
	 */
	public static Vector1d transformVector(Matrix1 m, Vector1 v) {
		Vector1d result = new Vector1d();
		double m00 = m.get(0, 0);

		result.x = m00 * v.getX();

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector1f transformVector(Matrix1f m, Vector1f v) {
		Vector1f result = new Vector1f();
		float m00 = m.getf(0, 0);

		result.x = m00 * v.x;

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector2d transformVector(Matrix2 m, Vector2 v) {
		Vector2d result = new Vector2d();
		double[][] matrix = m.getArray();

		result.x = matrix[0][0] * v.getX() + matrix[1][0] * v.getY();
		result.y = matrix[0][1] * v.getX() + matrix[1][1] * v.getY();

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector2f transformVector(Matrix2f m, Vector2f v) {
		Vector2f result = new Vector2f();
		float[][] matrix = m.matrix;

		result.x = matrix[0][0] * v.x + matrix[1][0] * v.y;
		result.y = matrix[0][1] * v.x + matrix[1][1] * v.y;

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector3d transformVector(Matrix3 m, Vector3 v) {
		Vector3d result = new Vector3d();
		double[][] matrix = m.getArray();

		result.x = matrix[0][0] * v.getX() + matrix[1][0] * v.getY() + matrix[2][0] * v.getZ();
		result.y = matrix[0][1] * v.getX() + matrix[1][1] * v.getY() + matrix[2][1] * v.getZ();
		result.z = matrix[0][2] * v.getX() + matrix[1][2] * v.getY() + matrix[2][2] * v.getZ();

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector3f transformVector(Matrix3f m, Vector3f v) {
		Vector3f result = new Vector3f();
		float[][] matrix = m.matrix;

		result.x = matrix[0][0] * v.x + matrix[1][0] * v.y + matrix[2][0] * v.z;
		result.y = matrix[0][1] * v.x + matrix[1][1] * v.y + matrix[2][1] * v.z;
		result.z = matrix[0][2] * v.x + matrix[1][2] * v.y + matrix[2][2] * v.z;

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector2f transformVector(Matrix4f m, Vector2f v) {
		Vector2f result = new Vector2f();
		float[][] matrix = m.matrix;

		result.x = matrix[0][0] * v.x + matrix[1][0] * v.y + matrix[3][0];
		result.y = matrix[0][1] * v.x + matrix[1][1] * v.y + matrix[3][1];

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector3f transformVector(Matrix4f m, Vector3f v) {
		Vector3f result = new Vector3f();
		float[][] matrix = m.matrix;

		result.x = matrix[0][0] * v.x + matrix[1][0] * v.y + matrix[2][0] * v.z + matrix[3][0];
		result.y = matrix[0][1] * v.x + matrix[1][1] * v.y + matrix[2][1] * v.z + matrix[3][1];
		result.z = matrix[0][2] * v.x + matrix[1][2] * v.y + matrix[2][2] * v.z + matrix[3][2];

		return result;
	}

	/**
	 * @see math.VecMath#transformVector(Matrix1, Vector1)
	 */
	public static Vector4f transformVector(Matrix4f m, Vector4f v) {
		Vector4f result = new Vector4f();
		float[][] matrix = m.matrix;

		result.x = matrix[0][0] * v.x + matrix[1][0] * v.y + matrix[2][0] * v.z + matrix[3][0] * v.w;
		result.y = matrix[0][1] * v.x + matrix[1][1] * v.y + matrix[2][1] * v.z + matrix[3][1] * v.w;
		result.z = matrix[0][2] * v.x + matrix[1][2] * v.y + matrix[2][2] * v.z + matrix[3][2] * v.w;
		result.w = matrix[0][3] * v.x + matrix[1][3] * v.y + matrix[2][3] * v.z + matrix[3][3] * v.w;

		return result;
	}
}