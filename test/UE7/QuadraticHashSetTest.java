package UE7;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Stefan
 * @author CyberFreak44
 */
public class QuadraticHashSetTest {

	private static final int CAPACITY = 10;

	private final MyHashSet hs = new QuadraticHashSet(CAPACITY);

	private boolean insertAsString(final int i) {
		return hs.insert(i, String.valueOf(i));
	}

	@Test
	void sizeBeforeInsert() {
		assertEquals(0, hs.size(), "Empty QuadraticHashSet should have 0 size");
	}

	@Test
	void testSizeNoInserts10Removes() {
		for (int i = 0; i < 10; i++) {
			hs.remove(i);
		}
		assertEquals(0, hs.size(), "Empty QuadraticHashSet should have 0 size");
	}

	@Test
	void testSizeAfter5Inserts() {
		for (int i = 0; i < 5; i++) {
			hs.insert(i, String.valueOf(i));
		}
		assertEquals(5, hs.size(), "QuadraticHashSet should contain 5 elements");
	}

	@Test
	void testSizeAfter5InsertsAnd2Removes() {
		for (int i = 0; i < 5; i++) {
			insertAsString(i);
		}
		hs.remove(1);
		hs.remove(4);
		assertEquals(3, hs.size(), "QuadraticHashSet should contain 3 elements");
	}

	@Test
	void testInsertNullKey() {
		assertThrows(IllegalArgumentException.class, () -> hs.insert(null, ""));
	}

	@Test
	void testInsertNullData() {
		assertThrows(IllegalArgumentException.class, () -> hs.insert(-1, null));
	}

	@Test
	void testInsert10DifferentElements() {
		for (int i = 0; i < 10; i++) {
			assertTrue(insertAsString(i), "QuadraticHashSet should be able to add elements");
		}
	}

	@Test
	void testInsertTooManyDifferentElements() {
		for (int i = 0; i < 10; i++) {
			assertTrue(insertAsString(i), "QuadraticHashSet should be able to add elements");
		}
		for (int i = 10; i < 20; i++) {
			assertFalse(insertAsString(i),
					String.format("QuadraticHashSet cannot add duplicate index: %d", i % CAPACITY));
		}
	}

	@Test
	void testInsertDuplicates() {
		for (int i = 0; i < 10; i++) {
			assertTrue(insertAsString(i), "QuadraticHashSet should be able to add elements");
		}
		for (int i = 0; i < 10; i++) {
			assertFalse(insertAsString(i), "No duplicates shall be added");
		}
	}

	@Test
	void testContainsNullKey() {
		assertThrows(IllegalArgumentException.class, () -> hs.contains(null));
	}

	@Test
	void testContains() {
		for (int i = 0; i < 10; i++) {
			insertAsString(i);
		}
		for (int i = 0; i < 10; i++) {
			assertTrue(hs.contains(i), "Element was not found in QuadraticHashSet");
		}
	}

	@Test
	void testNotContains() {
		for (int i = 0; i < 10; i++) {
			insertAsString(i);
		}
		for (int i = 0; i < 20; i++) {
			if (i < 10) assertTrue(hs.contains(i), "Element was not found in QuadraticHashSet");
			else assertFalse(hs.contains(i), "Element was wrongfully found in QuadraticHashSet");
		}
	}

	@Test
	void testRemoveNullKey() {
		assertThrows(IllegalArgumentException.class, () -> hs.remove(null));
	}

	@Test
	void testRemoveNoElement() {
		for (int i = 0; i < 100; i++) {
			assertFalse(hs.remove(i), "Empty QuadraticHashSet cannot remove any elements");
		}
	}

	@Test
	void testRemoveTwice() {
		for (int i = 0; i < 10; i++) {
			insertAsString(i);
		}
		for (int i = 0; i < 10; i++) {
			assertTrue(hs.remove(i), "Element was not removed");
		}
		for (int i = 0; i < 10; i++) {
			assertFalse(hs.remove(i), "Element not in QuadraticHashSet cannot be removed");
		}
	}

