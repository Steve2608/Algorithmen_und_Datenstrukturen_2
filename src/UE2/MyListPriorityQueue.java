package UE2;

import java.util.NoSuchElementException;

public class MyListPriorityQueue<T extends Comparable<T>> implements MyPriorityQueue<T> {

	private final MyList<T> list = new MyLinkedList<>();

	@Override
	public boolean isEmpty() {
		return size() <= 0;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void insert(final T elem) {
		list.addSorted(elem);
	}

	@Override
	public T removeMin() {
		try {
			return list.remove(0);
		} catch (final IndexOutOfBoundsException e) {
			throw new NoSuchElementException("No min element present");
		}
	}

	@Override
	public T min() {
		final T f = list.getFirst();
		if (f == null) throw new NoSuchElementException("No min element present");
		return f;
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}
}
