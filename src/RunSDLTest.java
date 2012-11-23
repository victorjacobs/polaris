import gui.CgPanel;
import gui.PolarisMainWindow;
import gui.Renderer;
import raytracer.Settings;
import scene.Scene;
import scene.lighting.AmbientLight;
import scene.material.Color3f;
import scene.parser.SceneBuilder;

import javax.swing.*;
import java.io.FileNotFoundException;

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
