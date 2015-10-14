package shapedata2d;

import objects.ShapeStructure;
import vector.Vector2f;

/**
 * Class structure for quads.
 * 
 * @author Oliver Schall
 * 
 */

public interface QuadStructure extends ShapeStructure<Vector2f> {
	/**
	 * Gets the half size of the quad.
	 * 
	 * @return half size of the quad
	 */
	public Vector2f getHalfSize();

	/**
	 * Gets the size of the quad.
	 * 
	 * @return size of the quad
	 */
	public Vector2f getSize();
}
