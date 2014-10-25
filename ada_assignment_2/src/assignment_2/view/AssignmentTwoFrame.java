package assignment_2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
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

import assignment_2.model.Question1;
import assignment_2.model.WGraph;

/**
 * Singleton pattern on the two questions.
 * 
 * @author Yue
 * @version 1.1
 *
 */
public class AssignmentTwoFrame extends JFrame {

	private JPanel contentPane;
	private JTabbedPane frameTabbedPane;
	private JPanel digraphPanel;
	private JPanel wgraph_panel;

	public MyDialog md = new MyDialog(this);

	// drawing process in miliseconds
	public static int TIME_ADJUSTMENT = 1000;

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
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnNewMenu = new JMenu("Help");
		mnNewMenu.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnNewMenu);
		JMenuItem mntnHelpMenuInstruction = new JMenuItem("Instructions");
		mntnHelpMenuInstruction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayInstructions();
			}
		});
		JMenuItem mntmHelpMenuItem = new JMenuItem("Version");
		mntmHelpMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayInfo();
			}
		});
		JMenu mnSettingMenu = new JMenu("Setting");
		menuBar.add(mnSettingMenu);
		JMenuItem config = new JMenuItem("Drawing time");
		config.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = JOptionPane
						.showInputDialog("Input drawing frequency in millisecondse e.g. 1000");
				try {
					AssignmentTwoFrame.TIME_ADJUSTMENT = Integer
							.parseInt(result);
				} catch (NumberFormatException exception) {
					System.out.println(exception.getMessage());
				}
			}
		});
		mnSettingMenu.add(config);
		mnNewMenu.add(mntnHelpMenuInstruction);
		mnNewMenu.add(mntmHelpMenuItem);

		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		initDigraphTab();
		initWgraphTab();

		// insert dialog
		JMenu testingMenu = new JMenu("Test Dialog");
		menuBar.add(testingMenu);
		JMenuItem test = new JMenuItem("Test");
		testingMenu.add(test);
		test.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				md.display();
			}
		});
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

		JOptionPane.showMessageDialog(this, "Digraph:\ndfs 0\n"
				+ "bfs 0\niscycle 0\nlinearize\nscc 0\n"
				+ "distance 0 4\nisEulerian\nisBipartite\n"
				+ "Wgraph:\ndijkstra 0 1\nmst\nprint floydwarshall"
				+ "\nprint bellmanford");
	}
}
