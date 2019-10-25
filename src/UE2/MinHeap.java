package UE2;

import java.util.NoSuchElementException;

public class MinHeap<FuckingAngabe> implements MyPriorityQueue<Long> {
	private Long[] content;
	private int maxSize;
	private int size;

	public MinHeap(final int capacity) {
		content = new Long[capacity + 1];
		maxSize = capacity;
	}

	@Override
	public boolean isEmpty() {
		return size <= 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void insert(final Long val) throws IllegalArgumentException {
		if (val == null)
			throw new IllegalArgumentException("Cannot add a null value");
		if (size == maxSize) {
			doubleSize();
		}
		content[++size] = val; // index 0 left blank
		upHeap(size);
	}

	private void doubleSize() {
		final Long[] copy = content;
		// remember to keep one blank field
		content = new Long[content.length * 2 - 1];
		maxSize = maxSize * 2;
		if (copy.length - 1 >= 0) System.arraycopy(copy, 1, content, 1, copy.length - 1);
	}


	@Override
	public Long removeMin() throws NoSuchElementException {
		if (size < 1 || content[1] == null)
			throw new NoSuchElementException("There is no element to be removed");

		swap(1, size);
		size--;
		final Long ret = content[size + 1];
		content[size + 1] = null;
		System.out.println(this);
		downHeap(1);
		return ret;
	}

	@Override
	public Long min() throws NoSuchElementException {
		if (size < 1 || content[1] == null)
			throw new NoSuchElementException("There is no element to be looked at");
		return content[1];
	}

	public Long get(final int index) {
		return index <= size && index >= 0 ? content[index] : null;
	}

	@Override
	public Object[] toArray() {
		// excluding the first (null) Element.
		final Object[] obj = new Object[size];
		System.arraycopy(content, 1, obj, 0, size);
		return obj;
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("\n");
		for (int i = 1; i < content.length && content[i] != null; i++)
			sb.append("[ Index = ")
					.append(i).append(" | Value = ")
					.append(content[i]).append(" | LeftChild = ")
					.append(leftChild(i)).append(" | RightChild = ")
					.append(rightChild(i)).append(" | Parent = ")
					.append(parent(i))
					.append("]\n");
		sb.append("{ Size = ").append(size).append(" }\n");
		// sb.append("Validity = " + validity() + " }\n");
		return sb.toString();
	}

	private void upHeap(final int p_index) {
		int v_index = p_index, v_parent = parent(v_index);

		while (v_parent > 0 && content[v_index].compareTo(content[v_parent]) < 0) {
			swap(v_index, v_parent);
			v_index = parent(v_index);
			v_parent = parent(v_index);
		}
	}

	private void downHeap(final int index) {
		System.out.println(index);
		if (index < 1 || index >= size || leftChild(index) < 0 || rightChild(index) < 0
				|| content[leftChild(index)].compareTo(content[index]) > 0
				&& content[rightChild(index)].compareTo(content[index]) > 0) {
			return;
		}

		if (rightChild(index) > 0) {
			// two children
			if (content[leftChild(index)].compareTo(content[rightChild(index)]) < 0) {
				if (content[index].compareTo(content[leftChild(index)]) > 0) {
					swap(leftChild(index), index);
					downHeap(leftChild(index));
				}
			} else {
				if (content[index].compareTo(content[rightChild(index)]) > 0) {
					swap(rightChild(index), index);
					downHeap(rightChild(index));
				}
			}
		} else {
			// just the left child
			if (content[index].compareTo(content[leftChild(index)]) > 0) {
				swap(leftChild(index), index);
			}
		}
	}

	private int parent(final int index) {
		if (index <= size && index > 1)
			return index / 2; // odd numbers do not matter (Integer Division!)
		return -1;
	}

	private int leftChild(final int index) {
		if (index * 2 <= size && index >= 1)
			return index * 2;
		return -1;
	}

	private int rightChild(final int index) {
		if (index * 2 + 1 <= size && index >= 1)
			return index * 2 + 1;
		return -1;
	}

	private void swap(final int index1, final int index2) {
		final Long temp = content[index1];
		content[index1] = content[index2];
		content[index2] = temp;
	}

}
