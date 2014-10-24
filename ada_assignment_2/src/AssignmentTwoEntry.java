import java.awt.EventQueue;

import assignment_2.view.AssignmentTwoFrame;
import assignment_2.view.Splash;

/**
 * Assignment 2 entry
 * 
 * @author Ximei Liu & Yue Li
 *
 */
public class AssignmentTwoEntry {

	public static void main(String[] args) {
		Splash ss = new Splash();
		ss.dispose();
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
}
