package UE6;

public class RabinKarp {

	private static final long BASE = 31;
	private static final long MOD = (2L << 61) - 1; // Mersenne prime

	private RabinKarp() {
	}

	public static Result search(final String s_pattern, final String s_text) throws IllegalArgumentException {
		if (s_text == null) throw new IllegalArgumentException("Cannot search in null text");
		if (s_pattern == null || s_pattern.length() <= 0)
			throw new IllegalArgumentException("Cannot search in [null | empty] pattern");

		final BetterResult result = BetterResult.none();
		if (s_pattern.length() > s_text.length()) return result;

		final char[] c_text = s_text.toCharArray(), c_pattern = s_pattern.toCharArray();
		final int m = c_pattern.length, n = c_text.length;

		final long hash_p = getHashValue(c_pattern);
		long hash_t = getHashValue(s_text.substring(0, s_pattern.length()));

		for (int i = 0; i + m < n; i++) {
			if (hash_p == hash_t && s_pattern.equals(s_text.substring(i, i + m))) {
				result.found(i);
			}
			hash_t = rollingHash(hash_t, m, c_text, i);
		}

		if (hash_p == hash_t && s_pattern.equals(s_text.substring(n - m))) {
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
