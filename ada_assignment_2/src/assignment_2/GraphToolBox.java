package assignment_2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import assignment_2.color.ColorProperty;
import assignment_2.pqs.HeapMinimumPriorityQueue;
import assignment_2.pqs.WeightNode;

/**
 * Inspired by Mahmoud as he created a draft for us to work on.
 * 
 * @author Ximei & Yue
 *
 */
public class GraphToolBox {

	private static HashMap<Integer, Integer> leastEdges;
	private static HashMap<Integer, NodeDistance> distances;
	private static HeapMinimumPriorityQueue<NodeDistance> priortyQueue;
	private static ArrayList<String> shortestPaths;

	// public static void dijkstra(WGraph wgraph, Integer sourceNode,
	// Integer sinkNode, boolean whetherPlot) {
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
	// System.out.println(priortyQueue.minimum().node + " "
	// + priortyQueue.minimum().distance);
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
	// System.out.println(priortyQueue.minimum().node + " "
	// + priortyQueue.minimum().distance);
	// }
	//
	// System.out.println("The distance from node " + sourceNode + " to "
	// + sinkNode + " is " + distances.get(sinkNode).distance
	// + "\nDijkstra path:\n");
	// if (whetherPlot)
	// System.out.println(showPathFromSource(sourceNode, sinkNode, wgraph,
	// whetherPlot));
	// }

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
				+ showPathFromSource(sourceNode, sinkNode, wgraph, true));
	}

	private static void dijkstraPath(WGraph wgraph, Integer sourceNode,
			Integer target) {
		// ArrayList<String> wNodes = new ArrayList<>();
		HeapMinimumPriorityQueue<NodeDistance> tempPQ = new HeapMinimumPriorityQueue<>();
		// yields fields
		HashMap<Integer, Integer> leastEdges = new HashMap<>();
		HashMap<Integer, NodeDistance> distances = new HashMap<>();
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
			tempPQ.insert(nodeDistance);
		}

		Integer currentSource = tempPQ.getMinimumum().node;
		while (tempPQ.size() > 0) {
			for (Integer adjNode : wgraph.data.get(currentSource).keySet()) {
				double distanceToAdjNode = distances.get(currentSource).distance
						+ wgraph.data.get(currentSource).get(adjNode);
				// relax all incident edges from a node
				if (distanceToAdjNode < distances.get(adjNode).distance) {
					// update distance from and least edge if ncessary
					NodeDistance nodeDistance = distances.get(adjNode);
					nodeDistance.distance = distanceToAdjNode;
					tempPQ.heapify();
					// wNodes.add(currentSource + " " + adjNode);
					leastEdges.put(adjNode, currentSource);
				}
			}
			currentSource = tempPQ.getMinimumum().node;
			showPathFromSource(sourceNode, target, wgraph, false);
		}
	}

	private static void initializeDistances(WGraph wgraph, Integer sourceNode) {
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
	}

	public static void mst(WGraph wgraph, Graphics g) {
		HeapMinimumPriorityQueue<WeightNode> tempPQ = wgraph.weightPQ.clone();
		initializeDistances(wgraph, tempPQ.minimum().from);
		ArrayList<NodeSet> sets = new ArrayList<>();
		NodeSet ns = new NodeSet();
		while (tempPQ.size() > 0) {
			WeightNode temp = tempPQ.getMinimumum();

			if (!ns.nodeSet.contains(temp.from)
					|| !ns.nodeSet.contains(temp.to)) {
				ns.nodePairs.add(temp);
				ns.nodeSet.add(temp.from);
				ns.nodeSet.add(temp.to);
			} else if (ns.nodeSet.contains(temp.to)
					&& ns.nodeSet.contains(temp.from)) {
				if (!ns.isTherePathBetweenNodes(temp.from, temp.to)
						&& leastEdges.get(temp.to) == temp.from) {
					dijkstraPath(wgraph, temp.from, temp.to);
					System.out.println(temp.from + " " + temp.to);
					for (String path : GraphToolBox.shortestPaths) {
						System.out.println(temp.from + " " + temp.to
								+ " distance");
						int from = Integer.parseInt(path.split(" ")[0]);
						int to = Integer.parseInt(path.split(" ")[1]);
						if (from == temp.from && to == temp.to
								|| from == temp.to && to == temp.from) {
							System.out.println("this is what we want " + from
									+ " " + to);
						}
					}
				}
			}
			for (WeightNode wn : ns.nodePairs) {
				wgraph.drawEdge(wn.from, wn.to, g, Color.GREEN, Color.RED,
						wn.weight);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (NodeSet nodeSet : sets) {
				System.out.println("node set:" + nodeSet);
			}
			System.out.println();
		}
	}

	private static String showPathFromSource(Integer sourceNode,
			Integer target, WGraph graph, boolean whetherPlot) {
		GraphToolBox.shortestPaths = new ArrayList<>();
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
		String first = stack.peek().toString() + " ";
		String result = "[" + stack.pop() + ", ";

		while (!stack.isEmpty() && stack.size() > 1) {
			first += stack.peek();
			GraphToolBox.shortestPaths.add(first);
			vertices += stack.peek();
			verticesLst.add(vertices);
			vertices = stack.peek() + " ";
			first = stack.peek() + " ";
			result += stack.pop() + ", ";
		}
		vertices += stack.peek();
		first += stack.peek();
		GraphToolBox.shortestPaths.add(first);
		result += stack.pop() + "]";
		verticesLst.add(vertices);
		if (whetherPlot)
			plotDijkstraPathAnimation(verticesLst, graph);
		return result;
	}

	private static void plotDijkstraPathAnimation(ArrayList<String> lst,
			WGraph g) {
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
