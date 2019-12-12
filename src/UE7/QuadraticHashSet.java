package UE7;

import java.util.StringJoiner;

public class QuadraticHashSet extends AbstractHashSet implements MyHashSet {

	private final OpenHashNode[] elems;
	private final int CAPACITY;
	private int size;

	public QuadraticHashSet(final int capacity) {
		if (capacity <= 0) throw new IllegalArgumentException("Capacity cannot be negative");
		elems = new OpenHashNode[capacity];
		CAPACITY = capacity;
		init();
	}

	public QuadraticHashSet() {
		this(10);
	}

	private void init() {
		for (int i = 0; i < CAPACITY; i++) {
			elems[i] = new OpenHashNode(null, null);
		}
	}

	private int offset(final int n) {
		final int temp = (n + 1) / 2;
		final int ret = temp * temp;
		return n % 2 == 0 ? -ret : ret;
	}

	private int index(final int n) {
		final int hash = n % elems.length;
		if (hash < 0) return hash + elems.length;
		return hash;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean insert(final Integer key, final String data) throws IllegalArgumentException {
		if (key == null || data == null)
			throw new IllegalArgumentException("Cannot insert null key");
		if (contains(key)) return false;

		final int hc = getHashCode(key, elems.length);
		int n, tries;
		for (n = 0, tries = 0; elems[index(hc + offset(n))].removed && tries < CAPACITY; n++, tries++)
			;
		if (tries > CAPACITY || elems[index(hc + offset(n))].removed) return false;

		final OpenHashNode insert = elems[index(hc + offset(n))];
		insert.key = key;
		insert.data = data;
		insert.removed = true;

		size++;
		return true;
	}

	@Override
	public boolean contains(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot contain null key");

		final int hc = index(key);
		int n, tries;
		for (n = 0, tries = 0; elems[index(hc + offset(n))].removed && tries < CAPACITY; n++, tries++) {
			if (key.equals(elems[index(hc + offset(n))].key))
				return true;
		}
		return false;
	}


	@Override
	public boolean remove(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot remove null key");
		if (!contains(key)) return false;

		final int hc = index(key);
		int n, tries;
		for (n = 0, tries = 0; elems[index(hc + offset(n))].removed && tries < CAPACITY; n++, tries++) {
			if (elems[index(hc + offset(n))].removed && key.equals(elems[index(hc + offset(n))].key)) {
				final OpenHashNode remove = elems[index(hc + offset(n))];
				remove.key = null;
				remove.data = null;
				remove.removed = true;

				size--;
				return true;
			}
		}
		// dead code actually
		return false;
	}

	@Override
	public void clear() {
		for (int i = 0; i < CAPACITY; i++) {
			elems[i].key = null;
			elems[i].data = null;
			elems[i].removed = false;
		}
		size = 0;
	}

	@Override
	public String toString() {
		final StringJoiner indices = new StringJoiner(", ");
		for (int i = 0; i < elems.length; i++) {
			indices.add(String.format("%d {%s}", i, elems[i].key != null ? elems[i].key : ""));
		}
		return indices.toString();
	}

}
