package UE2;

import java.util.NoSuchElementException;

public class MyListPriorityQueue<FuckingAngabe> implements MyPriorityQueue<Long> {

	private final MyLinkedList list = new MyLinkedList();

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
		final Long f = list.removeFirst();
		if (f == null) throw new NoSuchElementException("No min element present");
		return f;
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
