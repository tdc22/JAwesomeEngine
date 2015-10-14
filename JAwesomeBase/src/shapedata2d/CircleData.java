package shapedata2d;

import objects.DataObject2;
import vector.Vector2f;

/**
 * Not-rendered representation of circle data.
 * 
 * @author Oliver Schall
 * 
 */

public class CircleData extends DataObject2 implements CircleStructure {
	float radius;

	public CircleData(float x, float y, float radius) {
		super();
		translateTo(x, y);
		shapetype = SHAPE_CIRCLE;
		this.radius = radius;
	}

	public CircleData(Vector2f pos, float radius) {
		super();
		translateTo(pos);
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