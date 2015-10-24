package shapedata;

import objects.ShapeStructure;
import vector.Vector3f;

/**
 * Class structure for capsules.
 * 
 * @author Oliver Schall
 * 
 */

public interface CapsuleStructure extends ShapeStructure<Vector3f> {
	/**
	 * Gets the height of the capsule.
	 * 
	 * @return height of the capsule
	 */
	public float getHeight();

	/**
	 * Gets the half height of the capsule.
	 * 
	 * @return half height of the capsule
	 */
	public float getHalfHeight();

	/**
	 * Gets the radius of the capsule.
	 * 
	 * @return radius of the capsule
	 */
	public float getRadius();
}
