package math;

import quaternion.Complex;
import quaternion.Complexd;
import quaternion.Complexf;
import vector.Vector2;
import vector.Vector2d;
import vector.Vector2f;

/**
 * Applies mathematical operations on complex numbers.
 * 
 * @author Oliver Schall
 * 
 */

public class ComplexMath {

	private static final float thresholdValue = 0.99f;

	public static Complexd addition(Complex c1, Complex c2) {
		return new Complexd(c1.getReal() + c2.getReal(), c1.getImaginary() + c2.getImaginary());
	}

	public static Complexf addition(Complexf c1, Complexf c2) {
		return new Complexf(c1.getRealf() + c2.getRealf(), c1.getImaginaryf() + c2.getImaginaryf());
	}

	public static Complexd conjugate(Complex c) {
		return new Complexd(c.getReal(), -c.getImaginary());
	}

	public static Complexf conjugate(Complexf c) {
		return new Complexf(c.getRealf(), -c.getImaginaryf());
	}

	public static double dotproduct(Complex v1, Complex v2) {
		return v1.getReal() * v2.getReal() + v1.getImaginary() * v2.getImaginary();
	}

	public static float dotproduct(Complexf v1, Complexf v2) {
		return v1.getRealf() * v2.getRealf() + v1.getImaginaryf() * v2.getImaginaryf();
	}

	public static Complexd negate(Complex c) {
		return new Complexd(-c.getReal(), -c.getImaginary());
	}

	public static Complexf negate(Complexf c) {
		return new Complexf(-c.getRealf(), -c.getImaginaryf());
	}

