package assignment_2.structures.linear;

import java.util.NoSuchElementException;

/**
 * 
 * @author Yue
 * 
 * @param <E>
 */
public class CircularLinkedList<E> extends SinglyLinkedList<E> {

	protected transient Element<E> tail;

	@Override
	public void addFirst(E val) {
		if (size < 2) {
			head = new Element<E>(val, head);
		} else {
			tail = getTailElement();
			head = new Element<E>(val, head);
			tail.next = head;
		}
		size++;
	}

	@Override
	public E removeFirst() {
		E val = null;
		if (size == 0) {
			throw new NoSuchElementException();
		} else if (size == 1) {
			val = head.value;
			head = null;
			tail = null;
			size--;
		} else {
			val = head.value;
			head = head.next;
			if (tail != null && tail.next != null) {
				tail.next = head;
			}
			size--;
		}
		return val;
	}

	public boolean isLast(E val) {
		return tail.value.equals(val);
	}

	@Override
	public void addLast(E val) {
		Element<E> recentLast = new Element<E>(val, head);
		if (tail != null) {
			tail.next = recentLast;
			tail = tail.next;
		} else {
			tail = recentLast;
			head = tail;
		}
		size++;
	}

	@Override
	public String toString() {
		String string = "[ ";
		Element<E> finger = head;
		int index = 0;
		while (index++ < size - 1 && finger != null && finger.next != null) {
			string += finger.value + ", ";
			finger = finger.next;
		}
		if (head != null) {
			string += finger.value + " ]";
		} else {
			string += "]";
		}
		return string;
	}

	public static void main(String[] args) {
		CircularLinkedList<Integer> cl = new CircularLinkedList<>();
		for (int i = 0; i < 10; i++) {
			cl.addFirst(i);
			cl.addLast(i);
			System.out.println(cl);
		}
		// infinite loop
		// while (cl.size > 0) {
		// Integer i = cl.removeFirst();
		// cl.addLast(i);
		// System.out.println(cl);
		// }
		System.out.println(cl.size());
	}
}
