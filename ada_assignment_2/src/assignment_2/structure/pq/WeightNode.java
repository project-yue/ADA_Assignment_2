package assignment_2.structure.pq;

public class WeightNode implements Comparable<WeightNode> {

	public int from;
	public int to;
	public double weight;

	public WeightNode(int from, int to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	// @Override
	// public int compareTo(WeightNode o) {
	// if (this.weight < o.weight)
	// return 1;
	// else if (this.weight > o.weight)
	// return -1;
	// else
	// return 0;
	// }

	@Override
	public int compareTo(WeightNode o) {
		if (this.weight > o.weight)
			return 1;
		else if (this.weight < o.weight)
			return -1;
		else
			return 0;
	}

	public String toString() {
		return from + " " + to;
	}
}
