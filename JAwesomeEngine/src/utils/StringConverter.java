package utils;

import math.VecMath;
import matrix.Matrix;
import matrix.Matrix4f;
import vector.Vector3f;

public class StringConverter {
	public static String MatrixToString(Matrix matrix, String divisor) {
		if (matrix != null)
			return matrix.getf(0, 0) + divisor + matrix.getf(0, 1) + divisor
					+ matrix.getf(0, 2) + divisor + matrix.getf(0, 3) + divisor
					+ matrix.getf(1, 0) + divisor + matrix.getf(1, 1) + divisor
					+ matrix.getf(1, 2) + divisor + matrix.getf(1, 3) + divisor
					+ matrix.getf(2, 0) + divisor + matrix.getf(2, 1) + divisor
					+ matrix.getf(2, 2) + divisor + matrix.getf(2, 3) + divisor
					+ matrix.getf(3, 0) + divisor + matrix.getf(3, 1) + divisor
					+ matrix.getf(3, 2) + divisor + matrix.getf(3, 3);
		return "";
	}

	public static Matrix4f StringToMatrix(String string, String divisor) {
		String[] strings = string.split(divisor);
		if (strings.length >= 16) {
			Matrix4f result = new Matrix4f();

			result.m00 = Float.parseFloat(strings[0]);
			result.m01 = Float.parseFloat(strings[1]);
			result.m02 = Float.parseFloat(strings[2]);
			result.m03 = Float.parseFloat(strings[3]);

			result.m10 = Float.parseFloat(strings[4]);
			result.m11 = Float.parseFloat(strings[5]);
			result.m12 = Float.parseFloat(strings[6]);
			result.m13 = Float.parseFloat(strings[7]);

			result.m20 = Float.parseFloat(strings[8]);
			result.m21 = Float.parseFloat(strings[9]);
			result.m22 = Float.parseFloat(strings[10]);
			result.m23 = Float.parseFloat(strings[11]);

			result.m30 = Float.parseFloat(strings[12]);
			result.m31 = Float.parseFloat(strings[13]);
			result.m32 = Float.parseFloat(strings[14]);
			result.m33 = Float.parseFloat(strings[15]);

			return result;
		}
		return VecMath.identityMatrix();
	}

	public static Vector3f StringToVector(String string, String divisor) {
		String[] strings = string.split(divisor);
		return new Vector3f(Float.parseFloat(strings[0]),
				Float.parseFloat(strings[1]), Float.parseFloat(strings[2]));
	}

	public static String VectorToString(Vector3f vector, String divisor) {
		return vector.x + divisor + vector.y + divisor + vector.z;
	}
}
