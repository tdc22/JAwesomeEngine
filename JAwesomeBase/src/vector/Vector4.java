package vector;

import matrix.Matrix4;

/**
 * Superclass for 4-dimensional vectors.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Vector4 extends Vector {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Vector4) {
			return (getX() == ((Vector4) other).getX()
					&& getY() == ((Vector4) other).getY()
					&& getZ() == ((Vector4) other).getZ() && getW() == ((Vector4) other)
						.getW());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int getDimensions() {
		return 4;
	}

	/**
	 * Gets the w-value.
	 * 
	 * @return w-value
	 */
	public abstract double getW();

	/**
	 * @see Vector4#getW()
	 */
	public abstract float getWf();

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
	 * @see Vector3#getZ()
	 */
	public abstract double getZ();

	/**
	 * @see Vector3#getZf()
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
	 * @param scalew
	 *            w-factor
	 */
	public abstract void scale(double scalex, double scaley, double scalez,
			double scalew);

	/**
	 * @see Vector4#scale(double, double, double, double)
	 */
	public abstract void scale(float scalex, float scaley, float scalez,
			float scalew);

	/**
	 * @see Vector1#scale(Vector1)
	 */
	public abstract void scale(Vector4 scale);

	/**
	 * Sets the vector values.
	 * 
	 * @param x
	 *            new x-value
	 * @param y
	 *            new y-value
	 * @param z
	 *            new z-value
	 * @param w
	 *            new w-value
	 */
	public abstract void set(double x, double y, double z, double w);

	/**
	 * @see Vector4#set(double, double, double, double)
	 */
	public abstract void set(float x, float y, float z, float w);

	/**
	 * @see Vector1#set(Vector1)
	 */
	public abstract void set(Vector4 set);

	/**
	 * Sets the w-value.
	 * 
	 * @param w
	 *            new w-value
	 */
	public abstract void setW(double w);

	/**
	 * @see Vector4#setW(double)
	 */
	public abstract void setW(float w);

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
	 * @see Vector2#setY(double)
	 */
	public abstract void setY(float y);

	/**
	 * @see Vector3#setZ(double)
	 */
	public abstract void setZ(double z);

	/**
	 * @see Vector3#setZ(float)
	 */
	public abstract void setZ(float z);

	/**
	 * @see Vector1#transform(matrix.Matrix1)
	 */
	public abstract void transform(Matrix4 transform);

	/**
	 * @see Vector1#translate(double)
	 */
	public abstract void translate(double transx, double transy, double transz,
			double transw);

	/**
	 * @see Vector1#translate(float)
	 */
	public abstract void translate(float transx, float transy, float transz,
			float transw);

	/**
	 * @see Vector1#translate(Vector1)
	 */
	public abstract void translate(Vector4 trans);
}