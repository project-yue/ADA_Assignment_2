package assignment_2.structures.linear;

import java.util.NoSuchElementException;

import java.util.Random;

/**
 * 
 * @author Yue
 * 
 * @param <E>
 */
public class SinglyLinkedList<E> {
	public transient Element<E> head;
	public transient int size;

	public static Random RAN = new Random();

	public SinglyLinkedList() {
		head = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	public void addFirst(E value) {
		head = new Element<>(value, head);
		size++;
	}

	public E getFirst() {
		return head.value;
	}

	public E removeFirst() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		Element<E> temp = head;
		head = head.next;
		temp.next = null;
		size--;
		return temp.value;
	}

	public void clear() {
		head = null;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void addLast(E value) {
		Element<E> temp = new Element<>(value, null);
		if (head != null) {
			Element<E> finger = head;
			while (finger.next != null) {
				finger = finger.next;
			}
			finger.next = temp;
		} else {
			head = temp;
		}
		size++;
	}

	public Element<E> getTailElement() {
		int index = 0;
		Element<E> finger = head;
		while (index < size() - 1) {
			finger = finger.next;
			index++;
		}
		return finger;
	}

	public E removeLast() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		Element<E> finger = head;
		Element<E> previous = null;
		while (finger.next != null) {
			previous = finger;
			finger = finger.next;
		}
		if (previous == null) {
			head = null;
		} else {
			previous.next = null;
		}
		size--;
		return finger.value;
	}

	public E tail() {
		Element<E> finger = head;
		Element<E> previous = null;
		while (finger != null) {
			previous = finger;
			finger = finger.next;
		}
		return previous.value;
	}

	public E getIndex(int n) {
		if (n < 0 || n >= size) {
			throw new IndexOutOfBoundsException(
					"index should be in the range, but " + n);
		}
		int index = 0;
		Element<E> finger = head;
		while (index++ < n) {
			finger = finger.next;
		}
		return finger.value;
	}

	public String toString() {
		String string = "[ ";
		Element<E> finger = head;
		int index = 0;
		while (index++ < size && finger != null && finger.next != null) {
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

	public void reverseOrder() {
		recursiveReverseOrder(head);
	}

	/**
	 * get sublist from head to the following index number of the list
	 * 
	 * @param index
	 * @return subList
	 */
	public SinglyLinkedList<E> subList(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("the size is " + size()
					+ " you looked index " + index);
		}
		E target = getIndex(index);
		System.out.println("value " + target);
		SinglyLinkedList<E> resultLst = new SinglyLinkedList<>();
		Element<E> finger = head;
		System.out.println("finger is " + finger.value);
		boolean isFound = false;
		int sizeCount = 0;
		while (!isFound && finger.hasNext()) {
			if (finger.value.equals(target)) {
				resultLst.head = new Element<E>(head.value, head.next);
				isFound = true;
			}
			if (!isFound)
				sizeCount++;
			finger = finger.next;
		}
		resultLst.size = sizeCount;
		return resultLst;
	}

	private void recursiveReverseOrder(Element<E> element) {
		if (element == null) {
			return;
		}

		if (element.next == null) {
			head = element;
			return;
		}
		recursiveReverseOrder(element.next);
		element.next.next = element;
		element.next = null;
	}

	public static void main(String[] args) {
		SinglyLinkedList<Integer> sl = new SinglyLinkedList<>();
		Integer[] ary = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 11 };
		for (Integer integer : ary) {
			sl.addLast(integer);
		}
		System.out.println("head is " + sl.head.value);
		System.out.println("tail is " + sl.tail());
		SinglyLinkedList<Integer> tem = sl.subList(5);
		// index count from head
		System.out.println(tem);
		System.out.println(sl.getFirst());
		System.out.println("tail is " + sl.tail());
	}

	public static class Element<E> {

		public E value;
		public Element<E> next;

		// for singlyLinkedList implementation
		public Element(E value, Element<E> next) {
			this.value = value;
			this.next = next;
		}

		public boolean hasNext() {
			return next != null;
		}

	}
}
