package UE4;

public class BinaryTree implements BinarySearchTree {

	protected BinaryTreeNode root;
	private int size = 0;

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isRoot(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot compare with null key");
		return root != null && key.equals(root.key);
	}

	@Override
	public boolean isInternal(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot search for null key");
		return hasChildren(findNode(key));
	}

	@Override
	public boolean isExternal(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot search for null key");

		final BinaryTreeNode node = findNode(key);
		return node != null && !hasChildren(node);
	}

	@Override
	public Integer getParent(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot compare with null key");

		final BinaryTreeNode ret = findNode(key);
		if (ret == null) return null;

		final BinaryTreeNode par = getParent(ret);
		return par != null ? par.key : null;
	}

	BinaryTreeNode getParent(final BinaryTreeNode n) {
		if (n == null || n == root) return null;

		for (BinaryTreeNode curr = root; curr != null; ) {
			if (curr.left == n || curr.right == n) return curr;
			curr = n.key.compareTo(curr.key) < 0 ? curr.left : curr.right;
		}
		return null;
	}

	@Override
	public boolean insert(final Integer key, final String elem) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot insert a null key");
		if (elem == null) throw new IllegalArgumentException("Cannot insert a null elem");

		if (root == null) root = new BinaryTreeNode(key, elem);
		else {
			final BinaryTreeNode parent = findInsert(key);
			if (parent == null) return false;

			final BinaryTreeNode insert = new BinaryTreeNode(key, elem);
			if (key.compareTo(parent.key) < 0) parent.left = insert;
			else parent.right = insert;
		}
		size++;
		return true;
	}

	private BinaryTreeNode findInsert(final Integer elem) {
		BinaryTreeNode curr = root;
		while (hasChildren(curr)) {
			if (elem.equals(curr.key)) return null;
			if (elem.compareTo(curr.key) < 0) {
				if (curr.left == null) return curr;
				curr = curr.left;
			} else {
				if (curr.right == null) return curr;
				curr = curr.right;
			}
		}
		return elem.equals(curr.key) ? null : curr;
	}

	@Override
	public String find(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot look for a null key");

		final BinaryTreeNode node = findNode(key);
		return node != null ? node.elem : null;
	}

	private BinaryTreeNode findNode(final Integer key) {
		if (root == null || key == null) return null;

		BinaryTreeNode curr = root;
		while (curr != null && !key.equals(curr.key)) {
			curr = key.compareTo(curr.key) < 0 ? curr.left : curr.right;
		}
		return curr;
	}

	@Override
	public boolean remove(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot remove a null element");

		final BinaryTreeNode toRemove = findNode(key);
		if (toRemove == null) return false;

		final BinaryTreeNode parent = getParent(toRemove);
		if (!hasChildren(toRemove)) removeZeroChildren(parent, toRemove);
		else if (toRemove.left != null && toRemove.right == null)
			remove(parent, toRemove, toRemove.left);
		else if (toRemove.left == null && toRemove.right != null)
			remove(parent, toRemove, toRemove.right);
		else removeTwoChildren(toRemove);

		size--;
		return true;
	}

	private boolean hasChildren(final BinaryTreeNode n) {
		return n != null && (n.left != null || n.right != null);
	}

	private void remove(final BinaryTreeNode parent, final BinaryTreeNode toRemove, final BinaryTreeNode child) {
		if (parent == null) {
			root = child;
		} else {
			if (parent.key.compareTo(toRemove.key) < 0) parent.right = child;
			else parent.left = child;
		}
	}

	private void removeTwoChildren(final BinaryTreeNode toRemove) {
		final BinaryTreeNode parent = getParent(toRemove), nSuccessor = inOrderSuccessor(toRemove);
		assert nSuccessor != null : "inOrderSuccessor cannot be null";
		final BinaryTreeNode pSuccessor = getParent(nSuccessor);

		if (toRemove == root) removeRootTwoChildren(nSuccessor, pSuccessor);
		else removeInternalTwoChildren(toRemove, parent, nSuccessor, pSuccessor);
	}

	private void removeInternalTwoChildren(final BinaryTreeNode toRemove, final BinaryTreeNode parent,
	                                       final BinaryTreeNode nSuccessor, final BinaryTreeNode pSuccessor) {
		if (parent.left == toRemove) parent.left = nSuccessor;
		else parent.right = nSuccessor;

		if (pSuccessor == toRemove) {
			nSuccessor.left = toRemove.left;
			return;
		} else {
			pSuccessor.left = nSuccessor.right;
		}
		nSuccessor.right = toRemove.right;
		nSuccessor.left = toRemove.left;
	}

	private void removeRootTwoChildren(final BinaryTreeNode nSuccessor, final BinaryTreeNode pSuccessor) {
		if (nSuccessor == root.right) {
			nSuccessor.left = root.left;
		} else {
			if (pSuccessor != root) pSuccessor.left = nSuccessor.right;
			nSuccessor.left = root.left;
			nSuccessor.right = root.right;
		}
		root = nSuccessor;
	}

	private BinaryTreeNode inOrderSuccessor(final BinaryTreeNode node) {
		if (node.right != null) {
			BinaryTreeNode successor = node.right;
			while (successor.left != null) successor = successor.left;
			return successor;
		}

		BinaryTreeNode p = getParent(node), successor = node;
		while (p != null && p.right == successor) {
			successor = p;
			p = getParent(successor);
		}
		return p;
	}

	private void removeZeroChildren(final BinaryTreeNode parent, final BinaryTreeNode toRemove) {
		if (parent == null) root = null;
		else {
			if (parent.left == toRemove) parent.left = null;
			else parent.right = null;
		}
	}

	@Override
	public Object[] toArrayPostOrder() {
		final Object[] obj = new Object[size()];
		addToArrayPostOrder(obj, root, new Index());
		return obj;
	}

	private void addToArrayPostOrder(final Object[] obj, final BinaryTreeNode node, final Index index) {
		if (node != null) {
			addToArrayPostOrder(obj, node.left, index);
			addToArrayPostOrder(obj, node.right, index);
			obj[index.i++] = node.elem;
		}
	}

	@Override
	public Object[] toArrayInOrder() {
		final Object[] obj = new Object[size()];
		addToArrayInOrder(obj, root, new Index());
		return obj;
	}

	private void addToArrayInOrder(final Object[] obj, final BinaryTreeNode node, final Index index) {
		if (node != null) {
			addToArrayInOrder(obj, node.left, index);
			obj[index.i++] = node.elem;
			addToArrayInOrder(obj, node.right, index);
		}
	}

	@Override
	public Object[] toArrayPreOrder() {
		final Object[] obj = new Object[size()];
		addToArrayPreOrder(obj, root, new Index());
		return obj;
	}

	private void addToArrayPreOrder(final Object[] obj, final BinaryTreeNode node, final Index index) {
		if (node != null) {
			obj[index.i++] = node.elem;
			addToArrayPreOrder(obj, node.left, index);
			addToArrayPreOrder(obj, node.right, index);
		}
	}

	private void toString(final String prefix, final boolean isTail, final StringBuilder sb,
	                      final BinaryTreeNode node) {
		if (node != null) {
			if (node.right != null)
				toString(prefix + (isTail ? "│   " : "    "), false, sb, node.right);
			sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node).append("\n");
			if (node.left != null)
				toString(prefix + (isTail ? "    " : "│   "), true, sb, node.left);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		toString("", true, sb, root);
		return sb.toString();
	}

	private static class Index {
		private int i = 0;
	}

}
