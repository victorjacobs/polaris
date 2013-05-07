package scene.material;

import scene.data.Point2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 29/11/12
 * Time: 22:16
 */
public class Texture {
	private BufferedImage img = null;

	public Texture(String path) {
		System.out.println("Loading texture from file " + path);

		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println("ERROR: failed loading texture");
		}
	}

	/**
	 * Get color from the image at specified point in u-v
	 * @param point
	 * @return
	 */
	// TODO: Something weird here, x and y are switched around to make it work
	public Color3f getColor(Point2f point) {
		// Bilinear interpolation
		// x direction
		float diff = point.x - (float)Math.floor(point.x);

		Color3f c1 = new Color3f(new Color(img.getRGB((int)Math.floor(point.x), (int)Math.floor(point.y)))); // (x, y)
		Color3f c2 = new Color3f(new Color(img.getRGB((int)Math.floor(point.x), (int)Math.ceil(point.y))));	// (x, y + 1)
		Color3f c3 = new Color3f(new Color(img.getRGB((int)Math.ceil(point.x), (int)Math.floor(point.y))));	// (x + 1, y)
		Color3f c4 = new Color3f(new Color(img.getRGB((int)Math.ceil(point.x), (int)Math.ceil(point.y))));	// (x + 1, y + 1)

		


		int x = (int)Math.floor(point.x * img.getWidth()) - 1;
		int y = (int)Math.floor(point.y * img.getHeight()) - 1;

		Color3f ret;

		try {
			ret = new Color3f(new Color(img.getRGB(x, y)));
		} catch (IndexOutOfBoundsException e) {
			// TODO this shouldn't be happening. But it does for AA
			ret = new Color3f(0, 0, 0);
		}

		return ret;
	}
}
