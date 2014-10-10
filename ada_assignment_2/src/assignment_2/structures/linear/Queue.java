package assignment_2.structures.linear;

public class Queue<E> extends SinglyLinkedList<E> {

	public void enqueue(E e) {
		super.addLast(e);
	}

	public E dequeue() {
		return super.removeFirst();
	}

	public static void main(String[] args) {
		// Integer[] integers = { 1, 2, 4, 5, 6, 7, 8, 9, 12 };
		// Queue<Integer> queue = new Queue<>();
		// for (Integer integer : integers) {
		// queue.enqueue(integer);
		// System.out.println("enqueued integer is: " + integer);
		// System.out.println("current queue is: " + queue);
		// }
		// while (queue.size() > 0) {
		// System.out.println("Dequeued integer is: " + queue.dequeue());
		// System.out.println("Current queue is: " + queue);
		// }
		int[] pos = josephus(12, 2);
		for (int i : pos) {
			System.out.println(i);
		}
	}

	private static int[] josephus(int n, int k) {
		Queue<Integer> queue = new Queue<>();
		for (int i = 1; i <= n; i++) {
			queue.enqueue(i);
		}
		int round = 0;
		while (queue.size() > 2) {
			Integer temp = queue.dequeue();
			round++;
			if (round < k) {
				queue.enqueue(temp);
			} else {
				round = 0;
			}
		}
		int[] result = new int[2];
		result[0] = queue.dequeue();
		result[1] = queue.dequeue();

		return result;
	}
}
