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
	public static Complexd addition(Complex c1, Complex c2) {
		return new Complexd(c1.getReal() + c2.getReal(), c1.getImaginary()
				+ c2.getImaginary());
	}

	public static Complexf addition(Complexf c1, Complexf c2) {
		return new Complexf(c1.getRealf() + c2.getRealf(), c1.getImaginaryf()
				+ c2.getImaginaryf());
	}

	public static Complexd conjugate(Complex c) {
		return new Complexd(c.getReal(), -c.getImaginary());
	}

	public static Complexf conjugate(Complexf c) {
		return new Complexf(c.getRealf(), -c.getImaginaryf());
	}

	public static Complexd invert(Complex c) {
		Complexd conj = conjugate(c);
		double mag = Math.abs(conj.magnitudeSquared());
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Complexf invert(Complexf c) {
		Complexf conj = conjugate(c);
		float mag = (float) Math.abs(conj.magnitudeSquared());
		if (mag != 0)
			conj.scale(1 / mag);
		return conj;
	}

	public static Complexd multiplication(Complex c1, Complex c2) {
		return new Complexd(c1.getReal() * c2.getReal() - c1.getImaginary()
				* c2.getImaginary(), c1.getReal() * c2.getImaginary()
				+ c1.getImaginary() * c2.getReal());
	}

	public static Complexf multiplication(Complexf c1, Complexf c2) {
		return new Complexf(c1.getRealf() * c2.getRealf() - c1.getImaginaryf()
				* c2.getImaginaryf(), c1.getRealf() * c2.getImaginaryf()
				+ c1.getImaginaryf() * c2.getRealf());
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
		return new Complexd(c1.getReal() - c2.getReal(), c1.getImaginary()
				- c2.getImaginary());
	}

	public static Complexf substraction(Complexf c1, Complexf c2) {
		return new Complexf(c1.getRealf() - c2.getRealf(), c1.getImaginaryf()
				- c2.getImaginaryf());
	}

	public static Vector2 transform(Complex c, Vector2 v) {
		return new Vector2d((1 - 2 * c.getImaginary() * c.getImaginary())
				* v.getX() + (-2 * c.getImaginary() * c.getReal()) * v.getY(),
				(2 * c.getImaginary() * c.getReal()) * v.getX()
						+ (1 - 2 * c.getImaginary() * c.getImaginary())
						* v.getY());
	}

	public static Vector2f transform(Complexf c, Vector2f v) {
		return new Vector2f((1 - 2 * c.getImaginaryf() * c.getImaginaryf())
				* v.getXf() + (-2 * c.getImaginaryf() * c.getRealf())
				* v.getYf(), (2 * c.getImaginaryf() * c.getRealf()) * v.getXf()
				+ (1 - 2 * c.getImaginaryf() * c.getImaginaryf()) * v.getYf());
	}
}