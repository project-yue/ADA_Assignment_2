package assignment_2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
	private static HeapMinimumPriorityQueue<NodeDistance> priorityQueue;
	private static ArrayList<String> shortestPaths;

	public static void dijkstra(WGraph wgraph, Integer sourceNode,
			Integer sinkNode) {
		leastEdges = new HashMap<>();
		distances = new HashMap<>();
		priorityQueue = new HeapMinimumPriorityQueue<>();
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
			priorityQueue.insert(nodeDistance);
		}
		Integer currentSource = priorityQueue.getMinimumum().node;
		while (priorityQueue.size() > 0) {
			for (Integer adjNode : wgraph.data.get(currentSource).keySet()) {
				double distanceToAdjNode = distances.get(currentSource).distance
						+ wgraph.data.get(currentSource).get(adjNode);
				// relax all incident edges from a node
				if (distanceToAdjNode < distances.get(adjNode).distance) {
					// update distance from and least edge if ncessary
					NodeDistance nodeDistance = distances.get(adjNode);
					nodeDistance.distance = distanceToAdjNode;
					priorityQueue.heapify();
					leastEdges.put(adjNode, currentSource);
				}
			}
			currentSource = priorityQueue.getMinimumum().node;
		}
		System.out.println("The distance from node " + sourceNode + " to "
				+ sinkNode + " is " + distances.get(sinkNode).distance
				+ "\nDijkstra path:\n"
				+ showPathFromSource(sourceNode, sinkNode, wgraph, true));
	}

	private static void initializeDistances(WGraph wgraph, Integer sourceNode) {
		leastEdges = new HashMap<>();
		distances = new HashMap<>();
		priorityQueue = new HeapMinimumPriorityQueue<>();
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
			priorityQueue.insert(nodeDistance);
		}
		Integer currentSource = priorityQueue.getMinimumum().node;
		while (priorityQueue.size() > 0) {
			for (Integer adjNode : wgraph.data.get(currentSource).keySet()) {
				double distanceToAdjNode = distances.get(currentSource).distance
						+ wgraph.data.get(currentSource).get(adjNode);
				// relax all incident edges from a node
				if (distanceToAdjNode < distances.get(adjNode).distance) {
					// update distance from and least edge if ncessary
					NodeDistance nodeDistance = distances.get(adjNode);
					nodeDistance.distance = distanceToAdjNode;
					leastEdges.put(adjNode, currentSource);
				}
			}
			currentSource = priorityQueue.getMinimumum().node;
		}
	}

	public static void mst(WGraph wgraph, Graphics g) {
		HeapMinimumPriorityQueue<WeightNode> tempPQ = wgraph.weightPQ.clone();
		initializeDistances(wgraph, tempPQ.minimum().from);
		ArrayList<GraphSet> sets = new ArrayList<>();
		GraphSet set = new GraphSet();
		int minimalCount = 0;
		// edge should be less than edges -1
		while (tempPQ.size() > 0 && minimalCount < wgraph.nodeSet.size() - 1) {
			WeightNode temp = tempPQ.getMinimumum();
			System.out.println(temp.from + " " + temp.to + " " + temp.weight);
			// if there is no nodes nor edge, add it in
			if (!set.nodePairs.contains(temp)
					&& !set.nodeSet.contains(temp.from)
					&& !set.nodeSet.contains(temp.to)) {
				set.nodePairs.add(temp);
				set.nodeSet.add(temp.to);
				set.nodeSet.add(temp.from);
				minimalCount++;
			}// if there are nodes, test edge connectivity
			else if (set.nodeSet.contains(temp.from)
					&& set.nodeSet.contains(temp.to)) {
				boolean containEdge = false;
				for (WeightNode wn : set.nodePairs) {
					if (wn.from == temp.from && wn.to == temp.to
							|| wn.from == temp.to && wn.to == temp.from) {
						containEdge = true;
						System.out.println(temp.from + " " + temp.to
								+ " contain edge");
					}
				}
				if (!containEdge) {
					set.nodePairs.add(temp);
					set.nodeSet.add(temp.to);
					set.nodeSet.add(temp.from);
					minimalCount++;
				}
			}
			for (WeightNode wn : set.nodePairs) {
				wgraph.drawEdge(wn.from, wn.to, g, Color.GREEN, Color.RED,
						wn.weight);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (GraphSet nodeSet : sets) {
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
			System.out.println("visit " + tempNode);
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
		public Integer node;
		public Double distance;

		public NodeDistance() {
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
