package miscTests;

import math.QuatMath;
import math.VecMath;
import matrix.Matrix4f;
import quaternion.Quaternionf;
import vector.Vector3f;

public class QuaternionTest {
	public static void main(String[] args) {
		System.out.println(new Quaternionf().toMatrix());
		System.out.println();

		System.out.println("X-Axis:");
		Quaternionf rot1 = new Quaternionf();
		rot1.rotate(90, new Vector3f(1, 0, 0));
		System.out.println(rot1);
		System.out.println(rot1.toMatrix());
		System.out.println("------------");
		Matrix4f m = new Matrix4f();
		m.rotate(Math.toRadians(90), new Vector3f(1, 0, 0));
		System.out.println(m.getSubMatrix());
		System.out.println();

		System.out.println("Y-Axis:");
		rot1.setIdentity();
		rot1.rotate(90, new Vector3f(0, 1, 0));
		System.out.println(rot1);
		System.out.println(rot1.toMatrix());
		System.out.println("------------");
		m = new Matrix4f();
		m.rotate(Math.toRadians(90), new Vector3f(0, 1, 0));
		System.out.println(m.getSubMatrix());
		System.out.println();

		System.out.println("Y-Axis: (2)");
		rot1.rotate(60, new Vector3f(0, 1, 0));
		System.out.println(rot1);
		System.out.println(rot1.toMatrix());
		System.out.println("------------");
		m.rotate(60, new Vector3f(0, 1, 0));
		System.out.println(m.getSubMatrix());
		System.out.println();

		System.out.println("Z-Axis:");
		rot1.setIdentity();
		rot1.rotate(90, new Vector3f(0, 0, 1));
		System.out.println(rot1);
		System.out.println(rot1.toMatrix());
		System.out.println("------------");
		m = new Matrix4f();
		m.rotate(90, new Vector3f(0, 0, 1));
		System.out.println(m.getSubMatrix());
		System.out.println();

		rot1.setIdentity();
		System.out.println(rot1.toMatrix());
		System.out.println();

		Quaternionf quat = new Quaternionf(0, 2, -1, -3);
		quat.invert();
		System.out.println(quat);
		quat.invert();
		System.out.println(quat);
		quat.normalize();
		System.out.println(quat);
		System.out.println(quat.toMatrixf());

		System.out.println();
		System.out.println("Rotation by 90 deg: ");

		Quaternionf rot = new Quaternionf();
		rot.rotate(90, new Vector3f(0, 0, 1));
		System.out.println(rot);
		System.out.println(rot.toMatrix());
		System.out.println("Quaternion: " + QuatMath.transform(rot, new Vector3f(1, 0, 0)));

		Matrix4f rotm = new Matrix4f();
		rotm.rotate(90, new Vector3f(0, 0, 1));
		System.out.println("Matrix: " + VecMath.transformVector(rotm, new Vector3f(1, 0, 0)));
		System.out.println(rotm);

		System.out.println();
		Quaternionf threeangles = new Quaternionf();
		System.out.println("0: " + threeangles);
		threeangles.rotate(60, new Vector3f(1.0f, 0.0f, 0.0f));
		System.out.println("1: " + threeangles);
		threeangles.rotate(40, new Vector3f(0.0f, 1.0f, 0.0f));
		System.out.println("2: " + threeangles);
		threeangles.rotate(20, new Vector3f(0.0f, 0.0f, 1.0f));
		System.out.println("Three angles: " + threeangles); // expected: 0.77;
															// 0.51; 0.02; 0.37
	}
}
