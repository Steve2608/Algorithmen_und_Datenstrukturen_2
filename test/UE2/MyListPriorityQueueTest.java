package UE2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyListPriorityQueueTest {

	private MyPriorityQueue<Long> queue;

	@BeforeEach
	void setUp() {
		queue = new MyListPriorityQueue<>();
		queue.insert(11L);
		queue.insert(7L);
		queue.insert(9L);
	}

	@Test
	void testIsEmpty() {
		assertFalse(queue.isEmpty());

		queue.removeMin();
		assertFalse(queue.isEmpty());
		queue.removeMin();
		assertFalse(queue.isEmpty());
		queue.removeMin();

		assertTrue(queue.isEmpty());
	}

	@Test
	void testSize() {
		assertEquals(3, queue.size());
		queue.removeMin();
		assertEquals(2, queue.size());
		queue.removeMin();
		assertEquals(1, queue.size());
		queue.removeMin();
		assertEquals(0, queue.size());
	}

	@Test
	void testInsert() {
		queue.insert(15L);
		assertArrayEquals(new Long[]{7L, 9L, 11L, 15L}, queue.toArray());

		queue.insert(2L);
		assertArrayEquals(new Long[]{2L, 7L, 9L, 11L, 15L}, queue.toArray());

		queue.insert(10L);
		assertArrayEquals(new Long[]{2L, 7L, 9L, 10L, 11L, 15L}, queue.toArray());

	}

	@Test
	void testRemoveMin() {
		assertEquals(7L, queue.removeMin());
		assertEquals(9L, queue.removeMin());
		assertEquals(11L, queue.removeMin());

		assertThrows(NoSuchElementException.class, () -> queue.removeMin());
	}

	@Test
	void testMin() {
		assertEquals(7L, queue.min());

		queue.removeMin();
		queue.removeMin();
		queue.removeMin();

		assertThrows(NoSuchElementException.class, () -> queue.min());
	}

	@Test
	void testToArray() {
		assertArrayEquals(new Long[]{7L, 9L, 11L}, queue.toArray());

		queue.removeMin();
		queue.removeMin();
		queue.removeMin();

		assertArrayEquals(new Long[]{}, queue.toArray());
	}
}