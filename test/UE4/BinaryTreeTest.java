package UE4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {

	private static final int N_TRIALS = 500_000, BOUND = N_TRIALS / 1_000;

	private final BinaryTree tree = new BinaryTree();

	@BeforeEach
	void setUp() {
		tree.insert(5L);
		tree.insert(29L);
		tree.insert(12L);
		tree.insert(0L);
		tree.insert(3L);
		tree.insert(7L);
		tree.insert(18L);
		tree.insert(11L);
		tree.insert(1L);
		// System.out.println(tree);
	}

	@Test
	void testSize() {
		assertEquals(9, tree.size());

		tree.insert(12L);
		assertEquals(9, tree.size(), "Size must change when trying to insert duplicate value");

		tree.insert(2L);
		assertEquals(10, tree.size(), "Size was not updated upon insert");

		tree.remove(11L);
		assertEquals(9, tree.size(), "Size was not updated upon remove");

		tree.remove(11L);
		assertEquals(9, tree.size(), "Size must not change upon removal of not-contained element");
	}

	@Test
	void testIsRoot() {
		assertThrows(IllegalArgumentException.class, () -> tree.isRoot(null),
				"IllegalArgumentException must be thrown on empty element");
		assertTrue(tree.isRoot(5L), "Root was not detected as such");

		tree.remove(5L);
		assertTrue(tree.isRoot(7L), "Root was not updated correctly");

		assertFalse(tree.isRoot(5L), "Not-present element cannot be root");
		assertFalse(tree.isRoot(11L), "Non-root element was root");
	}

	@Test
	void testIsInternal() {
		assertThrows(IllegalArgumentException.class, () -> tree.isInternal(null),
				"IllegalArgumentException must be thrown on empty element");

		assertTrue(tree.isInternal(7L), "Internal Node was not detected");
		assertFalse(tree.isInternal(11L), "External Node was wrongfully detected as internal");

		assertFalse(tree.isInternal(24352345L), "Non-present element cannot be internal");

		tree.remove(11L);
		assertFalse(tree.isInternal(7L), "Node was detected as internal after remove");

		tree.insert(15L);
		assertTrue(tree.isInternal(18L), "Node was not detected as internal after insert");
		assertFalse(tree.isInternal(15L), "Newly added Node was detected as internal");
	}

	@Test
	void testIsExternal() {
		assertThrows(IllegalArgumentException.class, () -> tree.isExternal(null),
				"IllegalArgumentException must be thrown on empty element");
		assertTrue(tree.isExternal(11L), "External Node was not detected");
		assertFalse(tree.isExternal(7L), "Internal Node was wrongfully detected as external");

		assertFalse(tree.isExternal(24352345L), "Non-present element cannot be external");

		tree.remove(11L);
		assertTrue(tree.isExternal(7L), "New external Node after removal was not detected");

		tree.insert(15L);
		assertTrue(tree.isExternal(15L), "Newly added Node was not detected as external");
		assertFalse(tree.isExternal(18L), "Node was detected as external after insert");
	}

	@Test
	void testGetParent() {
		assertThrows(IllegalArgumentException.class, () -> tree.getParent(null),
				"IllegalArgumentException must be thrown on empty element");

		assertNull(tree.getParent(5L), "Root cannot have parent");
		assertNull(tree.getParent(2341342L), "Non-contained Node cannot have parent");
		assertEquals(12L, tree.getParent(7L), "Parent was not found");
	}

	@Test
	void testInsert() {
		assertThrows(IllegalArgumentException.class, () -> tree.insert(null),
				"IllegalArgumentException must be thrown on empty element");

		assertTrue(tree.insert(1231L), "New element was not inserted");
		assertFalse(tree.insert(1231L), "Duplicated element must not be inserted again");

		assertTrue(tree.insert(6L), "New element was not inserted");
		assertEquals(7L, tree.getParent(6L), "Node was not inserted at the correct position");
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

		assertTrue(tree.remove(1L), "Element with no children was not removed");
		assertTrue(tree.remove(7L), "Element with one child was not removed");
		assertTrue(tree.remove(12L), "Element with two children was not removed");
		assertTrue(tree.remove(5L), "Root with two children was not removed");
		assertFalse(tree.remove(5L), "Cannot remove non-contained Node");
		assertArrayEquals(new Long[]{0L, 3L, 11L, 18L, 29L}, tree.toArrayInOrder(), "Structure of tree was violated");
	}

	@Test
	void testToArrayPostOrder() {
		assertArrayEquals(new Long[]{1L, 3L, 0L, 11L, 7L, 18L, 12L, 29L, 5L}, tree.toArrayPostOrder());
		assertArrayEquals(new Long[0], new BinaryTree().toArrayPostOrder(), "Empty tree must produce empty array");
	}

	@Test
	void testToArrayInOrder() {
		assertArrayEquals(new Long[]{0L, 1L, 3L, 5L, 7L, 11L, 12L, 18L, 29L}, tree.toArrayInOrder());
		assertArrayEquals(new Long[0], new BinaryTree().toArrayInOrder(), "Empty tree must produce empty array");
	}

	@Test
	void testToArrayPreOrder() {
		assertArrayEquals(new Long[]{5L, 0L, 3L, 1L, 29L, 12L, 7L, 11L, 18L}, tree.toArrayPreOrder());
		assertArrayEquals(new Long[0], new BinaryTree().toArrayPreOrder(), "Empty tree must produce empty array");
	}

	@Test
	void testRandomOperations() {
		// takes roughly 25% of the timeout on my laptop on battery; should be more than enough
		assertTimeout(Duration.ofMillis(N_TRIALS / 100), () -> {
			final Random r = new Random(12);
			int size = tree.size();
			for (int i = 0; i < N_TRIALS; i++) {
				final Long l = (long) r.nextInt(BOUND);
				switch (r.nextInt(6)) {
					case 0:
						assertEquals(size, tree.size(), "Inconsistent size");
						break;
					case 1:
						if (tree.find(l) != null) {
							assertNotEquals(tree.isInternal(l), tree.isExternal(l),
									"Node cannot be internal and external at the same time");
						} else {
							assertFalse(tree.isInternal(l), "If element is not contained it cannot be internal");
							assertFalse(tree.isExternal(l), "If element is not contained it cannot be external");
						}
						break;
					case 2:
						if (tree.find(l) == null)
							assertNull(tree.getParent(l), "If Node cannot be count it cannot have a parent");
						else {
							if (tree.isRoot(l)) {
								assertNull(tree.getParent(l), "If Node is root it cannot have a parent");
							} else
								assertNotNull(tree.getParent(l), "If Node is != root it has to have a parent");
						}
						break;
					case 3:
						final boolean insert;
						assertNotEquals(tree.find(l), insert = tree.insert(l),
								"If tree contains element, it cannot be inserted and vice versa");
						if (insert) size++;
						break;
					case 4:
						final boolean remove;
						assertEquals(tree.find(l) != null, remove = tree.remove(l),
								"If tree contains element, it must be removeable and vice versa");
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
			}
		});
	}
}