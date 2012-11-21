package gui;

import scene.material.Color3f;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 21/11/12
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public interface MainWindowListener {
	public Color3f renderPixel(int x, int y);
	public void loadSDL(String file);
	public void reloadFile();
	public void render(int startDepth);
}
