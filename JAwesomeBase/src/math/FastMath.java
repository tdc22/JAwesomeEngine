package math;

/**
 * Optimizes sin and cos functions of the java.lang.Math class. Just helps if
 * angles are bigger than PI. (Tested on Linux 64 bit) (Original source lost)
 * 
 * @author Oliver Schall
 * 
 */

public class FastMath {
	public static final double PI = Math.PI;

	public static final double TWO_PI = 2.0f * PI;

	public static final double HALF_PI = 0.5f * PI;

	/**
	 * Computes the cosine of a given angle.
	 * 
	 * @param angle
	 *            an angle in radians
	 * @return the cosine of the angle
	 */
	public static double cos(double angle) {
		return sin(angle + HALF_PI);
	}

	public static float cos(float angle) {
		return (float) sin(angle + HALF_PI);
	}

	/**
	 * Reduces the given angle to a range between 0 and PI.
	 * 
	 * @param radians
	 *            the angle given in radians
	 * @return the reduced angle
	 */
	private static double reduceSinAngle(double radians) {
		radians %= TWO_PI;
		if (Math.abs(radians) > PI) {
			radians = radians - (TWO_PI);
		}
		if (Math.abs(radians) > HALF_PI) {
			radians = PI - radians;
		}

		return radians;
	}

	/**
	 * Computes the sine of a given angle.
	 * 
	 * @param angle
	 *            an angle in radians
	 * @return the sine of the angle
	 */
	public static double sin(double angle) {
		angle = reduceSinAngle(angle);

		if (Math.abs(angle) <= Math.PI / 4) {
			return Math.sin(angle);
		}

		return Math.cos(Math.PI / 2 - angle);
	}

	public static float sin(float angle) {
		return (float) sin((double) angle);
	}
}
