package SpriteAnimator.Sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Sprite class to handle drawing better
 */
public class Sprite {
	private int x;
	private int y;
	private BufferedImage img;

	public Sprite(BufferedImage image, int xpos, int ypos) {
		img = image;
		x = xpos;
		y = ypos;
	}

	/**
	 * Attaches itself to a {@link Graphics2D} object and draws itself accordingly.
	 * @param g - Graphics2D object
	 */
	public void draw(Graphics2D g, int xoffset, int yoffset) {
		g.drawImage(img, x + xoffset, y + yoffset, null);
	}
}