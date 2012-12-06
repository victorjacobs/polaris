package demo;

import gui.PolarisMainWindow;
import gui.Renderer;
import raytracer.Settings;
import scene.BasicScene;
import scene.GridAcceleratedScene;
import scene.Scene;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 06/12/12
 * Time: 14:45
 */
public abstract class Demo {
	public void runStandalone() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();

		Scene scene = new GridAcceleratedScene(new BasicScene());
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), Settings.NUMBER_OF_RENDER_PASSES);
		mainWindow.setListener(renderer);
		renderer.loadScene(scene);

		generateScene(scene);

		mainWindow.display();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		renderer.render();
	}

	public abstract void generateScene(Scene scene);
}
