package shapedata;

import vector.Vector3f;

/**
 * Class structure for boxes.
 * 
 * @author Oliver Schall
 * 
 */

public interface BoxStructure {
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