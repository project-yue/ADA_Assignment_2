package assignment_2.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import assignment_2.model.WGraph;

public class TableDialog extends JDialog {

	ArrayList<Vector> floydWarshallRows = new ArrayList<>();
	ArrayList<Vector> bellmanRows = new ArrayList<>();
	int bellmanIteration = 0;

	public TableDialog(JFrame frame) {
		super(frame, "Output", true);

	}

	public void display() {
		getContentPane().removeAll();
		Vector col = new Vector();
		col.add("Name");
		col.add("Roll No");
		col.add("Grade");
		Vector first = new Vector();
		first.add("Bhupendra");
		first.add("100");
		first.add("A+");
		Vector row = new Vector();
		row.add(first);
		JTable table = new JTable(row, col);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(table);
		this.pack();
		this.show();
	}

	public void floydWarshallTable(JPanel panel) {
		getContentPane().removeAll();
		WGraph wgraph = (WGraph) panel;
		Vector<String> col = new Vector<>();
		// columns
		col.add("Node");
		for (Integer node : wgraph.nodeSet) {
			col.add(Integer.toString(node));
		}
		Vector<Vector> row = new Vector<>();
		for (Vector vec : this.floydWarshallRows) {
			row.add(vec);
		}
		JTable table = new JTable(row, col);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(table);
		clearFloydWarshall();
		this.pack();
		this.show();
	}

	public void addFloydWarshallRow(String text) {
		Vector<String> row = new Vector<>();
		for (String temp : text.split("\t")) {
			row.add(temp);
		}
		this.floydWarshallRows.add(row);
	}

	public void clearFloydWarshall() {
		this.floydWarshallRows.clear();
	}

	public void bellmanfordTable(JPanel panel) {
		getContentPane().removeAll();
		WGraph wgraph = (WGraph) panel;
		Vector<String> col = new Vector<>();
		// columns
		col.add("Iteration");
		for (Integer node : wgraph.nodeSet) {
			col.add(Integer.toString(node));
		}
		Vector<Vector> row = new Vector<>();
		for (Vector vec : this.bellmanRows) {
			row.add(vec);
		}
		JTable table = new JTable(row, col);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(table);
		clearBellmanford();
		this.pack();
		this.show();
	}

	private void clearBellmanford() {
		this.bellmanIteration = 0;
		this.bellmanRows.clear();
	}

	public void addBellmanFordRows(String text) {
		Vector<String> row = new Vector<>();
		row.add(Integer.toString(++this.bellmanIteration));
		for (String temp : text.split("\\s")) {
			row.add(temp);
		}
		this.bellmanRows.add(row);
	}
}