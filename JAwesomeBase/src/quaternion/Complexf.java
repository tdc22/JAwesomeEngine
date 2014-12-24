package quaternion;

import matrix.Matrix2d;
import matrix.Matrix2f;

public class Complexf extends Complex {
	float real, imaginary;

	public Complexf() {
		setIdentity();
	}

	public Complexf(Complex comp) {
		this.real = comp.getRealf();
		this.imaginary = comp.getImaginaryf();
	}

	public Complexf(double angle) {
		real = (float) Math.cos(Math.toRadians(angle));
		imaginary = (float) Math.sin(Math.toRadians(angle));
	}

	public Complexf(double real, double imaginary) {
		this.real = (float) real;
		this.imaginary = (float) imaginary;
	}

	public Complexf(float real, float imaginary) {
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
		return imaginary;
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
		return real;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invert() {
		conjugate();
		double mag = Math.abs(magnitudeSquared());
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
		float r = comp.getRealf();
		float i = comp.getImaginaryf();
		real = real * r - imaginary * i;
		imaginary = real * i + imaginary * r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(double angle) {
		rotate(new Complexf(angle));
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
		real = comp.getRealf();
		imaginary = comp.getImaginaryf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double real, double imaginary) {
		this.real = (float) real;
		this.imaginary = (float) imaginary;
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
		real = (float) all;
		imaginary = (float) all;
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
		return new Matrix2d(1 - 2 * imaginary * imaginary, -2 * imaginary
				* real, 2 * imaginary * real, 1 - 2 * imaginary * imaginary);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix2f toMatrixf() {
		return new Matrix2f(1 - 2 * imaginary * imaginary, -2 * imaginary
				* real, 2 * imaginary * real, 1 - 2 * imaginary * imaginary);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Complexf[");
		sb.append(real);
		sb.append(", ");
		sb.append(imaginary);
		sb.append(']');
		return sb.toString();
	}
}