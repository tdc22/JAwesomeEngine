package miscTests;

import math.VecMath;
import vector.Vector3f;

public class NormalTest {
	public static void main(String[] args) {
		Vector3f a = new Vector3f(1, 1, 1);
		Vector3f b = new Vector3f(1, 1, 2);
		Vector3f c = new Vector3f(1, 2, 1);
		System.out.println(VecMath.computeNormal(a, b, c));

		Vector3f ab = VecMath.subtraction(b, a);
		Vector3f ac = VecMath.subtraction(c, a);
		System.out.println(VecMath.crossproduct(ab, ac));
	}
}
