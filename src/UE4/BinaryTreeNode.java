package UE4;

public class BinaryTreeNode {

	BinaryTreeNode parent, left, right;
	Long data;

	BinaryTreeNode(final Long elem) {
		data = elem;
		left = right = parent = null;
	}

	boolean hasChildren() {
		return left != null || right != null;
	}

	@Override
	public String toString() {
		return String.format("{%d}", data);
	}
}
