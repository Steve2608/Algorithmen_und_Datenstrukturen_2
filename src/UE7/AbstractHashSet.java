package UE7;


public abstract class AbstractHashSet {

	public final int getHashCode(final int key, final int hashTableLength) {
		final int hash = Integer.hashCode(key) % hashTableLength;

		if (hash < 0) return hash + hashTableLength;
		return hash;
	}
}
