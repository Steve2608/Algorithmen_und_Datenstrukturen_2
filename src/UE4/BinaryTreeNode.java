package UE4;

public class BinaryTreeNode {

	final Integer key;
	final String elem;
	BinaryTreeNode left, right, parent;

	BinaryTreeNode(final Integer key, final String elem) {
		this.key = key;
		this.elem = elem;
		left = right = parent = null;
	}

	@Override
	public String toString() {
		return String.format("[%d]:\'%s\'", key, elem);
	}
}
