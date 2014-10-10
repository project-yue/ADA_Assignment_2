package assignment_2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import assignment_2.pqs.HeapMinimumPriorityQueue;
import assignment_2.pqs.WeightNode;

/**
 * Inspired by Mahmoud as he created a draft for us to work on.
 * 
 * @author Mahmoud, Ximei & Yue
 *
 */
public class GraphToolBox {

	private static HashMap<Integer, Integer> leastEdges;
	private static HashMap<Integer, NodeDistance> distances;
	private static HeapMinimumPriorityQueue<NodeDistance> priortyQueue;

	public static void dijkstra(WGraph wgraph, Integer sourceNode,
			Integer sinkNode) {
		leastEdges = new HashMap<>();
		distances = new HashMap<>();
		priortyQueue = new HeapMinimumPriorityQueue<>();
		for (Integer node : wgraph.nodeSet) {
			NodeDistance nodeDistance = new NodeDistance();
			if (node != sourceNode) {
				leastEdges.put(node, null);
				nodeDistance.node = node;
				nodeDistance.distance = Double.POSITIVE_INFINITY;
			} else {
				leastEdges.put(node, sourceNode);
				nodeDistance.node = sourceNode;
				nodeDistance.distance = 0.0;
			}
			distances.put(node, nodeDistance);
			priortyQueue.insert(nodeDistance);
		}

		Integer currentSource = priortyQueue.getMinimumum().node;
		while (priortyQueue.size() > 0) {
			for (Integer adjNode : wgraph.data.get(currentSource).keySet()) {
				double distanceToAdjNode = distances.get(currentSource).distance
						+ wgraph.data.get(currentSource).get(adjNode);
				// relax all incident edges from a node
				if (distanceToAdjNode < distances.get(adjNode).distance) {
					// update distance from and least edge if ncessary
					NodeDistance nodeDistance = distances.get(adjNode);
					nodeDistance.distance = distanceToAdjNode;
					priortyQueue.heapify();
					leastEdges.put(adjNode, currentSource);
				}
			}
			currentSource = priortyQueue.getMinimumum().node;
		}

		// for (Integer node : distances.keySet()) {
		// System.out.println("Node " + node + " shortest distance from "
		// + "source node " + sourceNode + " is "
		// + distances.get(node).distance
		// + " with least incident edge " + "from " + "node "
		// + leastEdges.get(node) + " path: ");
		// }
		System.out.println("The pass from node " + sourceNode + " to "
				+ sinkNode + " is:\n"
				+ showPathFromSource(sourceNode, sinkNode));
	}

	public static void mst(WGraph wgraph) {
		wgraph.mstNodes = new LinkedList<>();
		// HashMap<Integer, Double> list = wgraph.data.get(source.to);
		int pre = Integer.MAX_VALUE;
		while (wgraph.weightPQ.size() > 0) {
			WeightNode temp = wgraph.weightPQ.getMinimumum();
			// init condition
			if (!wgraph.mstNodes.contains(temp.to)
					&& !wgraph.mstNodes.contains(temp.from)) {
				wgraph.mstNodes.add(temp.from);
				wgraph.mstNodes.add(temp.to);
			} else if (!wgraph.mstNodes.contains(temp.to)) {
				wgraph.mstNodes.add(temp.from);
				wgraph.mstNodes.add(temp.to);
			}
			// wgraph.mstNodes.add(temp.to);
			pre = temp.to;
		}
		for (Integer tem : wgraph.mstNodes)
			System.out.println(tem);
	}

	private static String showPathFromSource(Integer sourceNode, Integer target) {
		Stack<Integer> stack = new Stack<>();
		stack.push(target);
		int tempNode = getPreviousNode(target);
		while (tempNode != sourceNode) {// may be not connected by the source
			stack.push(tempNode);
			tempNode = getPreviousNode(tempNode);
		}
		stack.push(sourceNode);
		String result = "[";
		while (!stack.isEmpty() && stack.size() > 1)
			result += stack.pop() + ", ";
		result += stack.pop() + "]";
		return result;
	}

	private static Integer getPreviousNode(Integer node) {
		return leastEdges.get(node);
	}

	// public static void perform(WGraph wgraph, Integer sourceNode) {
	// leastEdges = new HashMap<>();
	// distances = new HashMap<>();
	// priortyQueue = new HeapMinimumPriorityQueue<>();
	// for (Integer node : wgraph.nodeSet) {
	// NodeDistance nodeDistance = new NodeDistance();
	// if (node != sourceNode) {
	// leastEdges.put(node, null);
	// nodeDistance.node = node;
	// nodeDistance.distance = Double.POSITIVE_INFINITY;
	// } else {
	// leastEdges.put(node, sourceNode);
	// nodeDistance.node = sourceNode;
	// nodeDistance.distance = 0.0;
	// }
	// distances.put(node, nodeDistance);
	// priortyQueue.insert(nodeDistance);
	// }
	//
	// Integer currentSource = priortyQueue.getMinimumum().node;
	// while (priortyQueue.size() > 0) {
	// for (Integer adjNode : wgraph.data.get(currentSource).keySet()) {
	// double distanceToAdjNode = distances.get(currentSource).distance
	// + wgraph.data.get(currentSource).get(adjNode);
	// // relax all incident edges from a node
	// if (distanceToAdjNode < distances.get(adjNode).distance) {
	// // update distance from and least edge if ncessary
	// NodeDistance nodeDistance = distances.get(adjNode);
	// nodeDistance.distance = distanceToAdjNode;
	// priortyQueue.heapify();
	// leastEdges.put(adjNode, currentSource);
	// }
	// }
	// currentSource = priortyQueue.getMinimumum().node;
	// }
	//
	// for (Integer node : distances.keySet()) {
	// System.out.println("Node " + node + " shortest distance from "
	// + "source node " + sourceNode + " is "
	// + distances.get(node).distance
	// + " with least incident edge " + "from " + "node "
	// + leastEdges.get(node));
	// }
	// }

	private static class NodeDistance implements Comparable<NodeDistance> {
		Integer node;
		Double distance;

		public NodeDistance() {
		}

		public NodeDistance(Integer node, Double distance) {
			this.node = node;
			this.distance = distance;
		}

		@Override
		public int compareTo(NodeDistance otherNode) {
			if (distance > otherNode.distance)
				return 1;
			else if (distance < otherNode.distance)
				return -1;
			else
				return 0;
		}
	}

}
