package shapedata;

import objects.ShapeStructure;
import vector.Vector3f;

/**
 * Class structure for boxes.
 * 
 * @author Oliver Schall
 * 
 */

public interface BoxStructure extends ShapeStructure<Vector3f> {
	/**
	 * Gets the half size vector of the box.
	 * 
	 * @return half size of the box
	 */
	public Vector3f getHalfSize();

	/**
	 * Gets size vector of the box.
	 * 
	 * @return size of the box
	 */
	public Vector3f getSize();
}