package miscTests;

import math.ComplexMath;
import quaternion.Complexf;
import vector.Vector2f;

public class ComplexTest {
	public static void main(String[] args) {
		Complexf comp = new Complexf();
		Vector2f up = new Vector2f(0, 1);
		System.out.println(comp);
		System.out.println(comp.toMatrixf());
		System.out.println(ComplexMath.transform(comp, up));
		System.out.println("---------------------");
		comp.rotate(45);
		System.out.println(comp);
		System.out.println(comp.toMatrixf());
		System.out.println(ComplexMath.transform(comp, up));
		System.out.println("---------------------");
		comp.rotate(45);
		System.out.println(comp);
		System.out.println(comp.toMatrixf());
		System.out.println(ComplexMath.transform(comp, up));
	}
}
