package shapedata2d;

import objects.DataObject;
import vector.Vector2f;

/**
 * Not-rendered representation of circle data.
 * 
 * @author Oliver Schall
 * 
 */

public class CircleData extends DataObject implements CircleStructure {
	float radius;

	public CircleData(float x, float y, float radius) {
		super();
		translate(x, y);
		shapetype = SHAPE_CIRCLE;
		this.radius = radius;
	}

	public CircleData(Vector2f pos, float radius) {
		super();
		translate(pos);
		shapetype = SHAPE_CIRCLE;
		this.radius = radius;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRadius() {
		return radius;
	}
}