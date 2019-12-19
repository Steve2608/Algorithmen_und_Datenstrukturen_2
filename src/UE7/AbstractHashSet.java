package UE7;


public abstract class AbstractHashSet {

	public final int getHashCode(final int key, final int hashTableLength) {
		return Integer.hashCode(key) % hashTableLength;
	}
}
