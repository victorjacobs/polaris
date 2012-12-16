package gui.panel;

import raytracer.Settings;
import scene.material.Color3f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 07/12/12
 * Time: 01:55
 */
public class FilePanel extends ScreenPanel {
	private int[] pixels;

	public FilePanel() {
		pixels = new int[getHeight() * getWidth()];
	}

	@Override
	public void drawPixel(int x, int y, float r, float g, float b) {
		if (x>=0 && x<getWidth() && y>=0 && y<getHeight()) {
			pixels[x+getWidth()*y] = 255<<24 | (int)(255*r)<<16 | (int)(255*g)<<8 | (int)(255*b);
		}
	}

	@Override
	public void drawPixel(int x, int y, Color3f c) {
		drawPixel(x, y, c.x, c.y, c.z);
	}

	@Override
	public void clear() {

	}

	@Override
	public void clear(float r, float g, float b) {

	}

	@Override
	public void flush() {

	}

	@Override
	public void saveImage(String file) {
		try {
			MemoryImageSource mis = new MemoryImageSource(getWidth(), getHeight(), pixels, 0, getWidth());
			mis.setAnimated(false);
			mis.newPixels();
			Graphics2D g2;
			BufferedImage buf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			g2 = buf.createGraphics();
			Image image = createImage(mis);
			g2.drawImage(image,null,null);
			ImageIO.write(buf, "png", new File(file));
			System.out.println("Saving of image to " + file + " succeeded.");
		}
		catch (Exception e) {
			System.out.println("Saving of image to " + file + " failed.");
		}
	}

	@Override
	public void paint(Graphics g) {

	}

	@Override
	public void update(Graphics g) {

	}

	@Override
	public int getWidth() {
		return Settings.SCREEN_X;
	}

	@Override
	public int getHeight() {
		return Settings.SCREEN_Y;
	}
}
