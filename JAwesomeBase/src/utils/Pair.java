package utils;

/**
 * Contains an unordered pair of two objects.
 * 
 * @author Oliver Schall
 * 
 * @param <A>
 *            first object type
 * @param <B>
 *            second object type
 */

// TODO: Merge A and B? Ordered or unordered? Caution: SAP!
public class Pair<A, B> {
	private A first;
	private B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public Pair(Pair<A, B> pair) {
		this.first = pair.first;
		this.second = pair.second;
	}

	/**
	 * Checks if the pair contains a given object.
	 * 
	 * @param object
	 *            object which gets checked
	 * @return true if the object is present in the pair
	 */
	public boolean contains(Object object) {
		return object.equals(first) || object.equals(second);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Pair) {
			return ((first.equals(((Pair<?, ?>) other).getFirst()) && second.equals(((Pair<?, ?>) other).getSecond()))
					|| (first.equals(((Pair<?, ?>) other).getSecond())
							&& second.equals(((Pair<?, ?>) other).getFirst())));
		}
		return false;
	}

	/**
	 * Gets the first pair element.
	 * 
	 * @return first pair element
	 */
	public A getFirst() {
		return first;
	}

	/**
	 * Gets the second pair element.
	 * 
	 * @return second pair element
	 */
	public B getSecond() {
		return second;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		return first.hashCode() * second.hashCode();
	}

	/**
	 * Sets the first pair element.
	 * 
	 * @param first
	 *            new first pair element
	 */
	public void setFirst(A first) {
		this.first = first;
	}

	/**
	 * Sets the second pair element.
	 * 
	 * @param second
	 *            new second pair element
	 */
	public void setSecond(B second) {
		this.second = second;
	}

	/**
	 * Sets the first and second pair element
	 * 
	 * @param first
	 *            new first pair element
	 * @param second
	 *            new second pair element
	 */
	public void set(A first, B second) {
		this.first = first;
		this.second = second;
	}
}