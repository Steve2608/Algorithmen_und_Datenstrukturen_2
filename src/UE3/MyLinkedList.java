package UE3;

public class MyLinkedList {
	MyNode head, tail;

	public void clear() {
		head = tail = null;
	}

	public void add(final Integer val) {
		if (head == null) {
			head = tail = new MyNode(val);
		} else {
			final MyNode tmp = new MyNode(val);
			tail.next = tmp;
			tail = tmp;
		}
	}

	public void link(final MyLinkedList list) {
		if (list == null || list.head == null) return;

		if (head == null) head = list.head;
		else tail.next = list.head;

		tail = list.tail;
	}

}
