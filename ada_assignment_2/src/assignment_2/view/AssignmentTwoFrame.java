package assignment_2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import assignment_2.Question1;
import assignment_2.WGraph;

/**
 * Singleton pattern on the two questions.
 * 
 * @author Yue
 * @version 1.1
 *
 */
public class AssignmentTwoFrame extends JFrame {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmHelpMenuItem;
	private JTabbedPane frameTabbedPane;
	private JPanel digraphPanel;
	private JPanel wgraph_panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Splash ss = new Splash();
		// ss.dispose();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AssignmentTwoFrame frame = new AssignmentTwoFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AssignmentTwoFrame() {
		super("Assignment 2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		setMinimumSize(new Dimension(300, 250));
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		mnNewMenu = new JMenu("Help");
		mnNewMenu.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnNewMenu);
		JMenuItem mntnHelpMenuInstruction = new JMenuItem("Instructions");
		mntnHelpMenuInstruction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayInstructions();
			}
		});
		mntmHelpMenuItem = new JMenuItem("Version");
		mntmHelpMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayInfo();
			}
		});
		mnNewMenu.add(mntnHelpMenuInstruction);
		mnNewMenu.add(mntmHelpMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		initDigraphTab();
		initWgraphTab();
	}

	private void initDigraphTab() {
		frameTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(frameTabbedPane, BorderLayout.CENTER);
		digraphPanel = new JPanel();
		frameTabbedPane.addTab("Digraph", null, digraphPanel, null);
		digraphPanel.setLayout(new BoxLayout(digraphPanel, BoxLayout.X_AXIS));
		Question1 q1 = new Question1();
		digraphPanel.add(q1);
	}

	private void initWgraphTab() {
		wgraph_panel = new JPanel();
		frameTabbedPane.addTab("Wgraph", null, wgraph_panel, null);
		wgraph_panel.setLayout(new BoxLayout(wgraph_panel, BoxLayout.X_AXIS));
		WGraph wgraph = new WGraph();
		wgraph_panel.add(wgraph);
	}

	private void displayInfo() {
		JOptionPane.showMessageDialog(this,
				"ADA Assignment 2:  Graph Theory \n"
						+ "                              by Ximei & Yue");
	}

	private void displayInstructions() {

		JOptionPane.showMessageDialog(this, "digraph\ndfs 0\n"
				+ "bfs 0\niscycle 0\nlinearize\nscc 0\n"
				+ "distance 0 4\nisEulerian\nisBipartite\n" + "wgrap\n");
	}
}
