package UE2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

	private MyPriorityQueue<Long> heap;

	@BeforeEach
	void setUp() {
		heap = new MinHeap<>(3);
		heap.insert(11L);
		heap.insert(7L);
		heap.insert(9L);
	}

	@Test
	void isEmpty() {
		assertFalse(heap.isEmpty());

		heap.removeMin();
		assertFalse(heap.isEmpty());
		heap.removeMin();
		assertFalse(heap.isEmpty());
		heap.removeMin();

		assertTrue(heap.isEmpty());
	}

	@Test
	void size() {
		assertEquals(3, heap.size());
		heap.removeMin();
		assertEquals(2, heap.size());
		heap.removeMin();
		assertEquals(1, heap.size());
		heap.removeMin();
		assertEquals(0, heap.size());
	}

	@Test
	void insert() {
		heap.insert(15L);
		assertArrayEquals(new Long[]{7L, 11L, 9L, 15L}, heap.toArray());

		heap.insert(2L);
		assertArrayEquals(new Long[]{2L, 7L, 9L, 15L, 11L}, heap.toArray());

		heap.insert(10L);
		assertArrayEquals(new Long[]{2L, 7L, 9L, 15L, 11L, 10L}, heap.toArray());
	}

	@Test
	void removeMin() {
		assertEquals(7L, heap.removeMin());
		assertEquals(9L, heap.removeMin());
		assertEquals(11L, heap.removeMin());

		assertThrows(NoSuchElementException.class, () -> heap.removeMin());
	}

	@Test
	void min() {
		assertEquals(7L, heap.min());

		heap.removeMin();
		heap.removeMin();
		heap.removeMin();

		assertThrows(NoSuchElementException.class, () -> heap.min());
	}

	@Test
	void toArray() {
		assertArrayEquals(new Long[]{7L, 11L, 9L}, heap.toArray());

		heap.removeMin();
		heap.removeMin();
		heap.removeMin();

		assertArrayEquals(new Long[0], heap.toArray());
	}
}