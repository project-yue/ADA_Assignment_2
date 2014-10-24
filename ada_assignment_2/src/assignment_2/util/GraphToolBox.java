package assignment_2.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import assignment_2.model.WGraph;
import assignment_2.structure.pq.HeapMinimumPriorityQueue;
import assignment_2.structure.pq.UndirectedWeightedEdge;
import assignment_2.view.AssignmentTwoFrame;

/**
 * a centralized utility class for q2
 * 
 * @author Yue
 *
 */
public class GraphToolBox {

	private static HashMap<Integer, Integer> leastEdges = new HashMap<>();
	private static HashMap<Integer, NodeDistance> distances = new HashMap<>();
	private static HeapMinimumPriorityQueue<NodeDistance> priorityQueue;
	private static ArrayList<String> shortestPaths;
	private static ArrayList<Double[]> bellmanProcessesList;

	public static void dijkstra(WGraph wgraph, Integer sourceNode,
			Integer sinkNode) {
		priorityQueue = new HeapMinimumPriorityQueue<>();
		for (Integer node : wgraph.nodeSet) {
			NodeDistance nodeDistance = new NodeDistance();
			if (nodeDistance.distance < 0.0) {
				return;
			}

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
				Thread.sleep(AssignmentTwoFrame.TIME_ADJUSTMENT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(AssignmentTwoFrame.TIME_ADJUSTMENT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Dijstra Path completed");
	}

	private static Integer getPreviousNode(Integer node) {
		return leastEdges.get(node);
	}

	public static void performBellmanFord(WGraph wgraph) {
		leastEdges = new HashMap<>();
		distances = new HashMap<>();
		bellmanProcessesList = new ArrayList<>();
		int n = wgraph.graphOrder();
		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		wgraph.status.setText("Bellmanford result:\n");
		System.out.println();
		for (Integer node : wgraph.nodeSet) {
			System.out.print("\t" + node);
			wgraph.status.append("\t" + node);
		}
		System.out.println();
		wgraph.status.append("\n");
		for (Integer sourceNode : wgraph.nodeSet) {
			if (!bellmanFord(sourceNode, wgraph)) {
				System.out.print(sourceNode + "\t");
				wgraph.status.append(sourceNode + "\t");
				for (Integer node : distances.keySet()) {
					System.out.print(distances.get(node).getDistance() + "\t");
					wgraph.status.append(distances.get(node).getDistance()
							+ "\t");
				}
			} else {
				System.out.println("The graph contains a negative cycle");
				wgraph.status.append("The graph contains a negative cycle"
						+ "\n");
			}
			System.out.println();
			wgraph.status.append("\n");
		}
		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		wgraph.status.append("\n");
		printBellmanfordProcesses(wgraph, bellmanProcessesList);
	}

	private static boolean bellmanFord(Integer sourceNode, WGraph wgraph) {
		boolean isNegativeCycle = false;
		for (Integer node : wgraph.nodeSet) {
			NodeDistance nodeDistance = new NodeDistance();
			nodeDistance.setNode(node);
			nodeDistance.setDistance((Double) Double.POSITIVE_INFINITY);
			distances.put(node, nodeDistance);
			leastEdges.put(node, null);
		}
		NodeDistance nodeDistance = new NodeDistance();
		nodeDistance.setNode(sourceNode);
		nodeDistance.setDistance(0.0);
		distances.put(sourceNode, nodeDistance);
		leastEdges.put(sourceNode, sourceNode);
		int n = wgraph.nodeSet.size();
		// ArrayList<Double[]> bellmanProcessesList = new ArrayList<>();
		bellmanProcessesList.add(printDistances(0, wgraph));
		for (int i = 1; i < n; i++) {
			for (Integer fromNode : wgraph.nodeSet) {
				for (Integer toNode : wgraph.data.get(fromNode).keySet()) {
					Double fromNodeDistance = distances.get(fromNode)
							.getDistance();
					Double toNodeDistance = distances.get(toNode).getDistance();
					Double edgeWeight = wgraph.data.get(fromNode).get(toNode);
					if (fromNodeDistance + edgeWeight < toNodeDistance) {
						nodeDistance = new NodeDistance();
						nodeDistance.setNode(toNode);
						nodeDistance.setDistance(fromNodeDistance + edgeWeight);
						distances.put(toNode, nodeDistance);
						leastEdges.put(toNode, fromNode);
					}
				}
			}
			bellmanProcessesList.add(printDistances(i, wgraph));
		}

		for (Integer fromNode : wgraph.nodeSet) {
			for (Integer toNode : wgraph.data.get(fromNode).keySet()) {
				Double fromNodeDistance = distances.get(fromNode).getDistance();
				Double toNodeDistance = distances.get(toNode).getDistance();
				Double edgeWeight = wgraph.data.get(fromNode).get(toNode);
				if (fromNodeDistance + edgeWeight < toNodeDistance) {
					nodeDistance = new NodeDistance();
					nodeDistance.setNode(toNode);
					nodeDistance.setDistance(fromNodeDistance + edgeWeight);
					distances.put(toNode, nodeDistance);
					leastEdges.put(toNode, fromNode);
					isNegativeCycle = true;
				}
			}
		}
		bellmanProcessesList.add(printDistances(n, wgraph));
		// printBellmanfordProcesses(wgraph, bellmanProcessesList);
		return isNegativeCycle;
	}

	private static void printBellmanfordProcesses(WGraph wgraph,
			ArrayList<Double[]> processList) {
		System.out.println();
		for (int i = 0; i < processList.size(); i++) {
			System.out.println("iteration " + i + ":");
			for (Integer node : wgraph.nodeSet) {
				System.out.print("\t" + node);
			}
			System.out.println();
			Double[] temp = processList.get(i);
			for (int l = 0; l < temp.length; l++) {
				if (temp[l] != Double.POSITIVE_INFINITY)
					System.out.print("\t" + temp[l]);
				else
					System.out.print("\t" + "i");
			}
			System.out.println();
		}
	}

	public static void mst(WGraph wgraph) {
		List<UndirectedWeightedEdge> undirectedWeightedEdges;
		Set<UndirectedWeightedEdge> undirectedWeightedEdgeSet = new HashSet<>();
		for (Integer node1 : wgraph.nodeSet) {
			for (Integer node2 : wgraph.data.get(node1).keySet()) {
				UndirectedWeightedEdge undirectedWeightedEdge = new UndirectedWeightedEdge(
						node1, node2, wgraph.data.get(node1).get(node2));
				undirectedWeightedEdgeSet.add(undirectedWeightedEdge);
			}
		}
		undirectedWeightedEdges = new ArrayList<UndirectedWeightedEdge>();
		for (UndirectedWeightedEdge undirectedWeightedEdge : undirectedWeightedEdgeSet) {
			undirectedWeightedEdges.add(undirectedWeightedEdge);
		}
		System.out.println("Minimum Spanning Tree esge set:");
		ArrayList<UndirectedWeightedEdge> minimumSpanningTree = kruskal(wgraph,
				undirectedWeightedEdges);
		// into pq operation
		// well it seems inefficient to me; however, implemented via pq
		// operation
		HeapMinimumPriorityQueue<UndirectedWeightedEdge> pq = new HeapMinimumPriorityQueue<>(
				minimumSpanningTree);
		while (pq.size() > 0) {
			UndirectedWeightedEdge wn = pq.getMinimumum();
			System.out.println(wn);
			wgraph.drawEdge(wn.node1, wn.node2, wgraph.getGraphics(),
					Color.GREEN, Color.RED, wn.weight);
			pq.heapify();
			try {
				Thread.sleep(AssignmentTwoFrame.TIME_ADJUSTMENT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static ArrayList<UndirectedWeightedEdge> kruskal(WGraph wgraph,
			List<UndirectedWeightedEdge> undirectedWeightedEdges) {
		ArrayList<UndirectedWeightedEdge> minimumSpanningTree = new ArrayList<>();
		Set<Set<Integer>> disjointSets = new HashSet<>();
		for (Integer node : wgraph.nodeSet) {
			Set<Integer> disjointSet = new HashSet<>();
			disjointSet.add(node);
			disjointSets.add(disjointSet);
		}
		Collections.sort(undirectedWeightedEdges);
		for (UndirectedWeightedEdge weightedEdge : undirectedWeightedEdges) {
			Set<Integer> node1Set = findSet(weightedEdge.getNode1(),
					disjointSets);
			Set<Integer> node2Set = findSet(weightedEdge.getNode2(),
					disjointSets);
			if (!node1Set.equals(node2Set)) {
				minimumSpanningTree.add(weightedEdge);
				node1Set.addAll(node2Set);
				disjointSets.remove(node2Set);
			}
		}
		// remove duplicated edges
		boolean isDuplicatesNotRemoved;
		UndirectedWeightedEdge tempWeightedEdge = null;
		do {
			isDuplicatesNotRemoved = false;
			for (UndirectedWeightedEdge outterEdge : minimumSpanningTree) {
				for (UndirectedWeightedEdge innerEdge : minimumSpanningTree) {
					if (outterEdge.node1.intValue() == innerEdge.node2
							.intValue()
							&& outterEdge.node2.intValue() == innerEdge.node1
									.intValue()
							&& outterEdge.weight.doubleValue() == innerEdge.weight
									.doubleValue()) {
						isDuplicatesNotRemoved = true;
						tempWeightedEdge = outterEdge;
					}
				}
			}
			if (isDuplicatesNotRemoved) {
				minimumSpanningTree.remove(tempWeightedEdge);
			}
		} while (isDuplicatesNotRemoved);
		return minimumSpanningTree;
	}

	private static Set<Integer> findSet(Integer node,
			Set<Set<Integer>> disjointSets) {
		for (Set<Integer> disjointSet : disjointSets) {
			if (disjointSet.contains(node)) {
				return disjointSet;
			}
		}
		return null;
	}

	public static void performFloydWarshall(WGraph wgraph) {
		int n = wgraph.nodeSet.size();
		List<List<List<Double>>> allPairsShortestPaths = new ArrayList<>();
		for (int k = 0; k <= n; k++) {
			allPairsShortestPaths.add(new ArrayList<List<Double>>());
			for (int i = 0; i < n; i++) {
				allPairsShortestPaths.get(k).add(new ArrayList<Double>());
				for (int j = 0; j < n; j++) {
					if (i == j) {
						allPairsShortestPaths.get(k).get(i).add(0.0);
					} else {
						allPairsShortestPaths.get(k).get(i)
								.add(Double.POSITIVE_INFINITY);
					}
				}
			}
		}
		floydWarshall(wgraph, allPairsShortestPaths);
		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		System.out.println();
		wgraph.status.setText("Floydwarshall result:\n");
		for (Integer node : wgraph.nodeSet) {
			System.out.print("\t" + node);
			wgraph.status.append("\t" + node);
		}
		wgraph.status.append("\n");
		System.out.println();
		String result = "";
		for (int k = 0; k <= n; k++) {
			System.out.println("k = " + k);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (k == n) {
						System.out.print(allPairsShortestPaths.get(k).get(i)
								.get(j)
								+ " ");
						result += allPairsShortestPaths.get(k).get(i).get(j)
								+ "\t";
					}
				}
				result += "\n";
				System.out.println();
			}
		}
		StringTokenizer st = new StringTokenizer(result, "\n");
		int node = 0;
		int size = st.countTokens();
		while (node++ < size) {
			wgraph.status.append(node + "\t" + st.nextToken() + "\n");
		}

		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	private static void floydWarshall(WGraph wgraph,
			List<List<List<Double>>> allPairsShortestPaths) {
		for (Integer fromNode : wgraph.nodeSet) {
			for (Integer toNode : wgraph.data.get(fromNode).keySet()) {
				allPairsShortestPaths.get(0).get(fromNode)
						.set(toNode, wgraph.data.get(fromNode).get(toNode));
			}
		}
		int n = wgraph.nodeSet.size();
		for (int k = 1; k <= n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					Double s = allPairsShortestPaths.get(k - 1).get(i)
							.get(k - 1)
							+ allPairsShortestPaths.get(k - 1).get(k - 1)
									.get(j);
					if (allPairsShortestPaths.get(k - 1).get(i).get(j) <= s) {
						allPairsShortestPaths
								.get(k)
								.get(i)
								.set(j,
										allPairsShortestPaths.get(k - 1).get(i)
												.get(j));
					} else {
						allPairsShortestPaths.get(k).get(i).set(j, s);
					}
				}
			}
		}
	}

	private static Double[] printDistances(int i, WGraph wgraph) {
		int n = wgraph.nodeSet.size();
		Double[] ans = new Double[n];
		String result = "i = " + i + ": ";
		for (int node = 0; node < n; node++) {
			result += distances.get(node).getDistance() + " ";
			ans[node] = distances.get(node).getDistance();
		}
		return ans;
	}

	private static class NodeDistance implements Comparable<NodeDistance> {

		private Integer node;
		private Double distance;

		public NodeDistance() {
		}

		public NodeDistance(Integer node, Double distance) {
			this.node = node;
			this.distance = distance;
		}

		@Override
		public int compareTo(NodeDistance otherNode) {
			if (getDistance() > otherNode.getDistance()) {
				return 1;
			} else if (getDistance() < otherNode.getDistance()) {
				return -1;
			} else {
				return 0;
			}
		}

		public Integer getNode() {
			return node;
		}

		public Double getDistance() {
			return distance;
		}

		public void setNode(Integer node) {
			this.node = node;
		}

		public void setDistance(Double distance) {
			this.distance = distance;
		}
	}

}
