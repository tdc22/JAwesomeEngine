package quaternion;

/**
 * Interface for every class that can describe a rotation.
 * 
 * @author Oliver Schall
 * 
 */
public interface Rotation {
	/**
	 * Inverts the rotation.
	 */
	public abstract void invert();
}