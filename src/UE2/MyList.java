package UE2;

public interface MyList<T> {
	int size();
	//Returns number of elements in the List (inO(1))

	void addFirst(T elem) throws IllegalArgumentException;
	//Adds element at the beginning of the list

	void addLast(T elem) throws IllegalArgumentException;
	//Adds element as end of list

	void addSorted(T val) throws IllegalArgumentException;
	//Adds element to the list(sorted in asc order)

	void sortASC();
	//Sorts the list in ascending order

	void sortDES();
	//Sorts the list in descending order

	void clear();
	//Clears the list by removing all elements (in O(1))

	T removeFirst();
	//Returns and removes the first element from the list

	T removeLast();
	//Returns and removes the last element from the list

	T getFirst();
	//Returns the first element from the list (no removal)

	T getLast();
	//Returns the last element from the list(no removal)

	boolean contains(T val) throws IllegalArgumentException;
	//Returns true if T is in list; false otherwise

	T get(int index);
	//Returns element at index position (no removal)

	T remove(int index);
	//Returns and removes element at index position

	String toString();
	//Returns a string representation of the list

	Object[] toArray();
	//Returns the list as array
}
