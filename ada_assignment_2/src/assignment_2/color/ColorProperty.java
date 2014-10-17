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
		for (int r = 0; r < 255; r = r + 16)
			for (int g = 0; g < 255; g = g + 16)
				for (int b = 0; b < 255; b = b + 16)
					colors.add(new Color(r, g, b));
	}
}
