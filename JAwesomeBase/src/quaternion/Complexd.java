package quaternion;

import matrix.Matrix2d;
import matrix.Matrix2f;

public class Complexd extends Complex {
	double real, imaginary;

	public Complexd() {
		setIdentity();
	}

	public Complexd(Complex comp) {
		real = comp.getReal();
		imaginary = comp.getImaginary();
	}

	public Complexd(double angle) {
		real = Math.cos(Math.toRadians(angle));
		imaginary = Math.sin(Math.toRadians(angle));
	}

	public Complexd(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public Complexd(float real, float imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void conjugate() {
		imaginary = -imaginary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getImaginaryf() {
		return (float) imaginary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getReal() {
		return real;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRealf() {
		return (float) real;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		conjugate();
		double mag = magnitudeSquared();
		if (mag != 0)
			scale(1 / mag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double magnitudeSquared() {
		return real * real + imaginary * imaginary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Complex comp) {
		double r = comp.getReal();
		double i = comp.getImaginary();
		real = real * r - imaginary * i;
		imaginary = real * i + imaginary * r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(double angle) {
		rotate(new Complexd(angle));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(float angle) {
		rotate(new Complexf(angle));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double scale) {
		real *= scale;
		imaginary *= scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(float scale) {
		real *= scale;
		imaginary *= scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Complex comp) {
		real = comp.getReal();
		imaginary = comp.getImaginary();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(float real, float imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double all) {
		real = all;
		imaginary = all;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(float all) {
		real = all;
		imaginary = all;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentity() {
		real = 1;
		imaginary = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix2d toMatrix() {
		return new Matrix2d(1 - 2 * imaginary * imaginary, -2 * imaginary * real, 2 * imaginary * real,
				1 - 2 * imaginary * imaginary);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix2f toMatrixf() {
		return new Matrix2f((float) (1 - 2 * imaginary * imaginary), (float) (-2 * imaginary * real),
				(float) (2 * imaginary * real), (float) (1 - 2 * imaginary * imaginary));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Complexd[");
		sb.append(real);
		sb.append(", ");
		sb.append(imaginary);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public double lengthSquared() {
		return real * real + imaginary * imaginary;
	}

	@Override
	public double angle() {
		if(real > 0) return Math.atan(imaginary/real);
		if(real < 0) {
			if(imaginary >= 0) return Math.atan(imaginary/real) + Math.PI;
			else return Math.atan(imaginary/real) - Math.PI;
		}
		if (imaginary > 0) return Math.PI/2f;
		if (imaginary < 0) return -Math.PI/2f;
		return 0;
	}
}