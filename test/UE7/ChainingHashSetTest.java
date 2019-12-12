package UE7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChainingHashSetTest {

	private static final int CAPACITY = 10;

	private final MyHashSet hs = new ChainingHashSet(CAPACITY);

	private boolean insertAsString(final int i) {
		return hs.insert(i, String.valueOf(i));
	}

	@Test
	void testSizeBeforeInsert() {
		assertEquals(0, hs.size(), "Empty ChainingHashSet should have 0 size");
	}

	@Test
	void testSizeNoInserts10Removes() {
		for (int i = 0; i < 10; i++) {
			hs.remove(i);
		}
		assertEquals(0, hs.size(), "Empty ChainingHashSet should have 0 size");
	}

	@Test
	void testSizeAfter5Inserts() {
		for (int i = 0; i < 5; i++) {
			hs.insert(i, String.valueOf(i));
		}
		assertEquals(5, hs.size(), "ChainingHashSet should contain 5 elements");
	}

	@Test
	void testSizeAfter5InsertsAnd2Removes() {
		for (int i = 0; i < 5; i++) {
			insertAsString(i);
		}
		hs.remove(1);
		hs.remove(4);
		assertEquals(3, hs.size(), "ChainingHashSet should contain 3 elements");
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
	void testInsert100DifferentElements() {
		for (int i = 0; i < 100; i++) {
			assertTrue(insertAsString(i), "ChainingHashSet should always be able to add elements");
		}
	}

	@Test
	void testInsertDuplicates() {
		for (int i = 0; i < 100; i++) {
			assertTrue(insertAsString(i), "ChainingHashSet should always be able to add elements");
		}
		for (int i = 0; i < 100; i++) {
			assertFalse(insertAsString(i), "No duplicates shall be added");
		}
	}

	@Test
	void testContainsNullKey() {
		assertThrows(IllegalArgumentException.class, () -> hs.contains(null));
	}

	@Test
	void testContains() {
		for (int i = 0; i < 100; i++) {
			insertAsString(i);
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(hs.contains(i), "Element was not found in ChainingHashSet");
		}
	}

	@Test
	void testNotContains() {
		for (int i = 0; i < 100; i++) {
			insertAsString(i);
		}
		for (int i = 0; i < 200; i++) {
			if (i < 100) assertTrue(hs.contains(i), "Element was not found in ChainingHashSet");
			else assertFalse(hs.contains(i), "Element was wrongfully found in ChainingHashSet");
		}
	}

	@Test
	void testRemoveNullKey() {
		assertThrows(IllegalArgumentException.class, () -> hs.remove(null));
	}

	@Test
	void testRemoveNoElement() {
		for (int i = 0; i < 100; i++) {
			assertFalse(hs.remove(i), "Empty ChainingHashSet cannot remove any elements");
		}
	}

	@Test
	void testRemoveTwice() {
		for (int i = 0; i < 100; i++) {
			insertAsString(i);
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(hs.remove(i), "Element was not removed");
		}
		for (int i = 0; i < 100; i++) {
			assertFalse(hs.remove(i), "Element not in ChainingHashSet cannot be removed");
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

		assertEquals(
				"0 {}, 1 {21}, 2 {}, 3 {3, 23}, 4 {}, 5 {5, 45}, 6 {}, 7 {}, 8 {}, 9 {}",
				hs.toString(),
				"Exact match not necessary - just in case :P");
	}
}