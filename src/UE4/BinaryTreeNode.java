package UE4;

public class BinaryTreeNode {

	BinaryTreeNode left, right;
	final Integer key;
	final String elem;

	BinaryTreeNode(final Integer key, final String elem) {
		this.key = key;
		this.elem = elem;
		left = right = null;
	}

	@Override
	public String toString() {
		return String.format("[%d]:\'%s\'", key, elem);
	}
}
