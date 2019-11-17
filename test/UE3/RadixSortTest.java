package UE3;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RadixSortTest {

	private static final Integer[] a = {4, 6, 9, 132, 12313, 453};
	private static final Integer[] b, c, d;

    static {
        b = Arrays.copyOf(a, a.length);
        Arrays.sort(b);

        final Random r = new Random(41);
        final MyLinkedList l = new MyLinkedList();
        for (int i = 0; i < 10_000; i++) {
            l.add(r.nextInt(1_000_000));
        }
        c = listToArray(l);
        d = Arrays.copyOf(c, c.length);
        Arrays.sort(d);
    }

    private static Integer[] listToArray(final MyLinkedList mll) {
        final List<Integer> list = new LinkedList<>();
        for (MyNode n = mll.head; n != null; n = n.next) {
            list.add(n.value);
        }
        return list.toArray(new Integer[0]);
    }

    @Test
    void testTestExceptions() {
        assertThrows(IllegalArgumentException.class, () -> RadixSort.sort(null));
        assertThrows(IllegalArgumentException.class, () -> RadixSort.sort(new Integer[]{5, null}));
    }

    @Test
    void testLink() {
        final MyLinkedList l1 = new MyLinkedList(), l2 = new MyLinkedList();
        l1.add(3);
        l1.add(7);
        l2.add(5);
        l2.add(2);

        l1.link(l2);
        assertEquals(3, l1.head.value);
        assertEquals(5, l2.head.value);
        assertEquals(2, l1.tail.value);
        assertEquals(2, l2.tail.value);

        final MyLinkedList l3 = new MyLinkedList();
        l3.link(l2);
        assertEquals(5, l2.head.value);
        assertEquals(5, l3.head.value);
        assertEquals(2, l2.tail.value);
        assertEquals(2, l3.tail.value);
    }

    @Test
    void testSort() {
        assertArrayEquals(b, listToArray(RadixSort.sort(a)));
        assertArrayEquals(d, listToArray(RadixSort.sort(c)));
    }
}