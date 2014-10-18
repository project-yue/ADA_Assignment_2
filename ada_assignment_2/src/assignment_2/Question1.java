/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JFrame;

/**
 *
 * @author mmahmoud
 */
public class Question1 extends Digraph implements ActionListener {

	private Map<Integer, Boolean> visited;
	private List<SelectedEdge> selectedEdges;
	private List<SelectedEdge> tempEdges;
	private Map<Integer, BTreeNode> searchForest;
	private Integer timer;
	private Map<Integer, Integer> prevList;
	private Map<Integer, Integer> postList;
	private Integer firstCycleNode;
	private boolean isCycle = false;
	private boolean sccCycle = false;
	private boolean getCycleStart = false;
	private Stack<Integer> stack;
	private ArrayList<List<SelectedEdge>> scc;
	private Map<Integer, Integer> standAloneSCC;
	private Integer sccNumber;
	private Integer standAloneNum;
	private static boolean sccEnabled = false;
	private Color[] colorArray = { Color.RED, Color.MAGENTA, Color.PINK,
			Color.GREEN, Color.CYAN, Color.BLACK, Color.BLUE, Color.YELLOW,
			Color.GRAY, Color.ORANGE };

	public Question1() {
		super();
	}

	// perfoms breadth first search on a digraph until all nodes are visited
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

	// perfoms depth first search on a digraph until all nodes are visited
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

	// depth first search from a node
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

	// perfoms depth first search on a digraph until all nodes are visited
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

	// depth first search from a node
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
			if (selectedEdges != null) {
				for (SelectedEdge selectedEdge : selectedEdges) {
					drawEdge(selectedEdge.getFromNode(),
							selectedEdge.getToNode(), g, Color.RED, Color.RED);
					isCycle = false;
				}
			}
		}
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
			case "iscycle": // iscycle sourceNode
				try {
					node1 = Integer.parseInt(st.nextToken());
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
								.println("The Graph has a cycle. It can be linearized!");
					}
				} catch (Exception e) {
					System.out.println("Invalid command");

				}

				break;
			case "scc":
				try {
					node1 = Integer.parseInt(st.nextToken());
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
			}
		}
		if (isPassEventToParent) {
			super.actionPerformed(actionEvent);
		} else {
			repaint();
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
		frame.setSize(450, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.getContentPane().add(g);
		frame.setVisible(true);
	}
}
