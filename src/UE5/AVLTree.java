package UE5;

import java.util.Arrays;
import java.util.Comparator;

public class AVLTree {

	private AVLTreeNode root;
	private int size = 0;

	public AVLTreeNode getRoot() {
		return root;
	}

	public int size() {
		return size;
	}

	public int height() {
		return root != null ? root.height : -1;
	}

	public boolean insert(final Integer key, final String elem) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot insert a null key");
		if (elem == null) throw new IllegalArgumentException("Cannot insert a null elem");

		if (root == null) root = new AVLTreeNode(key, elem);
		else {
			final AVLTreeNode parent = findInsert(key);
			if (parent == null) return false;

			final AVLTreeNode insert = new AVLTreeNode(key, elem);
			if (key.compareTo(parent.key) < 0) parent.left = insert;
			else parent.right = insert;
			insert.parent = parent;
		}
		size++;
		restoreAVL();
		return true;
	}

	private AVLTreeNode findInsert(final Integer elem) {
		AVLTreeNode curr = root;
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
		assert curr != null : "curr cannot be null here";
		return elem.equals(curr.key) ? null : curr;
	}

	private void restructure(final AVLTreeNode grandChild) {
		if (grandChild == null || size() <= 2) return;
		new NodeGroup(grandChild).cutAndLink();
	}

	private int height(final AVLTreeNode node) {
		return node != null ? node.height : -1;
	}

	private void setNewHeight(final AVLTreeNode node) {
		node.height = Math.max(height(node.left), height(node.right)) + 1;
	}

	private AVLTreeNode findErrorNode(final AVLTreeNode n) {
		if (!hasChildren(n)) return null;

		final int right = height(n.right);
		final int left = height(n.left);
		if (Math.abs(right - left) >= 2) {
			final AVLTreeNode child = right > left ? n.right : n.left;
			return height(child.right) > height(child.left) ? child.right : child.left;
		}
		final AVLTreeNode error = findErrorNode(n.left);
		return error != null ? error : findErrorNode(n.right);
	}

	public String find(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot look for a null key");

		final AVLTreeNode node = findNode(key);
		return node != null ? node.elem : null;
	}

	private AVLTreeNode findNode(final Integer key) {
		if (root == null || key == null) return null;

		AVLTreeNode curr = root;
		while (curr != null && !key.equals(curr.key)) {
			curr = key.compareTo(curr.key) < 0 ? curr.left : curr.right;
		}
		return curr;
	}

	public boolean remove(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot remove a null element");

		final AVLTreeNode toRemove = findNode(key);
		if (toRemove == null) return false;

		final AVLTreeNode parent = toRemove.parent;
		if (!hasChildren(toRemove)) removeZeroChildren(parent, toRemove);
		else if (toRemove.left != null && toRemove.right == null)
			removeOneChild(parent, toRemove, toRemove.left);
		else if (toRemove.left == null && toRemove.right != null)
			removeOneChild(parent, toRemove, toRemove.right);
		else removeTwoChildren(toRemove);

		size--;
		restoreAVL();
		return true;
	}

	private void restoreAVL() {
		for (AVLTreeNode node = findErrorNode(); node != null; node = findErrorNode()) {
			restructure(node);
		}
	}

	private AVLTreeNode findErrorNode() {
		updateHeightsDownward(root);
		return findErrorNode(root);
	}

	private void updateHeightsDownward(final AVLTreeNode node) {
		if (node == null) return;
		if (hasChildren(node)) {
			updateHeightsDownward(node.left);
			updateHeightsDownward(node.right);
		}
		setNewHeight(node);
	}

	private boolean hasChildren(final AVLTreeNode n) {
		return n != null && (n.left != null || n.right != null);
	}

	private void removeOneChild(final AVLTreeNode parent, final AVLTreeNode toRemove, final AVLTreeNode child) {
		child.parent = parent;
		if (toRemove == root) {
			root = child;
		} else {
			if (parent.left == toRemove) parent.left = child;
			else parent.right = child;
		}
	}

	private void removeTwoChildren(final AVLTreeNode toRemove) {
		final AVLTreeNode parent = toRemove.parent, nSuccessor = inOrderSuccessor(toRemove);
		assert nSuccessor != null;
		final AVLTreeNode pSuccessor = nSuccessor.parent;

		if (toRemove == root) removeRootTwoChildren(nSuccessor, pSuccessor);
		else removeInternalTwoChildren(toRemove, parent, nSuccessor, pSuccessor);
	}

	private void removeInternalTwoChildren(final AVLTreeNode toRemove, final AVLTreeNode parent,
	                                       final AVLTreeNode nSuccessor, final AVLTreeNode pSuccessor) {
		if (parent.left == toRemove) parent.left = nSuccessor;
		else parent.right = nSuccessor;
		nSuccessor.parent = parent;

		if (pSuccessor == toRemove) {
			nSuccessor.left = toRemove.left;
			toRemove.left.parent = nSuccessor;
		} else {
			pSuccessor.left = nSuccessor.right;
			if (nSuccessor.right != null) nSuccessor.right.parent = pSuccessor;

			nSuccessor.right = toRemove.right;
			toRemove.right.parent = nSuccessor;
			nSuccessor.left = toRemove.left;
			toRemove.left.parent = nSuccessor;
		}
	}

	private void removeRootTwoChildren(final AVLTreeNode nSuccessor, final AVLTreeNode pSuccessor) {
		if (nSuccessor == root.right) {
			nSuccessor.left = root.left;
			root.left.parent = nSuccessor;
		} else {
			pSuccessor.left = nSuccessor.right;
			if (nSuccessor.right != null) nSuccessor.right.parent = pSuccessor;
			nSuccessor.left = root.left;
			root.left.parent = nSuccessor;

			nSuccessor.right = root.right;
			root.right.parent = nSuccessor;
		}
		root = nSuccessor;
		root.parent = null;
	}

	private AVLTreeNode inOrderSuccessor(final AVLTreeNode node) {
		if (node.right != null) {
			AVLTreeNode successor = node.right;
			while (successor.left != null) successor = successor.left;
			return successor;
		}

		AVLTreeNode p = node.parent, successor = node;
		while (p != null && p.right == successor) {
			successor = p;
			p = successor.parent;
		}
		return p;
	}

	private void removeZeroChildren(final AVLTreeNode parent, final AVLTreeNode toRemove) {
		if (toRemove == root) root = null;
		else {
			if (parent.left == toRemove) parent.left = null;
			else parent.right = null;
		}
	}

	public Object[] toArray() {
		final Object[] obj = new Object[size()];
		toArray(obj, root, new Index());
		return obj;
	}

	private void toArray(final Object[] obj, final AVLTreeNode node, final Index index) {
		if (node != null) {
			toArray(obj, node.left, index);
			obj[index.i++] = node.elem;
			toArray(obj, node.right, index);
		}
	}

	private void toString(final String prefix, final boolean isTail, final StringBuilder sb,
	                      final AVLTreeNode node) {
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

	private class NodeGroup {
		private final AVLTreeNode newLeft, newRight, newParent;
		private final AVLTreeNode first, second, third, fourth;
		private final AVLTreeNode parent;

		NodeGroup(final AVLTreeNode grandChild) {
			final AVLTreeNode child = grandChild.parent;
			parent = child.parent;

			assert parent != null : "Parent cannot be null here!";
			final AVLTreeNode[] sort = {parent, child, grandChild};
			Arrays.sort(sort, Comparator.comparingInt(node -> node.key));

			first = sort[0].left;
			newLeft = sort[0];
			newParent = sort[1];
			newRight = sort[2];
			fourth = sort[2].right;

			if (newParent != child) {
				second = grandChild.left;
				third = grandChild.right;
			} else {
				if (grandChild.key < parent.key) {
					second = newLeft.right;
					third = newParent.right;
				} else {
					second = newParent.left;
					third = newRight.left;
				}
			}
		}

		void cutAndLink() {
			final AVLTreeNode grandparent = parent.parent;
			newParent.left = newLeft;
			newLeft.parent = newParent;
			newParent.right = newRight;
			newRight.parent = newParent;

			// newLeft.left = first;
			if (first != null) first.parent = newLeft;
			newLeft.right = second;
			if (second != null) second.parent = newLeft;

			newRight.left = third;
			if (third != null) third.parent = newRight;
			// newRight.right = fourth;
			if (fourth != null) fourth.parent = newRight;

			newParent.parent = grandparent;
			if (grandparent != null) {
				if (grandparent.left == parent) grandparent.left = newParent;
				else grandparent.right = newParent;

			}
			if (root == parent) {
				root = newParent;
				root.parent = null;
			}
		}
	}

}
