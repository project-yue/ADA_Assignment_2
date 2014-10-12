package assignment_2.pqs;

import java.util.ArrayList;

public class HeapMinimumPriorityQueue<E extends Comparable<? super E>> {

	private final ArrayList<E> heap;

	public HeapMinimumPriorityQueue() {
		heap = new ArrayList<>();
	}

	public HeapMinimumPriorityQueue(ArrayList<E> lst) {
		heap = lst;
	}

	public E getMinimumum() {
		if (heap.size() <= 0) {
			return null;
		} else {
			// get the first element of the array
			E minVal = heap.get(0);
			// replace the first element of the array with last (it's successor)
			heap.set(0, heap.get(heap.size() - 1));
			// remove the last element
			heap.remove(heap.size() - 1);
			// sink the element down to restore the min heap condition
			minimumHeapify(0);
			return minVal;
		}
	}

	public void insert(E element) {
		// insert at end of array
		heap.add(element);
		int index = heap.size() - 1;

		// swim element up to restore the min heap condition
		while (index > 0
				&& heap.get(index).compareTo(heap.get(parent(index))) < 0) {
			swap(index, parent(index));
			index = parent(index);
		}
	}

	public boolean isEmpty() {
		return heap.isEmpty();
	}

	public int size() {
		return heap.size();
	}

	public E minimum() {
		if (heap.size() <= 0) {
			return null;
		} else {
			return heap.get(0);
		}
	}

	public void heapify() {
		// restores the min heap condition from the root after an update
		minimumHeapify(0);
	}

	public HeapMinimumPriorityQueue<E> clone() {
		@SuppressWarnings("unchecked")
		HeapMinimumPriorityQueue<E> temp = new HeapMinimumPriorityQueue<>(
				(ArrayList<E>) this.heap.clone());
		return temp;
	}

	private void minimumHeapify(int index) {
		// traverses the heap at index i restoring the miniumum condition where
		// necessary
		int left = leftChild(index);
		int right = rightChild(index);
		int smallest;
		if (left <= heap.size() - 1
				&& heap.get(left).compareTo(heap.get(index)) < 0) {
			smallest = left;
		} else {
			smallest = index;
		}
		if (right <= heap.size() - 1
				&& heap.get(right).compareTo(heap.get(smallest)) < 0) {
			smallest = right;
		}
		if (smallest != index) {
			swap(index, smallest);
			minimumHeapify(smallest);
		}
	}

	private void swap(int i, int j) {
		E temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}

	private int leftChild(int index) {
		return 2 * index + 1;
	}

	private int rightChild(int index) {
		return 2 * index + 2;
	}

	private int parent(int index) {
		return (index - 1) / 2;
	}

}
