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

	public TableDialog(JFrame frame) {
		super(frame, "Hello", true);

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
		this.pack();
		this.show();
		System.out.println("cakke");
	}

	public void addFloydWarshallRow(String text) {
		Vector<String> row = new Vector<>();
		for (String temp : text.split("\t")) {
			row.add(temp);
		}
		this.floydWarshallRows.add(row);
	}
}