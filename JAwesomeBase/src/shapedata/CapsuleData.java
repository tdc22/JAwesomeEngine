package shapedata;

import objects.DataObject;
import vector.Vector3f;

/**
 * Not-rendered representation of capsule data.
 * 
 * @author Oliver Schall
 * 
 */

public class CapsuleData extends DataObject implements CapsuleStructure {
	float radius, halfheight;

	public CapsuleData(float x, float y, float z, float radius, float halfheight) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_CAPSULE;
		this.radius = radius;
		this.halfheight = halfheight;
	}

	public CapsuleData(Vector3f pos, float radius, float halfheight) {
		super();
		translateTo(pos);
		shapetype = SHAPE_CAPSULE;
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