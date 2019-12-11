package UE6;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class BoyerMoore {

	private static final int ASCII_MAX = 128;
	private static final int[] last = new int[ASCII_MAX];
	private static int[] match;

	private BoyerMoore() {
	}

	public static Result search(final String needle, final String haystack) throws IllegalArgumentException {
		if (haystack == null) throw new IllegalArgumentException("Cannot search in null text");
		if (needle == null || needle.isEmpty())
			throw new IllegalArgumentException("Cannot search for [null | empty] pattern");

		final BetterResult result = BetterResult.none();
		if (needle.length() > haystack.length()) return result;

		final char[] text = haystack.toCharArray(), pattern = needle.toCharArray();
		final int m = pattern.length, n = text.length;

		init(pattern);
		int i = m - 1, j = m - 1;

		do {
			if (pattern[j] == text[i]) {
				if (j == 0) {
					result.found(i);
					i += m;
					j = m - 1;
				} else {
					i--;
					j--;
				}
			} else {
				i += m - j - 1;
				i += max(j - last(text[i]), match(j));
				j = m - 1;
			}
		} while (i <= n - 1);

		return result;
	}

	private static void init(final char[] pattern) {
		initLast(pattern);
		initMatch(pattern);
	}

	private static void initLast(final char[] pattern) {
		for (char i = 0; i < last.length; i++) {
			int j = pattern.length - 1;
			while (j >= 0 && pattern[j] != i) j--;
			last[i] = j;
		}
	}

	private static int last(final char ch) {
		return ch < ASCII_MAX ? last[ch] : -1;
	}

	private static void initMatch(final char[] pattern) {
		match = new int[pattern.length];
		for (int i = 0; i < pattern.length; i++) {
			match[i] = getLongestSuffix(pattern, i);
		}
	}

	private static int getLongestSuffix(final char[] pattern, final int i) {
		final int m = pattern.length;

		int result = m;
		// Fall 1: P[i+1-s .. m-s-1] ist Suffix von P[i+1 .. m-1] und P[i] != P[i-s] mit s <= i
		for (int s = 1; s <= i + 1; s++) {
			if (s <= i && pattern[i] != pattern[i - s]) {
				if (i == m - 1 && s <= i && pattern[i] != pattern[i - s] ||
						isSuffix(pattern, i + 1 - s, m - 1 - s, i + 1, m - 1)) {
					result = min(s, result);
					break;
				}
			}
		}
		// Fall 2: P[0...m-s-1] Suffix von P[i+1 .. m-1] (i<s<m)
		for (int s = 1; s < result; s++) {
			if (i < s && s < m && isSuffix(pattern, 0, m - s - 1, i + 1, m - 1)) {
				return min(s, result);
			}
		}
		// Fall 3
		return result;
	}

	private static int match(final int j) {
		return j >= 0 && j < match.length ? match[j] : -1;
	}

	private static boolean isSuffix(final char[] pattern, final int needle_start, final int needle_end, final int haystack_start, final int haystack_end) {
		if (needle_start < 0 || needle_start >= pattern.length) return false;
		if (needle_end < needle_start || needle_end >= pattern.length) return false;
		if (haystack_start < 0 || haystack_start >= pattern.length) return false;
		if (haystack_end < haystack_start || haystack_end >= pattern.length) return false;
		if (needle_end - needle_start != haystack_end - haystack_start) return false;

		for (int i = needle_end, j = haystack_end; i >= needle_start && j >= haystack_start; i--, j--) {
			if (pattern[i] != pattern[j]) {
				return false;
			}
		}
		return true;
	}

}
