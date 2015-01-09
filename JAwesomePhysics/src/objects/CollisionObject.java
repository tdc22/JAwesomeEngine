package objects;

import vector.Vector;

/**
 * Class which represents every Object in the physics space.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class CollisionObject<L extends Vector> extends BaseObject
		implements SupportMap<L> {

}