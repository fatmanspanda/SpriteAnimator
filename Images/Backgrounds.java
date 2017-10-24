package SpriteAnimator.Images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Backgrounds {

	/**
	 * Names of current backgrounds available
	 */
	public static final String[] BACKGROUNDNAMES = {
		"Empty",
		"House",
		"Grass",
		"Still water",
		"Cave",
		"Eastern Palace",
		"Desert Palace",
		"Ice Palace",
		"Misery Mire",
		"Turtle Rock",
		"Moldorm",
		"Mothula",
		"Houlihan",
		"Tile room",
		"Spike cave"
	};

	/**
	 * Looks for the resources <tt><<b></b>S>_BG.png</tt>
	 * and puts them into a list of images for use.
	 */
	public static final BufferedImage[] getBackgrounds() {
		BufferedImage[] ret = new BufferedImage[BACKGROUNDNAMES.length];
		for (int i = 0; i < ret.length; i++) {
			try {
				ret[i] = ImageIO.read(Backgrounds.class.getResourceAsStream(
						"/SpriteAnimator/Images/" + BACKGROUNDNAMES[i] + " BG.png"));
			} catch (IOException e) {
			};
		}
		return ret;
	}
}