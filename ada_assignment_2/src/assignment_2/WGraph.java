package assignment_2;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import assignment_2.structure.pq.HeapMinimumPriorityQueue;
import assignment_2.structure.pq.WeightNode;

/**
 * This class implements the weighted directed graph ADT and a simple GUI for
 * visualising a weighted directed graph The data structure used for
 * implementing a digraph is the weighted adjacency matrix. The nodes are
 * labeled by Integers
 *
 * The GUI should allow the user to add/remove nodes/edges, as well as
 * performing simple updates and queries on the graph Control to the data
 * structure is done by both the mouse and through a textfield at the bottom of
 * the window
 *
 *
 * @Author:
 */
public class WGraph extends JPanel implements MouseMotionListener,
		MouseListener, ActionListener {

	private int barb; // size of an arrow edge
	private double phi; // angle of an arrow edge
	private Integer moveNode; // the node the user is moving on the GUI
	private Integer selectedNode; // the node selected
	public final static int CIRCLEDIAMETER = 40; // Diameter of the nodes
	// This is the adjacency list representation of the digraph
	// The nodes are denoted here as Integers
	// Each node is associated with a list of Integers, which indicates its
	// out-neighbours
	public HashMap<Integer, HashMap<Integer, Double>> data;
	//
	public static HashMap<Integer, Node> NODE_LIST;
	// The collection of node in the graph
	// This set is the key set of data
	public Set<Integer> nodeSet;
	// The textfield used for user to specify commands
	private JTextField tf;

	// Added weight PQ and set for mst
	public HeapMinimumPriorityQueue<WeightNode> weightPQ;
	// public Collection<Integer> mstEdges;
	public boolean isMst;

	//
	public double[][] floydWarshall;

	// The Constructor
	public WGraph() {
		setSize(450, 450);
		data = new HashMap<Integer, HashMap<Integer, Double>>();
		NODE_LIST = new HashMap<Integer, Node>();
		nodeSet = data.keySet();

		// init starts
		weightPQ = new HeapMinimumPriorityQueue<>();
		// init ends

		JPanel panel = new JPanel();
		barb = 20; // barb length
		phi = Math.PI / 12; // 30 degrees barb angle
		setBackground(Color.white);
		addMouseMotionListener(this);
		addMouseListener(this);
		tf = new JTextField();
		tf.setText("load q2.dat");
		tf.addActionListener(this);
		setLayout(new BorderLayout());
		add(panel, BorderLayout.NORTH);
		add(tf, BorderLayout.SOUTH);
		moveNode = -1; // Initial values of moveNode is -1
		selectedNode = -1; // Initial values of moveNode is -1

	}

	/**
	 * The method adds a node to the digraph, labeled by the int value node
	 *
	 */
	public void add(int node) {
		// If the label node is already in the digraph, do nothing and return
		if (data.containsKey((Integer) node)) {
			return;
		}
		// Create a new linked list
		HashMap<Integer, Double> list = new HashMap<Integer, Double>();
		// Add a new entry to the adjacency list
		data.put((Integer) node, list);

		// Create a new node in the GUI
		// Set a random initial position
		// Link the new node with the corresponding node in the GUI
		Node nodeVisual = new Node(node);
		nodeVisual.xpos = 50 + (int) (Math.random() * 320);
		nodeVisual.ypos = 50 + (int) (Math.random() * 320);
		NODE_LIST.put((Integer) node, nodeVisual);

	}

	/**
	 * The method adds an edge to the weighted digraph The source of the edge is
	 * labeled node1 The target of the edge is labeled node2 The weight of the
	 * edge is weight
	 */
	public void addEdge(int node1, int node2, double weight) {
		if (node1 == node2) {
			return;
		}
		if (!data.containsKey((Integer) node1)
				|| !data.containsKey((Integer) node2)) {
			return;
		}
		HashMap<Integer, Double> list = data.get((Integer) node1);
		if (!list.containsKey((Integer) node2)) {
			list.put((Integer) node2, (Double) weight);
			System.out.println(node1 + " " + node2 + " " + weight);
			WeightNode wn = new WeightNode(node1, node2, weight);
			this.weightPQ.insert(wn);
		}
	}

	/**
	 * The method loads a weighted digraph stored in the file fileName in
	 * adjacency matrix representation The top line of the file contains the
	 * number n of nodes in the graph The nodes in the graph are then given the
	 * indices 0,...,n-1
	 *
	 */
	public void load(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			int numNodes = Integer.parseInt(br.readLine());
			int pos = 0;
			String output;
			for (int i = 0; i < numNodes; i++) {
				add(i);
			}
			int col = 0;
			String st = "";
			for (int i = 0; i < numNodes; i++) {
				output = br.readLine();
				if (output != null) {
					StringTokenizer token = new StringTokenizer(output);
					while (token.hasMoreTokens()) {
						st = token.nextToken();
						if (!st.equals("#")) {
							addEdge(i, col, Double.parseDouble(st));
						}
						col++;
					}
					col = 0;
				}
			}
			br.close();

		} catch (FileNotFoundException e) {
			System.out.println("File can't be found.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * The method removes an edge from the digraph The source of the edge is
	 * labeled node1 The target of the edge is labeled node2
	 *
	 */
	public void removeEdge(int node1, int node2) {
		if (!data.containsKey((Integer) node1)
				|| !data.containsKey((Integer) node2)) {
			return;
		}
		HashMap<Integer, Double> list = data.get((Integer) node1);
		if (list.containsKey((Integer) node2)) {
			list.remove((Integer) node2);
		}
	}

	/**
	 * The methods changes the weight of the edges from node1 to node2 to weight
	 * Do nothing if the edge does not exist in the graph
	 *
	 */
	public void changeWeight(int node1, int node2, double weight) {
		if (!data.containsKey((Integer) node1)
				|| !data.containsKey((Integer) node2)) {
			return;
		}
		HashMap<Integer, Double> list = data.get((Integer) node1);
		if (list.containsKey((Integer) node2)) {
			list.put((Integer) node2, (Double) weight);
		}
	}

	/**
	 * The method removes a node from the digraph You need to complete this
	 * method It should do nothing if the node is not contained in the digraph
	 */
	public void remove(int node) {
		if (!data.containsKey((Integer) node)) {
			return;
		}
		HashMap<Integer, Double> list;
		for (Integer i : nodeSet) {
			list = data.get((Integer) i);
			if (list.containsKey((Integer) node)) {
				list.remove((Integer) node);
			}
		}
		data.remove((Integer) node);
		NODE_LIST.remove((Integer) node);

	}

	/**
	 * This method computes and returns the order (number of nodes) of the graph
	 *
	 */
	public int graphOrder() {
		return data.size();
	}

	/**
	 * This method prints out the adjacency matrix of the graph You need to
	 * complete this method
	 *
	 * The method computes the following data structure: 1. a HashMap labels
	 * which associates each number between 0 and n-1 a unique node label 2. a
	 * boolean nxn matrix adjMatrix storing the adjacency matrix where: the ith
	 * row/column corresponds to the node with label labels.get(i)
	 *
	 * The method then prints out the adjacency matrix To the left and on top of
	 * the matrix, the method also prints out the node label which corresponds
	 * to each row and column
	 */
	public void printMatrix() {
		// n is the order of the graph
		int n = graphOrder();

		// the HashMap associates an index in [0..n-1] with a node label
		HashMap<Integer, Integer> labels = new HashMap<Integer, Integer>();

		// the adjacency matrix of the digraph, where the node indices in
		// the matrix are indicated by the labels HashMap
		String[][] adjMatrix = new String[n][n];
		// this.floydWarshall = new double[n][n];
		HashMap<Integer, Double> list;
		// int row = 0;
		// int column = 0;
		int index = 0;

		for (Integer i : nodeSet) {
			labels.put((Integer) index, (Integer) i);
			index++;
		}
		for (int i = 0; i < n; i++) {
			list = data.get(labels.get((Integer) i));
			for (int j = 0; j < n; j++) {
				if (list.containsKey(labels.get((Integer) j))) {
					adjMatrix[i][j] = "" + list.get(labels.get((Integer) j));
				} else {
					adjMatrix[i][j] = "#";
				}
			}
		}

		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		System.out.print('\n');

		System.out.print("" + '\t');
		for (int i = 0; i < n; i++) {
			System.out.print("" + labels.get((Integer) i) + '\t');
		}
		System.out.print("" + '\n');

		for (int i = 0; i < n; i++) {
			System.out.print("" + labels.get((Integer) i));
			for (int j = 0; j < n; j++) {
				System.out.print('\t' + adjMatrix[i][j]);
			}
			System.out.print("" + '\n');
		}
		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		System.out.print('\n');

	}

	public void printFloydWarshall() {
		// n is the order of the graph
		int n = graphOrder();

		// the HashMap associates an index in [0..n-1] with a node label
		HashMap<Integer, Integer> labels = new HashMap<Integer, Integer>();
		this.floydWarshall = new double[n][n];

		int index = 0;
		HashMap<Integer, Double> list;

		for (Integer i : nodeSet) {
			labels.put((Integer) index, (Integer) i);
			index++;
		}
		for (int i = 0; i < n; i++) {
			list = data.get(labels.get((Integer) i));
			for (int j = 0; j < n; j++) {
				if (i == j) {
					floydWarshall[i][j] = 0;
				} else if (list.containsKey(labels.get((Integer) j))) {
					floydWarshall[i][j] = list.get(labels.get((Integer) j));
				} else {
					floydWarshall[i][j] = 20;
				}
			}
		}
		// this.floydWarshall = computeFloydWarshall(this.floydWarshall);
		this.floydWarshall = floydWarshall(this.floydWarshall);
		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		System.out.print('\n');

		System.out.print("" + '\t');
		for (int i = 0; i < n; i++) {
			System.out.print("" + labels.get((Integer) i) + '\t');
		}
		System.out.print("" + '\n');

		for (int i = 0; i < n; i++) {
			System.out.print("" + labels.get((Integer) i));
			for (int j = 0; j < n; j++) {
				System.out.print("  " + this.floydWarshall[i][j]);
			}
			System.out.print("" + '\n');
		}
		for (int i = 0; i <= 8 * n; i++) {
			System.out.print("-");
		}
		System.out.print('\n');
	}

	public double[][] floydWarshall(double[][] d) {
		double[][] p = constructInitialMatixOfPredecessors(d);
		for (int k = 0; k < d.length; k++) {
			for (int i = 0; i < d.length; i++) {
				for (int j = 0; j < d.length; j++) {
					if (d[i][k] == Integer.MAX_VALUE
							|| d[k][j] == Integer.MAX_VALUE) {
						continue;
					}

					if (d[i][j] > d[i][k] + d[k][j]) {
						d[i][j] = d[i][k] + d[k][j];
						p[i][j] = p[k][j];
					}

				}
			}
		}
		return p;
	}

	private double[][] constructInitialMatixOfPredecessors(double[][] d) {
		double[][] p = new double[d.length][d.length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d.length; j++) {
				if (d[i][j] != 0 && d[i][j] != Integer.MAX_VALUE) {
					p[i][j] = i;
				} else {
					p[i][j] = 0;
				}
			}
		}
		return p;
	}

	/**
	 * Clear the digraph
	 *
	 */
	public void clear() {
		data.clear();
		NODE_LIST.clear();
	}

	/**
	 * This method specifies how the digraph may be controled by the user by
	 * inputing commands in the TextField
	 *
	 */
	public void actionPerformed(ActionEvent evt) {
		String command = tf.getText();

		StringTokenizer st = new StringTokenizer(command);

		String token, opt;
		Integer node1, node2;
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			token = token.toLowerCase();
			switch (token) {
			case "dijkstra":
				try {
					node1 = Integer.parseInt(st.nextToken());
					node2 = Integer.parseInt(st.nextToken());
					GraphToolBox.dijkstra(this, node1, node2);
				} catch (Exception e) {
					System.out.println("There is no pass between "
							+ "the source node and the sink node");
				}
				break;

			case "mst":
				GraphToolBox.mst(this, super.getGraphics());
				break;

			case "load":
				try {
					clear();
					opt = st.nextToken();
					load(opt);
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;
			case "add":
				try {
					opt = st.nextToken();
					opt = opt.toLowerCase();
					if (opt.equals("edge")) {
						node1 = Integer.parseInt(st.nextToken());
						node2 = Integer.parseInt(st.nextToken());
						double weight = Double.parseDouble(st.nextToken());
						if (st.hasMoreTokens()) {
							break;
						}
						addEdge(node1, node2, weight);
					} else if (opt.equals("node")) {
						node1 = Integer.parseInt(st.nextToken());
						if (st.hasMoreTokens()) {
							break;
						}
						add(node1);
					}
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;
			case "remove":
				try {
					opt = st.nextToken();
					opt = opt.toLowerCase();
					if (opt.equals("edge")) {
						node1 = Integer.parseInt(st.nextToken());
						node2 = Integer.parseInt(st.nextToken());
						if (st.hasMoreTokens()) {
							break;
						}
						removeEdge(node1, node2);
					} else if (opt.equals("node")) {
						node1 = Integer.parseInt(st.nextToken());
						if (st.hasMoreTokens()) {
							break;
						}
						remove(node1);
					}
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;
			case "change":
				try {
					opt = st.nextToken();
					opt = opt.toLowerCase();
					if (opt.equals("weight")) {
						node1 = Integer.parseInt(st.nextToken());
						node2 = Integer.parseInt(st.nextToken());
						double w = Double.parseDouble(st.nextToken());
						if (st.hasMoreTokens()) {
							break;
						}
						changeWeight(node1, node2, w);
					}
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;

			case "print":
				try {
					opt = st.nextToken();
					opt = opt.toLowerCase();
					if (st.hasMoreTokens()) {
						System.out.println("Invalid command");
						break;
					}
					switch (opt) {
					case "order":
						System.out.println("Order of the digraph: "
								+ graphOrder());
						break;
					case "matrix":
						printMatrix();
						break;
					case "floyd-warshall":
						printFloydWarshall();
						break;
					default:
						System.out.println("Invalid command");
						break;
					}
				} catch (Exception e) {
					System.out.println("Invalid command");
				}
				break;
			case "clear":
				if (!st.hasMoreTokens()) {
					clear();
				} else {
					System.out.println("Invalid command");
				}
				break;
			default:
				System.out.println("Invalid command");
				break;
			}
		}
		repaint();
	}

	/**
	 * Paint the digraph to the panel
	 *
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// clear the previous screen
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 1500, 1500);
		g2d.setColor(Color.BLACK);

		HashMap<Integer, Double> outNeighbours;

		for (Integer i : nodeSet) {
			if (selectedNode == i) {
				NODE_LIST.get(i).draw(g, Color.RED);
			} else {
				NODE_LIST.get(i).draw(g, Color.BLACK);
			}
			outNeighbours = data.get(i);
			for (Integer j : nodeSet) {
				if (outNeighbours.containsKey(j) && !this.isMst) {
					drawEdge(i, j, g, Color.BLACK, Color.BLUE,
							outNeighbours.get(j));
				}
			}

		}

	}

	/**
	 * Draw a directed edge between 2 nodes with the specific color for the line
	 * and the arrow.
	 */
	public void drawEdge(Integer node1, Integer node2, Graphics g,
			Color colorLine, Color colorArrow, Double weight) {

		Graphics2D g2 = (Graphics2D) g;

		int startX = NODE_LIST.get(node1).getEdgeX(NODE_LIST.get(node2));
		int startY = NODE_LIST.get(node1).getEdgeY(NODE_LIST.get(node2));

		int destX = NODE_LIST.get(node2).getEdgeX(NODE_LIST.get(node1));
		int destY = NODE_LIST.get(node2).getEdgeY(NODE_LIST.get(node1));

		g2.setStroke(new BasicStroke(2));
		g2.setColor(colorLine);

		QuadCurve2D q = new QuadCurve2D.Float();
		// draw QuadCurve2D.Float with set coordinates
		int ctrlx = calcTextPosX(startX, destX, startY, destY);
		int ctrly = calcTextPosY(startX, destX, startY, destY);
		q.setCurve(startX, startY, ctrlx, ctrly, destX, destY);
		g2.draw(q);

		g2.drawString("" + weight, ctrlx, ctrly);

		double theta, x, y;
		g2.setPaint(colorArrow);
		theta = Math.atan2(destY - ctrly, destX - ctrlx);
		drawArrow(g2, theta, destX, destY);

	}

	public int calcTextPosX(int x1, int x2, int y1, int y2) {
		if (x1 < x2) {
			if (y1 < y2) {
				return (x1 + x2) / 2 + 20;
			} else {
				return (x1 + x2) / 2 - 24;
			}
		} else {
			if (y1 < y2) {
				return (x1 + x2) / 2 + 20;
			} else {
				return (x1 + x2) / 2 - 24;
			}
		}

	}

	public int calcTextPosY(int x1, int x2, int y1, int y2) {
		if (x1 < x2) {
			return (y1 + y2) / 2 - 10;
		} else {
			return (y1 + y2) / 2 + 10;
		}
	}

	// draws the arrows on the edges
	private void drawArrow(Graphics2D g2, double theta, double x0, double y0) {
		g2.setStroke(new BasicStroke(3));
		double x = x0 - barb * Math.cos(theta + phi);
		double y = y0 - barb * Math.sin(theta + phi);
		g2.draw(new Line2D.Double(x0, y0, x, y));
		x = x0 - barb * Math.cos(theta - phi);
		y = y0 - barb * Math.sin(theta - phi);
		g2.draw(new Line2D.Double(x0, y0, x, y));
	}

	// Mouse Actions:
	// Moving a node: The user may drag and drop a node to any position
	// Add a node: The user may add a node by clicking a white area of the frame
	// The newly added node will be automatically selected
	// Selecting a node: The user may select a node by click on a node
	// Add an edge: Once a node is selected, the user may add an outgoing edge
	// to the selected node by clicking another node
	@Override
	public void mouseDragged(MouseEvent e) {
		if (moveNode >= 0) {
			Node node = NODE_LIST.get(moveNode);
			node.xpos = e.getPoint().x;
			node.ypos = e.getPoint().y;
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Node node;
		boolean onNode = false;
		Integer clicked = -1;
		for (Integer i : nodeSet) {
			node = NODE_LIST.get(i);
			// Calculate the distance to the center of a node
			double distance = Math.sqrt(Math.pow((e.getX() - node.xpos), 2)
					+ Math.pow((e.getY() - node.ypos), 2));
			if (distance <= (1.0 * CIRCLEDIAMETER / 2)) {
				onNode = true;
				clicked = i;
			}
		}
		if (onNode) {
			if (selectedNode == -1) {
				selectedNode = clicked;
			} else {
				if (clicked.equals(selectedNode)) {
					selectedNode = -1;
				} else {
					addEdge(selectedNode, clicked, 1);
					selectedNode = -1;
				}
			}
		}
		if (!onNode) {
			int newNode = 0;
			while (nodeSet.contains((Integer) newNode)) {
				newNode++;
			}
			add((Integer) newNode);
			selectedNode = newNode;
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Node node;
		for (Integer i : nodeSet) {
			node = NODE_LIST.get(i);
			// Calculate the distance to the center of a node
			double distance = Math.sqrt(Math.pow((e.getX() - node.xpos), 2)
					+ Math.pow((e.getY() - node.ypos), 2));
			if (distance <= (1.0 * CIRCLEDIAMETER / 2)) {
				moveNode = i;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		moveNode = -1;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	// An inner class storing information regarding the visualisation of a node
	private class Node {

		public int xpos;
		public int ypos;
		public int nodeNum;
		public int inEdges, arraySpot;
		int dirX, dirY;

		public Node(int num) {
			xpos = 0;
			ypos = 0;
			nodeNum = num;
			inEdges = 0;
			arraySpot = 0;

			double rand = Math.random();

			if (rand < 0.25) {
				dirX = 1;
				dirY = 1;
			} else if (rand < 0.5) {
				dirX = -1;
				dirY = 1;
			} else if (rand < 0.75) {
				dirX = 1;
				dirY = -1;
			} else {
				dirX = -1;
				dirY = -1;
			}

		}

		// returns the label of the node
		public int label() {
			return nodeNum;
		}

		// compute the x coordinate of the source of the edge from this to the
		// specified node
		public int getEdgeX(Node node) {
			double direction = 1.0;
			if (node.xpos < xpos) {
				direction = -1.0;
			}
			double x2subx1sqr = Math.pow((node.xpos - xpos), 2);
			double y2suby1sqr = Math.pow((node.ypos - ypos), 2);
			double rsqr = Math.pow(CIRCLEDIAMETER * 1.0 / 2, 2);
			double x = Math
					.sqrt((x2subx1sqr * rsqr / (x2subx1sqr + y2suby1sqr)))
					* direction + xpos;
			return (int) Math.round(x);
		}

		// compute the y coordinate of the source of the edge from this to the
		// specified node
		public int getEdgeY(Node node) {
			double direction = 1.0;
			if (node.ypos < ypos) {
				direction = -1.0;
			}
			double x2subx1sqr = Math.pow((node.xpos - xpos), 2);
			double y2suby1sqr = Math.pow((node.ypos - ypos), 2);
			double rsqr = Math.pow(CIRCLEDIAMETER * 1.0 / 2, 2);// Square root
																// of radius
			double y = Math
					.sqrt((y2suby1sqr * rsqr / (x2subx1sqr + y2suby1sqr)))
					* direction + ypos;
			return (int) Math.round(y);
		}

		// draw the node
		public void draw(Graphics g, Color color) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(color);
			g2d.drawOval(xpos - (CIRCLEDIAMETER / 2), ypos
					- (CIRCLEDIAMETER / 2), CIRCLEDIAMETER, CIRCLEDIAMETER);
			g2d.setColor(Color.BLUE);
			g2d.drawString("" + nodeNum, xpos - 3, ypos + 4);
			g2d.setColor(Color.BLACK);
		}

		public void move() {
			xpos = xpos - 2 * dirX;
			ypos = ypos - 2 * dirY;

			if (xpos < 50 || xpos > 360) {
				dirX = (-1) * dirX;
			}
			if (ypos < 50 || ypos > 360) {
				dirY = (-1) * dirY;
			}

		}
	}

	// The main method builds a digraph and add three initial nodes to the
	// digraph labeled 0,1,2
	// and adds some edges among these nodes
	//
	public static void main(String[] args) {
		WGraph g = new WGraph();
		// Splash ss = new Splash();
		// ss.dispose();
		JFrame frame = new JFrame("Weighted Graph Implementation");
		frame.setSize(450, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		// frame.setLocation(0, 0);
		frame.getContentPane().add(g);
		frame.setVisible(true);

		// Expected Print Output:
		//
		// Node 0 indegree: 0 outdegree: 2
		// Node 1 indegree: 1 outdegree: 1
		// Node 2 indegree: 2 outdegree: 0
		// Size of the digraph: 3
		// Order of the digraph: 3
		// -------------------------
		// 0 1 2 0 0 1 1 1 0 0 1 2 0 0 0
		// -------------------------
		// ---------
		// 0: 1 2
		// 1: 2
		// 2:
		// ---------
		// Universal Source found: 0
		// Universal Sink found: 2
		// Transpose
		// -------------------------
		// 0 1 2 0 0 0 0 1 1 0 0 2 1 1 0
		// -------------------------
		// Underlying Graph
		// -------------------------
		// 0 1 2 0 0 1 1 1 1 0 1 2 1 1 0
		// -------------------------
	}
}