package vector;

import matrix.Matrix2;

/**
 * Superclass for 2-dimensional vectors.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Vector2 extends Vector {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Vector2) {
			return (getX() == ((Vector2) other).getX() && getY() == ((Vector2) other)
					.getY());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int getDimensions() {
		return 2;
	}

	/**
	 * @see Vector1#getX()
	 */
	public abstract double getX();

	/**
	 * @see Vector1#getXf()
	 */
	public abstract float getXf();

	/**
	 * Gets the y-value.
	 * 
	 * @return y-value
	 */
	public abstract double getY();

	/**
	 * @see Vector2#getY()
	 */
	public abstract float getYf();

	/**
	 * Scales the vector.
	 * 
	 * @param scalex
	 *            x-factor
	 * @param scaley
	 *            y-factor
	 */
	public abstract void scale(double scalex, double scaley);

	/**
	 * @see Vector2#scale(double, double)
	 */
	public abstract void scale(float scalex, float scaley);

	/**
	 * @see Vector1#scale(Vector1)
	 */
	public abstract void scale(Vector2 scale);

	/**
	 * Sets the vector values.
	 * 
	 * @param x
	 *            new x-value
	 * @param y
	 *            new y-value
	 */
	public abstract void set(double x, double y);

	/**
	 * @see Vector2#set(double, double)
	 */
	public abstract void set(float x, float y);

	/**
	 * @see Vector1#set(Vector1)
	 */
	public abstract void set(Vector2 set);

	/**
	 * @see Vector1#setX(double)
	 */
	public abstract void setX(double x);

	/**
	 * @see Vector1#setX(float)
	 */
	public abstract void setX(float x);

	/**
	 * Sets the y-value.
	 * 
	 * @param y
	 *            new y-value
	 */
	public abstract void setY(double y);

	/**
	 * @see Vector2#setY(double)
	 */
	public abstract void setY(float y);

	/**
	 * @see Vector1#transform(matrix.Matrix1)
	 */
	public abstract void transform(Matrix2 transform);

	/**
	 * @see Vector1#translate(double)
	 */
	public abstract void translate(double transx, double transy);

	/**
	 * @see Vector1#translate(float)
	 */
	public abstract void translate(float transx, float transy);

	/**
	 * @see Vector1#translate(Vector1)
	 */
	public abstract void translate(Vector2 trans);
}