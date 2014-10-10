package assignment_2.structures.linear;

public class Stack<E> extends SinglyLinkedList<E> {

	public void push(E value) {
		super.addFirst(value);
	}

	public E pop() {
		return super.removeFirst();
	}

	public static void main(String[] args) {
		Stack<Integer> stack = new Stack<>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
		stack.push(6);
		System.out.println(stack);
		String[] testArray = {
				"A but tuba.",
				"A dog, a plan, a canal: pagoda.",
				"A man, a plan, a cat, a ham, a yak, a yam, a hat, a canal-Panama!",
				"Are we not pure? \"No sir!\" Panama’s moody Noriega brags. \"It is garbage!\" Irony dooms a man; a prisoner up to new era.",
				"Implementing the Card Deck using a Circularly Linked List",
				"Join this list at the end of the other list" };
		for (String test : testArray) {
			System.out.println("Test:\n" + test + "\nis a palindrome: "
					+ isPalindrome(test));
		}
	}

	public static boolean isPalindrome(String text) {
		boolean result = false;
		String plainText = removeNonLetterElements(text);
		String reversedText = reverseText(plainText);
		if (plainText.compareTo(reversedText) == 0) {
			result = true;
		}
		return result;
	}

	private static String removeNonLetterElements(String text) {
		String result = "";
		for (char temp : text.toLowerCase().toCharArray()) {
			// ascii approach
			if (temp >= 97 && temp < 123) {
				result += temp;
			}
		}
		return result;
	}

	private static String reverseText(String text) {
		String temp = "";
		Stack<Character> stack = new Stack<>();
		int index = 0;
		while (stack.size() < text.length()) {
			stack.push(text.charAt(index++));
		}
		while (stack.size() > 0) {
			temp += stack.pop();
		}
		System.out.println(temp);
		return temp;
	}
}
