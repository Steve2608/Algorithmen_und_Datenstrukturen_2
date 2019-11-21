package UE5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Helper class for easier debugging of tree
 */
class DebugTree extends AVLTree {

	/**
	 * Checks if references between all children and parents match
	 *
	 * @return match
	 */
	boolean checkSymmetricReferences() {
		return checkSymmetricReferences(getRoot());
	}

	private boolean checkSymmetricReferences(final AVLTreeNode n) {
		if (n != null) {
			final AVLTreeNode parent = getParent(n), left = n.left, right = n.right;
			if (parent != null && (parent.left == n && parent.right == n || parent.left != n && parent.right != n)) {
				System.out.println("Parent: " + toString(parent));
				return false;
			}
			if (left != null && getParent(left) != n) {
				System.out.println("Left: " + toString(left));
				return false;
			}
			if (right != null && getParent(right) != n) {
				System.out.println("Right: " + toString(right));
				return false;
			}
			return checkSymmetricReferences(left) && checkSymmetricReferences(right);
		}
		return true;
	}

	AVLTreeNode getParent(final AVLTreeNode n) {
		if (n == null || n == getRoot()) return null;

		for (AVLTreeNode curr = getRoot(); curr != null; ) {
			if (curr.left == n || curr.right == n) return curr;
			curr = n.key.compareTo(curr.key) < 0 ? curr.left : curr.right;
		}
		return null;
	}

	/**
	 * Checks if >/< condition holds for all nodes in the tree
	 *
	 * @return tree-structure
	 */
	boolean isAVLTreeStructure() {
		return isAVLTreeStructure(getRoot());
	}

	private boolean isAVLTreeStructure(final AVLTreeNode n) {
		if (n != null) {
			final AVLTreeNode left = n.left, right = n.right;
			if (left != null && n.key.compareTo(left.key) < 0) return false;
			if (right != null && n.key.compareTo(right.key) > 0) return false;
			if (n.height != Math.max(height(left), height(right)) + 1) {
				System.out.println(toString(n));
				return false;
			}
			return isAVLTreeStructure(left) && isAVLTreeStructure(right);
		}
		return true;
	}

	private int height(final AVLTreeNode node) {
		return node != null ? node.height : -1;
	}

	private String toString(final AVLTreeNode node) {
		return node != null ? String.format("[%d]@(%d):\'%s\'", node.key, node.height, node.elem) : null;
	}

	private void toString(final String prefix, final boolean isTail, final StringBuilder sb,
	                      final AVLTreeNode node) {
		if (node != null) {
			if (node.right != null)
				toString(prefix + (isTail ? "│   " : "    "), false, sb, node.right);
			sb.append(prefix).append(isTail ? "└── " : "┌── ").append(toString(node)).append("\n");
			if (node.left != null)
				toString(prefix + (isTail ? "    " : "│   "), true, sb, node.left);
		}
	}

	/**
	 * Helper function to print entire tree
	 *
	 * @return String representation of tree
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		toString("", true, sb, getRoot());
		return sb.toString();
	}

}

class AVLTreeTest {

	/**
	 * Number of random trial runs
	 */
	private static final int N_TRIALS = 100_000;
	/**
	 * Highest value a key can take (exclusive)
	 */
	private static final int MAX_KEY = N_TRIALS / 1_000;
	/**
	 * Print tree after each change in all tests but <tt>testRandomOperations</tt>
	 */
	private static final boolean PRINT = true;
	/**
	 * Use Debug Tree in all tests and assert correctness in <tt>testRandomOperations</tt>
	 */
	private static final boolean USE_DEBUG_TREE = true;
	/**
	 * Print debug information in <tt>testRandomOperations</tt>
	 * Caution: Will slow down test considerably (~80 times slower on my machine)
	 */
	private static final boolean PRINT_DEBUG = false;
	private final AVLTree tree = USE_DEBUG_TREE ? new DebugTree() : new AVLTree();

	@BeforeEach
	void setUp() {
		final int[] values = {5, 29, 12, 0, 3, 7, 18, 18, 11, 1};
		for (final int elem : values) {
			tree.insert(elem, String.valueOf(-elem));
		}
		if (PRINT) System.out.println(tree);
	}

	@Test
	void testSize() {
		assertEquals(9, tree.size());

		tree.insert(12, String.valueOf(-12));
		if (PRINT) System.out.println(tree);
		assertEquals(9, tree.size(), "Size must change when trying to insert duplicate value");

		tree.insert(2, String.valueOf(-2));
		if (PRINT) System.out.println(tree);
		assertEquals(10, tree.size(), "Size was not updated upon insert");

		tree.remove(11);
		if (PRINT) System.out.println(tree);
		assertEquals(9, tree.size(), "Size was not updated upon remove");

		tree.remove(11);
		if (PRINT) System.out.println(tree);
		assertEquals(9, tree.size(), "Size must not change upon removal of not-contained element");
	}

	@Test
	void testIsRoot() {
		assertThrows(IllegalArgumentException.class, () -> tree.isRoot(null),
				"IllegalArgumentException must be thrown on empty element");
		assertTrue(tree.isRoot(5), "Root was not detected as such");

		tree.remove(5);
		if (PRINT) System.out.println(tree);
		assertTrue(tree.isRoot(7), "Root was not updated correctly");

		assertFalse(tree.isRoot(5), "Not-present element cannot be root");
		assertFalse(tree.isRoot(11), "Non-root element was root");
	}

