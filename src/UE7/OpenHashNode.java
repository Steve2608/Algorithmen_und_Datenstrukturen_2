package UE7;

public class OpenHashNode {
	Integer key;
	String data;
	boolean removed = false;

	OpenHashNode(final Integer key, final String data) {
		this.key = key;
		this.data = data;
	}
}
