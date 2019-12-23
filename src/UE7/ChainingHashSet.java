package UE7;

import java.util.Arrays;
import java.util.StringJoiner;

public class ChainingHashSet extends AbstractHashSet implements MyHashSet {

	private final ChainingHashNode[] elems;
	private int size = 0;

	public ChainingHashSet(final int capacity) {
		if (capacity <= 0) throw new IllegalArgumentException("Capacity cannot be negative");
		elems = new ChainingHashNode[capacity];
	}

	public ChainingHashSet() {
		this(10);
	}

	public int getHashCode(final int key) {
		final int hash = getHashCode(key, elems.length);
		return hash < 0 ? hash + elems.length : hash;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean insert(final Integer key, final String data) throws IllegalArgumentException {
		if (key == null || data == null)
			throw new IllegalArgumentException("Parameters cannot be null");
		if (contains(key)) return false;

		final int index = getHashCode(key);
		if (elems[index] == null) {
			elems[index] = new ChainingHashNode(key, data);
		} else {
			ChainingHashNode curr = elems[index];
			while (curr.next != null) {
				curr = curr.next;
			}
			curr.next = new ChainingHashNode(key, data);
		}
		size++;
		return true;
	}

	@Override
	public boolean contains(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Parameters cannot be null");

		for (ChainingHashNode curr = elems[getHashCode(key)]; curr != null; curr = curr.next) {
			if (key.equals(curr.key)) return true;
		}
		return false;
	}

	@Override
	public boolean remove(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Parameters cannot be null");
		if (!contains(key)) return false;

		final int index = getHashCode(key);
		if (key.equals(elems[index].key)) {
			elems[index] = elems[index].next;
		} else {
			ChainingHashNode curr = elems[index];
			while (!key.equals(curr.next.key)) {
				curr = curr.next;
			}
			curr.next = curr.next.next;
		}
		size--;
		return true;
	}

	@Override
	public void clear() {
		size = 0;
		Arrays.fill(elems, null);
	}

	@Override
	public String toString() {
		final StringJoiner indices = new StringJoiner(", ");
		for (int i = 0; i < elems.length; i++) {
			final StringJoiner keys = new StringJoiner(", ", String.format("%d {", i), "}");
			for (ChainingHashNode curr = elems[i]; curr != null; curr = curr.next) {
				keys.add(String.valueOf(curr.key));
			}

			indices.add(keys.toString());
		}
		return indices.toString();
	}
}
