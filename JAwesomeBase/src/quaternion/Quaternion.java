package quaternion;

import matrix.Matrix3;
import matrix.Matrix3f;
import vector.Vector3;

/**
 * Superclass for a 4 element quaternion. Format: q0 + q1i + q2j + q3k
 * 
 * @author Oliver Schall
 * 
 */

public abstract class Quaternion implements Rotation {
	/**
	 * Conjugates the quaternion.
	 */
	public abstract void conjugate();

	/**
	 * Gets the 2d rotation as a complex number.
	 * 
	 * @return 2d rotation as complex number
	 */
	public abstract Complex get2dRotation();

	/**
	 * @see Quaternion#get2dRotation()
	 */
	public abstract Complexf get2dRotationf();

	/**
	 * Gets the value of q0.
	 * 
	 * @return q0
	 */
	public abstract double getQ0();

	/**
	 * @see Quaternion#getQ0()
	 */
	public abstract float getQ0f();

	/**
	 * Gets the value of q1.
	 * 
	 * @return q1
	 */
	public abstract double getQ1();

	/**
	 * @see Quaternion#getQ1()
	 */
	public abstract float getQ1f();

	/**
	 * Gets the value of q2.
	 * 
	 * @return q2
	 */
	public abstract double getQ2();

	/**
	 * @see Quaternion#getQ2()
	 */
	public abstract float getQ2f();

	/**
	 * Gets the value of q3.
	 * 
	 * @return q3
	 */
	public abstract double getQ3();

	/**
	 * @see Quaternion#getQ3()
	 */
	public abstract float getQ3f();

	/**
	 * Inverts the quaternion.
	 */
	@Override
	public abstract void invert();

	/**
	 * Calculates the magnitude of the quaternion.
	 * 
	 * @return magnitude of the quaternion
	 */
	public double magnitude() {
		return Math.sqrt(magnitudeSquared());
	}

	/**
	 * Calculates the squared magnitude of the quaternion.
	 * 
	 * @return squared magnitude of the quaternion
	 */
	public abstract double magnitudeSquared();

	/**
	 * Normalizes the quaternion.
	 */
	public void normalize() {
		double mag = magnitude();
		if (mag != 0) {
			scale(1 / mag);
		} else
			throw new IllegalStateException("Zero magnitude quaternion");
	}

	/**
	 * Rotates the quaternion by an angle around an axis.
	 * 
	 * @param angle
	 *            rotation angle
	 * @param axis
	 *            rotation axis
	 */
	public abstract void rotate(double angle, Vector3 axis);

	/**
	 * @see Quaternion#rotate(double, Vector3)
	 */
	public abstract void rotate(float angle, Vector3 axis);

	/**
	 * Rotates the quaternion by another one.
	 * 
	 * @param quat
	 *            rotation quaternion
	 */
	public abstract void rotate(Quaternion quat);

	/**
	 * Scales the quaternion by a factor.
	 * 
	 * @param scale
	 *            scaling factor
	 */
	public abstract void scale(double scale);

	/**
	 * @see Quaternion#scale(double)
	 */
	public abstract void scale(float scale);

	/**
	 * Sets the values of the quaternion.
	 * 
	 * @param q0
	 *            first value
	 * @param q1
	 *            second value
	 * @param q2
	 *            third value
	 * @param q3
	 *            fourth value
	 */
	public abstract void set(double q0, double q1, double q2, double q3);

	/**
	 * @see Quaternion#set(double, double, double, double)
	 */
	public abstract void set(float q0, float q1, float q2, float q3);

	/**
	 * Sets the quaternion.
	 * 
	 * @param quat
	 *            quaternion to set to
	 */
	public abstract void set(Quaternion quat);

	/**
	 * Sets all values to one.
	 * 
	 * @param all
	 *            new value
	 */
	public abstract void setAll(double all);

	/**
	 * @see Quaternion#setAll(double)
	 */
	public abstract void setAll(float all);

	/**
	 * Sets the identity.
	 */
	public abstract void setIdentity();

	/**
	 * Converts the quaternion to a rotation matrix.
	 * 
	 * @return rotation matrix
	 */
	public abstract Matrix3 toMatrix();

	/**
	 * @see Quaternion#toMatrix()
	 */
	public abstract Matrix3f toMatrixf();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();
}