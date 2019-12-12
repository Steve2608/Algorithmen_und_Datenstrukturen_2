package UE7;

public interface MyHashSet {

	int size();

	boolean insert(Integer key, String data) throws IllegalArgumentException;

	boolean contains(Integer key) throws IllegalArgumentException;

	boolean remove(Integer key) throws IllegalArgumentException;

	void clear();
}