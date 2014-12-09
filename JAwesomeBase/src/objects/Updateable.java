package objects;

/**
 * Interface for classes to use update methods which can be called directly from
 * the engine main update loop.
 * 
 * @author Oliver Schall
 * 
 */

public interface Updateable {
	/**
	 * Executes an update method.
	 * 
	 * @param delta
	 *            time delta in milliseconds
	 */
	public void update(int delta);
}