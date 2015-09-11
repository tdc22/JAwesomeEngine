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
	float radius, halfheight;

	public EllipseData(float x, float y, float radius, float halfheight) {
		super();
		translateTo(x, y);
		shapetype = SHAPE_ELLIPSE;
		this.radius = radius;
		this.halfheight = halfheight;
	}

	public EllipseData(Vector2f pos, float radius, float halfheight) {
		super();
		translateTo(pos);
		shapetype = SHAPE_ELLIPSE;
		this.radius = radius;
		this.halfheight = halfheight;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getHeight() {
		return 2 * halfheight;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRadius() {
		return radius;
	}

	@Override
	public float getHalfHeight() {
		return halfheight;
	}
}