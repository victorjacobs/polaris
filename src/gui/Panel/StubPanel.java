package gui.Panel;

import raytracer.Settings;
import scene.material.Color3f;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 06/12/12
 * Time: 14:07
 */
public class StubPanel extends ScreenPanel {
	public StubPanel() {

	}

	@Override
	public void drawPixel(int x, int y, Color3f c) {

	}

	@Override
	public void drawPixel(int x, int y, float r, float g, float b) {

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
