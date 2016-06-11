package quaternion;

import matrix.Matrix3d;
import matrix.Matrix3f;
import vector.Vector3;

/**
 * Holds a 4 element float quaternion.
 * 
 * @author Oliver Schall
 * 
 */

public class Quaternionf extends Quaternion {
	float q0, q1, q2, q3;

	public Quaternionf() {
		setIdentity();
	}

	public Quaternionf(double all) {
		q0 = (float) all;
		q1 = (float) all;
		q2 = (float) all;
		q3 = (float) all;
	}

	public Quaternionf(double q0, double q1, double q2, double q3) {
		this.q0 = (float) q0;
		this.q1 = (float) q1;
		this.q2 = (float) q2;
		this.q3 = (float) q3;
	}

	public Quaternionf(float all) {
		q0 = all;
		q1 = all;
		q2 = all;
		q3 = all;
	}

	public Quaternionf(float q0, float q1, float q2, float q3) {
		this.q0 = q0;
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
	}

	public Quaternionf(Quaternion quat) {
		q0 = quat.getQ0f();
		q1 = quat.getQ1f();
		q2 = quat.getQ2f();
		q3 = quat.getQ3f();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void conjugate() {
		q1 = -q1;
		q2 = -q2;
		q3 = -q3;
	}

	@Override
	public Complex get2dRotation() {
		Complexd result = new Complexd(q0, q3);
		result.square();
		return result;
	}

	@Override
	public Complexf get2dRotationf() {
		Complexf result = new Complexf(q0, q3);
		result.square();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getQ0() {
		return q0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getQ0f() {
		return q0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getQ1() {
		return q1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getQ1f() {
		return q1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getQ2() {
		return q2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getQ2f() {
		return q2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getQ3() {
		return q3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getQ3f() {
		return q3;
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
		return q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(double angle, Vector3 axis) {
		float halfangle = (float) Math.toRadians(angle / 2f);
		float sina = (float) Math.sin(halfangle);
		float nq1 = axis.getXf() * sina;
		float nq2 = axis.getYf() * sina;
		float nq3 = axis.getZf() * sina;
		float nq0 = (float) Math.cos(halfangle);
		rotate(nq0, nq1, nq2, nq3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(float angle, Vector3 axis) {
		float halfangle = (float) Math.toRadians(angle / 2f);
		float sina = (float) Math.sin(halfangle);
		float nq1 = axis.getXf() * sina;
		float nq2 = axis.getYf() * sina;
		float nq3 = axis.getZf() * sina;
		float nq0 = (float) Math.cos(halfangle);
		rotate(nq0, nq1, nq2, nq3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Quaternion quat) {
		float nq0 = quat.getQ0f();
		float nq1 = quat.getQ1f();
		float nq2 = quat.getQ2f();
		float nq3 = quat.getQ3f();
		float tq1 = q0 * nq1 + q1 * nq0 + q2 * nq3 - q3 * nq2;
		float tq2 = q0 * nq2 + q2 * nq0 + q3 * nq1 - q1 * nq3;
		float tq3 = q0 * nq3 + q3 * nq0 + q1 * nq2 - q2 * nq1;
		q0 = q0 * nq0 - q1 * nq1 - q2 * nq2 - q3 * nq3;
		q1 = tq1;
		q2 = tq2;
		q3 = tq3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double scale) {
		q0 *= scale;
		q1 *= scale;
		q2 *= scale;
		q3 *= scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(float scale) {
		q0 *= scale;
		q1 *= scale;
		q2 *= scale;
		q3 *= scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double q0, double q1, double q2, double q3) {
		this.q0 = (float) q0;
		this.q1 = (float) q1;
		this.q2 = (float) q2;
		this.q3 = (float) q3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(float q0, float q1, float q2, float q3) {
		this.q0 = q0;
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Quaternion quat) {
		q0 = quat.getQ0f();
		q1 = quat.getQ1f();
		q2 = quat.getQ2f();
		q3 = quat.getQ3f();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double all) {
		q0 = (float) all;
		q1 = (float) all;
		q2 = (float) all;
		q3 = (float) all;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(float all) {
		q0 = all;
		q1 = all;
		q2 = all;
		q3 = all;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentity() {
		q0 = 1;
		q1 = 0;
		q2 = 0;
		q3 = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix3d toMatrix() {
		return new Matrix3d(1 - 2 * q2 * q2 - 2 * q3 * q3, 2 * q1 * q2 + 2 * q3 * q0, 2 * q1 * q3 - 2 * q2 * q0,
				2 * q1 * q2 - 2 * q3 * q0, 1 - 2 * q1 * q1 - 2 * q3 * q3, 2 * q2 * q3 + 2 * q1 * q0,
				2 * q1 * q3 + 2 * q2 * q0, 2 * q2 * q3 - 2 * q1 * q0, 1 - 2 * q1 * q1 - 2 * q2 * q2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix3f toMatrixf() {
		return new Matrix3f(1 - 2 * q2 * q2 - 2 * q3 * q3, 2 * q1 * q2 + 2 * q3 * q0, 2 * q1 * q3 - 2 * q2 * q0,
				2 * q1 * q2 - 2 * q3 * q0, 1 - 2 * q1 * q1 - 2 * q3 * q3, 2 * q2 * q3 + 2 * q1 * q0,
				2 * q1 * q3 + 2 * q2 * q0, 2 * q2 * q3 - 2 * q1 * q0, 1 - 2 * q1 * q1 - 2 * q2 * q2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Quaternionf[");
		sb.append(q0);
		sb.append(", ");
		sb.append(q1);
		sb.append(", ");
		sb.append(q2);
		sb.append(", ");
		sb.append(q3);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public void rotate(double q0, double q1, double q2, double q3) {
		float tq1 = (float) (this.q0 * q1 + this.q1 * q0 + this.q2 * q3 - this.q3 * q2);
		float tq2 = (float) (this.q0 * q2 + this.q2 * q0 + this.q3 * q1 - this.q1 * q3);
		float tq3 = (float) (this.q0 * q3 + this.q3 * q0 + this.q1 * q2 - this.q2 * q1);
		this.q0 = (float) (this.q0 * q0 - this.q1 * q1 - this.q2 * q2 - this.q3 * q3);
		this.q1 = tq1;
		this.q2 = tq2;
		this.q3 = tq3;
	}

	@Override
	public void rotate(float q0, float q1, float q2, float q3) {
		float tq1 = this.q0 * q1 + this.q1 * q0 + this.q2 * q3 - this.q3 * q2;
		float tq2 = this.q0 * q2 + this.q2 * q0 + this.q3 * q1 - this.q1 * q3;
		float tq3 = this.q0 * q3 + this.q3 * q0 + this.q1 * q2 - this.q2 * q1;
		this.q0 = this.q0 * q0 - this.q1 * q1 - this.q2 * q2 - this.q3 * q3;
		this.q1 = tq1;
		this.q2 = tq2;
		this.q3 = tq3;
	}
}
