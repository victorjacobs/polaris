package demo;

import gui.Renderer;
import gui.panel.FilePanel;
import scene.Scene;
import scene.SceneFactory;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 22/12/12
 * Time: 13:47
 */
public class Movie {
	public static final int NB_OF_FRAMES = 360;

	public static void main(String[] args) {
		new Movie();
	}

	public Movie() {
		File out = new File("");

		FilePanel panel = new FilePanel();
		Renderer renderer = new Renderer(panel, 1);
		SceneGenerator sg = new ObjParser();
		Scene scene = SceneFactory.getGridScene();
		renderer.loadScene(scene);
		sg.generateScene(scene);

		for (int i = 0; i < NB_OF_FRAMES; i++) {
			panel.clear();

			renderer.render();

			renderer.join();

			panel.saveImage(out.getAbsolutePath() + "/products/" + i + ".png");

			// Move camera
			scene.getCamera().applyMatrixTransformation(AffineTransformation.rotation(new Vector3f(0, 1, 0), 1));
		}
	}
}
