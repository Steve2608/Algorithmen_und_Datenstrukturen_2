package UE3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    /**
     * if `true` `vorlesung` checks for the exact array presented in the slides.
     * Only validity of heap-structure will be tested otherwise.
     */
    private static final boolean MATCH_LECTURE_EXACTLY = false;

    private MinHeap<Long> heap = new MinHeap<>(3);

    @BeforeEach
    void setUp() {
        heap.insert(11L);
        heap.insert(7L);
        heap.insert(9L);
    }

    @AfterEach
    void tearDown() {
        heap = new MinHeap<>(3);
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
        System.out.println(heap);
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

    @Test
    void contains() {
        assertTrue(heap.contains(9L));
        assertTrue(heap.contains(11L));
        assertTrue(heap.contains(7L));

        assertFalse(heap.contains(3L));
        heap.insert(3L);
        assertTrue(heap.contains(3L));

        assertThrows(IllegalArgumentException.class, () -> heap.contains(null));
    }

    @Test
    void constructor() {
        assertThrows(IllegalArgumentException.class, () -> new MinHeap<>(null));
        assertThrows(IllegalArgumentException.class, () -> new MinHeap<>(new Long[]{1L, 2L, null}));

        final Long[] longs = {1L, 7L, 9L, 2L, 5L, 5L};
        heap = new MinHeap<>(longs);

        // checking if inplace
        for (int i = 0; i < heap.size(); i++) {
            assertSame(heap.get(i), longs[i]);
        }

        // checking if valid heap
        final int error = findErrorInHeap(longs);
        assertEquals(-1, error, String.format("Heap structure violated at index %d\n%s", error, heap.toString()));
    }

    private int findErrorInHeap(final Long[] heap) {
        for (int i = 0; i < heap.length / 2 - 1; i++) {
            if (heap[i].compareTo(heap[leftChild(heap, i)]) > 0) return i;
            if (heap[i].compareTo(heap[rightChild(heap, i)]) > 0) return i;
        }
        return -1;
    }

    private int leftChild(final Long[] heap, final int index) {
        final int left = index * 2 + 1;
        return 0 <= index && left < heap.length ? left : -1;
    }

    private int rightChild(final Long[] heap, final int index) {
        final int right = (index + 1) * 2;
        return 0 <= index && right < heap.length ? right : -1;
    }

    @Test
    void vorlesungsFolien() {
        final Long[] longs = {16L, 5L, 4L, 12L, 6L, 7L, 23L, 20L, 25L, 5L, 11L, 27L, 9L, 8L};
        final MinHeap<Long> heap = new MinHeap<>(longs);

        // checking if valid heap
        final int error = findErrorInHeap(longs);
        assertEquals(-1, error, String.format("Heap structure violated at index %d\n%s", error, heap.toString()));

        if (MATCH_LECTURE_EXACTLY) {
            final Long[] lecture = {4L, 5L, 5L, 8L, 12L, 6L, 11L, 16L, 20L, 25L, 27L, 9L, 9L, 23L};
            for (int i = 0; i < longs.length; i++) {
                if (!longs[i].equals(lecture[i])) {
                    fail(String.format("Arrays differed at index <%d>: %s != %s\n", i, longs[i].toString(), lecture[i].toString()));
                }
            }
        }
    }

    @Test
    void sort() {
        assertThrows(IllegalArgumentException.class, () -> MinHeap.sort(null));
        assertThrows(IllegalArgumentException.class, () -> MinHeap.sort(new Long[]{1L, 2L, null}));

        final Long l1 = 1L, l7 = 7L, l9 = 9L, l2 = 2L, l5 = 5L;
        final Long[] sorted = {l9, l7, l5, l2, l1};
        final Long[] toSort = {l1, l7, l9, l2, l5};
        MinHeap.sort(toSort);

        for (int i = 0; i < sorted.length; i++) {
            assertSame(sorted[i], toSort[i]);
        }
    }
}