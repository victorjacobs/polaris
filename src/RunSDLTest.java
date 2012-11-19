import gui.CgPanel;
import gui.Renderer;
import raytracer.Settings;
import scene.Scene;
import scene.geometry.Vector3f;
import scene.parser.AffineTransformation;
import scene.parser.SceneBuilder;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA. User: victor Date: 18/11/12 Time: 23:19 To change this template use File | Settings |
 * File Templates.
 */
public class RunSDLTest {
	public static void main(String[] args) {
		try {
			SceneBuilder builder = new SceneBuilder();
			Scene scene = builder.loadScene("data/scenes/example.sdl");

			CgPanel panel = new CgPanel();
			JFrame frame = new JFrame();
			frame.setSize(Settings.SCREEN_X, Settings.SCREEN_Y);
			frame.getContentPane().add(panel);
			frame.setVisible(true);

			Renderer renderer = new Renderer(scene, panel);

			try {
				Thread.sleep(100);
			} catch (Throwable e) {

			}

			renderer.render(8);
		} catch (FileNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
