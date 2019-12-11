package UE6;

import java.util.Arrays;

public class Result {

	int[] resultIndices;
	int count;

	public Result(final int[] resultIndices, final int count) {
		this.resultIndices = resultIndices.clone();
		this.count = count;
	}
}

class BetterResult extends Result {

	public BetterResult(final int[] resultIndices, final int count) {
		super(resultIndices, count);
	}

	private BetterResult() {
		this(new int[0], 0);
	}

	public static BetterResult none() {
		return new BetterResult();
	}

	public void found(final int index) {
		final int len = resultIndices.length;
		resultIndices = Arrays.copyOf(resultIndices, len + 1);
		resultIndices[len] = index;
		count++;
	}

	@Override
	public String toString() {
		return String.format("Result{resultIndices=%s, count=%d}", Arrays.toString(resultIndices), count);
	}
}
