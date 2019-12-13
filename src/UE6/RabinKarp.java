package UE6;

public class RabinKarp {

	private static final int BASE = 31;
	private static final int MOD = 2 << 30;

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

		final int hash_p = getHashValue(pattern);
		int hashText = getHashValue(haystack.substring(0, needle.length()));

		for (int i = 0; i + m < n; i++) {
			if (hash_p == hashText && needle.equals(haystack.substring(i, i + m))) {
				result.found(i);
			}
			hashText = rollingHash(hashText, m, text[i], text[i + m]);
		}

		if (hash_p == hashText && needle.equals(haystack.substring(n - m))) {
			result.found(n - m);
		}
		return result;
	}

	private static int getHashValue(final String string) {
		return getHashValue(string.toCharArray());
	}

	private static int getHashValue(final char[] string) {
		int result = 0;
		for (final char ch : string) {
			result = (result * BASE + ch) % MOD;
		}
		if (result < 0) return result + MOD;
		return result;
	}

	private static int rollingHash(final int old, final int m, final char oldLetter, final char newLetter) {
		final int hash = (old * BASE - oldLetter * pow(m) + newLetter) % MOD;

		if (hash < 0) return hash + MOD;
		return hash;
	}

	private static int pow(int exp) {
		int result = 1;
		while (exp-- > 0) {
			result = result * BASE;
		}
		assert result > 0;
		return result;
	}

}
