import gui.PolarisMainWindow;
import gui.Renderer;

/**
 * Created with IntelliJ IDEA. User: victor Date: 18/11/12 Time: 23:19 To change this template use File | Settings |
 * File Templates.
 */
public class RunSDLTest {
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), 16);
		renderer.loadSDL("data/scenes/default.sdl");

		mainWindow.setListener(renderer);

		mainWindow.display();

		try {
			Thread.sleep(100);
		} catch (Throwable e) {

		}

		renderer.render();
	}
}
