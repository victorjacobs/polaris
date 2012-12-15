package gui;

import demo.SceneGenerator;
import scene.material.Color3f;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 21/11/12
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public interface MainWindowListener {
	Color3f renderPixel(int x, int y);
	void loadSDL(String file);
	void reloadFile();
	void render();
	void abortRender(boolean willFlush);

	void applySceneGenerator(SceneGenerator sg);

	void rotateCamera(int direction);
}
