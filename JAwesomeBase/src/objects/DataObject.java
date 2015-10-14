package objects;

import quaternion.Rotation;
import vector.Vector;

/**
 * Wrapper class for not rendered objects. For rendered objects use DataObject
 * of JAwesomeEngine.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class DataObject<L extends Vector, A extends Rotation> extends BaseObject<L, A> implements ObjectData {
	protected int shapetype;

	public DataObject(L rotcenter, L translation, A rotation, L scale) {
		super(rotcenter, translation, rotation, scale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getShapeType() {
		return shapetype;
	}
}