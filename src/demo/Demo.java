package demo;

import gui.PolarisMainWindow;
import gui.Renderer;
import gui.panel.FilePanel;
import raytracer.Settings;
import scene.SceneConstructor;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 06/12/12
 * Time: 14:45
 */
public class Demo {
	private SceneGenerator sg;

	public Demo(SceneGenerator sg) {
		this.sg = sg;
	}

	public void runStandalone() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();

		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), SceneConstructor.getBVHScene(), Settings.NUMBER_OF_RENDER_PASSES);
		mainWindow.setListener(renderer);

		renderer.applySceneGenerator(sg);

		mainWindow.display();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		renderer.render();
	}

	public void runHeadless() {
		FilePanel panel = new FilePanel();

		Renderer renderer = new Renderer(panel, SceneConstructor.getGridScene(), Settings.NUMBER_OF_RENDER_PASSES);

		renderer.applySceneGenerator(sg);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		renderer.render();

		renderer.join();

		File toFile = new File("");
		panel.saveImage(toFile.getAbsolutePath() + "/products/" + (System.currentTimeMillis() % 1000) + ".png");
	}
}
