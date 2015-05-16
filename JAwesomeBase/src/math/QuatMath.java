package math;

import quaternion.Quaternion;
import quaternion.Quaterniond;
import quaternion.Quaternionf;
import vector.Vector3;
import vector.Vector3d;
import vector.Vector3f;

/**
 * Applies mathematical operations on Quaternions.
 * 
 * @author Oliver Schall
 * 
 */

public class QuatMath {
	public static Quaterniond addition(Quaternion q1, Quaternion q2) {
		return new Quaterniond(q1.getQ0() + q2.getQ0(),
				q1.getQ1() + q2.getQ1(), q1.getQ2() + q2.getQ2(), q1.getQ3()
						+ q2.getQ3());
	}

	public static Quaternionf addition(Quaternionf q1, Quaternionf q2) {
		return new Quaternionf(q1.getQ0f() + q2.getQ0f(), q1.getQ1f()
				+ q2.getQ1f(), q1.getQ2f() + q2.getQ2f(), q1.getQ3f()
				+ q2.getQ3f());
	}

	public static Quaterniond conjugate(Quaternion q) {
		return new Quaterniond(q.getQ0(), -q.getQ1(), -q.getQ2(), -q.getQ3());
	}

	public static Quaternionf conjugate(Quaternionf q) {
		return new Quaternionf(q.getQ0f(), -q.getQ1f(), -q.getQ2f(),
				-q.getQ3f());
	}

	public static double dotproduct(Quaternion v1, Quaternion v2) {
		return v1.getQ0() * v2.getQ0() + v1.getQ1() * v2.getQ1() + v1.getQ2()
				* v2.getQ2() + v1.getQ3() * v2.getQ3();
	}

	public static float dotproduct(Quaternionf v1, Quaternionf v2) {
		return v1.getQ0f() * v2.getQ0f() + v1.getQ1f() * v2.getQ1f()
				+ v1.getQ2f() * v2.getQ2f() + v1.getQ3f() * v2.getQ3f();
	}

	// public static Quaterniond multiplication(Quaternion q1, Quaternion q2) {
	// return new Quaterniond(q1.getQ0() * q2.getQ0(),
	// q1.getQ1() * q2.getQ1(), q1.getQ2() * q2.getQ2(), q1.getQ3()
	// * q2.getQ3());
	// }
	//
	// public static Quaternionf multiplication(Quaternionf q1, Quaternionf q2)
	// {
	// return new Quaternionf(q1.getQ0f() * q2.getQ0f(), q1.getQ1f()
	// * q2.getQ1f(), q1.getQ2f() * q2.getQ2f(), q1.getQ3f()
	// * q2.getQ3f());
	// }

