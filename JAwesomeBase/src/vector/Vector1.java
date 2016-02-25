package vector;

import matrix.Matrix1;
import matrix.Matrix1f;

/**
 * Superclass for 1-dimensional vectors.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Vector1 extends Vector {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Vector1) {
			return (getX() == ((Vector1) other).getX());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getDimensions() {
		return 1;
	}

	/**
	 * Gets the x-value.
	 * 
	 * @return x-value
	 */
	public abstract double getX();

	/**
	 * @see Vector1#getX()
	 */
	public abstract float getXf();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(getX());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Scales the vector by another one.
	 * 
	 * @param scale
	 *            scaling vector
	 */
	public abstract void scale(Vector1 scale);

	/**
	 * Sets the value.
	 * 
	 * @param x
	 *            new value
	 */
	public abstract void set(double x);

	/**
	 * @see Vector1#set(double)
	 */
	public abstract void set(float x);

	/**
	 * Sets the vector to another one.
	 * 
	 * @param set
	 *            new vector
	 */
	public abstract void set(Vector1 set);

	/**
	 * Sets the x-value.
	 * 
	 * @param x
	 *            new x-value
	 */
	public abstract void setX(double x);

	/**
	 * @see Vector1#setX(double)
	 */
	public abstract void setX(float x);

	/**
	 * Transforms the vector by a matrix.
	 * 
	 * @param transform
	 *            matrix to transform by
	 */
	public abstract void transform(Matrix1 transform);

	/**
	 * Translates the vector.
	 * 
	 * @param transx
	 *            value to translate by
	 */
	public abstract void translate(double transx);

	/**
	 * @see Vector1#translate(double)
	 */
	public abstract void translate(float transx);

	/**
	 * Translates the vector by another one.
	 * 
	 * @param trans
	 *            vector to translate by
	 */
	public abstract void translate(Vector1 trans);
}