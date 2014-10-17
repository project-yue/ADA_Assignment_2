package assignment_2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import assignment_2.color.ColorProperty;
import assignment_2.pqs.WeightNode;

/**
 * created a node set that contains all the valid node pair for edges as a group
 * 
 * @author mk-19
 *
 */
public class NodeSet {

	public Set<WeightNode> nodePairs;
	public Set<Integer> nodeSet;
	public int colorIndex;

	public NodeSet(WeightNode numberPair, int colorIndex) {
		this.nodePairs = new HashSet<>();
		this.nodeSet = new HashSet<>();
		nodePairs.add(numberPair);
		addNodes(numberPair);
		this.colorIndex = colorIndex;
	}

	private void addNodes(WeightNode nodePair) {
		this.nodeSet.add(nodePair.from);
		this.nodeSet.add(nodePair.to);
	}

	/**
	 * bi-direction intersect check
	 * 
	 * @param set
	 * @return
	 */
	public ArrayList<Integer> intersect(NodeSet set) {
		ArrayList<Integer> temp = new ArrayList<>();
		for (Integer node : nodeSet) {
			// the target set contain this set's node
			if (set.nodeSet.contains(node)) {
				temp.add(node);
			}
		}
		return temp;
	}

	public void union(NodeSet targetSet, ArrayList<Integer> intersectNodes) {
		Set<Integer> tempIntSet = new HashSet<>();
		Set<WeightNode> tempWNodeSet = new HashSet<>();
		for (WeightNode nodesPair : targetSet.nodePairs) {
			Integer from = nodesPair.from;
			Integer to = nodesPair.to;
			if (!this.nodeSet.contains(from) || !this.nodeSet.contains(to)) {
				if (intersectNodes.contains(from)
						|| intersectNodes.contains(to)) {
					tempWNodeSet.add(nodesPair);
				}
				if (intersectNodes.contains(to)) {
					tempIntSet.add(from);

				} else if (intersectNodes.contains(from)) {
					tempIntSet.add(to);
				}
				// prefixed
			}
		}
		this.nodePairs.addAll(tempWNodeSet);
		this.nodeSet.addAll(tempIntSet);
	}

	public String toString() {
		String result = "[";
		for (Iterator<WeightNode> ite = this.nodePairs.iterator(); ite
				.hasNext();) {
			WeightNode temp = ite.next();
			result += "(" + temp + "), ";
		}
		result = result.substring(0, result.length() - 2);
		result += "]";
		return result;
	}

	public static void main(String[] args) {
		WeightNode wn1 = new WeightNode(0, 1, 1.0);
		WeightNode wn2 = new WeightNode(3, 2, 1.0);
		WeightNode wn3 = new WeightNode(0, 3, 1.0);
		NodeSet set1 = new NodeSet(wn1, 0);
		NodeSet set2 = new NodeSet(wn2, 1);
		NodeSet set3 = new NodeSet(wn3, 2);

		ArrayList<Integer> intersects = set1.intersect(set3);
		if (intersects.size() > 0) {
			set1.union(set3, intersects);
		}
		intersects = set1.intersect(set2);
		if (intersects.size() > 0)
			set1.union(set2, intersects);
		System.out.println(set1);
	}

	public Color getColor(ColorProperty cp) {
		return cp.colors.get(this.colorIndex);
	}
}
