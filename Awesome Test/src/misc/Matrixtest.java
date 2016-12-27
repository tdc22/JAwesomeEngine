package misc;

import matrix.Matrix4f;
import vector.Vector3f;

public class Matrixtest {
	public static void main(String[] args) {
		Matrix4f mat1 = new Matrix4f();
		Matrix4f mat2 = mat1;
		System.out.println(mat1);
		System.out.println(mat2);
		mat1.translate(new Vector3f(1, 2, 3));
		System.out.println(mat1);
		System.out.println(mat2);
		mat2.rotate(45, new Vector3f(1, 0, 0));
		System.out.println(mat1);
		System.out.println(mat2);
		System.out.println((float) Math.cos(Math.toRadians(45)));
	}
}
