package UE6;

public class KMP {

	private static int[] failure;

	private KMP() {
	}

	public static Result search(final String needle, final String haystack) throws IllegalArgumentException {
		if (haystack == null) throw new IllegalArgumentException("Cannot search in null text");
		if (needle == null || needle.length() <= 0)
			throw new IllegalArgumentException("Cannot search in [null | empty] pattern");

		final BetterResult result = BetterResult.none();
		if (needle.length() > haystack.length()) return result;

		final char[] c_text = haystack.toCharArray(), c_pattern = needle.toCharArray();
		final int m = c_pattern.length, n = c_text.length;

		initFailure(c_pattern);
		int i = 0, j = 0;

		while (i < n) {
			if (c_pattern[j] == c_text[i]) {
				if (j == needle.length() - 1) {
					result.found(i - (m - 1));
					i = i - (m - 2);
					j = 0;
				} else {
					i++;
					j++;
				}
			} else if (j > 0) {
				j = failure[j - 1];
			} else {
				i++;
			}
		}

		return result;
	}

	private static void initFailure(final char[] pattern) {
		failure = new int[pattern.length];
		failure[0] = 0;

		for (int j = 1; j < failure.length; j++) {
			for (int i = j; i >= 1; i--) {
				if (prefixEqualsSuffix(pattern, i - 1, j - i + 1, j)) {
					failure[j] = i;
					break;
				}
			}
		}
	}

	private static boolean prefixEqualsSuffix(final char[] pattern, final int end1, final int start2, final int end2) {
		if (end1 < 0 || end1 >= pattern.length) return false;
		if (start2 < 0 || start2 >= pattern.length) return false;
		if (end2 < start2 || end2 >= pattern.length) return false;
		if (end1 != end2 - start2) return false;

		for (int i = 0, j = start2; i <= end1 && j <= end2; i++, j++) {
			if (pattern[i] != pattern[j]) {
				return false;
			}
		}
		return true;
	}

}