	public static Quaterniond invert(Quaternion q) {
		Quaterniond conj = conjugate(q);
		double mag = Math.abs(conj.magnitudeSquared());
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Quaternionf invert(Quaternionf q) {
		Quaternionf conj = conjugate(q);
		float mag = (float) Math.abs(conj.magnitudeSquared());
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Quaterniond lerp(Quaternion q1, Quaternion q2, double t) {
		return normalize(addition(scale(q1, 1 - t), scale(q2, t)));
	}

	public static Quaternionf lerp(Quaternionf q1, Quaternionf q2, float t) {
		return normalize(addition(scale(q1, 1 - t), scale(q2, t)));
	}

	public static Quaterniond multiplication(Quaternion q1, Quaternion q2) {
		return new Quaterniond(q1.getQ0() * q2.getQ0() - q1.getQ1()
				* q2.getQ1() - q1.getQ2() * q2.getQ2() - q1.getQ3()
				* q2.getQ3(), q1.getQ0() * q2.getQ1() + q1.getQ1() * q2.getQ0()
				+ q1.getQ2() * q2.getQ3() - q1.getQ3() * q2.getQ2(), q1.getQ0()
				* q2.getQ2() + q1.getQ2() * q2.getQ0() + q1.getQ3()
				* q2.getQ1() - q1.getQ1() * q2.getQ3(), q1.getQ0() * q2.getQ3()
				+ q1.getQ3() * q2.getQ0() + q1.getQ1() * q2.getQ2()
				- q1.getQ2() * q2.getQ1());
	}

	public static Quaternionf multiplication(Quaternionf q1, Quaternionf q2) {
		return new Quaternionf(q1.getQ0f() * q2.getQ0f() - q1.getQ1f()
				* q2.getQ1f() - q1.getQ2f() * q2.getQ2f() - q1.getQ3f()
				* q2.getQ3f(), q1.getQ0f() * q2.getQ1f() + q1.getQ1f()
				* q2.getQ0f() + q1.getQ2f() * q2.getQ3f() - q1.getQ3f()
				* q2.getQ2f(), q1.getQ0f() * q2.getQ2f() + q1.getQ2f()
				* q2.getQ0f() + q1.getQ3f() * q2.getQ1f() - q1.getQ1f()
				* q2.getQ3f(), q1.getQ0f() * q2.getQ3f() + q1.getQ3f()
				* q2.getQ0f() + q1.getQ1f() * q2.getQ2f() - q1.getQ2f()
				* q2.getQ1f());
	}

	public static Quaterniond normalize(Quaternion q) {
		double length = q.magnitude();
		return new Quaterniond(q.getQ0() / length, q.getQ1() / length,
				q.getQ2() / length, q.getQ3() / length);
	}

	public static Quaternionf normalize(Quaternionf q) {
		float length = (float) q.magnitude();
		return new Quaternionf(q.getQ0f() / length, q.getQ1f() / length,
				q.getQ2f() / length, q.getQ3f() / length);
	}

	public static Quaterniond scale(Quaternion q, double scale) {
		return new Quaterniond(q.getQ0() * scale, q.getQ1() * scale, q.getQ2()
				* scale, q.getQ3() * scale);
	}

	public static Quaternionf scale(Quaternionf q, float scale) {
		return new Quaternionf(q.getQ0f() * scale, q.getQ1f() * scale,
				q.getQ2f() * scale, q.getQ3f() * scale);
	}

	// Totalbiscuit: Engines are hard to make, especially 3d ones.

	public static Quaterniond slerp(Quaternion q1, Quaternion q2, double t) {
		Quaterniond result = new Quaterniond();
		double dot = dotproduct(q1, q2);
		if (dot < 0) {
			dot = -dot;
			result = scale(q2, -1);
		} else {
			result.set(q2);
		}

		if (dot < 0.97f) {
			double angle = Math.acos(dot);
			return scale(
					addition(scale(q1, Math.sin(angle * (1 - t))),
							scale(result, Math.sin(angle * t))),
					Math.sin(angle));
		}

		return lerp(q1, result, t);
	}

	public static Quaternionf slerp(Quaternionf q1, Quaternionf q2, float t) {
		Quaternionf result = new Quaternionf();
		float dot = dotproduct(q1, q2);
		if (dot < 0) {
			dot = -dot;
			result = scale(q2, -1);
		} else {
			result = q2;
		}

		if (dot < 0.97f) {
			float angle = (float) Math.acos(dot);
			return scale(
					addition(scale(q1, (float) Math.sin(angle * (1 - t))),
							scale(result, (float) Math.sin(angle * t))),
					(float) Math.sin(angle));
		}

		return lerp(q1, result, t);
	}

	public static Quaterniond substraction(Quaternion q1, Quaternion q2) {
		return new Quaterniond(q1.getQ0() - q2.getQ0(),
				q1.getQ1() - q2.getQ1(), q1.getQ2() - q2.getQ2(), q1.getQ3()
						- q2.getQ3());
	}

	public static Quaternionf substraction(Quaternionf q1, Quaternionf q2) {
		return new Quaternionf(q1.getQ0f() - q2.getQ0f(), q1.getQ1f()
				- q2.getQ1f(), q1.getQ2f() - q2.getQ2f(), q1.getQ3f()
				- q2.getQ3f());
	}

	public static Vector3 transform(Quaternion q, Vector3 v) {
		Vector3 u = new Vector3d(q.getQ1(), q.getQ2(), q.getQ3());
		double s = q.getQ0();
		return VecMath.addition(
				VecMath.scale(u, 2f * VecMath.dotproduct(u, v)), VecMath
						.addition(VecMath.scale(v,
								s * s - VecMath.dotproduct(u, u)), VecMath
								.scale(VecMath.crossproduct(u, v), 2f * s)));
		// Vector3 qv = new Vector3d(q.getQ1(), q.getQ2(), q.getQ3());
		// Vector3 t = VecMath.scale(VecMath.crossproduct(qv, v), 2);
		// return VecMath.addition(v, VecMath.addition(VecMath.scale(t,
		// q.getQ0()), VecMath.crossproduct(qv, t)));
	}

	public static Vector3f transform(Quaternionf q, Vector3f v) {
		Vector3f u = new Vector3f(q.getQ1f(), q.getQ2f(), q.getQ3f());
		float s = q.getQ0f();
		return VecMath.addition(
				VecMath.scale(u, 2f * VecMath.dotproduct(u, v)), VecMath
						.addition(VecMath.scale(v,
								s * s - VecMath.dotproduct(u, u)), VecMath
								.scale(VecMath.crossproduct(u, v), 2f * s)));
		// Vector3f qv = new Vector3f(q.getQ1f(), q.getQ2f(), q.getQ3f());
		// Vector3f t = VecMath.scale(VecMath.crossproduct(qv, v), 2);
		// return VecMath.addition(v, VecMath.addition(VecMath.scale(t,
		// q.getQ0f()), VecMath.crossproduct(qv, t)));
	}
}
