package UE4;

public class BinaryTreeNode {

	BinaryTreeNode parent, left, right;
	final Integer key;
	final String elem;

	BinaryTreeNode(final Integer key, final String elem) {
		this.key = key;
		this.elem = elem;
		parent = left = right = null;
	}

	boolean hasChildren() {
		return left != null || right != null;
	}

	@Override
	public String toString() {
		return String.format("[%d]:\'%s\'", key, elem);
	}
}
