package miscTests;

import math.QuatMath;
import matrix.Matrix3f;
import quaternion.Quaternionf;
import vector.Vector3f;

public class QuaternionTest2 {
	public static void main(String[] args) {
		// Quaternion to Matrix
		// source:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/
		Quaternionf q1 = new Quaternionf(0.7071, 0.7071, 0, 0);
		System.out.println(q1.toMatrix());

		System.out.println();

		// Matrix to Quaternion
		// source:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/
		Matrix3f m = new Matrix3f(1, 0, 0, 0, 0, -1, 0, 1, 0);
		System.out.println(m.toQuaternion());

		System.out.println();

		// Quaternion Multiplication
		// source:
		// http://www.wolframalpha.com/input/?i=quaternion%3A+0%2B2i-j-3k+*+-5%2B3i-4j%2B6k
		Quaternionf a1 = new Quaternionf(0, 2, -1, -3);
		Quaternionf a2 = new Quaternionf(-5, 3, -4, 6);
		System.out.println(QuatMath.multiplication(a1, a2));
		a1.rotate(a2);
		System.out.println(a1);

		System.out.println();

		// Quaternion angle axis rotation
		// source:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToQuaternion/
		Quaternionf q2 = new Quaternionf();
		q2.rotate(Math.toRadians(90), new Vector3f(1, 0, 0));

		System.out.println(q2);

		System.out.println();
	}
}
