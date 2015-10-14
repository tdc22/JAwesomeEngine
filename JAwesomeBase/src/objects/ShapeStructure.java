package objects;

import vector.Vector;

public interface ShapeStructure<L extends Vector> {
	/**
	 * Gets the translation of the shape.
	 * 
	 * @return translation of the shape
	 */
	public L getTranslation();
}
