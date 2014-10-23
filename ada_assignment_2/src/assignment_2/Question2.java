/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_2;

//import assignment_2.toolkit.HeapMinimumPriorityQueue;
//import assignment2.toolkit.WGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import assignment_2.structure.pq.HeapMinimumPriorityQueue;

/**
 *
 * @author mmahmoud
 */
public class Question2 extends WGraph implements ActionListener {

	private HashMap<Integer, Integer> leastEdges;
	private HashMap<Integer, NodeDistance> distances;
	private List<UndirectedWeightedEdge> undirectedWeightedEdges;
	private List<List<List<Double>>> allPairsShortestPaths;

	private JTextArea status;

	public Question2() {
		super();
		status = new JTextArea();
		status.setEditable(false);
		status.setEnabled(false);
		status.addMouseListener(this);
		status.addMouseMotionListener(this);
		status.setDisabledTextColor(Color.BLACK);
		status.setLineWrap(true);
		status.setOpaque(false);
		add(status, BorderLayout.CENTER);
	}

	private void floydWarshall() {
		for (Integer fromNode : nodeSet) {
			for (Integer toNode : data.get(fromNode).keySet()) {
				allPairsShortestPaths.get(0).get(fromNode)
						.set(toNode, data.get(fromNode).get(toNode));
			}
		}
		int n = nodeSet.size();
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

	private void performFloydWarshall() {
		int n = nodeSet.size();
		allPairsShortestPaths = new ArrayList<>();
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
		floydWarshall();
		for (int k = 0; k <= n; k++) {
			System.out.println("k = " + k);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					System.out.print(allPairsShortestPaths.get(k).get(i).get(j)
							+ " ");
				}
				System.out.println();
			}
		}
	}

	private List<UndirectedWeightedEdge> kruskal() {
		List<UndirectedWeightedEdge> minimumSpanningTree = new ArrayList<>();
		Set<Set<Integer>> disjointSets = new HashSet<>();
		for (Integer node : nodeSet) {
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
			if (node1Set != node2Set) {
				minimumSpanningTree.add(weightedEdge);
				node1Set.addAll(node2Set);
				disjointSets.remove(node2Set);
			}
		}
		return minimumSpanningTree;
	}

	private Set<Integer> findSet(Integer node, Set<Set<Integer>> disjointSets) {
		for (Set<Integer> disjointSet : disjointSets) {
			if (disjointSet.contains(node)) {
				return disjointSet;
			}
		}
		return null;
	}

	private void performKruskal() {
		Set<UndirectedWeightedEdge> undirectedWeightedEdgeSet = new HashSet<>();
		for (Integer node1 : nodeSet) {
			for (Integer node2 : data.get(node1).keySet()) {
				UndirectedWeightedEdge undirectedWeightedEdge = new UndirectedWeightedEdge(
						node1, node2, data.get(node1).get(node2));
				undirectedWeightedEdgeSet.add(undirectedWeightedEdge);
			}
		}
		undirectedWeightedEdges = new ArrayList<>();
		for (UndirectedWeightedEdge undirectedWeightedEdge : undirectedWeightedEdgeSet) {
			undirectedWeightedEdges.add(undirectedWeightedEdge);
		}
		System.out.println("Minimum Spanning Tree esge set:");
		List<UndirectedWeightedEdge> minimumSpanningTree = kruskal();
		for (UndirectedWeightedEdge weightedEdge : minimumSpanningTree) {
			System.out.println(weightedEdge);
		}
	}

	private void performBellmanFord(Integer sourceNode) {
		leastEdges = new HashMap<>();
		distances = new HashMap<>();
		if (!bellmanFord(sourceNode)) {
			for (Integer node : distances.keySet()) {
				status.append("Node " + node + " shortest distance from "
						+ "source node " + sourceNode + " is "
						+ distances.get(node).getDistance()
						+ " with least incident edge " + "from " + "node "
						+ leastEdges.get(node) + "\n");
				System.out.println("Node " + node + " shortest distance from "
						+ "source node " + sourceNode + " is "
						+ distances.get(node).getDistance()
						+ " with least incident edge " + "from " + "node "
						+ leastEdges.get(node));
			}
		} else {
			status.append("The graph contains a negative cycle\n");
			System.out.println("The graph contains a negative cycle");
		}
	}

	private boolean bellmanFord(Integer sourceNode) {
		boolean isNegativeCycle = false;
		for (Integer node : nodeSet) {
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
		int n = nodeSet.size();
		printDistances(0);
		for (int i = 1; i < n; i++) {
			for (Integer fromNode : nodeSet) {
				for (Integer toNode : data.get(fromNode).keySet()) {
					Double fromNodeDistance = distances.get(fromNode)
							.getDistance();
					Double toNodeDistance = distances.get(toNode).getDistance();
					Double edgeWeight = data.get(fromNode).get(toNode);
					if (fromNodeDistance + edgeWeight < toNodeDistance) {
						nodeDistance = new NodeDistance();
						nodeDistance.setNode(toNode);
						nodeDistance.setDistance(fromNodeDistance + edgeWeight);
						distances.put(toNode, nodeDistance);
						leastEdges.put(toNode, fromNode);
					}
				}
			}
			printDistances(i);
		}

		for (Integer fromNode : nodeSet) {
			for (Integer toNode : data.get(fromNode).keySet()) {
				Double fromNodeDistance = distances.get(fromNode).getDistance();
				Double toNodeDistance = distances.get(toNode).getDistance();
				Double edgeWeight = data.get(fromNode).get(toNode);
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
		printDistances(n);
		return isNegativeCycle;
	}

	private void printDistances(int i) {
		String result = "i = " + i + ": ";
		int n = nodeSet.size();
		for (int node = 0; node < n; node++) {
			result += distances.get(node).getDistance() + " ";
		}
		System.out.println(result.trim());
	}

	private boolean dijkstra(Integer sourceNode) {
		HeapMinimumPriorityQueue<NodeDistance> priortyQueue = new HeapMinimumPriorityQueue<>();
		for (Integer node : nodeSet) {
			NodeDistance nodeDistance = new NodeDistance();
			if (node != sourceNode) {
				leastEdges.put(node, null);
				nodeDistance.setNode(node);
				nodeDistance.setDistance((Double) Double.POSITIVE_INFINITY);
			} else {
				leastEdges.put(node, sourceNode);
				nodeDistance.setNode(sourceNode);
				nodeDistance.setDistance((Double) 0.0);
			}
			distances.put(node, nodeDistance);
			priortyQueue.insert(nodeDistance);
		}

		Integer currentSource = priortyQueue.getMinimumum().getNode();
		while (priortyQueue.size() > 0) {
			for (Integer adjNode : data.get(currentSource).keySet()) {
				if (data.get(currentSource).get(adjNode) < 0.0) {
					return false;
				}
				double distanceToAdjNode = distances.get(currentSource)
						.getDistance() + data.get(currentSource).get(adjNode);
				// relax all incident edges from a node
				if (distanceToAdjNode < distances.get(adjNode).getDistance()) {
					// update distance from and least edge if ncessary
					NodeDistance nodeDistance = distances.get(adjNode);
					nodeDistance.setDistance((Double) distanceToAdjNode);
					priortyQueue.heapify();
					leastEdges.put(adjNode, currentSource);
				}
			}
			currentSource = priortyQueue.getMinimumum().getNode();
		}
		return true;
	}

	private void performDijkstra(Integer sourceNode) {
		leastEdges = new HashMap<>();
		distances = new HashMap<>();
		if (dijkstra(sourceNode)) {
			for (Integer node : distances.keySet()) {
				status.append("Node " + node + " shortest distance from "
						+ "source node " + sourceNode + " is "
						+ distances.get(node).getDistance()
						+ " with least incident edge " + "from " + "node "
						+ leastEdges.get(node) + "\n");
				System.out.println("Node " + node + " shortest distance from "
						+ "source node " + sourceNode + " is "
						+ distances.get(node).getDistance()
						+ " with least incident edge " + "from " + "node "
						+ leastEdges.get(node));
			}
		} else {
			status.append("Graph contains negative weighted edge, Dijkstra cannot be performed");
			System.out
					.println("Graph contains negative weighted edge, Dijkstra cannot be performed");
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String command = tf.getText();

		StringTokenizer st = new StringTokenizer(command);
		String token, opt;
		Integer node1, node2;
		boolean isPassEventToParent = true;
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			token = token.toLowerCase();
			switch (token) {
			case "dijkstra":
				node1 = Integer.parseInt(st.nextToken());
				performDijkstra(node1);
				isPassEventToParent = false;
				break;
			case "bellmanford":
				node1 = Integer.parseInt(st.nextToken());
				performBellmanFord(node1);
				isPassEventToParent = false;
				break;
			case "kruskal":
				performKruskal();
				isPassEventToParent = false;
				break;
			case "floydwarshall":
				performFloydWarshall();
				isPassEventToParent = false;
				break;
			case "clear":
			case "load":
				status.setText("");
				leastEdges = null;
				distances = null;
				undirectedWeightedEdges = null;
				allPairsShortestPaths = null;
				break;
			}
		}
		if (isPassEventToParent) {
			super.actionPerformed(actionEvent);
		} else {
			repaint();
		}
	}

	private static class UndirectedWeightedEdge implements
			Comparable<UndirectedWeightedEdge> {

		private Integer node1;
		private Integer node2;
		private Double weight;

		public UndirectedWeightedEdge(Integer node1, Integer node2,
				Double weight) {
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

	public static void main(String[] args) {
		Question2 g = new Question2();

		JFrame frame = new JFrame("Assignment 2 Question 2");
		frame.setSize(450, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.getContentPane().add(g);
		frame.setVisible(true);
	}
}
