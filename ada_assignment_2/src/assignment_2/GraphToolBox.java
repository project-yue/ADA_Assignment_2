package assignment_2;

import java.util.HashMap;
import java.util.Stack;

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

		for (Integer node : distances.keySet()) {
			System.out.println("Node " + node + " shortest distance from "
					+ "source node " + sourceNode + " is "
					+ distances.get(node).distance
					+ " with least incident edge " + "from " + "node "
					+ leastEdges.get(node) + " path: ");
		}
		showPathFromSource(sourceNode);
		// System.out.println(getPath(sourceNode, sinkNode, ""));
	}

	public static void showPathFromSource(Integer sourceNode) {
		for (Integer node : distances.keySet()) {
			// if (node != sourceNode)
			// System.out.println(node + ", " + getPath(node, sourceNode, ""));
			System.out.println(node);
		}
	}

	public static String getPath(Object node, Object source, String ans) {

		if ((Integer) node == (Integer) source)
			return ans.substring(0, ans.length() - 2);
		else {
			Object obj = leastEdges.get(node);
			ans += (Integer) obj + ", ";
			System.out.println(ans);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getPath(obj, source, ans);
		}
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
