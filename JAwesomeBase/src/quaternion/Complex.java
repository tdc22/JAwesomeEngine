package quaternion;

import matrix.Matrix2d;
import matrix.Matrix2f;

public abstract class Complex implements Rotation {
	/**
	 * Conjugates the complex number.
	 */
	public abstract void conjugate();

	/**
	 * Gets the imaginary part of the complex number.
	 * 
	 * @return imaginary part of the number
	 */
	public abstract double getImaginary();

	/**
	 * @see Complex#getImaginary()
	 */
	public abstract float getImaginaryf();

	/**
	 * Gets the real part of the complex number.
	 * 
	 * @return real part of the number
	 */
	public abstract double getReal();

	/**
	 * @see Complex#getReal()
	 */
	public abstract float getRealf();

	/**
	 * Calculates the magnitude of the complex number.
	 * 
	 * @return magnitude of the complex number
	 */
	public double magnitude() {
		return Math.sqrt(magnitudeSquared());
	}

	/**
	 * Calculates the squared magnitude of the complex number.
	 * 
	 * @return squared magnitude of the complex number
	 */
	public abstract double magnitudeSquared();

	/**
	 * Normalizes the complex number.
	 */
	public void normalize() {
		double mag = magnitude();
		if (mag != 0) {
			scale(1 / mag);
		} else
			throw new IllegalStateException("Zero magnitude number");
	}

	/**
	 * Rotates the complex number by another one.
	 * 
	 * @param comp
	 *            rotation complex number
	 */
	public abstract void rotate(Complex comp);

	/**
	 * Rotate the complex number by another one.
	 * 
	 * @param real
	 *            real part of the complex number
	 * @param imaginary
	 *            imaginary part of the complex number
	 */
	public abstract void rotate(double real, double imaginary);

	/**
	 * @see Complex#rotate(double, double)
	 */
	public abstract void rotate(float real, float imaginary);

	/**
	 * Rotates the complex number by an angle around an axis.
	 * 
	 * @param angle
	 *            rotation angle
	 */
	public abstract void rotate(double angle);

	/**
	 * @see Complex#rotate(double)
	 */
	public abstract void rotate(float angle);

	/**
	 * Scales the complex number by a factor.
	 * 
	 * @param scale
	 *            scaling factor
	 */
	public abstract void scale(double scale);

	/**
	 * @see Complex#scale(double)
	 */
	public abstract void scale(float scale);

	/**
	 * Sets the complex number to another one.
	 * 
	 * @param comp
	 *            new complex number
	 */
	public abstract void set(Complex comp);

	/**
	 * Sets the values of the complex number.
	 * 
	 * @param real
	 *            sets the real part of the number
	 * @param imaginary
	 *            sets the imaginary part of the number
	 */
	public abstract void set(double real, double imaginary);

	/**
	 * @see Complex#set(double, double)
	 */
	public abstract void set(float real, float imaginary);

	/**
	 * Sets all values to one.
	 * 
	 * @param all
	 *            new value
	 */
	public abstract void setAll(double all);

	/**
	 * @see Complex#setAll(double)
	 */
	public abstract void setAll(float all);

	/**
	 * Converts the complex number to a rotation matrix.
	 * 
	 * @return rotation matrix
	 */
	public abstract Matrix2d toMatrix();

	/**
	 * @see Complex#toMatrix()
	 */
	public abstract Matrix2f toMatrixf();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();

	/*
	 * Calculates the angle in polar coordinates.
	 */
	public abstract double angle();
}