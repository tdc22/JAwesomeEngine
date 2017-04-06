package shapedata;

import objects.ShapeStructure;
import vector.Vector2f;
import vector.Vector3f;

/**
 * Class structure for planes.
 * 
 * @author Oliver Schall
 * 
 */

public interface PlaneStructure extends ShapeStructure<Vector3f> {
	/**
	 * Gets the half size vector of the plane.
	 * 
	 * @return half size of the plane
	 */
	public Vector2f getHalfSize();

	/**
	 * Gets size vector of the plane.
	 * 
	 * @return size of the plane
	 */
	public Vector2f getSize();
}