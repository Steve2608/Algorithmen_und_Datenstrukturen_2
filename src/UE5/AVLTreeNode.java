package UE5;

public class AVLTreeNode {

	Integer key;
	String elem;
	AVLTreeNode left, right, parent;
	int height;

	AVLTreeNode(final Integer key, final String elem) {
		this.key = key;
		this.elem = elem;
		left = right = parent = null;
	}

	@Override
	public String toString() {
		return String.format("[%d]:\'%s\'", key, elem);
	}
}
