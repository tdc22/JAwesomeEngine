package shapedata2d;

import math.VecMath;
import objects.DataObject;
import vector.Vector2f;

/**
 * Not-rendered representation of quad data.
 * 
 * @author Oliver Schall
 * 
 */

public class QuadData extends DataObject implements QuadStructure {
	Vector2f halfsize;

	public QuadData(float x, float y, float halfsizex, float halfsizey) {
		super();
		translate(x, y);
		shapetype = SHAPE_QUAD;
		halfsize = new Vector2f(halfsizex, halfsizey);
	}

	public QuadData(float x, float y, Vector2f halfsize) {
		super();
		translate(x, y);
		shapetype = SHAPE_QUAD;
		this.halfsize = halfsize;
	}

	public QuadData(Vector2f pos, float halfsizex, float halfsizey) {
		super();
		translate(pos);
		shapetype = SHAPE_QUAD;
		halfsize = new Vector2f(halfsizex, halfsizey);
	}

	public QuadData(Vector2f pos, Vector2f halfsize) {
		super();
		translate(pos);
		shapetype = SHAPE_QUAD;
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