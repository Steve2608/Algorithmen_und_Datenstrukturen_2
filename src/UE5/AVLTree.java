package UE5;

import java.util.Arrays;
import java.util.Comparator;

public class AVLTree {

	private AVLTreeNode root, errorNode;
	private int size = 0;

	public AVLTreeNode getRoot() {
		return root;
	}

	public int size() {
		return size;
	}

	public boolean isRoot(final Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Cannot compare with null key");
		return root != null && key.equals(root.key);
	}

	AVLTreeNode getParent(final AVLTreeNode n) {
		return n != null ? n.parent : null;
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

			updateHeightsUpward(insert);
			rebalanceUpward(insert);
		}
		size++;
		setNewHeight(root);
		return true;
	}

	private boolean isBalancedGrandChild(final AVLTreeNode node) {
		return node.parent != null && node.parent.parent != null
				&& Math.abs(height(node.parent.parent.left) - height(node.parent.parent.right)) <= 1;
	}

	private void rebalanceUpward(AVLTreeNode curr) {
		while (curr != root) {
			while (curr != null && isBalancedGrandChild(curr)) {
				curr = curr.parent;
			}
			if (curr != null) {
				if (curr.parent != null && curr.parent.parent != null) {
					restructure(curr);
					return;
				} else {
					curr = curr.parent;
				}
			} else return;
		}
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
		if (grandChild == null) return;
		if (size() == 1) {
			updateHeightsUpward(grandChild);
			return;
		}
		final AVLTreeNode child = grandChild.parent, parent = child.parent;
		final AVLTreeNode[] sort = {parent, child, grandChild};
		Arrays.sort(sort, Comparator.comparingInt(node -> node.key));

		final NodeGroup nodes = new NodeGroup();
		nodes.first = sort[0].left;
		nodes.newLeft = sort[0];
		nodes.newParent = sort[1];
		nodes.newRight = sort[2];
		nodes.fourth = sort[2].right;

		if (nodes.newParent != child) {
			nodes.second = grandChild.left;
			nodes.third = grandChild.right;
		} else {
			if (grandChild.key < parent.key) {
				nodes.second = nodes.newLeft.right;
				nodes.third = nodes.newParent.right;
			} else {
				nodes.second = nodes.newParent.left;
				nodes.third = nodes.newRight.left;
			}
		}

		// Cut & Link
		final AVLTreeNode grandparent = parent.parent;
		if (root == parent || grandparent == null) root = nodes.newParent;
		nodes.newParent.left = nodes.newLeft;
		nodes.newLeft.parent = nodes.newParent;
		nodes.newParent.right = nodes.newRight;
		nodes.newRight.parent = nodes.newParent;

		nodes.newLeft.left = nodes.first;
		if (nodes.first != null) nodes.first.parent = nodes.newLeft;
		nodes.newLeft.right = nodes.second;
		if (nodes.second != null) nodes.second.parent = nodes.newLeft;

		nodes.newRight.left = nodes.third;
		if (nodes.third != null) nodes.third.parent = nodes.newRight;
		nodes.newRight.right = nodes.fourth;
		if (nodes.fourth != null) nodes.fourth.parent = nodes.newRight;

		nodes.newParent.parent = grandparent;
		if (grandparent != null) {
			if (grandparent.left == parent) {
				grandparent.left = nodes.newParent;
			} else if (grandparent.right == parent) {
				grandparent.right = nodes.newParent;
			}
		}

		setNewHeight(nodes.newLeft);
		setNewHeight(nodes.newRight);
		updateHeightsUpward(nodes.newParent);
	}

	private int height(final AVLTreeNode node) {
		return node != null ? node.height : -1;
	}

	private void setNewHeight(final AVLTreeNode node) {
		node.height = Math.max(height(node.left), height(node.right)) + 1;
	}

	private void updateHeightsUpward(AVLTreeNode curr) {
		while (curr != null) {
			setNewHeight(curr);
			curr = curr.parent;
		}
	}

	private boolean isAVLTree(final AVLTreeNode n) {
		if (errorNode != null) return false;
		if (!hasChildren(n)) return true;

		assert n.parent != n && n.left != n && n.right != n : "cyclic reference" + toString(n) + '\n' + this;
		assert n.parent != n.left : "parent equals left" + toString(n) + '\n' + this;
		assert n.parent != n.right : "parent equals right" + toString(n) + '\n' + this;
		assert n.left != n.right : "parent equals right" + toString(n) + '\n' + this;

		assert n.right == null || n.right.parent == null || n.right.parent == n : "wrong right pointers";
		assert n.left == null || n.left.parent == null || n.left.parent == n : "wrong left pointers";
		assert n.parent == null || n.parent.left == null && n.parent.right == null || n.parent.left == n && n.parent.right != n || n.parent.left != n && n.parent.right == n : "wrong " +
				"parent pointer";

		if (!isAVLTree(n.left)) return false;
		final int right = height(n.right);
		final int left = height(n.left);
		if (Math.abs(right - left) >= 2) {
			errorNode = right < left ? n.right : n.left;
			return false;
		}
		return isAVLTree(n.right);
	}

	private void findAndCorrect(final AVLTreeNode n) {
		if (n == null) return;

		if (Math.abs(height(n.left) - height(n.right)) > 1) {
			final AVLTreeNode child = height(n.left) >= height(n.right) ? n.left : n.right;
			if (child != null) {
				restructure(height(child.left) >= height(child.right) ? child.left : child.right);
			} else {
				restructure(n);
			}
			errorNode = null;
		}
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

		final AVLTreeNode parent = getParent(toRemove);
		if (!hasChildren(toRemove)) removeZeroChildren(parent, toRemove);
		else if (toRemove.left != null && toRemove.right == null)
			removeOneChild(parent, toRemove, toRemove.left);
		else if (toRemove.left == null && toRemove.right != null)
			removeOneChild(parent, toRemove, toRemove.right);
		else removeTwoChildren(toRemove);

		rebalanceUpward(parent);
		while (!isAVLTree()) {
			findAndCorrect(errorNode);
		}

		size--;
		return true;
	}

	private boolean isAVLTree() {
		updateHeightsDownward(root);
		return isAVLTree(root);
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
		if (parent == null) {
			root = child;
			root.height--;
		} else {
			if (parent.left == toRemove) parent.left = child;
			else parent.right = child;
			updateHeightsUpward(child);
			restructure(child);
		}
	}

	private void removeTwoChildren(final AVLTreeNode toRemove) {
		final AVLTreeNode parent = getParent(toRemove), nSuccessor = inOrderSuccessor(toRemove);
		final AVLTreeNode pSuccessor = getParent(nSuccessor);

		if (toRemove == root) removeRootTwoChildren(nSuccessor, pSuccessor);
		else removeInternalTwoChildren(toRemove, parent, nSuccessor, pSuccessor);
	}

	private String toString(final AVLTreeNode node) {
		return node != null ? String.format("[%d]@(%d):\'%s\'", node.key, node.height, node.elem) : null;
	}

	private void removeInternalTwoChildren(final AVLTreeNode toRemove, final AVLTreeNode parent,
	                                       final AVLTreeNode nSuccessor, final AVLTreeNode pSuccessor) {
		if (parent.left == toRemove) parent.left = nSuccessor;
		else parent.right = nSuccessor;
		nSuccessor.parent = parent;

		if (pSuccessor == toRemove) {
			nSuccessor.left = toRemove.left;
			toRemove.left.parent = nSuccessor;

			updateHeightsUpward(nSuccessor);
			rebalanceUpward(parent);
		} else {
			pSuccessor.left = nSuccessor.right;
			if (pSuccessor.right != null) pSuccessor.right.parent = pSuccessor;

			nSuccessor.right = toRemove.right;
			toRemove.right.parent = nSuccessor;
			nSuccessor.left = toRemove.left;
			toRemove.left.parent = nSuccessor;
			updateHeightsUpward(nSuccessor);
			rebalanceUpward(nSuccessor);
		}
	}

	private void removeRootTwoChildren(final AVLTreeNode nSuccessor, final AVLTreeNode pSuccessor) {
		if (nSuccessor == root.right) {
			nSuccessor.left = root.left;
		} else {
			if (pSuccessor != root) {
				pSuccessor.left = nSuccessor.right;
				if (nSuccessor.right != null)
					nSuccessor.right.parent = pSuccessor;
			}
			nSuccessor.left = root.left;
			nSuccessor.right = root.right;
		}
		root = nSuccessor;
		root.parent = null;

		setNewHeight(root);
	}

	private AVLTreeNode inOrderSuccessor(final AVLTreeNode node) {
		if (node.right != null) {
			AVLTreeNode successor = node.right;
			while (successor.left != null) successor = successor.left;
			return successor;
		}

		AVLTreeNode p = getParent(node), successor = node;
		while (p != null && p.right == successor) {
			successor = p;
			p = getParent(successor);
		}
		return p;
	}

	private void removeZeroChildren(final AVLTreeNode parent, final AVLTreeNode toRemove) {
		if (parent == null) root = null;
		else {
			if (parent.left == toRemove) parent.left = null;
			else parent.right = null;
		}
		updateHeightsUpward(parent);
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

	private static class NodeGroup {
		private AVLTreeNode newLeft, newRight, newParent;
		private AVLTreeNode first, second, third, fourth;
	}

}
