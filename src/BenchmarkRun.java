import gui.Panel.StubPanel;
import gui.Renderer;
import scene.BasicScene;
import scene.GridAcceleratedScene;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 06/12/12
 * Time: 13:53
 */
public class BenchmarkRun {

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		Renderer renderer = new Renderer(new StubPanel(), new GridAcceleratedScene(new BasicScene()), 1);

		renderer.loadSDL("data/scenes/787.sdl");

		try {
			Thread.sleep(100);
		} catch (Throwable e) {

		}

		renderer.render();
	}

}
