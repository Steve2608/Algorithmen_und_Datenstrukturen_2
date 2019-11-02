package UE2;

public class MyListNode<T> {

	private final T element;
	private MyListNode<T> next;

	MyListNode() {
		this(null, null);
	}

	MyListNode(final T element) {
		this(element, null);
	}

	MyListNode(final T element, final MyListNode<T> next) {
		this.element = element;
		this.next = next;
	}

	T getElement() {
		return element;
	}

	MyListNode<T> getNext() {
		return next;
	}

	void setNext(final MyListNode<T> next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return String.valueOf(element);
	}
}