import gui.PolarisMainWindow;
import gui.Renderer;
import raytracer.Settings;
import scene.BasicScene;
import scene.GridAcceleratedScene;

/**
 * Created with IntelliJ IDEA. User: victor Date: 18/11/12 Time: 23:19 To change this template use File | Settings |
 * File Templates.
 */
public class Run {
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), new GridAcceleratedScene(new BasicScene()), Settings.NUMBER_OF_RENDER_PASSES);
		renderer.loadSDL("data/scenes/cornell.sdl");

		mainWindow.setListener(renderer);

		mainWindow.display();

		try {
			Thread.sleep(100);
		} catch (Throwable e) {

		}

		renderer.render();
	}
}
