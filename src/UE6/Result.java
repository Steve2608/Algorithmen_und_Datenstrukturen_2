package UE6;

public class Result {

	int[] resultIndices;
	int count;

	public Result(final int[] resultIndices, final int count) {
		this.resultIndices = resultIndices.clone();
		this.count = count;
	}
}

class BetterResult extends Result {

	BetterResult(final int[] resultIndices, final int count) {
		super(resultIndices, count);
	}

	private BetterResult() {
		this(new int[0], 0);
	}

	static BetterResult none() {
		return new BetterResult();
	}

	void found(final int index) {
		final int[] newResults = new int[resultIndices.length + 1];
		System.arraycopy(resultIndices, 0, newResults, 0, resultIndices.length);
		newResults[resultIndices.length] = index;

		resultIndices = newResults;
		count++;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("[");
		for (final int resultIndex : resultIndices) {
			sb.append(resultIndex).append(", ");
		}
		sb.replace(sb.length() - 2, sb.length(), "]");

		return String.format("Result{resultIndices=%s, count=%d}", sb, count);
	}
}
