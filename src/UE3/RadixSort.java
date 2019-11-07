
package UE3;


import java.util.Arrays;

public class RadixSort {

    private static final MyLinkedList mergeList = new MyLinkedList();
    private static MyLinkedList[] buckets;

    private static int BASE = 10;

    public static MyLinkedList sort(final Integer[] list) {
        if (list == null) throw new IllegalArgumentException("List must not be null!");
        for (final Integer i : list) {
            if (i == null) throw new IllegalArgumentException("Values must not be null!");
            if (i < 0)
                throw new IllegalArgumentException("Negative values cannot be handled properly!");
        }

        initialise(list);
        final int noSorts = noSorts(list);
        for (int i = 0; i < noSorts; i++) {
            for (MyNode node = mergeList.head; node != null; node = node.next) {
                final Integer val = node.value;
                buckets[getBucketIndex(val, i)].add(val);
            }
            nextIter();
        }
        return mergeList;
    }

    private static int noSorts(final Integer[] list) {
        int n = 0;
        for (int curr = getMax(list); curr > 0; curr /= getBase()) {
            n++;
        }
        return n;
    }

    private static int getBucketIndex(final int val, final int exponent) {
        return (int) (val / Math.pow(getBase(), exponent) % getBase());
    }

    private static void nextIter() {
        mergeList.clear();
        for (final MyLinkedList bucket : buckets) {
            mergeList.link(bucket);
        }
        resetBuckets();
    }

    private static void resetBuckets() {
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] == null) buckets[i] = new MyLinkedList();
            else buckets[i].clear();
        }
    }

    private static void initialise(final Integer[] list) {
        buckets = new MyLinkedList[getBase()];
        resetBuckets();
        mergeList.clear();
        for (final Integer i : list) {
            mergeList.add(i);
        }
    }

    private static int getMax(final Integer[] list) {
        return Arrays.stream(list).reduce(Math::max).orElseThrow(
                () -> new IllegalArgumentException("No max element present"));
    }

    public static int getBase() {
        return BASE;
    }

    public static int setBase(final int base) {
        if (base > 1) BASE = base;
        return BASE;
    }

}
