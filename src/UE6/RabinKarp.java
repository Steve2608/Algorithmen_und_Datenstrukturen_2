package UE6;

public class RabinKarp {

	private static final long BASE = 31;
	private static final long MOD = (2L << 61) - 1; // Mersenne prime

	private RabinKarp() {
	}

	public static Result search(final String needle, final String haystack) throws IllegalArgumentException {
		if (haystack == null) throw new IllegalArgumentException("Cannot search in null text");
		if (needle == null || needle.isEmpty())
			throw new IllegalArgumentException("Cannot search for [null | empty] pattern");

		final BetterResult result = BetterResult.none();
		if (needle.length() > haystack.length()) return result;

		final char[] text = haystack.toCharArray(), pattern = needle.toCharArray();
		final int m = pattern.length, n = text.length;

		final long hash_p = getHashValue(pattern);
		long hashText = getHashValue(haystack.substring(0, needle.length()));

		for (int i = 0; i + m < n; i++) {
			if (hash_p == hashText && needle.equals(haystack.substring(i, i + m))) {
				result.found(i);
			}
			hashText = rollingHash(hashText, m, text, i);
		}

		if (hash_p == hashText && needle.equals(haystack.substring(n - m))) {
			result.found(n - m);
		}
		return result;
	}

	private static long getHashValue(final String string) {
		return getHashValue(string.toCharArray());
	}

	private static long getHashValue(final char[] string) {
		long result = 0;
		for (final char ch : string) {
			result = result * BASE + ch;
		}
		return result % MOD;
	}

	private static long rollingHash(final long old, final int m, final char[] text, final int i) {
		final long hash = (old * BASE - text[i] * pow(m) + text[i + m]) % MOD;
		return (hash + MOD) % MOD;
	}

	private static long pow(long exp) {
		long result = 1;
		while (exp-- > 0) {
			result = result * BASE;
		}
		return result;
	}

}
