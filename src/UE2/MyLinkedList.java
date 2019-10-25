package UE2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<FuckingAngabe> implements MyList<Long>, Iterable<Long> {

	private MyListNode head, tail;
	private int size;

	public MyLinkedList() {
	}

	public MyLinkedList(final Long[] array) {
		buildFromArray(array);
	}

	private void buildFromArray(final Long[] array) {
		clear();
		for (final Long f : array) {
			addLast(f);
		}
	}

	@Override
	public int size() {
		return size;
	}

	private void inputCheck(final Long elem) {
		if (elem == null) throw new IllegalArgumentException("Parameter can't be null");
	}

	@Override
	public void addFirst(final Long elem) throws IllegalArgumentException {
		inputCheck(elem);

		if (head == null) {
			head = tail = new MyListNode(elem);
		} else {
			final MyListNode temp = new MyListNode(elem);
			temp.setNext(head);
			head = temp;
		}
		size++;
	}

	@Override
	public void addLast(final Long elem) throws IllegalArgumentException {
		inputCheck(elem);

		if (head == null) {
			head = tail = new MyListNode(elem);
		} else {
			final MyListNode temp = new MyListNode(elem);
			tail.setNext(temp);
			tail = temp;
		}
		size++;
	}

	@Override
	public void addSorted(final Long val) throws IllegalArgumentException {
		inputCheck(val);

		addLast(val);
		sortASC();
	}

	@Override
	public void sortASC() {
		final Long[] longs = toLongs();

		for (int i = 0; i < longs.length; i++) {
			int min = i;
			for (int j = i + 1; j < longs.length; j++) {
				if (longs[j].compareTo(longs[min]) < 0) {
					min = j;
				}
			}
			final Long tmp = longs[i];
			longs[i] = longs[min];
			longs[min] = tmp;
		}
		buildFromArray(longs);
	}

	@Override
	public void sortDES() {
		final Long[] Longs = toLongs();

		for (int i = 0; i < Longs.length; i++) {
			int max = i;
			for (int j = i + 1; j < Longs.length; j++) {
				if (Longs[j].compareTo(Longs[max]) > 0) {
					max = j;
				}
			}
			final Long tmp = Longs[i];
			Longs[i] = Longs[max];
			Longs[max] = tmp;
		}
		buildFromArray(Longs);
	}

	@Override
	public void clear() {
		head = tail = null;
		size = 0;
	}

	@Override
	public Long removeFirst() {
		if (size() == 0) return null;

		MyListNode ret = null;
		if (size() == 1) {
			ret = head;
			head = tail = null;
		} else if (size() == 2) {
			ret = head;
			ret.setNext(null);
		} else {
			ret = head;
			head = head.getNext();
		}
		size--;
		return ret != null ? ret.getElement() : null;
	}

	@Override
	public Long removeLast() {
		if (size() == 0) return null;

		final Long ret;
		if (size() == 1) {
			ret = tail.getElement();
			head = tail = null;
		} else if (size() == 2) {
			ret = tail.getElement();
			tail = head;
			head.setNext(null);
		} else {
			MyListNode curr = head;
			for (int i = 0; i < size() - 2; i++) {
				curr = curr.getNext();
			}
			ret = tail.getElement();
			tail = curr;
			tail.setNext(null);
		}
		size--;
		return ret;
	}

	@Override
	public Long getFirst() {
		return head != null ? head.getElement() : null;
	}

	@Override
	public Long getLast() {
		return tail != null ? tail.getElement() : null;
	}

	@Override
	public boolean contains(final Long val) throws IllegalArgumentException {
		inputCheck(val);

		for (final Long f : this) {
			if (f.equals(val)) return true;
		}
		return false;
	}

	@Override
	public Long get(final int index) {
		if (index < 0 || index >= size()) return null;

		if (index == 0) return getFirst();
		if (index == size() - 1) return getLast();

		// Ignore first Element
		MyListNode curr = head.getNext();
		for (int i = 1; i < index; i++) {
			curr = curr.getNext();
		}
		return curr.getElement();
	}

	@Override
	public Long remove(final int index) {
		if (index < 0 || index >= size()) return null;

		if (index == 0) return removeFirst();
		if (index == size() - 1) return removeLast();

		MyListNode curr = head;
		for (int i = 0; i < index - 1; i++) {
			curr = curr.getNext();
		}
		final MyListNode toRemove = curr.getNext();
		curr.setNext(toRemove.getNext());
		toRemove.setNext(null);
		size--;
		return toRemove.getElement();
	}

	@Override
	public Object[] toArray() {
		return toLongs();
	}

	private Long[] toLongs() {
		final Long[] obj = new Long[size()];
		int i = 0;
		for (final Long f : this) {
			obj[i++] = f;
		}
		return obj;
	}

	@Override
	public String toString() {
		return "Size=" + size() + " " + Arrays.toString(toArray());
	}

	@Override
	public Iterator<Long> iterator() {
		return new Iterator<>() {

			MyListNode current = head;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public Long next() {
				if (!hasNext()) throw new NoSuchElementException();
				final Long ret = current.getElement();
				current = current.getNext();
				return ret;
			}
		};
	}
}
