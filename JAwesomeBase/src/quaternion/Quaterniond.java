package quaternion;

import matrix.Matrix3d;
import matrix.Matrix3f;
import vector.Vector3;

/**
 * Holds a 4 element double quaternion.
 * 
 * @author Oliver Schall
 * 
 */

public class Quaterniond extends Quaternion {
	double q0, q1, q2, q3;

	public Quaterniond() {
		setIdentity();
	}

	public Quaterniond(double all) {
		q0 = all;
		q1 = all;
		q2 = all;
		q3 = all;
	}

	public Quaterniond(double q0, double q1, double q2, double q3) {
		this.q0 = q0;
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
	}

	public Quaterniond(float q0, float q1, float q2, float q3) {
		this.q0 = q0;
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
	}

	public Quaterniond(Quaternion quat) {
		q0 = quat.getQ0();
		q1 = quat.getQ1();
		q2 = quat.getQ2();
		q3 = quat.getQ3();
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
		return new Complexd(q0, q1);
	}

	@Override
	public Complexf get2dRotationf() {
		return new Complexf(q0, q1);
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
		return (float) q0;
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
		return (float) q1;
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
		return (float) q2;
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
		return (float) q3;
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
		return q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(double angle, Vector3 axis) {
		double halfangle = angle / 2d;
		double sina = Math.sin(halfangle);
		double nq1 = axis.getX() * sina;
		double nq2 = axis.getY() * sina;
		double nq3 = axis.getZ() * sina;
		double nq0 = Math.cos(halfangle);
		rotate(new Quaterniond(nq0, nq1, nq2, nq3));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(float angle, Vector3 axis) {
		double halfangle = angle / 2d;
		double sina = Math.sin(halfangle);
		double nq1 = axis.getX() * sina;
		double nq2 = axis.getY() * sina;
		double nq3 = axis.getZ() * sina;
		double nq0 = Math.cos(halfangle);
		rotate(new Quaterniond(nq0, nq1, nq2, nq3));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Quaternion quat) {
		double nq0 = quat.getQ0();
		double nq1 = quat.getQ1();
		double nq2 = quat.getQ2();
		double nq3 = quat.getQ3();
		double tq1 = q0 * nq1 + q1 * nq0 + q2 * nq3 - q3 * nq2;
		double tq2 = q0 * nq2 + q2 * nq0 + q3 * nq1 - q1 * nq3;
		double tq3 = q0 * nq3 + q3 * nq0 + q1 * nq2 - q2 * nq1;
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
		this.q0 = q0;
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
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
		q0 = quat.getQ0();
		q1 = quat.getQ1();
		q2 = quat.getQ2();
		q3 = quat.getQ3();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAll(double all) {
		q0 = all;
		q1 = all;
		q2 = all;
		q3 = all;
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
		q0 = 0;
		q1 = 0;
		q2 = 0;
		q3 = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix3d toMatrix() {
		return new Matrix3d(1 - 2 * q2 * q2 - 2 * q3 * q3, 2 * q1 * q2 + 2 * q3
				* q0, 2 * q1 * q3 - 2 * q2 * q0, 2 * q1 * q2 - 2 * q3 * q0, 1
				- 2 * q1 * q1 - 2 * q3 * q3, 2 * q2 * q3 + 2 * q1 * q0, 2 * q1
				* q3 + 2 * q2 * q0, 2 * q2 * q3 - 2 * q1 * q0, 1 - 2 * q1 * q1
				- 2 * q2 * q2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix3f toMatrixf() {
		return new Matrix3f((float) (1 - 2 * q2 * q2 - 2 * q3 * q3), (float) (2
				* q1 * q2 + 2 * q3 * q0), (float) (2 * q1 * q3 - 2 * q2 * q0),
				(float) (2 * q1 * q2 - 2 * q3 * q0),
				(float) (1 - 2 * q1 * q1 - 2 * q3 * q3),
				(float) (2 * q2 * q3 + 2 * q1 * q0), (float) (2 * q1 * q3 + 2
						* q2 * q0), (float) (2 * q2 * q3 - 2 * q1 * q0),
				(float) (1 - 2 * q1 * q1 - 2 * q2 * q2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Quaterniond[");
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
}