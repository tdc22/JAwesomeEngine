package miscTests;

import math.VecMath;
import vector.Vector3f;

public class TetrahedronValues {
	public static void main(String[] args) {
		System.out.println(VecMath.normalize(new Vector3f(1, 0, -1 / Math.sqrt(2))));
		System.out.println(VecMath.normalize(new Vector3f(-1, 0, -1 / Math.sqrt(2))));
		System.out.println(VecMath.normalize(new Vector3f(0, 1, 1 / Math.sqrt(2))));
		System.out.println(VecMath.normalize(new Vector3f(0, -1, 1 / Math.sqrt(2))));
		System.out.println("------------------------------------");
		float tao = 1.61803399f;
		float gs = (1 + (float) Math.sqrt(5)) / 2f;
		System.out.println(tao + "; " + gs);
		System.out.println("------------------------------------");
		System.out.println(VecMath.normalize(new Vector3f(1, tao, 0)));
		System.out.println(VecMath.normalize(new Vector3f(-1, tao, 0)));
		System.out.println(VecMath.normalize(new Vector3f(1, -tao, 0)));
		System.out.println(VecMath.normalize(new Vector3f(-1, -tao, 0)));
		System.out.println(VecMath.normalize(new Vector3f(0, 1, tao)));
		System.out.println(VecMath.normalize(new Vector3f(0, -1, tao)));
		System.out.println(VecMath.normalize(new Vector3f(0, 1, -tao)));
		System.out.println(VecMath.normalize(new Vector3f(0, -1, -tao)));
		System.out.println(VecMath.normalize(new Vector3f(tao, 0, 1)));
		System.out.println(VecMath.normalize(new Vector3f(-tao, 0, 1)));
		System.out.println(VecMath.normalize(new Vector3f(tao, 0, -1)));
		System.out.println(VecMath.normalize(new Vector3f(-tao, 0, -1)));
	}
}
