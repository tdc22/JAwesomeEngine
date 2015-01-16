package miscTests;

import math.VecMath;
import vector.Vector3f;

public class TetrahedronValues {
	public static void main(String[] args) {
		System.out.println(VecMath.normalize(new Vector3f(1, 0, -1/Math.sqrt(2))));
		System.out.println(VecMath.normalize(new Vector3f(-1, 0, -1/Math.sqrt(2))));
		System.out.println(VecMath.normalize(new Vector3f(0, 1, 1/Math.sqrt(2))));
		System.out.println(VecMath.normalize(new Vector3f(0, -1, 1/Math.sqrt(2))));
	}
}
