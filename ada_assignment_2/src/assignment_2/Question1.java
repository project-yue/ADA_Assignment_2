/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author mmahmoud
 */
public class Question1 extends Digraph implements ActionListener {

	private Map<Integer, Boolean> visited; // check if a node has visited
	private List<SelectedEdge> selectedEdges; // tree edge in dfs or the edge in
												// the path of bfs
	private List<SelectedEdge> tempEdges; // edges that visited when do dfs
											// research used for cycle check
	private Map<Integer, BTreeNode> searchForest; // used for saving the BTree
	private Integer timer; // record the prev and post timer of a node
	private Map<Integer, Integer> prevList; // prev timer list of all nodes
	private Map<Integer, Integer> postList;// post timer list of all nodes
	private Integer firstCycleNode; // record the 1st cycle point when searching
	private boolean isCycle = false; // cycle flag
	private Stack<Integer> stack; // used in dfs
	private ArrayList<List<SelectedEdge>> scc; // scc is used for record the
												// each SCC with cycle
	private Map<Integer, Integer> standAloneSCC; // stand alone SCC
	private Integer sccNumber; // number of SCC with cycle
	private Integer standAloneNum; // number of stand alone SCC
	private static boolean sccEnabled = false; // scc enabled
	private HashMap<Integer, Integer> dist; // distance of each node
	private Color[] colorArray = { Color.RED, Color.MAGENTA, Color.PINK,
			Color.GREEN, Color.CYAN, Color.BLACK, Color.BLUE, Color.YELLOW,
			Color.GRAY, Color.ORANGE }; // used for coloring each SCC
	private static HashMap<Integer, Integer> parent; // record the parent node
														// in distance
														// determination
	private static Map<Integer, Integer> indegreeMap; // each node indegree map
	private static Map<Integer, Integer> outdegreeMap;// each node outdegree map
	private List<Edge> edgeList; // list of all edges in the graph, for Eulerian
									// trail search
	private Queue<Edge> trail; // record the path of Eulerian trail
	private ArrayList redList; // one color node set in the bipartite
	private ArrayList blueList;// the 2nd color node set in the bipartite

	public Question1() {
		super();
		super.tf.setText("load q1.dat");
	}

	/**
	 * perform breadth first search on a digraph until all nodes are visited
	 *
	 * @param source
	 */
	private void performBFS(Integer source) {
		visited = new HashMap<>();
		selectedEdges = new ArrayList<>();
		searchForest = new HashMap<>();

		for (Integer node : nodeSet) {
			visited.put(node, Boolean.FALSE);
		}

		bfs(source);
		source = getNextUnvistedNode();
		while (source != null) {
			bfs(source);
			source = getNextUnvistedNode();
		}
		for (Integer node : searchForest.keySet()) {
			if (searchForest.get(node).getParent() == null) {
				System.out.print(searchForest.get(node));
			}
		}
		System.out.println();
	}

	// breadth first search from a node
	private void bfs(Integer source) {
		visited.put(source, Boolean.TRUE);
		searchForest.put(source, new BTreeNode(source));
		Queue<Integer> bfsQueue = new LinkedList<>();
		bfsQueue.offer(source);
		while (!bfsQueue.isEmpty()) {
			Integer node = bfsQueue.poll();
			for (Integer adjacentNode : data.get(node)) {
				if (!visited.get(adjacentNode)) {
					visited.put(adjacentNode, Boolean.TRUE);
					selectedEdges.add(new SelectedEdge(node, adjacentNode));
					searchForest.put(adjacentNode, new BTreeNode(adjacentNode));
					searchForest.get(node).addChild(
							searchForest.get(adjacentNode));
					bfsQueue.offer(adjacentNode);
				}
			}
		}
	}

	// perfoms depth first search on a digraph until all nodes are visited
	private void performDFS(Integer source) {
		visited = new HashMap<>();
		selectedEdges = new ArrayList<>();

		for (Integer node : nodeSet) {
			visited.put(node, Boolean.FALSE);
		}

		recursiveDFS(source);
		source = getNextUnvistedNode();
		while (source != null) {
			recursiveDFS(source);
			source = getNextUnvistedNode();
		}
		System.out.println();
	}