	@Test
	void testInsert() {
		assertThrows(IllegalArgumentException.class, () -> tree.insert(null, "Lol"),
				"IllegalArgumentException must be thrown on empty key");
		assertThrows(IllegalArgumentException.class, () -> tree.insert(2324, null),
				"IllegalArgumentException must be thrown on empty element");
		assertThrows(IllegalArgumentException.class, () -> tree.insert(null, null),
				"IllegalArgumentException must be thrown if both key and element are null");

		assertTrue(tree.insert(1231, String.valueOf(-1231)), "New element was not inserted");
		if (PRINT) System.out.println(tree);
		assertFalse(tree.insert(1231, String.valueOf(-1231)), "Duplicated element must not be inserted again");

		assertTrue(tree.insert(6, String.valueOf(-6)), "New element was not inserted");
	}

	@Test
	void testFind() {
		assertThrows(IllegalArgumentException.class, () -> tree.find(null),
				"IllegalArgumentException must be thrown on empty element");

		assertEquals("-7", tree.find(7), "Contained element was not found");
		assertEquals("-5", tree.find(5), "Contained element was not found");
		assertNull(tree.find(-10), "Not contained element was found");

		tree.remove(7);
		if (PRINT) System.out.println(tree);
		assertNull(tree.find(7), "Key must not be found after removal");

		tree.insert(-10, String.valueOf(10));
		if (PRINT) System.out.println(tree);
		assertEquals("10", tree.find(-10), "Key not found after insert");
	}

	@Test
	void testRemove() {
		assertThrows(IllegalArgumentException.class, () -> tree.remove(null),
				"IllegalArgumentException must be thrown on empty element");

		assertTrue(tree.remove(1), "Element with no children was not removed");
		if (PRINT) System.out.println(tree);
		assertTrue(tree.remove(7), "Element with one child was not removed");
		if (PRINT) System.out.println(tree);
		assertTrue(tree.remove(12), "Element with two children was not removed");
		if (PRINT) System.out.println(tree);
		assertTrue(tree.remove(5), "Root with two children was not removed");
		if (PRINT) System.out.println(tree);
		assertFalse(tree.remove(5), "Cannot remove non-contained Node");
		if (PRINT) System.out.println(tree);
		final Object[] strings = IntStream.of(0, 3, 11, 18, 29)
				.mapToObj(i -> String.valueOf(-i)).toArray();
		assertArrayEquals(strings, tree.toArray(), "Structure of tree was violated");
	}

	@Test
	void testHeight() {
		assertEquals(3, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(1);
		assertEquals(3, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(7);
		assertEquals(3, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(12);
		assertEquals(2, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(18);
		assertEquals(2, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(11);
		assertEquals(2, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(5);
		assertEquals(1, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(3);
		assertEquals(1, tree.height(), "Height was assessed incorrectly");

		if (PRINT) System.out.println(tree);
		tree.remove(0);
		assertEquals(0, tree.height(), "Tree with one node must have height == 0");

		if (PRINT) System.out.println(tree);
		tree.remove(29);
		assertEquals(-1, tree.height(), "Empty tree must have height == -1");

		assertEquals(-1, new AVLTree().height(), "New must have size == -1");
	}

	@Test
	void testToArray() {
		final Object[] strings = IntStream.of(0, 1, 3, 5, 7, 11, 12, 18, 29)
				.mapToObj(i -> String.valueOf(-i)).toArray();
		assertArrayEquals(strings, tree.toArray());
		assertArrayEquals(new String[0], new AVLTree().toArray(), "Empty tree must produce empty array");
	}

	@Test
	void testRandomOperations() {
		final Random r = new Random(12);
		final RandomChoice[] operations = RandomChoice.values();

		int size = tree.size();
		for (int i = 0; i < N_TRIALS; i++) {
			final RandomChoice choice = operations[r.nextInt(operations.length)];
			final int k = r.nextInt(MAX_KEY);

			if (PRINT) System.out.println(String.format("[i:%d, c:%s, k:%d]", i, choice, k));
			if (PRINT_DEBUG) System.out.println(tree);

			switch (choice) {
				case SIZE:
					assertEquals(size, tree.size(), "Inconsistent size");
					break;
				case INSERT:
					final boolean insert;
					assertNotEquals(tree.find(k), insert = tree.insert(k, String.valueOf(-k)),
							"If tree contains element, it cannot be inserted and vice versa");
					if (insert) size++;
					break;
				case HEIGHT:
					// super basic
					assertDoesNotThrow(tree::height);
				case REMOVE:
					final boolean remove;
					assertEquals(tree.find(k) != null, remove = tree.remove(k),
							"If tree contains element, it must be removable and vice versa");
					if (remove) size--;
					break;
				case TO_ARRAY:
					final Object[] in = tree.toArray();
					assertEquals(size, in.length, String.format("Lengths differed: Expected: %d, but was: %d", size,
							in.length));
					break;
				default:
					fail("This case should never be reached");
			}
			if (USE_DEBUG_TREE) {
				final DebugTree debug = (DebugTree) tree;
				assertTrue(debug.isAVLTreeStructure(), "Malformed tree structure");
				// Dies at i=528 for me because apparently I suck
				assertTrue(debug.checkSymmetricReferences(), "Messed up pointers");
			}
		}
	}

	enum RandomChoice {
		SIZE, INSERT, HEIGHT, REMOVE, TO_ARRAY
	}

}