package shapedata2d;

import objects.DataObject;
import vector.Vector2f;

/**
 * Not-rendered representation of ellipse data.
 * 
 * @author Oliver Schall
 * 
 */

public class EllipseData extends DataObject implements EllipseStructure {
	float radius, height;

	public EllipseData(float x, float y, float radius, float height) {
		super();
		translate(x, y);
		shapetype = SHAPE_ELLIPSE;
		this.radius = radius;
		this.height = height;
	}

	public EllipseData(Vector2f pos, float radius, float height) {
		super();
		translate(pos);
		shapetype = SHAPE_ELLIPSE;
		this.radius = radius;
		this.height = height;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getHeight() {
		return height;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRadius() {
		return radius;
	}
}