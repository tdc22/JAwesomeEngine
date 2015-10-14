package shapedata2d;

import objects.ShapeStructure;
import vector.Vector2f;

/**
 * Class structure for circles.
 * 
 * @author Oliver Schall
 * 
 */

public interface CircleStructure extends ShapeStructure<Vector2f> {
	/**
	 * Gets the radius of the circle.
	 * 
	 * @return radius of the circle
	 */
	public float getRadius();
}
