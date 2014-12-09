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
		return new Vector2d(c.getReal() * v.getX() + -c.getImaginary()
				* v.getY(), c.getImaginary() * v.getX() + c.getReal()
				* v.getY());
	}

	public static Vector2f transform(Complexf c, Vector2f v) {
		return new Vector2f(c.getRealf() * v.getXf() + -c.getImaginaryf()
				* v.getYf(), c.getImaginaryf() * v.getXf() + c.getRealf()
				* v.getYf());
	}
}