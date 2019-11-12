package UE2;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public class MyLinkedList<T extends Comparable<T>> implements MyList<T>, Iterable<T> {

	private MyListNode<T> head = null, tail = null;
	private boolean isAscending = true, isDescending = true;
	private int size = 0;

	public MyLinkedList() {
	}

	public MyLinkedList(final T[] array) {
		buildFromArray(array);
		isAscending = checkIsAscending();
		isDescending = checkIsDescending();
	}

	private void buildFromArray(final Object[] array) {
		clear();
		for (final Object val : array) {
			addLast((T) val);
		}
	}

	@Override
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() <= 0;
	}

	private void addOnlyNode(final T elem) {
		head = tail = new MyListNode<>(elem);
		isAscending = isDescending = true;
		size = 1;
	}

	private void inputCheck(final T elem) {
		if (elem == null) throw new IllegalArgumentException("Parameter can't be null");
	}

	@Override
	public void addFirst(final T elem) throws IllegalArgumentException {
		inputCheck(elem);

		if (head == null) {
			addOnlyNode(elem);
		} else {
			head = new MyListNode<>(elem, head);
			isAscending &= elem.compareTo(head.getNext().getElement()) <= 0;
			isDescending &= head.getNext().getElement().compareTo(elem) <= 0;
			size++;
		}
	}

	@Override
	public void addLast(final T elem) throws IllegalArgumentException {
		inputCheck(elem);

		if (head == null) {
			addOnlyNode(elem);
		} else {
			final MyListNode<T> temp = new MyListNode<>(elem);
			tail.setNext(temp);
			isAscending &= tail.getElement().compareTo(elem) <= 0;
			isDescending &= tail.getElement().compareTo(elem) >= 0;
			tail = temp;
			size++;
		}
	}

	@Override
	public void addSorted(final T val) throws IllegalArgumentException {
		inputCheck(val);

		if (isEmpty()) addOnlyNode(val);
		else if (isAscending) addSortedAscending(val);
		else {
			addLast(val);
			sortASC();
		}
	}

	private void addSortedAscending(final T val) {
		if (val.compareTo(head.getElement()) < 0) {
			head = new MyListNode<>(val, head);
			isDescending &= val.compareTo(head.getNext().getElement()) >= 0;
		} else if (val.compareTo(tail.getElement()) > 0) {
			final MyListNode<T> temp = new MyListNode<>(val);
			tail.setNext(temp);
			isDescending &= tail.getElement().compareTo(val) >= 0;
			tail = temp;
		} else {
			MyListNode<T> next = head.getNext(), prev = head;
			while (next != null && next.getElement().compareTo(val) < 0) {
				prev = next;
				next = next.getNext();
			}

			final MyListNode<T> temp = new MyListNode<>(val, next);
			prev.setNext(temp);
			isDescending &= next == null || val.compareTo(next.getElement()) >= 0;
		}
		size++;
	}

	private boolean checkIsAscending() {
		for (MyListNode<T> curr = head; curr != tail; curr = curr.getNext()) {
			if (curr.getElement().compareTo(curr.getNext().getElement()) > 0) return false;
		}
		return true;
	}

	private boolean checkIsDescending() {
		for (MyListNode<T> curr = head; curr != tail; curr = curr.getNext()) {
			if (curr.getElement().compareTo(curr.getNext().getElement()) < 0) return false;
		}
		return true;
	}

	@Override
	public void sortASC() {
		if (isAscending) return;

		final Object[] vals = toArray();
		for (int i = 0; i < vals.length; i++) {
			int min = i;
			for (int j = i + 1; j < vals.length; j++) {
				if (((T) vals[j]).compareTo((T) vals[min]) < 0) {
					min = j;
				}
			}
			final Object tmp = vals[i];
			vals[i] = vals[min];
			vals[min] = tmp;
		}
		buildFromArray(vals);
		isAscending = true;
		isDescending = checkIsDescending();
	}

	@Override
	public void sortDES() {
		if (isDescending) return;

		final Object[] vals = toArray();
		for (int i = 0; i < vals.length; i++) {
			int max = i;
			for (int j = i + 1; j < vals.length; j++) {
				if (((T) vals[j]).compareTo((T) vals[max]) > 0) {
					max = j;
				}
			}
			final Object tmp = vals[i];
			vals[i] = vals[max];
			vals[max] = tmp;
		}
		buildFromArray(vals);
		isAscending = checkIsAscending();
		isDescending = true;
	}

	@Override
	public void clear() {
		head = tail = null;
		isAscending = isDescending = true;
		size = 0;
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) return null;

		MyListNode<T> ret = null;
		if (size() == 1) {
			ret = head;
			head = tail = null;
		} else {
			ret = head;
			head = head.getNext();
		}
		size--;
		return ret != null ? ret.getElement() : null;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) return null;

		final T ret;
		if (size() == 1) {
			ret = tail.getElement();
			head = tail = null;
		} else {
			MyListNode<T> curr = head;
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
	public T getFirst() {
		return head != null ? head.getElement() : null;
	}

	@Override
	public T getLast() {
		return tail != null ? tail.getElement() : null;
	}

	@Override
	public boolean contains(final T val) throws IllegalArgumentException {
		inputCheck(val);
		for (final T f : this)
			if (f.equals(val)) return true;
		return false;
	}

	@Override
	public T get(final int index) {
		if (index < 0 || index >= size()) throw new IndexOutOfBoundsException(index);

		if (index == 0) return getFirst();
		if (index == size() - 1) return getLast();

		// Ignore first Element
		MyListNode<T> curr = head.getNext();
		for (int i = 1; i < index; i++) {
			curr = curr.getNext();
		}
		return curr.getElement();
	}

	@Override
	public T remove(final int index) {
		if (index < 0 || index >= size()) throw new IndexOutOfBoundsException(index);

		if (index == 0) return removeFirst();
		if (index == size() - 1) return removeLast();

		MyListNode<T> curr = head;
		for (int i = 0; i < index - 1; i++) {
			curr = curr.getNext();
		}
		final MyListNode<T> toRemove = curr.getNext();
		curr.setNext(toRemove.getNext());
		size--;
		return toRemove.getElement();
	}

	@Override
	public Object[] toArray() {
		final Object[] obj = new Object[size()];
		int i = 0;
		for (final T f : this) {
			obj[i++] = f;
		}
		return obj;
	}

	@Override
	public String toString() {
		final StringJoiner joiner = new StringJoiner(", ");
		for (final T val : this)
			joiner.add(val.toString());

		return String.format("MyLinkedList(asc=%b, des=%b)[%s]", isAscending, isDescending, joiner.toString());
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<>() {

			private MyListNode<T> current = head;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				final T ret = current.getElement();
				current = current.getNext();
				return ret;
			}
		};
	}
}
