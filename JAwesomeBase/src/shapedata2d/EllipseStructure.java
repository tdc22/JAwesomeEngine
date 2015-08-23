package shapedata2d;

/**
 * Class structure for ellipses.
 * 
 * @author Oliver Schall
 * 
 */

public interface EllipseStructure extends ShapeStructure2 {
	/**
	 * Gets the height of the ellipse.
	 * 
	 * @return height of the ellipse
	 */
	public float getHeight();

	public float getHalfHeight();

	/**
	 * Gets the radius of the ellipse.
	 * 
	 * @return radius of the ellipse
	 */
	public float getRadius();
}
