import gui.PolarisMainWindow;
import gui.Renderer;
import raytracer.Grid;
import raytracer.Settings;
import scene.Scene;

/**
 * Created with IntelliJ IDEA. User: victor Date: 18/11/12 Time: 23:19 To change this template use File | Settings |
 * File Templates.
 */
public class Run {
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();
		Scene scene = new Scene();
		scene.setTraversalStrategy(new Grid());	// TODO make constructor which sets traversal
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), scene, Settings.NUMBER_OF_RENDER_PASSES);
		// TODO loadSDL should go to scene
		renderer.loadSDL("data/scenes/teapot.sdl");

		mainWindow.setListener(renderer);

		mainWindow.display();

		try {
			Thread.sleep(100);
		} catch (Throwable e) {

		}

		renderer.render();
	}
}
