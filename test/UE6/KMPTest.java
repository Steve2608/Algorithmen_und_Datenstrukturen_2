package UE6;

import org.junit.jupiter.api.Test;

import static UE6.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class KMPTest {

	@Test
	public void search() {
		final Result expected = initResult(3, 5, 11, 12);
		final Result actual = KMP.search(NEEDLE_ASSIGNMENT, HAYSTACK_ASSIGNMENT);
		assertEquals(expected.count, actual.count, "Number of matches differed");
		assertArrayEquals(expected.resultIndices, actual.resultIndices, "Match indices differed");
	}

	@Test
	public void searchSelf() {
		final Result expected = initResult(1, 0);
		final Result actual = KMP.search(NEEDLE_ASSIGNMENT, NEEDLE_ASSIGNMENT);
		assertEquals(expected.count, actual.count, "Number of matches differed");
		assertArrayEquals(expected.resultIndices, actual.resultIndices, "Match indices differed");
	}

	@Test
	public void searchLoremIpsum() {
		final Result expected = initResult(18, 24, 32, 34, 106, 168, 179, 200, 215, 292, 320, 328, 330, 402, 464, 475, 496, 511, 588);
		final Result actual = KMP.search(NEEDLE_LOREM_IPSUM, HAYSTACK_LOREM_IPSUM);
		assertEquals(expected.count, actual.count, "Number of matches differed");
		assertArrayEquals(expected.resultIndices, actual.resultIndices, "Match indices differed");
	}

	@Test
	public void searchFaust() {
		final Result expected = initResult(1, 67200);
		final Result actual = KMP.search(NEEDLE_FAUST, HAYSTACK_FAUST);
		assertEquals(expected.count, actual.count, "Number of matches differed");
		assertArrayEquals(expected.resultIndices, actual.resultIndices, "Match indices differed");
	}

	@Test
	public void searchNoMatches() {
		final Result expected = initResult(0);
		final Result actual = KMP.search("bla", "blub");
		assertEquals(expected.count, actual.count, "Number of matches differed");
		assertArrayEquals(expected.resultIndices, actual.resultIndices, "Match indices differed");
	}

	@Test
	public void searchNullText() {
		assertThrows(IllegalArgumentException.class, () -> KMP.search(NEEDLE_ASSIGNMENT, null),
				"IllegalArgumentException must be thrown on null haystack");
	}

	@Test
	public void searchNullPattern() {
		assertThrows(IllegalArgumentException.class, () -> KMP.search(null, HAYSTACK_ASSIGNMENT),
				"IllegalArgumentException must be thrown on null needle");
	}

	@Test
	public void searchEmptyPattern() {
		assertThrows(IllegalArgumentException.class, () -> KMP.search("", HAYSTACK_ASSIGNMENT),
				"IllegalArgumentException must be thrown on empty haystack");
	}

}