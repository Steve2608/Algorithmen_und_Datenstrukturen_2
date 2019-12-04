package UE2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

	private MyList<Long> list;

	@BeforeEach
	void setUp() {
		list = new MyLinkedList<>();
		list.addFirst(4L);
		list.addFirst(2L);
		list.addFirst(6L);
	}

	@Test
	void testSize() {
		assertEquals(3, list.size());
		list.clear();
		assertEquals(0, list.size());

		list.addLast(3L);
		assertEquals(1, list.size());
	}

	@Test
	void testAddFirst() {
		assertThrows(IllegalArgumentException.class, () -> list.addFirst(null));

		assertEquals(6L, list.get(0));
		list.addFirst(20L);
		assertEquals(20L, list.get(0));
	}

	@Test
	void testAddLast() {
		assertThrows(IllegalArgumentException.class, () -> list.addLast(null));

		assertEquals(4L, list.get(list.size() - 1));
		list.addLast(20L);
		assertEquals(20L, list.get(list.size() - 1));
	}

	@Test
	void testAddSorted() {
		assertThrows(IllegalArgumentException.class, () -> list.addSorted(null));

		list.addSorted(8L);
		for (int i = 0; i < list.size() - 1; i++) {
			assertTrue(list.get(i) <= list.get(i + 1));
		}
	}

	@Test
	void testSortASC() {
		list.addFirst(1234L);
		list.addLast(34L);
		list.addLast(12L);
		list.addFirst(1L);

		list.sortASC();
		for (int i = 0; i < list.size() - 1; i++) {
			assertTrue(list.get(i) <= list.get(i + 1));
		}
	}

	@Test
	void testSortDES() {
		list.addFirst(1234L);
		list.addLast(34L);
		list.addLast(12L);
		list.addFirst(1L);

		list.sortDES();
		for (int i = 0; i < list.size() - 1; i++) {
			assertTrue(list.get(i) >= list.get(i + 1));
		}
	}

	@Test
	void testClear() {
		assertEquals(3, list.size());
		list.clear();

		assertEquals(0, list.size());
		try {
			assertNull(list.get(0));
		} catch (final IndexOutOfBoundsException e) {
			// either return null or throw IOOB Exception
		}
	}

	@Test
	void testRemoveFirst() {
		assertEquals(6L, list.removeFirst());
		assertEquals(2, list.size());

		list.addFirst(5L);

		assertEquals(5L, list.removeFirst());
		assertEquals(2, list.size());

		assertEquals(2L, list.removeFirst());
		assertEquals(1, list.size());

		assertEquals(4L, list.removeFirst());
		assertEquals(0, list.size());
	}

	@Test
	void testRemoveLast() {
		assertEquals(4L, list.removeLast());
		assertEquals(2, list.size());

		list.addFirst(5L);

		assertEquals(2L, list.removeLast());
		assertEquals(2, list.size());

		assertEquals(6L, list.removeLast());
		assertEquals(1, list.size());

		assertEquals(5L, list.removeLast());
		assertEquals(0, list.size());
	}

	@Test
	void testGetFirst() {
		assertEquals(6L, list.getFirst());

		list.addFirst(5L);
		assertEquals(5L, list.getFirst());
	}

	@Test
	void testGetLast() {
		assertEquals(4L, list.getLast());

		list.addLast(5L);
		assertEquals(5L, list.getLast());
	}

	@Test
	void testContains() {
		assertThrows(IllegalArgumentException.class, () -> list.contains(null));

		assertTrue(list.contains(4L));
		assertFalse(list.contains(5L));
	}

	@Test
	void testGet() {
		assertEquals(6L, list.get(0));
		assertEquals(2L, list.get(1));
		assertEquals(4L, list.get(2));
	}

	@Test
	void testRemove() {
		assertEquals(2L, list.remove(1));
		assertEquals(4L, list.remove(1));
		assertEquals(6L, list.remove(0));
	}

	@Test
	void testToArray() {
		assertArrayEquals(new Long[]{6L, 2L, 4L}, list.toArray());

		list.clear();
		assertArrayEquals(new Long[]{}, list.toArray());
	}

	@Test
	void testManyElems() {
		assertTimeout(Duration.ofMillis(5000), () -> {
			for (int i = 0; i < 10_000; i++) {
				list.addSorted((long) (Math.random() * 1_000_000_000));
			}
		});

		// Indexed access is the slowest operation basically
		for (int i = 0; i < list.size() - 1; i++)
			assertTrue(list.get(i) <= list.get(i + 1));
	}

}