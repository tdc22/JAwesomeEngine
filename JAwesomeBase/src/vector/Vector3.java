package vector;

import matrix.Matrix3;

/**
 * Superclass for 3-dimensional vectors.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Vector3 extends Vector {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Vector3) {
			return (getX() == ((Vector3) other).getX()
					&& getY() == ((Vector3) other).getY() && getZ() == ((Vector3) other)
						.getZ());
		}
		return false;
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
	 * @see Vector2#getY()
	 */
	public abstract double getY();

	/**
	 * @see Vector2#getYf()
	 */
	public abstract float getYf();

	/**
	 * Gets the z-value.
	 * 
	 * @return z-value
	 */
	public abstract double getZ();

	/**
	 * @see Vector3#getZ()
	 */
	public abstract float getZf();

	/**
	 * Scales the vector.
	 * 
	 * @param scalex
	 *            x-factor
	 * @param scaley
	 *            y-factor
	 * @param scalez
	 *            z-factor
	 */
	public abstract void scale(double scalex, double scaley, double scalez);

	/**
	 * @see Vector3#scale(double, double, double)
	 */
	public abstract void scale(float scalex, float scaley, float scalez);

	/**
	 * @see Vector1#scale(Vector1)
	 */
	public abstract void scale(Vector3 scale);

	/**
	 * Sets the vector values.
	 * 
	 * @param x
	 *            new x-value
	 * @param y
	 *            new y-value
	 * @param z
	 *            new z-value
	 */
	public abstract void set(double x, double y, double z);

	/**
	 * @see Vector3#set(double, double, double)
	 */
	public abstract void set(float x, float y, float z);

	/**
	 * @see Vector1#set(Vector1)
	 */
	public abstract void set(Vector3 set);

	/**
	 * @see Vector1#setX(double)
	 */
	public abstract void setX(double x);

	/**
	 * @see Vector1#setX(float)
	 */
	public abstract void setX(float x);

	/**
	 * @see Vector2#setY(double)
	 */
	public abstract void setY(double y);

	/**
	 * @see Vector2#setY(float)
	 */
	public abstract void setY(float y);

	/**
	 * Sets the z-value.
	 * 
	 * @param z
	 *            new z-value
	 */
	public abstract void setZ(double z);

	/**
	 * @see Vector3#setZ(double)
	 */
	public abstract void setZ(float z);

	/**
	 * @see Vector1#transform(matrix.Matrix1)
	 */
	public abstract void transform(Matrix3 transform);

	/**
	 * @see Vector1#translate(double)
	 */
	public abstract void translate(double transx, double transy, double transz);

	/**
	 * @see Vector1#translate(float)
	 */
	public abstract void translate(float transx, float transy, float transz);

	/**
	 * @see Vector1#translate(Vector1)
	 */
	public abstract void translate(Vector3 trans);
}