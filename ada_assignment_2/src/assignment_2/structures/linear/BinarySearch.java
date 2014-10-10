/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment_2.structures.linear;

/**
 * Binary search for big Theta logn
 * 
 * @author mmahmoud
 */
public class BinarySearch {

	private static int numberOfIterations;

	public static <E extends Comparable<? super E>> int getIndex(E[] array,
			E item) {
		int index = 0;
		int exp = 0;
		numberOfIterations = 0;
		while (true) {
			numberOfIterations++;
			try {
				if (array[index].compareTo(item) == 0) {
					return index;
				} else if (array[index].compareTo(item) < 0) {
					index = (int) Math.pow(2, exp);
					exp++;
				} else {
					break;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				break;
			}
		}

		int left = (index / 2) + 1;
		int right = index - 1;
		int middle = 0;
		while (left <= right) {
			numberOfIterations++;
			try {
				middle = left + (right - left) / 2;
				if (array[middle].compareTo(item) == 0) {
					return middle;
				} else if (array[middle].compareTo(item) < 0) {
					left = middle + 1;
				} else {
					right = middle - 1;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				right = middle - 1;
			}
		}
		return -1;
	}

	public static void main(String[] args) {

		Integer[] sortedNumbers = new Integer[1000000];
		for (int number = 0; number < 1000000; number++) {
			if (number == 492385) {
				sortedNumbers[number] = number - 1;
			} else {
				sortedNumbers[number] = number;
			}
		}

		int item = 492385;
		int index = getIndex(sortedNumbers, item);
		if (index != -1) {
			System.out.println("Searching for item: " + item
					+ ". Found item using " + numberOfIterations
					+ " iterations");
		} else {
			System.out.println("Searching for item: " + item
					+ ". Found no item using " + numberOfIterations
					+ " iterations");
		}

		item = 183673;
		index = getIndex(sortedNumbers, item);
		if (index != -1) {
			System.out.println("Searching for item: " + item
					+ ". Found item using " + numberOfIterations
					+ " iterations");
		} else {
			System.out.println("Searching for item: " + item
					+ ". Found no item using " + numberOfIterations
					+ " iterations");
		}

		item = 1000000;
		index = getIndex(sortedNumbers, item);
		if (index != -1) {
			System.out.println("Searching for item: " + item
					+ ". Found item using " + numberOfIterations
					+ " iterations");
		} else {
			System.out.println("Searching for item: " + item
					+ ". Found no item using " + numberOfIterations
					+ " iterations");
		}
	}

}
