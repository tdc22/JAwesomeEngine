package shapedata;

import objects.DataObject3;
import vector.Vector3f;

/**
 * Not-rendered representation of sphere data.
 * 
 * @author Oliver Schall
 * 
 */

public class SphereData extends DataObject3 implements SphereStructure {
	float radius;

	public SphereData(float x, float y, float z, float radius) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_SPHERE;
		this.radius = radius;
	}

	public SphereData(Vector3f pos, float radius) {
		super();
		translateTo(pos);
		shapetype = SHAPE_SPHERE;
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