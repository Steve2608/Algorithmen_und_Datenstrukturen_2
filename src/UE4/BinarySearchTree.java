package UE4;

public interface BinarySearchTree {

	boolean insert(Long elem) throws IllegalArgumentException;

	Long find(Long key) throws IllegalArgumentException;

	boolean remove(Long key) throws IllegalArgumentException;

	int size();

	Object[] toArrayPostOrder();

	Object[] toArrayInOrder();

	Object[] toArrayPreOrder();

	Long getParent(Long key) throws IllegalArgumentException;

	boolean isRoot(Long key) throws IllegalArgumentException;

	boolean isInternal(Long key) throws IllegalArgumentException;

	boolean isExternal(Long key) throws IllegalArgumentException;
}

