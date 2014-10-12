package assignment_2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
		System.out.println("The distance from node " + sourceNode + " to "
				+ sinkNode + " is " + distances.get(sinkNode).distance
				+ "\nDijkstra path:\n"
				+ showPathFromSource(sourceNode, sinkNode, wgraph));
	}

	public static void mst(WGraph wgraph, Graphics g) {
		LinkedList<Integer> mstEdges = new LinkedList<>();
		HashSet<Integer> set = new HashSet<>();
		HeapMinimumPriorityQueue<WeightNode> tempPQ = wgraph.weightPQ.clone();
		int pre = Integer.MAX_VALUE;
		double lastDistance = 0.0;
		while (tempPQ.size() > 0) {
			WeightNode temp = tempPQ.getMinimumum();
			// init condition
			if (!mstEdges.contains(temp.to) && !mstEdges.contains(temp.from)) {
				mstEdges.add(temp.from);
				mstEdges.add(temp.to);
				if (set.size() == 0) {
					set.add(temp.from);
					set.add(temp.to);
					lastDistance = temp.weight;
				} else if (mstEdges.contains(temp.to)
						&& mstEdges.contains(temp.from)
						&& temp.weight == lastDistance) {
					wgraph.drawEdge(temp.from, temp.to, g, Color.GREEN,
							Color.BLUE, temp.weight);
				} else {
					if (set.contains(temp.from)) {
						wgraph.drawEdge(temp.from, temp.to, g, Color.GREEN,
								Color.BLUE, temp.weight);
						set.add(temp.to);
					} else if (set.contains(temp.to)) {
						wgraph.drawEdge(temp.to, temp.from, g, Color.GREEN,
								Color.BLUE, temp.weight);
						set.add(temp.from);
					}
				}
				wgraph.drawEdge(temp.from, temp.to, g, Color.GREEN, Color.BLUE,
						temp.weight);
			} else if (mstEdges.contains(temp.to)
					&& mstEdges.contains(temp.from)) {
				HashMap<Integer, Double> list = wgraph.data.get(temp.from);
				if (list.containsKey(temp.to) && temp.weight == 2
						&& temp.from == 1)
					wgraph.drawEdge(temp.from, temp.to, g, Color.GREEN,
							Color.BLUE, temp.weight);
				if (temp.weight == lastDistance) {
					wgraph.drawEdge(temp.from, temp.to, g, Color.GREEN,
							Color.BLUE, temp.weight);
				}
			} else if (!mstEdges.contains(temp.to)) {
				mstEdges.add(temp.from);
				wgraph.drawEdge(temp.from, temp.to, g, Color.GREEN, Color.BLUE,
						temp.weight);
			} else if (temp.weight == lastDistance) {
				wgraph.drawEdge(temp.from, temp.to, g, Color.GREEN, Color.BLUE,
						temp.weight);
			}
			pre = temp.to;
			wgraph.repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Integer tem : mstEdges)
			System.out.println(tem);
		// make thread to sleep for 5 secondscd
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String showPathFromSource(Integer sourceNode,
			Integer target, WGraph graph) {
		Stack<Integer> stack = new Stack<>();
		stack.push(target);
		int tempNode = getPreviousNode(target);
		while (tempNode != sourceNode) {// may be not connected by the source
			stack.push(tempNode);
			tempNode = getPreviousNode(tempNode);
		}
		stack.push(sourceNode);
		ArrayList<String> verticesLst = new ArrayList<>();
		String vertices = Integer.toString(stack.peek()) + " ";
		String result = "[" + stack.pop() + ", ";

		while (!stack.isEmpty() && stack.size() > 1) {
			vertices += stack.peek();
			verticesLst.add(vertices);
			vertices = stack.peek() + " ";
			result += stack.pop() + ", ";
		}
		vertices += stack.peek();
		result += stack.pop() + "]";
		verticesLst.add(vertices);
		plotDijkstraPath(verticesLst, graph);
		return result;
	}

	private static void plotDijkstraPath(ArrayList<String> lst, WGraph g) {
		System.out.println("plot Dijstra Path:");
		for (String t : lst) {
			String[] temp = t.split(" ");
			int from = Integer.parseInt(temp[0]);
			int to = Integer.parseInt(temp[1]);
			double distance = g.data.get(from).get(to);
			g.drawEdge(from, to, g.getGraphics(), Color.CYAN, Color.BLUE,
					distance);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Dijstra Path completed");
	}

	private static Integer getPreviousNode(Integer node) {
		return leastEdges.get(node);
	}

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
