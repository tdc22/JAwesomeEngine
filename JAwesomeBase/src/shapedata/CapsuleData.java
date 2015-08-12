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
	float radius, height;

	public CapsuleData(float x, float y, float z, float radius, float height) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_CAPSULE;
		this.radius = radius;
		this.height = height;
	}

	public CapsuleData(Vector3f pos, float radius, float height) {
		super();
		translateTo(pos);
		shapetype = SHAPE_CAPSULE;
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