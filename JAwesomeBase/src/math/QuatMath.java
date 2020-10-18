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
			double sina = Math.sin(angle);
			double sinaomt = Math.sin(angle * (1 - t));
			result.scale(Math.sin(angle * t));
			result.set((result.getQ0() + q1.getQ0() * sinaomt) / sina, (result.getQ1() + q1.getQ1() * sinaomt) / sina,
					(result.getQ2() + q1.getQ2() * sinaomt) / sina, (result.getQ3() + q1.getQ3() * sinaomt) / sina);
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
			float sina = (float) Math.sin(angle);
			float sinaomt = (float) Math.sin(angle * (1 - t));
			result.scale(Math.sin(angle * t));
			result.set((result.getQ0f() + q1.getQ0f() * sinaomt) / sina,
					(result.getQ1f() + q1.getQ1f() * sinaomt) / sina, (result.getQ2f() + q1.getQ2f() * sinaomt) / sina,
					(result.getQ3f() + q1.getQ3f() * sinaomt) / sina);
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
		Quaternion a = slerpNoInvert(q1, q4, t);
		Quaternion b = slerpNoInvert(q2, q3, t);
		// 2 * t * (1 - t) -> 0, 0.5, 0
		return slerpNoInvert(a, b, 2 * t * (1 - t));
	}

	public static Quaternionf squad(Quaternionf q1, Quaternionf q2, Quaternionf q3, Quaternionf q4, float t) {
		Quaternionf a = slerpNoInvert(q1, q4, t);
		Quaternionf b = slerpNoInvert(q2, q3, t);
		// 2 * t * (1 - t) -> 0, 0.5, 0
		return slerpNoInvert(a, b, 2 * t * (1 - t));
	}

	public static Vector3 transform(Quaternion q, Vector3 v) {
		double s = q.getQ0();
		double dotUV = q.getQ1() * v.getXf() + q.getQ2() * v.getYf() + q.getQ3() * v.getZf();
		double dotUU = q.getQ1() * q.getQ1() + q.getQ2() * q.getQ2() + q.getQ3() * q.getQ3();
		return new Vector3d(
				q.getQ1() * 2 * dotUV + v.getX() * (s * s - dotUU)
						+ (q.getQ2() * v.getZ() - q.getQ3() * v.getY()) * 2 * s,
				q.getQ2() * 2 * dotUV + v.getY() * (s * s - dotUU)
						+ (q.getQ3() * v.getX() - q.getQ1() * v.getZ()) * 2 * s,
				q.getQ3() * 2 * dotUV + v.getZ() * (s * s - dotUU)
						+ (q.getQ1() * v.getY() - q.getQ2() * v.getX()) * 2 * s);
	}

	public static Vector3f transform(Quaternionf q, Vector3f v) {
		float s = q.getQ0f();
		double dotUV = q.getQ1() * v.getXf() + q.getQ2() * v.getYf() + q.getQ3() * v.getZf();
		double dotUU = q.getQ1() * q.getQ1() + q.getQ2() * q.getQ2() + q.getQ3() * q.getQ3();
		return new Vector3f(
				q.getQ1f() * 2 * dotUV + v.x * (s * s - dotUU) + (q.getQ2f() * v.z - q.getQ3f() * v.y) * 2 * s,
				q.getQ2f() * 2 * dotUV + v.y * (s * s - dotUU) + (q.getQ3f() * v.x - q.getQ1f() * v.z) * 2 * s,
				q.getQ3f() * 2 * dotUV + v.z * (s * s - dotUU) + (q.getQ1f() * v.y - q.getQ2f() * v.x) * 2 * s);
	}
}
