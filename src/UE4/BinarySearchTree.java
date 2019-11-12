package UE4;

public interface BinarySearchTree {

	boolean insert(Integer key, String elem) throws IllegalArgumentException;

	String find(Integer key) throws IllegalArgumentException;

	boolean remove(Integer key) throws IllegalArgumentException;

	int size();

	Object[] toArrayPostOrder();

	Object[] toArrayInOrder();

	Object[] toArrayPreOrder();

	Integer getParent(Integer key) throws IllegalArgumentException;

	boolean isRoot(Integer key) throws IllegalArgumentException;

	boolean isInternal(Integer key) throws IllegalArgumentException;

	boolean isExternal(Integer key) throws IllegalArgumentException;
}

