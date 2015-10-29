package shapedata;

import objects.DataObject3;
import vector.Vector3f;

/**
 * Not-rendered representation of capsule data.
 * 
 * @author Oliver Schall
 * 
 */

public class EllipsoidData extends DataObject3 implements EllipsoidStructure {
	float radiusX, radiusY, radiusZ;

	public EllipsoidData(float x, float y, float z, float radiusX, float radiusY, float radiusZ) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_ELLIPSOID;
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.radiusZ = radiusZ;
	}

	public EllipsoidData(Vector3f pos, float radiusX, float radiusY, float radiusZ) {
		super();
		translateTo(pos);
		shapetype = SHAPE_ELLIPSOID;
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.radiusZ = radiusZ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRadiusX() {
		return radiusX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRadiusY() {
		return radiusX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRadiusZ() {
		return radiusX;
	}
}