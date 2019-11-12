package UE3;

import java.util.NoSuchElementException;

public interface MyPriorityQueue<T extends Comparable<T>> {
	boolean isEmpty();

	int size();

	void insert(T elem) throws IllegalArgumentException;

	T removeMin() throws NoSuchElementException;

	T min() throws NoSuchElementException;

	Object[] toArray();
}