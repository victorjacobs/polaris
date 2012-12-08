package gui.Panel;

import raytracer.Settings;

import java.awt.*;
import java.awt.image.MemoryImageSource;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 07/12/12
 * Time: 01:55
 */
public class FilePanel extends ScreenPanel {
	private MemoryImageSource mis;
	private int[] pixels;

	public FilePanel() {
		mis = new MemoryImageSource(getWidth(), getHeight(), pixels, 0, getWidth());
		mis.setAnimated(false);
		pixels = new int[getHeight() * getWidth()];
	}

	@Override
	public void drawPixel(int x, int y, float r, float g, float b) {
		if (x>=0 && x<getWidth() && y>=0 && y<getHeight()) {
			pixels[x+getWidth()*y] = 255<<24 | (int)(255*r)<<16 | (int)(255*g)<<8 | (int)(255*b);
		}
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
