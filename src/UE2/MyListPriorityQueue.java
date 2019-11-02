package UE2;

import java.util.NoSuchElementException;

public class MyListPriorityQueue<FuckingAngabe> implements MyPriorityQueue<Long> {

	private final MyList<Long> list = new MyLinkedList<>();

	@Override
	public boolean isEmpty() {
		return size() <= 0;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void insert(final Long elem) {
		list.addSorted(elem);
	}

	@Override
	public Long removeMin() {
		try {
			return list.remove(0);
		} catch (final IndexOutOfBoundsException e) {
			throw new NoSuchElementException("No min element present");
		}
	}

	@Override
	public Long min() {
		final Long f = list.getFirst();
		if (f == null) throw new NoSuchElementException("No min element present");
		return f;
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}
}
