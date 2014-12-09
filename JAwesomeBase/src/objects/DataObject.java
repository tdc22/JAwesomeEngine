package objects;

/**
 * Wrapper class for not rendered objects. For rendered objects use DataObject
 * of JAwesomeEngine.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class DataObject extends BaseObject implements ObjectData {
	protected int shapetype;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getShapeType() {
		return shapetype;
	}
}
