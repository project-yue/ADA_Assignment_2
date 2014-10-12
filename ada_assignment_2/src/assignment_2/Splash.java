package assignment_2;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class Splash extends JWindow {
	JLabel lbl;
	Image image;
	ImageIcon icon;
	JPanel contentPane;

	public Splash() {
		image = new ImageIcon((getClass().getResource("yolo.png"))).getImage();
		icon = new ImageIcon(image);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			lbl = new JLabel();
			lbl.setIcon(icon);
			contentPane = ((JPanel) this.getContentPane());
			contentPane.add(lbl);
			setSplashLocation();

			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}

	private void setSplashLocation() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frame = this.getSize();

		int x = (int) (screen.getWidth() - frame.getWidth()) / 2;
		int y = (int) (screen.getHeight() - frame.getHeight()) / 2 - 50;

		this.setLocation(x - 50, y - 30);
		// this.setLocationRelativeTo(null);
	}

	public static void main(String[] arg) {
		Splash splash = new Splash();
		// get rid of window as it finished showing
		splash.dispose();
	}
}