	// depth first search from a node
	private void recursiveDFS(Integer node) {
		visited.put(node, Boolean.TRUE);
		System.out.print("(" + node);
		for (Integer adjacentNode : data.get(node)) {
			if (!visited.get(adjacentNode)) {
				selectedEdges.add(new SelectedEdge(node, adjacentNode));
				recursiveDFS(adjacentNode);
			}
		}
		System.out.print(node + ")");
	}

	// obtains the next unvisted node
	private Integer getNextUnvistedNode() {
		for (Integer node : visited.keySet()) {
			if (!visited.get(node)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * check if there is a cycle in the graph
	 *
	 * @param source
	 */
	private void cycleCheck(Integer source) {
		visited = new HashMap<>();
		selectedEdges = new ArrayList<>();
		tempEdges = new ArrayList<>();
		stack = new Stack<>();
		prevList = new HashMap<>();
		postList = new HashMap<>();
		timer = 0;
		for (Integer node : nodeSet) {
			visited.put(node, Boolean.FALSE);
			prevList.put(node, -1);
			postList.put(node, -1);
		}
		recursiveCycleCheck(source);
		if (!isCycle) {
			source = getNextUnvistedNode();
			while (source != null) {
				recursiveCycleCheck(source);
				if (!isCycle) {
					source = getNextUnvistedNode();
				} else {
					break;
				}
			}
		}

	}

	/**
	 * cycle check using depth first search from a node
	 *
	 * @param node
	 */
	private void recursiveCycleCheck(Integer node) {
		visited.put(node, Boolean.TRUE);
		prevList.put(node, timer++);
		for (Integer adjacentNode : data.get(node)) {
			if (!visited.get(adjacentNode)) {
				tempEdges.add(new SelectedEdge(node, adjacentNode));
				recursiveCycleCheck(adjacentNode);
				if (isCycle) {
					break;
				}
			} else if ((prevList.get(node) > prevList.get(adjacentNode))
					&& postList.get(adjacentNode) == -1) {
				tempEdges.add(new SelectedEdge(node, adjacentNode));
				firstCycleNode = adjacentNode;
				isCycle = true;
				break;
			}
		}
		if (!isCycle) {
			stack.push(node);
			postList.put(node, timer++);
		}
	}

	/**
	 * perform SCC based on the DFS research on Graph G
	 *
	 * @param source
	 */
	private void performSCC(Integer source) {
		visited = new HashMap<>();
		selectedEdges = new ArrayList<>();
		stack = new Stack<>();
		for (Integer node : nodeSet) {
			visited.put(node, Boolean.FALSE);
		}
		recursiveSCC(source);
		source = getNextUnvistedNode();
		while (source != null) {
			recursiveSCC(source);
			source = getNextUnvistedNode();
		}
		System.out.println();
		Integer lastNode = stack.peek();
		transpose();
		scc = new ArrayList();
		standAloneSCC = new HashMap<>();
		performTansposeDFS(lastNode);

	}

	/**
	 * SCC check using depth first search from a node
	 *
	 * @param node
	 */
	// depth first search from a node
	private void recursiveSCC(Integer node) {
		visited.put(node, Boolean.TRUE);
		for (Integer adjacentNode : data.get(node)) {
			if (!visited.get(adjacentNode)) {
				selectedEdges.add(new SelectedEdge(node, adjacentNode));
				recursiveSCC(adjacentNode);
			}
		}
		stack.push(node);
	}

	/**
	 * perform dfs research based on G^T
	 *
	 * @param source
	 */
	private void performTansposeDFS(Integer source) {
		visited = new HashMap<>();
		selectedEdges = new ArrayList<>();
		prevList = new HashMap<>();
		postList = new HashMap<>();
		timer = 0;
		sccNumber = 0;
		standAloneNum = 0;
		for (Integer node : nodeSet) {
			visited.put(node, Boolean.FALSE);
			prevList.put(node, -1);
			postList.put(node, -1);
		}
		recursiveTansposeDFS(source);
		postEdgeProcess();
		while (stack.size() > 0) {
			Integer lastNode = stack.peek();
			recursiveTansposeDFS(lastNode);
			postEdgeProcess();
		}
	}

	/**
	 * SCC check using depth first search from a node on G^T
	 *
	 * @param node
	 */
	private void recursiveTansposeDFS(Integer node) {
		visited.put(node, Boolean.TRUE);
		prevList.put(node, timer++);
		if (outdegree(node) == 0) {
			standAloneSCC.put(standAloneNum++, node);
			List<Integer> list;
			for (Integer j : nodeSet) {
				if (j != node) {
					list = data.get((Integer) j);
					if (list.contains((Integer) node)) {
						removeEdge(j, node);
					}
				}
			}
		} else {
			for (Integer adjacentNode : data.get(node)) {
				if (!visited.get(adjacentNode)) {
					selectedEdges.add(new SelectedEdge(node, adjacentNode));
					recursiveTansposeDFS(adjacentNode);
				} else if ((prevList.get(node) > prevList.get(adjacentNode))
						&& postList.get(adjacentNode) == -1) {
					selectedEdges.add(new SelectedEdge(node, adjacentNode));

				}
			}
		}
		stack.pop();
		postList.put(node, timer++);
	}

	/**
	 * remove the edge between one vertex is outside of SCC and other vertex is
	 * inside of SCC
	 */
	public void postEdgeProcess() {
		if (selectedEdges.size() > 0) {
			List<SelectedEdge> tempList = new ArrayList<>();
			for (SelectedEdge currentEdge : selectedEdges) {
				tempList.add(currentEdge);
			}
			scc.add(sccNumber++, tempList);

			for (Integer stackNode : stack) {
				List<Integer> list = data.get(stackNode);
				for (SelectedEdge curEdge : selectedEdges) {
					if (list.contains(curEdge.getFromNode())) {
						removeEdge(stackNode, curEdge.getFromNode());
					}
				}
			}

			for (SelectedEdge curEdge : selectedEdges) {
				List<Integer> list = data.get(curEdge.getFromNode());

				for (Integer stackNode : stack) {
					if (list.contains(stackNode)) {
						removeEdge(curEdge.getFromNode(), stackNode);
					}
				}
				List<Integer> list2 = data.get(curEdge.getToNode());
				if (list2.contains(curEdge.getFromNode())) {
					removeEdge(curEdge.getToNode(), curEdge.getFromNode());
				}
			}

			selectedEdges.clear();
		}
	}

	/**
	 * calculate the length of the shortest path from node1 to node2
	 *
	 * @param node1
	 * @param node2
	 */
	public void performBFSDistance(Integer node1, Integer node2) {
		visited = new HashMap<>();
		selectedEdges = new ArrayList<>();
		parent = new HashMap<>();
		dist = new HashMap<>();
		for (Integer node : nodeSet) {
			visited.put(node, false);
			dist.put(node, Integer.MAX_VALUE);
		}

		BFSDistance(node1, node2);
		if (!visited.get(node2)) {
			System.out.println("The distance from node " + node1 + " to node "
					+ node2 + " is infinity.");
		} else {
			System.out.println("The distance from node " + node1 + " to node "
					+ node2 + " is " + dist.get(node2));
			Integer par = parent.get(node2);
			Integer chi = node2;
			while (par != null) {
				selectedEdges.add(new SelectedEdge(par, chi));
				chi = par;
				par = parent.get(par);
			}
		}
	}

	/**
	 * the shortest distance between node1 and node2
	 *
	 * @param node1
	 * @param node2
	 */
	public void BFSDistance(Integer node1, Integer node2) {
		visited.put(node1, Boolean.TRUE);
		dist.put(node1, 0);
		parent.put(0, null);
		Queue<Integer> bfsQueue = new LinkedList<>();
		bfsQueue.offer(node1);
		while (!bfsQueue.isEmpty()) {
			Integer node = bfsQueue.poll();
			for (Integer adjacentNode : data.get(node)) {
				if (!visited.get(adjacentNode)) {
					visited.put(adjacentNode, Boolean.TRUE);
					parent.put(adjacentNode, node);
					dist.put(adjacentNode, dist.get(node) + 1);
					bfsQueue.offer(adjacentNode);
				}
			}
		}
	}

	/**
	 * check if the graph is Eulerian
	 *
	 * @return boolean
	 */
	public boolean isEulerian() {
		outdegreeMap = new HashMap<>();
		indegreeMap = new HashMap<>();
		edgeList = new ArrayList();
		selectedEdges = new ArrayList<>();

		List<Integer> greaterOutdegree = new ArrayList<>(); // for nodes that
															// outdegree is
															// greater one than
															// indegree
		List<Integer> greaterIndegree = new ArrayList<>(); // for nodes that
															// indegree is
															// greater one than
															// outdegree
		List<Integer> equalList = new ArrayList<>();
		for (Integer node : nodeSet) {
			Integer outNum = outdegree(node);
			Integer inNum = indegree(node);
			outdegreeMap.put(node, outNum);
			indegreeMap.put(node, inNum);
			if (outNum == (inNum + 1)) {
				greaterOutdegree.add(node);
			} else if (inNum == (outNum + 1)) {
				greaterIndegree.add(node);
			} else if (outNum == inNum) {
				equalList.add(node);
			} else {
				return false;
			}
		}
		if (((greaterOutdegree.size() == 1) && (greaterIndegree.size() == 1))
				|| (equalList.size() == graphOrder())) {
			List<Integer> adjList = new ArrayList<>();
			for (Integer node : nodeSet) {
				adjList = data.get(node);
				for (int adj : adjList) {
					edgeList.add(new Edge(node, adj));
				}
			}
			trail = new LinkedBlockingQueue<Edge>();

			Integer start;
			if ((greaterOutdegree.size() == 1) && (greaterIndegree.size() == 1)) {
				start = greaterOutdegree.get(0);
			} else {
				Random random = new Random();
				Integer index = random.nextInt(graphOrder());
				start = equalList.get(index);
			}
			Edge edge = getUnvisitedEdge(start);
			while (edge != null) {
				edge.visited = true;
				trail.offer(edge);
				edge = getUnvisitedEdge(edge.getToNode());
			}

		} else {
			return false;
		}

		return true;
	}

	/**
	 * return the unvisited edge with the fromPoint of the edge is start
	 *
	 * @param start
	 * @return
	 */
	public Edge getUnvisitedEdge(Integer start) {
		for (Edge edge : edgeList) {
			if (edge.visited == false && (edge.getFromNode() == start)) {
				return edge;
			}
		}
		return null;
	}

	/**
	 * check if the graph is Bipartite
	 *
	 * @return boolean
	 */
	public boolean isBipartite() {
		edgeList = new ArrayList();
		visited = new HashMap<>();
		dist = new HashMap<>();
		redList = new ArrayList();
		blueList = new ArrayList();

		List<Integer> adjList = new ArrayList<>();
		for (Integer node : nodeSet) {
			visited.put(node, Boolean.FALSE);
			adjList = data.get(node);
			for (int adj : adjList) {
				edgeList.add(new Edge(node, adj));
			}
		}
		Integer start;
		for (Integer node : nodeSet) {
			if (!visited.get(node)) {
				start = node;
				recursiveBipartiteCheck(start);
			}
		}

		Integer from, to, distance1, distanc2;
		Boolean flag = true;
		for (Edge edge : edgeList) {
			from = edge.getFromNode();
			to = edge.getFromNode();
			distance1 = dist.get(from) % 2;
			distanc2 = dist.get(to) % 2;
			if (distance1 == distanc2) {
				return false;
			}
		}
		return flag;
	}

	public void recursiveBipartiteCheck(Integer start) {
		visited.put(start, Boolean.TRUE);
		dist.put(start, 0);
		redList.add(start);
		Queue<Integer> bfsQueue = new LinkedList<>();
		bfsQueue.offer(start);
		while (!bfsQueue.isEmpty()) {
			Integer node = bfsQueue.poll();
			for (Integer adjacentNode : data.get(node)) {
				if (!visited.get(adjacentNode)) {
					visited.put(adjacentNode, Boolean.TRUE);
					Integer distance = dist.get(node) + 1;
					dist.put(adjacentNode, distance);
					if (distance % 2 == 0) {
						redList.add(adjacentNode);
					} else {
						blueList.add(adjacentNode);
					}
					bfsQueue.offer(adjacentNode);
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (sccEnabled) {
			int colorIndex = 0;
			int currentIndex;
			if (!isCycle) {// DAG
				for (int i = 0; i < nodeSet.size(); i++) {
					currentIndex = colorIndex % colorArray.length;
					nodeList.get(i).draw(g, colorArray[currentIndex]);
					colorIndex++;
				}

			} else {// SCC
				for (int j = 0; j < standAloneSCC.size(); j++) {
					currentIndex = colorIndex % colorArray.length;
					nodeList.get(standAloneSCC.get(j)).draw(g,
							colorArray[currentIndex]);
					colorIndex++;
				}
				for (int i = 0; i < sccNumber; i++) {
					currentIndex = colorIndex % colorArray.length;
					for (SelectedEdge selectedEdge : scc.get(i)) {
						nodeList.get(selectedEdge.getFromNode()).draw(g,
								colorArray[currentIndex]);
						nodeList.get(selectedEdge.getToNode()).draw(g,
								colorArray[currentIndex]);
						drawEdge(selectedEdge.getFromNode(),
								selectedEdge.getToNode(), g,
								colorArray[currentIndex],
								colorArray[currentIndex]);
					}
					colorIndex++;
				}
				isCycle = false;
			}

			sccEnabled = false;
		} else {
			if (isCycle) {
				isCycle = false;
			}
			if (selectedEdges != null) {
				for (SelectedEdge selectedEdge : selectedEdges) {
					drawEdge(selectedEdge.getFromNode(),
							selectedEdge.getToNode(), g, Color.RED, Color.RED);
					try {
						Thread.sleep(300);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				selectedEdges.clear();
			}
			if ((redList != null) || (blueList != null)) {
				for (int i = 0; i < redList.size(); i++) {
					nodeList.get(redList.get(i)).draw(g, Color.RED);
				}
				redList.clear();
				for (int i = 0; i < blueList.size(); i++) {
					nodeList.get(blueList.get(i)).draw(g, Color.GREEN);
				}
				blueList.clear();
			}

		}
	}

	public void demo() {

	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String command = tf.getText();

		StringTokenizer st = new StringTokenizer(command);

		String token, opt;
		Integer node1, node2;
		boolean isPassEventToParent = false;
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			token = token.toLowerCase();
			switch (token) {
			case "dfs":
				try {
					node1 = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					System.out.println("DFS parenthesis form is:");
					performDFS(node1);
					isPassEventToParent = false;
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;
			case "bfs":
				try {
					node1 = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					System.out.println("BFS parenthesis form is:");
					performBFS(node1);
					isPassEventToParent = false;
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;
			case "clear":
			case "add":
			case "remove":
			case "print":
			case "transpose":
			case "underlying":
			case "find":
			case "contract":
			case "load":
				visited = null;
				selectedEdges = null;
				searchForest = null;
				isPassEventToParent = true;
				break;
			case "cycle": // iscycle sourceNode
				try {
					node1 = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					cycleCheck(node1);
					if (isCycle == false) {
						System.out.println("The graph is a DAG");
					} else {
						for (int i = 0; i < tempEdges.size(); i++) {
							if (tempEdges.get(i).getFromNode() == firstCycleNode) {
								for (int j = i; j < tempEdges.size(); j++) {
									SelectedEdge selectedEdge = tempEdges
											.get(j);
									selectedEdges.add(selectedEdge);
								}
								break;
							}
						}
					}
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;
			case "linearize":
				try {
					node1 = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					cycleCheck(node1);
					if (isCycle == false) {
						System.out.println("The linearization list is: ");
						int x;
						while (stack.size() > 0) {
							x = stack.pop();
							System.out.print(x + " ");
						}
						System.out.println();
					} else {
						System.out
								.println("The Graph has a cycle. It cannot be linearized!");
					}
				} catch (Exception e) {
					System.out.println("Invalid command");

				}

				break;
			case "scc":
				try {
					node1 = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					sccEnabled = true;
					cycleCheck(node1);
					if (isCycle == false) {
						System.out.println("The graph is a DAG.There are "
								+ graphOrder() + " SCC");
						for (int srcNode : nodeSet) {
							List<Integer> list = data.get(srcNode);
							while (list.size() > 0) {
								removeEdge(srcNode, list.get(list.size() - 1));
							}
						}
					} else {
						performSCC(node1);
					}
				} catch (Exception e) {
					System.out.println("Invalid command");

				}
				break;
			case "distance":
				try {
					node1 = Integer.parseInt(st.nextToken());
					node2 = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					performBFSDistance(node1, node2);
				} catch (Exception e) {
					System.out.println("Invalid command");

				}
				break;
			case "eulerian":
				try {
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					boolean isEulerianTrail = isEulerian();
					if (!isEulerianTrail) {
						System.out.println("Eurian Trail does not exist!");
					} else {
						Edge edge;
						System.out.println();
						while (trail.size() > 1) {
							edge = trail.poll();
							selectedEdges.add(new SelectedEdge(edge
									.getFromNode(), edge.getToNode()));
							System.out.print(edge.getFromNode() + "->"
									+ edge.getToNode() + ", ");
						}
						edge = trail.poll();
						selectedEdges.add(new SelectedEdge(edge.getFromNode(),
								edge.getToNode()));
						System.out.print(edge.getFromNode() + "->"
								+ edge.getToNode());
						System.out.println();
					}
				} catch (Exception e) {
					System.out.println("Invalid command");

				}
				break;
			case "bipartite":
				try {
					if (st.hasMoreTokens()) {
						System.out.println("It's an invalid command");
						break;
					}
					boolean isbipartite = isBipartite();
					if (!isbipartite) {
						System.out.println("The digraph is not bipartite!");
					} else {
						System.out.println("Yes, the digraph is bipartite!");
					}
				} catch (Exception e) {
					System.out.println("Invalid command");

				}
				break;
			}
		}
		if (isPassEventToParent) {
			super.actionPerformed(actionEvent);
		} else {
			repaint();
		}

	}

	private class Edge extends SelectedEdge {

		public boolean visited = false;

		public Edge(Integer fromNode, Integer toNode) {
			super(fromNode, toNode);
		}

	}

	private class SelectedEdge {

		private final Integer fromNode;
		private final Integer toNode;

		public SelectedEdge(Integer fromNode, Integer toNode) {
			this.fromNode = fromNode;
			this.toNode = toNode;
		}

		public Integer getFromNode() {
			return fromNode;
		}

		public Integer getToNode() {
			return toNode;
		}
	}

	private class BTreeNode {

		private final Integer node;
		private BTreeNode parent;
		private final List<BTreeNode> children;

		public BTreeNode(Integer node) {
			this.node = node;
			parent = null;
			children = new ArrayList<>();
		}

		public BTreeNode getParent() {
			return parent;
		}

		public void addChild(BTreeNode child) {
			child.parent = this;
			children.add(child);
		}

		@Override
		public String toString() {
			String result = "(" + node;
			for (BTreeNode child : children) {
				result += child.toString();
			}
			return result + node + ")";
		}
	}

	public static void main(String[] args) {
		Question1 g = new Question1();

		JFrame frame = new JFrame("Assignment 2 Question 1");
		frame.setMinimumSize(new Dimension(450, 450));

		initMenu(frame, g);

		// frame.setSize(450, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.getContentPane().add(g);
		frame.setVisible(true);
	}

	private static void initMenu(JFrame frame, Digraph dg) {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		// CustomizedMenuItem menuBar = new CustomizedMenuItem();
		// menuBar.addFrame(frame);
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		CustomizedMenuItem mntmDiGraph = new CustomizedMenuItem("Digraph");
		mntmDiGraph.addGraphPanel(dg);
		mntmDiGraph.addFrame(frame);
		mntmDiGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomizedMenuItem cmi = (CustomizedMenuItem) e.getSource();
				cmi.frame.remove((WGraph) cmi.panel);
				cmi.repaint();
				Question1 q1 = new Question1();
				cmi.frame.add(q1);
				cmi.panel = q1;
				cmi.repaint();
				cmi.frame.pack();
			}
		});
		mnFile.add(mntmDiGraph);

		CustomizedMenuItem mntmWGraph = new CustomizedMenuItem("WGraph");
		mntmWGraph.addGraphPanel(dg);
		mntmWGraph.addFrame(frame);
		mntmWGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomizedMenuItem cmi = (CustomizedMenuItem) e.getSource();
				Question1 q1 = ((Question1) cmi.panel);
				cmi.frame.remove(q1);
				cmi.frame.repaint();
				WGraph wgraph = new WGraph();
				cmi.frame.add(wgraph);
				cmi.panel = wgraph;
				// cmi.frame.setMinimumSize(new Dimension(450, 450));
				cmi.repaint();
				cmi.frame.pack();
			}
		});
		mnFile.add(mntmWGraph);

		CustomizedMenuItem mntmExit = new CustomizedMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addFrame(frame);
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomizedMenuItem cmi = (CustomizedMenuItem) e.getSource();
				cmi.frame.dispose();
			}
		});
	}
}
