package shapedata;

import objects.ShapeStructure;
import vector.Vector3f;

/**
 * Class structure for ellipsoids.
 * 
 * @author Oliver Schall
 * 
 */

public interface EllipsoidStructure extends ShapeStructure<Vector3f> {
	/**
	 * Gets the radius of the ellipsoid along the x-axis.
	 * 
	 * @return radius along x-axis
	 */
	public float getRadiusX();

	/**
	 * Gets the radius of the ellipsoid along the y-axis.
	 * 
	 * @return radius along y-axis
	 */
	public float getRadiusY();

	/**
	 * Gets the radius of the ellipsoid along the z-axis.
	 * 
	 * @return radius along z-axis
	 */
	public float getRadiusZ();
}
