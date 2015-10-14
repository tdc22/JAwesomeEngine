package shapedata;

import objects.DataObject3;
import vector.Vector3f;

/**
 * Not-rendered representation of cylinder data.
 * 
 * @author Oliver Schall
 * 
 */

public class CylinderData extends DataObject3 implements CylinderStructure {
	float radius, halfheight;

	public CylinderData(float x, float y, float z, float radius, float halfheight) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_CYLINDER;
		this.radius = radius;
		this.halfheight = halfheight;
	}

	public CylinderData(Vector3f pos, float radius, float halfheight) {
		super();
		translateTo(pos);
		shapetype = SHAPE_CYLINDER;
		this.radius = radius;
		this.halfheight = halfheight;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getHalfHeight() {
		return halfheight;
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
}