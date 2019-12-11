package UE6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Utils {

	static final String HAYSTACK_ASSIGNMENT = "abcdexxxunbxxxxke";
	static final String NEEDLE_ASSIGNMENT = "xxx";

	static final String HAYSTACK_LOREM_IPSUM = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
	static final String NEEDLE_LOREM_IPSUM = "et";

	static final String HAYSTACK_FAUST;
	static final String NEEDLE_FAUST = "Pudels Kern";

	static {
		String temp;
		try {
			// TODO replace for Java 8
			temp = Files.readString(Paths.get("test/UE6/Faust.txt"));
		} catch (final IOException e) {
			e.printStackTrace();
			temp = "Dann halt nicht.";
		}
		HAYSTACK_FAUST = temp;
	}

	private Utils() {
	}

	static Result initResult(final int count, final int... vars) {
		return new Result(vars, count);
	}
}
