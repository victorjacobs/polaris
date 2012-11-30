package scene.material;

import scene.data.Point2f;

import javax.imageio.ImageIO;
import java.awt.Color;
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
	private String path;
	private BufferedImage img = null;

	public Texture(String path) {
		this.path = path;

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
	public Color3f getColor(Point2f point) {
		int x = (int)Math.floor(point.x * img.getWidth());
		int y = (int)Math.floor(point.y * img.getHeight());

		return new Color3f(new Color(img.getRGB(x, y)));
	}
}
