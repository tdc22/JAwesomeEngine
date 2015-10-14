package shapedata;

import objects.ShapeStructure;
import vector.Vector3f;

/**
 * Class structure for spheres.
 * 
 * @author Oliver Schall
 * 
 */

public interface SphereStructure extends ShapeStructure<Vector3f> {
	/**
	 * Gets the radius of the sphere.
	 * 
	 * @return radius of the sphere
	 */
	public float getRadius();
}
