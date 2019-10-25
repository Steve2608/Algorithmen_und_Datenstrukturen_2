package UE2;

public class MyListNode<FuckingAngabe> {

	private Long element;
	private MyListNode next;

	public MyListNode() {
		element = null;
		next = null;
	}

	public MyListNode(final Long element) {
		this.element = element;
		next = null;
	}

	public Long getElement() {
		return element;
	}

	public void setElement(final Long element) {
		this.element = element;
	}

	public MyListNode getNext() {
		return next;
	}

	public void setNext(final MyListNode next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return String.valueOf(element);
	}
}