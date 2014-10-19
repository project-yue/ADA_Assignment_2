package assignment_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import assignment_2.pqs.WeightNode;

/**
 * created a node set that contains all the valid node pair for edges as a group
 * 
 * @author mk-19
 *
 */
public class GraphSet {

	public Set<WeightNode> nodePairs;
	public Set<Integer> nodeSet;
	public HashMap<Integer, Integer> pathMap;
	public int colorIndex;
	public HashMap<Integer, ArrayList<Integer>> edgesMap;

	public GraphSet(WeightNode numberPair, int colorIndex) {
		this.edgesMap = new HashMap<>();
		this.nodePairs = new HashSet<>();
		this.nodeSet = new HashSet<>();
		this.pathMap = new HashMap<>();
		addNodes(numberPair);
		updateNodePairs(numberPair);
		this.colorIndex = colorIndex;
	}

	public GraphSet() {
		this.edgesMap = new HashMap<>();
		this.nodePairs = new HashSet<>();
		this.nodeSet = new HashSet<>();
	}

	public WeightNode obtainWeight(Integer from, Integer to) {
		for (WeightNode wn : nodePairs) {
			if (from == wn.from && to == wn.to)
				return wn;
		}
		return null;
	}

	private void addNodes(WeightNode nodePair) {
		this.nodeSet.add(nodePair.from);
		this.nodeSet.add(nodePair.to);
		this.pathMap.put(nodePair.from, nodePair.to);
	}

	private void updateNodePairs(WeightNode wn) {
		this.nodePairs.add(wn);
	}

	public boolean isTherePathBetweenNodes(Integer from, Integer to) {
		// boolean result = false;
		for (WeightNode wn : this.nodePairs) {
			if (wn.from == from && wn.to == to || wn.from == to
					&& wn.to == from) {
				Integer targetLocation = this.pathMap.get(wn.from);
				while (targetLocation != null) {
					System.out.println("test from " + wn.from + " to "
							+ targetLocation);
					if (targetLocation == wn.to) {
						return true;
					}
					targetLocation = this.pathMap.get(targetLocation);
				}
			}
		}
		return false;
	}

	/**
	 * bi-direction intersect check
	 * 
	 * @param set
	 * @return
	 */
	// public ArrayList<Integer> intersect(NodeSet set) {
	// ArrayList<Integer> temp = new ArrayList<>();
	// ArrayList<WeightNode> temp2 = new ArrayList<>();
	// // for (Integer node : this.nodeSet) {
	// // // the target set contain this set's node
	// // if (set.nodeSet.contains(node)) {
	// // temp.add(node);
	// // }
	// // }
	// // if the node is found in this node set, then there must be an edge
	// // between the target node set and this node set
	// for (Integer node : set.nodeSet) {
	// if (this.nodeSet.contains(node)) {
	// // look for the edge that having the nodes
	// for (WeightNode wn : set.nodePairs) {
	// if (wn.from == node && !this.nodeSet.contains(wn.to)) {
	// this.nodeSet.add(wn.to);
	// this.nodePairs.add(wn);
	// } else if (wn.to == node && !this.nodeSet.contains(wn.from)) {
	// this.nodeSet.add(wn.from);
	// this.nodePairs.add(wn);
	// }
	// // else both nodes are in the current set, do nothing
	// }
	// }
	// }
	// return temp;
	// }
	//
	// public void union(NodeSet targetSet, ArrayList<Integer> intersectNodes) {
	// Set<Integer> tempIntSet = new HashSet<>();
	// Set<WeightNode> tempWNodeSet = new HashSet<>();
	// for (WeightNode nodesPair : targetSet.nodePairs) {
	// Integer from = nodesPair.from;
	// Integer to = nodesPair.to;
	// if (!this.nodeSet.contains(from) || !this.nodeSet.contains(to)) {
	// if (intersectNodes.contains(from)
	// || intersectNodes.contains(to)) {
	// tempWNodeSet.add(nodesPair);
	// }
	// if (intersectNodes.contains(to)) {
	// tempIntSet.add(from);
	//
	// } else if (intersectNodes.contains(from)) {
	// tempIntSet.add(to);
	// }
	// // prefixed
	// }
	// }
	// this.nodePairs.addAll(tempWNodeSet);
	// this.nodeSet.addAll(tempIntSet);
	// }

	public int size() {
		return this.nodePairs.size();
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
		GraphSet set1 = new GraphSet(wn1, 0);
		GraphSet set2 = new GraphSet(wn2, 1);
		GraphSet set3 = new GraphSet(wn3, 2);

		// ArrayList<Integer> intersects = set1.intersect(set3);
		// if (intersects.size() > 0) {
		// set1.union(set3, intersects);
		// }
		// intersects = set1.intersect(set2);
		// if (intersects.size() > 0)
		// set1.union(set2, intersects);
		System.out.println(set1);
	}

}
