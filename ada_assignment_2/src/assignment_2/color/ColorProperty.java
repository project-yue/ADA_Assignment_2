package assignment_2.color;

import java.awt.Color;
import java.util.ArrayList;

public class ColorProperty {

	public ArrayList<Color> colors;

	public ColorProperty() {
		initializeColors();
	}

	private void initializeColors() {
		this.colors = new ArrayList<>();
		for (int r = 0; r < 255; r = r + 50)
			for (int g = 0; g < 255; g = g + 50)
				for (int b = 0; b < 255; b = b + 50)
					colors.add(new Color(r, g, b));
	}

	private Color[] colorArr = new Color[] { Color.BLACK, Color.RED,
			Color.BLUE, Color.CYAN, Color.YELLOW, Color.MAGENTA };

	public static void main(String[] args) {
		ColorProperty cp = new ColorProperty();
		for (Color color : cp.colors) {
			System.out.println(color.getRed() + " " + color.getGreen() + " "
					+ color.getBlue());
		}
	}
}
