package UE4;

public class BinaryTree implements BinarySearchTree {

	protected BinaryTreeNode root;
	private int size = 0;

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isRoot(final Long key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot compare with null key");
		return root != null && key.equals(root.data);
	}

	@Override
	public boolean isInternal(final Long key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot search for null key");

		final BinaryTreeNode node = findNode(key);
		return node != null && node.hasChildren();
	}

	@Override
	public boolean isExternal(final Long key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot search for null key");

		final BinaryTreeNode node = findNode(key);
		return node != null && !node.hasChildren();
	}

	@Override
	public Long getParent(final Long key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot compare with null key");

		final BinaryTreeNode ret = findNode(key);
		if (ret == null) return null;

		final BinaryTreeNode par = ret.parent;
		return par != null ? par.data : null;
	}

	@Override
	public boolean insert(final Long elem) throws IllegalArgumentException {
		if (elem == null) throw new IllegalArgumentException("Cannot insert a null element");

		if (root == null) root = new BinaryTreeNode(elem);
		else {
			final BinaryTreeNode parent = findInsert(elem);
			if (parent == null) return false;

			final BinaryTreeNode insert = new BinaryTreeNode(elem);
			if (elem.compareTo(parent.data) < 0) parent.left = insert;
			else parent.right = insert;
			insert.parent = parent;
		}
		size++;
		return true;
	}

	private BinaryTreeNode findInsert(final Long elem) {
		BinaryTreeNode curr = root;
		while (curr.hasChildren()) {
			if (elem.equals(curr.data)) return null;
			if (elem.compareTo(curr.data) < 0) {
				if (curr.left == null) return curr;
				curr = curr.left;
			} else {
				if (curr.right == null) return curr;
				curr = curr.right;
			}
		}
		return elem.equals(curr.data) ? null : curr;
	}

	@Override
	public Long find(final Long key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot look for a null key");

		final BinaryTreeNode node = findNode(key);
		return node != null ? node.data : null;
	}

	private BinaryTreeNode findNode(final Long key) {
		if (root == null || key == null) return null;

		BinaryTreeNode curr = root;
		while (curr != null && !key.equals(curr.data)) {
			curr = key.compareTo(curr.data) < 0 ? curr.left : curr.right;
		}
		return curr;
	}

	@Override
	public boolean remove(final Long key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot remove a null element");

		final BinaryTreeNode toRemove = findNode(key);
		if (toRemove == null) return false;

		if (!toRemove.hasChildren()) removeZeroChildren(toRemove.parent, toRemove);
		else if (toRemove.left != null && toRemove.right == null) remove(toRemove.parent, toRemove, toRemove.left);
		else if (toRemove.left == null && toRemove.right != null) remove(toRemove.parent, toRemove, toRemove.right);
		else removeTwoChildren(toRemove);

		size--;
		return true;
	}

	private void remove(final BinaryTreeNode parent, final BinaryTreeNode toRemove, final BinaryTreeNode child) {
		if (parent == null) {
			root = child;
			root.parent = null;
		} else {
			if (parent.data.compareTo(toRemove.data) < 0) parent.right = child;
			else parent.left = child;

			child.parent = parent;
		}
	}

	private void removeTwoChildren(final BinaryTreeNode toRemove) {
		final BinaryTreeNode parent = toRemove.parent, nSuccessor = inOrderSuccessor(toRemove);
		assert nSuccessor != null : "inOrderSuccessor cannot be null";
		final BinaryTreeNode pSuccessor = nSuccessor.parent;

		if (toRemove == root) {
			removeRootTwoChildren(toRemove, nSuccessor, pSuccessor);
			root.parent = null;
		} else {
			removeInternalTwoChildren(toRemove, parent, nSuccessor, pSuccessor);
		}
	}

	private void removeInternalTwoChildren(final BinaryTreeNode toRemove, final BinaryTreeNode parent,
	                                       final BinaryTreeNode nSuccessor, final BinaryTreeNode pSuccessor) {
		if (parent.left == toRemove) parent.left = nSuccessor;
		else parent.right = nSuccessor;
		nSuccessor.parent = parent;

		if (pSuccessor == toRemove) {
			nSuccessor.left = toRemove.left;
			toRemove.left.parent = nSuccessor;
			return;
		} else {
			pSuccessor.left = nSuccessor.right;
			if (nSuccessor.right != null) nSuccessor.right.parent = pSuccessor;
		}
		nSuccessor.right = toRemove.right;
		toRemove.right.parent = nSuccessor;

		nSuccessor.left = toRemove.left;
		toRemove.left.parent = nSuccessor;
	}

	private void removeRootTwoChildren(final BinaryTreeNode toRemove, final BinaryTreeNode nSuccessor, final BinaryTreeNode pSuccessor) {
		if (pSuccessor != toRemove && nSuccessor.right != null) {
			nSuccessor.right.parent = pSuccessor;
			pSuccessor.left = nSuccessor;
		}

		if (nSuccessor.parent != toRemove) nSuccessor.parent.left = nSuccessor.right;

		if (nSuccessor != toRemove.left) {
			nSuccessor.left = toRemove.left;
			toRemove.left.parent = nSuccessor;
		}
		if (nSuccessor != toRemove.right) {
			nSuccessor.right = toRemove.right;
			toRemove.right.parent = nSuccessor;
		}

		root = nSuccessor;
	}

	private BinaryTreeNode inOrderSuccessor(final BinaryTreeNode node) {
		if (node.right != null) {
			BinaryTreeNode successor = node.right;
			while (successor.left != null) successor = successor.left;
			return successor;
		}

		BinaryTreeNode p = node.parent, successor = node;
		while (p != null && p.right == successor) {
			successor = p;
			p = successor.parent;
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
			obj[index.i++] = node.data;
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
			obj[index.i++] = node.data;
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
			obj[index.i++] = node.data;
			addToArrayPreOrder(obj, node.left, index);
			addToArrayPreOrder(obj, node.right, index);
		}
	}

	private void toString(final String prefix, final boolean isTail, final StringBuilder sb,
	                      final BinaryTreeNode node) {
		if (node != null) {
			if (node.right != null)
				toString(prefix + (isTail ? "│   " : "    "), false, sb, node.right);
			sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.data).append("\n");
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
