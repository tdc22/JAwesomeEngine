package vector;

import java.nio.FloatBuffer;

/**
 * The base class for all vectors.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Vector {
	/**
	 * Gets the n-th element of the vector.
	 * 
	 * @param i
	 *            number of the element
	 * @return element at position n
	 */
	public abstract double get(int i);

	/**
	 * @see Vector#get(int)
	 */
	public abstract float getf(int i);

	/**
	 * Inverts all elements of the vector. (e.g. 1/x)
	 */
	public abstract void invert();

	/**
	 * Calculates the length of the vector.
	 * 
	 * @return length of the vector
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Calculates the squared length of the vector.
	 * 
	 * @return squared length of the vector
	 */
	public abstract double lengthSquared();

	/**
	 * Loads the vector from a FloatBuffer.
	 * 
	 * @param buf
	 *            FloatBuffer to load from
	 */
	public abstract void load(FloatBuffer buf);

	/**
	 * Negates the vector.
	 */
	public abstract void negate();

	/**
	 * Normalizes the vector by scaling it with the inverse of the length.
	 * 
	 * @throws IllegalStateException
	 *             if the vector has a length of zero
	 */
	public void normalize() {
		double length = length();
		if (length != 0) {
			scale(1 / length);
		} else
			throw new IllegalStateException("Zero length vector");
	}

	/**
	 * Scales the vector by a factor.
	 * 
	 * @param scale
	 *            scaling factor
	 */
	public abstract void scale(double scale);

	/**
	 * @see Vector#scale(double)
	 */
	public abstract void scale(float scale);

	/**
	 * Sets all elements of the vector to a value.
	 * 
	 * @param set
	 *            value to set all vector components to
	 */
	public abstract void setAll(double set);

	/**
	 * @see Vector#setAll(double)
	 */
	public abstract void setAll(float set);

	/**
	 * Sets the length of the vector.
	 * 
	 * @param length
	 *            new length of the vector
	 */
	public void setLength(double length) {
		normalize();
		scale(length);
	}

	/**
	 * Stores the vector in a FloatBuffer.
	 * 
	 * @param buf
	 *            the FloatBuffer the vector gets stored in
	 */
	public abstract void store(FloatBuffer buf);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();
}