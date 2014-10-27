package assignment_2.structure.pq;

import java.awt.Color;

public class WeightNode implements Comparable<WeightNode> {

	public int from;
	public int to;
	public double weight;
	public Color color;

	public WeightNode(int from, int to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
		this.color = Color.black;
	}

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