	public static Complexd invert(Complex c) {
		Complexd conj = conjugate(c);
		double mag = conj.magnitudeSquared();
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Complexf invert(Complexf c) {
		Complexf conj = conjugate(c);
		float mag = (float) conj.magnitudeSquared();
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Complexd multiplication(Complex c1, Complex c2) {
		return new Complexd(c1.getReal() * c2.getReal() - c1.getImaginary() * c2.getImaginary(),
				c1.getReal() * c2.getImaginary() + c1.getImaginary() * c2.getReal());
	}

	public static Complexf multiplication(Complexf c1, Complexf c2) {
		return new Complexf(c1.getRealf() * c2.getRealf() - c1.getImaginaryf() * c2.getImaginaryf(),
				c1.getRealf() * c2.getImaginaryf() + c1.getImaginaryf() * c2.getRealf());
	}

	public static Complexd normalize(Complex c) {
		double length = c.magnitude();
		return new Complexd(c.getReal() / length, c.getImaginary() / length);
	}

	public static Complexf normalize(Complexf c) {
		float length = (float) c.magnitude();
		return new Complexf(c.getRealf() / length, c.getImaginaryf() / length);
	}

	public static Complexd scale(Complex c, double scale) {
		return new Complexd(c.getReal() * scale, c.getImaginary() * scale);
	}

	public static Complexf scale(Complexf c, float scale) {
		return new Complexf(c.getRealf() * scale, c.getImaginaryf() * scale);
	}

	public static Complexd substraction(Complex c1, Complex c2) {
		return new Complexd(c1.getReal() - c2.getReal(), c1.getImaginary() - c2.getImaginary());
	}

	public static Complexf substraction(Complexf c1, Complexf c2) {
		return new Complexf(c1.getRealf() - c2.getRealf(), c1.getImaginaryf() - c2.getImaginaryf());
	}

	public static Complexd lerp(Complex q1, Complex q2, double t) {
		double oneMt = 1 - t;
		Complexd result = new Complexd(q1.getReal() * oneMt + q2.getReal() * t,
				q1.getImaginary() * oneMt + q2.getImaginary() * t);
		result.normalize();
		return result;
	}

	public static Complexf lerp(Complexf q1, Complexf q2, float t) {
		float oneMt = 1 - t;
		Complexf result = new Complexf(q1.getReal() * oneMt + q2.getReal() * t,
				q1.getImaginary() * oneMt + q2.getImaginary() * t);
		result.normalize();
		return result;
	}

	public static Complexd slerp(Complex c1, Complex c2, double t) {
		Complexd result;
		double dot = dotproduct(c1, c2);
		if (dot < 0) {
			dot = -dot;
			result = negate(c2);
		} else {
			result = new Complexd(c2);
		}

		if (dot < thresholdValue) {
			double angle = Math.acos(dot);
			Complex temp = scale(c1, Math.sin(angle * (1 - t)));
			result.scale(Math.sin(angle * t));
			double sina = Math.sin(angle);
			result.set((result.getReal() + temp.getReal()) / sina,
					(result.getImaginary() + temp.getImaginary()) / sina);
			return result;
		}

		return lerp(c1, result, t);
	}

	public static Complexf slerp(Complexf c1, Complexf c2, float t) {
		Complexf result;
		float dot = dotproduct(c1, c2);
		if (dot < 0) {
			dot = -dot;
			result = negate(c2);
		} else {
			result = new Complexf(c2);
		}

		if (dot < thresholdValue) {
			float angle = (float) Math.acos(dot);
			Complexf temp = scale(c1, (float) Math.sin(angle * (1 - t)));
			result.scale((float) Math.sin(angle * t));
			float sina = (float) Math.sin(angle);
			result.set((result.getRealf() + temp.getRealf()) / sina,
					(result.getImaginaryf() + temp.getImaginaryf()) / sina);
			return result;
		}

		return lerp(c1, result, t);
	}

	// public static Complexd slerp(Complex q1, Complex q2, double t) {
	// DIFFERENT FROM QUATERNIONS
	// - keine Dotproduct-Abfrage
	// }
	//
	// public static Complexf slerp(Complexf q1, Complexf q2, float t) {
	//
	// }

	public static Complexd slerpNoInvert(Complex q1, Complex q2, double t) {
		double dot = dotproduct(q1, q2);

		if (dot > -thresholdValue && dot < thresholdValue) {
			double angle = Math.acos(dot);
			double sina = Math.sin(angle);
			double sinat = Math.sin(angle * t);
			double sinaomt = Math.sin(angle * (1 - t));
			return new Complexd((q1.getReal() * sinaomt + q2.getReal() * sinat) / sina,
					(q1.getImaginary() * sinaomt + q2.getImaginary() * sinat) / sina);
		}
		return lerp(q1, q2, t);
	}

	public static Complexf slerpNoInvert(Complexf q1, Complexf q2, float t) {
		float dot = dotproduct(q1, q2);

		if (dot > -thresholdValue && dot < thresholdValue) {
			float angle = (float) Math.acos(dot);
			float sina = (float) Math.sin(angle);
			float sinat = (float) Math.sin(angle * t);
			float sinaomt = (float) Math.sin(angle * (1 - t));
			return new Complexf((q1.getReal() * sinaomt + q2.getReal() * sinat) / sina,
					(q1.getImaginary() * sinaomt + q2.getImaginary() * sinat) / sina);
		}
		return lerp(q1, q2, t);
	}

	public static Complexd squad(Complex q1, Complex q2, Complex q3, Complex q4, double t) {
		Complex a = slerpNoInvert(q1, q2, t);
		Complex b = slerpNoInvert(q3, q4, t);
		return slerpNoInvert(a, b, 2 * t * (1 - t));
	}

	public static Complexf squad(Complexf q1, Complexf q2, Complexf q3, Complexf q4, float t) {
		Complexf a = slerpNoInvert(q1, q2, t);
		Complexf b = slerpNoInvert(q3, q4, t);
		return slerpNoInvert(a, b, 2 * t * (1 - t));
	}

	public static Vector2 transform(Complex c, Vector2 v) {
		return new Vector2d(v.getX() * c.getReal() + v.getY() * c.getImaginary(),
				v.getX() * -c.getImaginary() + v.getY() * c.getReal());
	}

	public static Vector2f transform(Complexf c, Vector2f v) {
		return new Vector2f(v.getXf() * c.getRealf() + v.getYf() * c.getImaginaryf(),
				v.getXf() * -c.getImaginaryf() + v.getYf() * c.getRealf());
		// return new Vector2f(
		// (1 - 2 * c.getImaginaryf() * c.getImaginaryf()) * v.getXf()
		// + (-2 * c.getImaginaryf() * c.getRealf()) * v.getYf(),
		// (2 * c.getImaginaryf() * c.getRealf()) * v.getXf()
		// + (1 - 2 * c.getImaginaryf() * c.getImaginaryf()) * v.getYf());
	}
}