package shapedata;

import math.VecMath;
import objects.DataObject;
import vector.Vector3f;

/**
 * Not-rendered representation of box data.
 * 
 * @author Oliver Schall
 * 
 */

public class BoxData extends DataObject implements BoxStructure {
	Vector3f halfsize;

	public BoxData(float x, float y, float z, float halfsizex, float halfsizey,
			float halfsizez) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_BOX;
		halfsize = new Vector3f(halfsizex, halfsizey, halfsizez);
	}

	public BoxData(float x, float y, float z, Vector3f halfsize) {
		super();
		translateTo(x, y, z);
		shapetype = SHAPE_BOX;
		this.halfsize = halfsize;
	}

	public BoxData(Vector3f pos, float halfsizex, float halfsizey,
			float halfsizez) {
		super();
		translateTo(pos);
		shapetype = SHAPE_BOX;
		halfsize = new Vector3f(halfsizex, halfsizey, halfsizez);
	}

	public BoxData(Vector3f pos, Vector3f halfsize) {
		super();
		translateTo(pos);
		shapetype = SHAPE_BOX;
		this.halfsize = halfsize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getHalfSize() {
		return halfsize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSize() {
		return VecMath.scale(halfsize, 2);
	}
}