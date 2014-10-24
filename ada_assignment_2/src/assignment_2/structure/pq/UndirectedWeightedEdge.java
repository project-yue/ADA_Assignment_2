package assignment_2.structure.pq;

public class UndirectedWeightedEdge implements
		Comparable<UndirectedWeightedEdge> {

	public Integer node1;
	public Integer node2;
	public Double weight;

	public UndirectedWeightedEdge(Integer node1, Integer node2, Double weight) {
		this.node1 = node1;
		this.node2 = node2;
		this.weight = weight;
	}

	@Override
	public int compareTo(UndirectedWeightedEdge otherWeightedEdge) {
		if (getWeight() > otherWeightedEdge.getWeight()) {
			return 1;
		} else if (getWeight() < otherWeightedEdge.getWeight()) {
			return -1;
		} else {
			return 0;
		}
	}

	public Integer getNode1() {
		return node1;
	}

	public Integer getNode2() {
		return node2;
	}

	public Double getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + node1;
		hash = 41 * hash + node2;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UndirectedWeightedEdge other = (UndirectedWeightedEdge) obj;
		return this.hashCode() == other.hashCode();
	}

	@Override
	public String toString() {
		return "Edge connects " + node1 + " and " + node2 + " with weight "
				+ weight;
	}

}
