package demo;

import scene.Scene;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 11/12/12
 * Time: 23:15
 */
public interface SceneGenerator {

	public void generateScene(Scene scene);
	public void generateScene(Scene scene, int size);

}
