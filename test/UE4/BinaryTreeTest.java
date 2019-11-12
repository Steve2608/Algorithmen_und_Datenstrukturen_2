package UE4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Helper class for easier debugging of tree
 * Requires <tt>root</tt> in <tt>BinaryTree</tt> to be >= package
 */
class DebugTree extends BinaryTree {

	/**
	 * Checks if references between all children and parents match
	 *
	 * @return match
	 */
	boolean checkValidReferences() {
		return checkValidReferences(root);
	}

	private boolean checkValidReferences(final BinaryTreeNode n) {
		if (n != null) {
			final BinaryTreeNode parent = n.parent, left = n.left, right = n.right;
			if (parent != null && (parent.left == n && parent.right == n || parent.left != n && parent.right != n))
				return false;
			if (left != null && left.parent != n) return false;
			if (right != null && right.parent != n) return false;
			return checkValidReferences(left) && checkValidReferences(right);
		}
		return true;
	}

	/**
	 * Checks if >/< condition holds for all nodes in the tree
	 *
	 * @return tree-structure
	 */
	boolean isTreeStructure() {
		return isTreeStructure(root);
	}

	private boolean isTreeStructure(final BinaryTreeNode n) {
		if (n != null) {
			final BinaryTreeNode left = n.left, right = n.right;
			if (left != null && n.key.compareTo(left.key) < 0) return false;
			if (right != null && n.key.compareTo(right.key) > 0) return false;
			return isTreeStructure(left) && isTreeStructure(right);
		}
		return true;
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

	/**
	 * Helper function to print entire tree
	 *
	 * @return String representation of tree
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		toString("", true, sb, root);
		return sb.toString();
	}

}

class BinaryTreeTest {

	private static final int N_TRIALS = 100_000, BOUND = N_TRIALS / 1_000;
	private static final boolean PRINT_TREE = true, USE_DEBUG_TREE = true;

	private final BinarySearchTree tree = USE_DEBUG_TREE ? new DebugTree() : new BinaryTree();

	@BeforeEach
	void setUp() {
		final int[] values = {5, 29, 12, 0, 3, 7, 18, 18, 11, 1};
		for (final int elem : values) {
			tree.insert(elem, String.valueOf(Objects.hashCode(elem)));
		}
		if (PRINT_TREE) System.out.println(tree);
	}

	@Test
	void testSize() {
		assertEquals(9, tree.size());

		tree.insert(12, String.valueOf(Objects.hashCode(12)));
		if (PRINT_TREE) System.out.println(tree);
		assertEquals(9, tree.size(), "Size must change when trying to insert duplicate value");

		tree.insert(2, String.valueOf(Objects.hashCode(2)));
		if (PRINT_TREE) System.out.println(tree);
		assertEquals(10, tree.size(), "Size was not updated upon insert");

		tree.remove(11);
		if (PRINT_TREE) System.out.println(tree);
		assertEquals(9, tree.size(), "Size was not updated upon remove");

		tree.remove(11);
		if (PRINT_TREE) System.out.println(tree);
		assertEquals(9, tree.size(), "Size must not change upon removal of not-contained element");
	}

	@Test
	void testIsRoot() {
		assertThrows(IllegalArgumentException.class, () -> tree.isRoot(null),
				"IllegalArgumentException must be thrown on empty element");
		assertTrue(tree.isRoot(5), "Root was not detected as such");

		tree.remove(5);
		if (PRINT_TREE) System.out.println(tree);
		assertTrue(tree.isRoot(7), "Root was not updated correctly");

		assertFalse(tree.isRoot(5), "Not-present element cannot be root");
		assertFalse(tree.isRoot(11), "Non-root element was root");
	}

	@Test
	void testIsInternal() {
		assertThrows(IllegalArgumentException.class, () -> tree.isInternal(null),
				"IllegalArgumentException must be thrown on empty element");

		assertTrue(tree.isInternal(7), "Internal Node was not detected");
		assertFalse(tree.isInternal(11), "External Node was wrongfully detected as internal");

		assertFalse(tree.isInternal(24352345), "Non-present element cannot be internal");

		tree.remove(11);
		if (PRINT_TREE) System.out.println(tree);
		assertFalse(tree.isInternal(7), "Node was detected as internal after remove");

		tree.insert(15, String.valueOf(Objects.hashCode(15)));
		if (PRINT_TREE) System.out.println(tree);
		assertTrue(tree.isInternal(18), "Node was not detected as internal after insert");
		assertFalse(tree.isInternal(15), "Newly added Node was detected as internal");
	}

	@Test
	void testIsExternal() {
		assertThrows(IllegalArgumentException.class, () -> tree.isExternal(null),
				"IllegalArgumentException must be thrown on empty element");
		assertTrue(tree.isExternal(11), "External Node was not detected");
		assertFalse(tree.isExternal(7), "Internal Node was wrongfully detected as external");

		assertFalse(tree.isExternal(24352345), "Non-present element cannot be external");

		tree.remove(11);
		if (PRINT_TREE) System.out.println(tree);
		assertTrue(tree.isExternal(7), "New external Node after removal was not detected");

		tree.insert(15, String.valueOf(Objects.hashCode(15)));
		if (PRINT_TREE) System.out.println(tree);
		assertTrue(tree.isExternal(15), "Newly added Node was not detected as external");
		assertFalse(tree.isExternal(18), "Node was detected as external after insert");
	}

	@Test
	void testGetParent() {
		assertThrows(IllegalArgumentException.class, () -> tree.getParent(null),
				"IllegalArgumentException must be thrown on empty element");

		assertNull(tree.getParent(5), "Root cannot have parent");
		assertNull(tree.getParent(2341342), "Non-contained Node cannot have parent");
		assertEquals(12, tree.getParent(7), "Parent was not found");
	}

	@Test
	void testInsert() {
		assertThrows(IllegalArgumentException.class, () -> tree.insert(null, "Lol"),
				"IllegalArgumentException must be thrown on empty key");
		assertThrows(IllegalArgumentException.class, () -> tree.insert(2324, null),
				"IllegalArgumentException must be thrown on empty element");

		assertTrue(tree.insert(1231, String.valueOf(Objects.hashCode(1231))), "New element was not inserted");
		if (PRINT_TREE) System.out.println(tree);
		assertFalse(tree.insert(1231, String.valueOf(Objects.hashCode(1231))), "Duplicated element must not be inserted again");

		assertTrue(tree.insert(6, String.valueOf(Objects.hashCode(6))), "New element was not inserted");
		if (PRINT_TREE) System.out.println(tree);
		assertEquals(7, tree.getParent(6), "Node was not inserted at the correct position");
	}

	@Test
	void testFind() {
		assertThrows(IllegalArgumentException.class, () -> tree.find(null),
				"IllegalArgumentException must be thrown on empty element");

		// TODO dumb case
	}

	@Test
	void testRemove() {
		assertThrows(IllegalArgumentException.class, () -> tree.remove(null),
				"IllegalArgumentException must be thrown on empty element");

		assertTrue(tree.remove(1), "Element with no children was not removed");
		if (PRINT_TREE) System.out.println(tree);
		assertTrue(tree.remove(7), "Element with one child was not removed");
		if (PRINT_TREE) System.out.println(tree);
		assertTrue(tree.remove(12), "Element with two children was not removed");
		if (PRINT_TREE) System.out.println(tree);
		assertTrue(tree.remove(5), "Root with two children was not removed");
		if (PRINT_TREE) System.out.println(tree);
		assertFalse(tree.remove(5), "Cannot remove non-contained Node");
		if (PRINT_TREE) System.out.println(tree);
		final Object[] strings = IntStream.of(0, 3, 11, 18, 29)
				.mapToObj(i -> String.valueOf(Objects.hashCode(i))).toArray();
		assertArrayEquals(strings, tree.toArrayInOrder(), "Structure of tree was violated");
	}

	@Test
	void testToArrayPostOrder() {
		final Object[] strings = IntStream.of(1, 3, 0, 11, 7, 18, 12, 29, 5)
				.mapToObj(i -> String.valueOf(Objects.hashCode(i))).toArray();
		assertArrayEquals(strings, tree.toArrayPostOrder());
		assertArrayEquals(new String[0], new BinaryTree().toArrayPostOrder(), "Empty tree must produce empty array");
	}

	@Test
	void testToArrayInOrder() {
		final Object[] strings = IntStream.of(0, 1, 3, 5, 7, 11, 12, 18, 29)
				.mapToObj(i -> String.valueOf(Objects.hashCode(i))).toArray();
		assertArrayEquals(strings, tree.toArrayInOrder());
		assertArrayEquals(new String[0], new BinaryTree().toArrayInOrder(), "Empty tree must produce empty array");
	}

	@Test
	void testToArrayPreOrder() {
		final Object[] strings = IntStream.of(5, 0, 3, 1, 29, 12, 7, 11, 18)
				.mapToObj(i -> String.valueOf(Objects.hashCode(i))).toArray();
		assertArrayEquals(strings, tree.toArrayPreOrder());
		assertArrayEquals(new String[0], new BinaryTree().toArrayPreOrder(), "Empty tree must produce empty array");
	}

	@Test
	void testRandomOperations() {
		final Random r = new Random(12);
		final DebugTree debug = (DebugTree) tree;

		// takes roughly 70% of the timeout on my weak laptop on battery; should be more than enough
		assertTimeout(Duration.ofMillis(N_TRIALS / 10), () -> {
			int size = tree.size();
			for (int i = 0; i < N_TRIALS; i++) {
				final int k = r.nextInt(BOUND);
				switch (r.nextInt(6)) {
					case 0:
						assertEquals(size, tree.size(), "Inconsistent size");
						break;
					case 1:
						if (tree.find(k) != null) {
							assertNotEquals(tree.isInternal(k), tree.isExternal(k),
									"Node cannot be internal and external at the same time");
						} else {
							assertFalse(tree.isInternal(k), "If element is not contained it cannot be internal");
							assertFalse(tree.isExternal(k), "If element is not contained it cannot be external");
						}
						break;
					case 2:
						if (tree.find(k) == null)
							assertNull(tree.getParent(k), "If Node cannot be count it cannot have a parent");
						else {
							if (tree.isRoot(k))
								assertNull(tree.getParent(k), "If Node is root it cannot have a parent");
							else
								assertNotNull(tree.getParent(k), "If Node is != root it has to have a parent");
						}
						break;
					case 3:
						final boolean insert;
						assertNotEquals(tree.find(k), insert = tree.insert(k, String.valueOf(Objects.hashCode(k))),
								"If tree contains element, it cannot be inserted and vice versa");
						if (insert) size++;
						break;
					case 4:
						final boolean remove;
						assertEquals(tree.find(k) != null, remove = tree.remove(k),
								"If tree contains element, it must be removable and vice versa");
						if (remove) size--;
						break;
					case 5:
						final Object[] pre = tree.toArrayPreOrder();
						final Object[] in = tree.toArrayInOrder();
						final Object[] post = tree.toArrayPostOrder();
						if (pre.length != post.length || in.length != pre.length) {
							fail(String.format("Lengths of arrays differed: len(pre)=%d len(in)=%d len(post)=%d",
									pre.length, in.length, post.length));
						}
						break;
					default:
						fail("This case should never be reached");
				}
				if (USE_DEBUG_TREE) {
					assertTrue(debug.isTreeStructure(), "Tree structure was violated: \n" + tree);
					assertTrue(debug.checkValidReferences(), "Some pointers are messed up");
				}
			}
		});
	}
}