package UE2;

import java.util.NoSuchElementException;

public class MinHeap<T extends Comparable<T>> implements MyPriorityQueue<T> {

	private static final int MIN_INDEX = 1, EMPTY_INDEX = -1;

	private Object[] content;
	private int size;

	public MinHeap(final int capacity) {
		if (capacity <= 0)
			throw new IllegalArgumentException("Capacity must be positive: " + capacity);
		content = new Object[capacity + 1];
	}

	@Override
	public boolean isEmpty() {
		return size() <= 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void insert(final T val) throws IllegalArgumentException {
		if (val == null)
			throw new IllegalArgumentException("Cannot add a null value");
		if (size() >= content.length - 1) doubleSize();

		content[++size] = val; // index 0 left blank
		upHeap(size());
	}

	private void doubleSize() {
		final Object[] copy = content;
		// remember to keep one blank field
		content = new Object[content.length * 2];
		System.arraycopy(copy, MIN_INDEX, content, MIN_INDEX, copy.length - 1);
	}

	private boolean validIndex(final int index) {
		return MIN_INDEX <= index && index <= size();
	}

	@Override
	public T removeMin() throws NoSuchElementException {
		if (isEmpty())
			throw new NoSuchElementException("There is no min element to be removed");

		swap(MIN_INDEX, size());
		final T ret = (T) content[size()];
		content[size--] = null;
		downHeap(MIN_INDEX);
		return ret;
	}

	@Override
	public T min() throws NoSuchElementException {
		if (isEmpty())
			throw new NoSuchElementException("There is no min Element");
		return (T) content[MIN_INDEX];
	}

	public T get(final int index) {
		return validIndex(index) ? (T) content[index] : null;
	}

	@Override
	public Object[] toArray() {
		// excluding the first (null) Element.
		final Object[] obj = new Object[size()];
		System.arraycopy(content, MIN_INDEX, obj, 0, size());
		return obj;
	}

	private void upHeap(final int index) {
		for (int curr = index, par = parent(curr); par != EMPTY_INDEX && ((T) content[curr]).compareTo((T) content[par]) < 0; curr = par, par = parent(curr))
			swap(curr, par);
	}

	private void downHeap(final int index) {
		final int right_i = rightChild(index), left_i = leftChild(index);
		if (canDownHeap(index, left_i, right_i)) {
			// two children
			if (right_i != EMPTY_INDEX) {
				// find smaller child
				final int target = ((T) content[left_i]).compareTo((T) content[right_i]) < 0 ? left_i : right_i;
				swap(target, index);
				downHeap(target);
			} else {
				// just the left child
				swap(left_i, index);
			}
		}
	}

	private boolean canDownHeap(final int index, final int left, final int right) {
		return validIndex(index) && validIndex(left) &&
				(((T) content[left]).compareTo((T) content[index]) < 0 ||
						validIndex(right) && ((T) content[right]).compareTo((T) content[index]) < 0);
	}

	private int parent(final int index) {
		// odd numbers do not matter (Integer Division!)
		return MIN_INDEX < index && index <= size() ? index / 2 : EMPTY_INDEX;
	}

	private int leftChild(final int index) {
		final int left = index * 2;
		return MIN_INDEX <= index && left <= size() ? left : EMPTY_INDEX;
	}

	private int rightChild(final int index) {
		final int right = index * 2 + 1;
		return MIN_INDEX <= index && right <= size() ? right : EMPTY_INDEX;
	}

	private void swap(final int from, final int to) {
		final Object temp = content[from];
		content[from] = content[to];
		content[to] = temp;
	}

	private void toString(final String prefix, final boolean isTail, final StringBuilder sb, final int index) {
		final int right = rightChild(index), left = leftChild(index);
		if (right != EMPTY_INDEX)
			toString(prefix + (isTail ? "│   " : "    "), false, sb, right);
		sb.append(prefix).append(isTail ? "└── " : "┌── ").append(content[index]).append("\n");
		if (left != EMPTY_INDEX)
			toString(prefix + (isTail ? "    " : "│   "), true, sb, left);
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		toString("", true, s, MIN_INDEX);
		return s.toString();
	}

}