	@Test
	void testSizeAfterClear() {
		for (int i = 0; i < 5; i++) {
			insertAsString(i);
		}
		hs.clear();
		assertEquals(0, hs.size(), "Size was not 0 after clear");
	}

	@Test
	void testRandomOperationsNoException() {
		int size = 0;
		for (int i = 0; i < 10000; i++) {
			final int choice = (int) (Math.random() * 5);
			switch (choice) {
				case 0:
					if (insertAsString((int) (Math.random() * 10))) size++;
					break;
				case 1:
					if (hs.remove((int) (Math.random() * 10))) size--;
					break;
				case 2:
					assertEquals(size, hs.size(), "Size was calculated incorrectly");
					break;
				case 3:
					hs.contains((int) (Math.random() * 10));
					break;
				case 4:
					hs.clear();
					size = 0;
					assertEquals(size, hs.size(), "Size has to be 0 after clear");
					break;
			}
		}
	}

	@Test
	void testToString() {
		insertAsString(3);
		insertAsString(23);
		insertAsString(21);
		insertAsString(5);
		insertAsString(45);
		insertAsString(5);
		insertAsString(58);
		insertAsString(38);
		insertAsString(18);

		assertEquals(
				"0 {}, 1 {21}, 2 {}, 3 {3}, 4 {23}, 5 {5}, 6 {45}, 7 {18}, 8 {58}, 9 {38}",
				hs.toString(),
				"Exact match not necessary - just in case :P");
	}

	@Test
	void testInsertWithoutFullUsage() {
		for (int i = 0; i < CAPACITY; i++) {
			if (i < 6)
				assertTrue(insertAsString(i * CAPACITY + 4), "It should be possible to add up to 6 of these keys");
			else
				assertFalse(insertAsString(i * CAPACITY + 4), "Only 6 of these keys should be possible to be stored");
		}
		assertEquals(6, hs.size(), "There should be 6 keys stored");
	}

	@Test
	void testInsertTerminateOnFull() {
		for (int i = 0; i < CAPACITY; i++) {
			insertAsString(i);
		}
		assertTimeoutPreemptively(Duration.ofMillis(100), () -> insertAsString(13),
				"The method should eventually terminate.");
	}

	@Test
	void testNotContainTerminateOnFull() {
		for (int i = 0; i < CAPACITY; i++) {
			insertAsString(i);
		}
		assertTimeoutPreemptively(Duration.ofMillis(100), () -> hs.contains(13),
				"The method should eventually terminate.");
	}

	@Test
	void testCantRemoveTerminateOnFull() {
		for (int i = 0; i < CAPACITY; i++) {
			insertAsString(i);
		}
		assertTimeoutPreemptively(Duration.ofMillis(100), () -> hs.remove(13),
				"The method should eventually terminate.");
	}

	@Test
	void testRemoveWithMultiProbing() {
		for (int i = 0; i < CAPACITY; i++) {
			insertAsString(i * 10);
		}
		for (int i = 0; i < CAPACITY; i++) {
			if (i < 6) assertTrue(hs.remove(i * 10), "This key should be present.");
			else assertFalse(hs.remove(i * 10), "This key shouldn't be present.");
		}
	}

	@Test
	void testInsertNegativeKey() {
		for (int i = 0; i < CAPACITY; i++) {
			assertTrue(insertAsString(-i), "Negative keys also should be treated right.");
		}
	}

	@Test
	void testContainNegativeKey() {
		for (int i = 0; i < CAPACITY; i++) {
			insertAsString(-i);
		}
		assertTrue(hs.contains(-4), "Key not found.");
	}

	@Test
	void removeRemoveNegativeKey() {
		for (int i = 0; i < CAPACITY; i++) {
			insertAsString(-i);
		}
		for (int i = 0; i < CAPACITY; i++) {
			assertTrue(hs.remove(-i), "Key not found.");
		}
	}
}

