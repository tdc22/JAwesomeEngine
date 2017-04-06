package shapedata;

import math.VecMath;
import objects.DataObject3;
import vector.Vector2f;
import vector.Vector3f;

/**
 * Not-rendered representation of plane data.
 * 
 * @author Oliver Schall
 * 
 */

public class PlaneData extends DataObject3 implements PlaneStructure {
	Vector2f halfsize;

	public PlaneData(float x, float y, float z, float halfsizex, float halfsizey) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_PLANE;
		halfsize = new Vector2f(halfsizex, halfsizey);
	}

	public PlaneData(float x, float y, float z, Vector2f halfsize) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_PLANE;
		this.halfsize = halfsize;
	}

	public PlaneData(Vector3f pos, float halfsizex, float halfsizey) {
		super();
		translateTo(pos);
		shapetype = SHAPE_PLANE;
		halfsize = new Vector2f(halfsizex, halfsizey);
	}

	public PlaneData(Vector3f pos, Vector2f halfsize) {
		super();
		translateTo(pos);
		shapetype = SHAPE_PLANE;
		this.halfsize = halfsize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2f getHalfSize() {
		return halfsize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2f getSize() {
		return VecMath.scale(halfsize, 2);
	}
}