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

	private static final float thresholdValue = 0.99f;

	public static Quaterniond addition(Quaternion q1, Quaternion q2) {
		return new Quaterniond(q1.getQ0() + q2.getQ0(), q1.getQ1() + q2.getQ1(), q1.getQ2() + q2.getQ2(),
				q1.getQ3() + q2.getQ3());
	}

	public static Quaternionf addition(Quaternionf q1, Quaternionf q2) {
		return new Quaternionf(q1.getQ0f() + q2.getQ0f(), q1.getQ1f() + q2.getQ1f(), q1.getQ2f() + q2.getQ2f(),
				q1.getQ3f() + q2.getQ3f());
	}

	public static Quaterniond substraction(Quaternion q1, Quaternion q2) {
		return new Quaterniond(q1.getQ0() - q2.getQ0(), q1.getQ1() - q2.getQ1(), q1.getQ2() - q2.getQ2(),
				q1.getQ3() - q2.getQ3());
	}

	public static Quaternionf substraction(Quaternionf q1, Quaternionf q2) {
		return new Quaternionf(q1.getQ0f() - q2.getQ0f(), q1.getQ1f() - q2.getQ1f(), q1.getQ2f() - q2.getQ2f(),
				q1.getQ3f() - q2.getQ3f());
	}

	public static Quaterniond conjugate(Quaternion q) {
		return new Quaterniond(q.getQ0(), -q.getQ1(), -q.getQ2(), -q.getQ3());
	}

	public static Quaternionf conjugate(Quaternionf q) {
		return new Quaternionf(q.getQ0f(), -q.getQ1f(), -q.getQ2f(), -q.getQ3f());
	}

	public static double dotproduct(Quaternion v1, Quaternion v2) {
		return v1.getQ0() * v2.getQ0() + v1.getQ1() * v2.getQ1() + v1.getQ2() * v2.getQ2() + v1.getQ3() * v2.getQ3();
	}

	public static float dotproduct(Quaternionf v1, Quaternionf v2) {
		return v1.getQ0f() * v2.getQ0f() + v1.getQ1f() * v2.getQ1f() + v1.getQ2f() * v2.getQ2f()
				+ v1.getQ3f() * v2.getQ3f();
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
		double mag = conj.magnitudeSquared();
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Quaternionf invert(Quaternionf q) {
		Quaternionf conj = conjugate(q);
		float mag = (float) conj.magnitudeSquared();
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Quaterniond negate(Quaternion q) {
		return new Quaterniond(-q.getQ0(), -q.getQ1(), -q.getQ2(), -q.getQ3());
	}

	public static Quaternionf negate(Quaternionf q) {
		return new Quaternionf(-q.getQ0f(), -q.getQ1f(), -q.getQ2f(), -q.getQ3f());
	}

	public static Quaterniond multiplication(Quaternion q1, Quaternion q2) {
		return new Quaterniond(
				q1.getQ0() * q2.getQ0() - q1.getQ1() * q2.getQ1() - q1.getQ2() * q2.getQ2() - q1.getQ3() * q2.getQ3(),
				q1.getQ0() * q2.getQ1() + q1.getQ1() * q2.getQ0() + q1.getQ2() * q2.getQ3() - q1.getQ3() * q2.getQ2(),
				q1.getQ0() * q2.getQ2() + q1.getQ2() * q2.getQ0() + q1.getQ3() * q2.getQ1() - q1.getQ1() * q2.getQ3(),
				q1.getQ0() * q2.getQ3() + q1.getQ3() * q2.getQ0() + q1.getQ1() * q2.getQ2() - q1.getQ2() * q2.getQ1());
	}

	public static Quaternionf multiplication(Quaternionf q1, Quaternionf q2) {
		return new Quaternionf(
				q1.getQ0f() * q2.getQ0f() - q1.getQ1f() * q2.getQ1f() - q1.getQ2f() * q2.getQ2f()
						- q1.getQ3f() * q2.getQ3f(),
				q1.getQ0f() * q2.getQ1f() + q1.getQ1f() * q2.getQ0f() + q1.getQ2f() * q2.getQ3f()
						- q1.getQ3f() * q2.getQ2f(),
				q1.getQ0f() * q2.getQ2f() + q1.getQ2f() * q2.getQ0f() + q1.getQ3f() * q2.getQ1f()
						- q1.getQ1f() * q2.getQ3f(),
				q1.getQ0f() * q2.getQ3f() + q1.getQ3f() * q2.getQ0f() + q1.getQ1f() * q2.getQ2f()
						- q1.getQ2f() * q2.getQ1f());
	}

	public static Quaterniond normalize(Quaternion q) {
		double length = q.magnitude();
		return new Quaterniond(q.getQ0() / length, q.getQ1() / length, q.getQ2() / length, q.getQ3() / length);
	}

	public static Quaternionf normalize(Quaternionf q) {
		float length = (float) q.magnitude();
		return new Quaternionf(q.getQ0f() / length, q.getQ1f() / length, q.getQ2f() / length, q.getQ3f() / length);
	}

	public static Quaterniond scale(Quaternion q, double scale) {
		return new Quaterniond(q.getQ0() * scale, q.getQ1() * scale, q.getQ2() * scale, q.getQ3() * scale);
	}

	public static Quaternionf scale(Quaternionf q, float scale) {
		return new Quaternionf(q.getQ0f() * scale, q.getQ1f() * scale, q.getQ2f() * scale, q.getQ3f() * scale);
	}

	// Totalbiscuit: Engines are hard to make, especially 3d ones.

	public static Quaterniond lerp(Quaternion q1, Quaternion q2, double t) {
		double oneMt = 1 - t;
		Quaterniond result = new Quaterniond(q1.getQ0() * oneMt + q2.getQ0() * t, q1.getQ1() * oneMt + q2.getQ1() * t,
				q1.getQ2() * oneMt + q2.getQ2() * t, q1.getQ3() * oneMt + q2.getQ3() * t);
		result.normalize();
		return result;
	}

	public static Quaternionf lerp(Quaternionf q1, Quaternionf q2, float t) {
		float oneMt = 1 - t;
		Quaternionf result = new Quaternionf(q1.getQ0f() * oneMt + q2.getQ0f() * t,
				q1.getQ1f() * oneMt + q2.getQ1f() * t, q1.getQ2f() * oneMt + q2.getQ2f() * t,
				q1.getQ3f() * oneMt + q2.getQ3f() * t);
		result.normalize();
		return result;
	}

	public static Quaterniond slerp(Quaternion q1, Quaternion q2, double t) {
		Quaterniond result;
		double dot = dotproduct(q1, q2);
		if (dot < 0) {
			dot = -dot;
			result = negate(q2);
		} else {
			result = new Quaterniond(q2);
		}

		if (dot < thresholdValue) {
			double angle = Math.acos(dot);
			Quaternion temp = scale(q1, Math.sin(angle * (1 - t)));
			result.scale(Math.sin(angle * t));
			result.set(result.getQ0() + temp.getQ0(), result.getQ1() + temp.getQ1(), result.getQ2() + temp.getQ2(),
					result.getQ3() + temp.getQ3());
			result.scale(1 / Math.sin(angle));
			return result;
		}

		return lerp(q1, result, t);
	}

	public static Quaternionf slerp(Quaternionf q1, Quaternionf q2, float t) {
		Quaternionf result;
		float dot = dotproduct(q1, q2);
		if (dot < 0) {
			dot = -dot;
			result = negate(q2);
		} else {
			result = new Quaternionf(q2);
		}

		if (dot < thresholdValue) {
			float angle = (float) Math.acos(dot);
			Quaternionf temp = scale(q1, (float) Math.sin(angle * (1 - t)));
			result.scale(Math.sin(angle * t));
			result.set(result.getQ0f() + temp.getQ0f(), result.getQ1f() + temp.getQ1f(),
					result.getQ2f() + temp.getQ2f(), result.getQ3f() + temp.getQ3f());
			result.scale(1 / Math.sin(angle));
			return result;
		}

		return lerp(q1, result, t);
	}

	public static Quaterniond slerpNoInvert(Quaternion q1, Quaternion q2, double t) {
		double dot = dotproduct(q1, q2);

		if (dot > -thresholdValue && dot < thresholdValue) {
			double angle = Math.acos(dot);
			// TODO: check for sina == 0
			double sina = Math.sin(angle);
			double sinat = Math.sin(angle * t);
			double sinaomt = Math.sin(angle * (1 - t));
			return new Quaterniond((q1.getQ0() * sinaomt + q2.getQ0() * sinat) / sina,
					(q1.getQ1() * sinaomt + q2.getQ1() * sinat) / sina,
					(q1.getQ2() * sinaomt + q2.getQ2() * sinat) / sina,
					(q1.getQ3() * sinaomt + q2.getQ3() * sinat) / sina);
		}
		return lerp(q1, q2, t);
	}

	public static Quaternionf slerpNoInvert(Quaternionf q1, Quaternionf q2, float t) {
		float dot = dotproduct(q1, q2);

		if (dot > -thresholdValue && dot < thresholdValue) {
			float angle = (float) Math.acos(dot);
			// TODO: check for sina == 0
			float sina = (float) Math.sin(angle);
			float sinat = (float) Math.sin(angle * t);
			float sinaomt = (float) Math.sin(angle * (1 - t));
			return new Quaternionf((q1.getQ0f() * sinaomt + q2.getQ0f() * sinat) / sina,
					(q1.getQ1f() * sinaomt + q2.getQ1f() * sinat) / sina,
					(q1.getQ2f() * sinaomt + q2.getQ2f() * sinat) / sina,
					(q1.getQ3f() * sinaomt + q2.getQ3f() * sinat) / sina);
		}
		return lerp(q1, q2, t);
	}

	public static Quaterniond squad(Quaternion q1, Quaternion q2, Quaternion q3, Quaternion q4, double t) {
		Quaternion a = slerpNoInvert(q1, q2, t);
		Quaternion b = slerpNoInvert(q3, q4, t);
		return slerpNoInvert(a, b, 2 * t * (1 - t));
	}

	public static Quaternionf squad(Quaternionf q1, Quaternionf q2, Quaternionf q3, Quaternionf q4, float t) {
		Quaternionf a = slerpNoInvert(q1, q2, t);
		Quaternionf b = slerpNoInvert(q3, q4, t);
		return slerpNoInvert(a, b, 2 * t * (1 - t));
	}

	public static Vector3 transform(Quaternion q, Vector3 v) {
		Vector3 u = new Vector3d(q.getQ1(), q.getQ2(), q.getQ3());
		double s = q.getQ0();
		double dotUV = VecMath.dotproduct(u, v);
		double dotUU = VecMath.dotproduct(u, u);
		return new Vector3d(
				u.getX() * 2 * dotUV + v.getX() * (s * s - dotUU) + (u.getY() * v.getZ() - u.getZ() * v.getY()) * 2 * s,
				u.getY() * 2 * dotUV + v.getY() * (s * s - dotUU) + (u.getZ() * v.getX() - u.getX() * v.getZ()) * 2 * s,
				u.getZ() * 2 * dotUV + v.getZ() * (s * s - dotUU)
						+ (u.getX() * v.getY() - u.getY() * v.getX()) * 2 * s);
		// return VecMath.addition(VecMath.scale(u, 2f * VecMath.dotproduct(u,
		// v)), VecMath.addition(
		// VecMath.scale(v, s * s - VecMath.dotproduct(u, u)),
		// VecMath.scale(VecMath.crossproduct(u, v), 2f * s)));
	}

	public static Vector3f transform(Quaternionf q, Vector3f v) {
		Vector3f u = new Vector3f(q.getQ1f(), q.getQ2f(), q.getQ3f());
		float s = q.getQ0f();
		float dotUV = VecMath.dotproduct(u, v);
		float dotUU = VecMath.dotproduct(u, u);
		return new Vector3f(u.x * 2 * dotUV + v.x * (s * s - dotUU) + (u.y * v.z - u.z * v.y) * 2 * s,
				u.y * 2 * dotUV + v.y * (s * s - dotUU) + (u.z * v.x - u.x * v.z) * 2 * s,
				u.z * 2 * dotUV + v.z * (s * s - dotUU) + (u.x * v.y - u.y * v.x) * 2 * s);
		// return VecMath.addition(VecMath.scale(u, 2f * VecMath.dotproduct(u,
		// v)), VecMath.addition(
		// VecMath.scale(v, s * s - VecMath.dotproduct(u, u)),
		// VecMath.scale(VecMath.crossproduct(u, v), 2f * s)));
		// Vector3f qv = new Vector3f(q.getQ1f(), q.getQ2f(), q.getQ3f());
		// Vector3f t = VecMath.scale(VecMath.crossproduct(qv, v), 2);
		// return VecMath.addition(v, VecMath.addition(VecMath.scale(t,
		// q.getQ0f()), VecMath.crossproduct(qv, t)));
	}
}
