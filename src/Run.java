import gui.*;
import gui.Renderer;
import raytracer.Settings;
import scene.Camera;
import scene.Scene;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.geometry.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.PointLight;
import scene.material.*;

import javax.swing.*;

/*
 * TODO optimalisatie schaduwstralen: check inwendig product normaal ding en straal
 * TODO: verschil tussen shading normaal en echte normaal op een vlak!!!
 * TODO parse squares
 * TODO Fix reflections
 * 		+ recursive with reflection coefficient
 * TODO refractive surfaces
 * TODO texture mapping
 * TODO XML
 * TODO Soft shadows
 * 		+ Area lights
 * TODO Anti aliasing
 * TODO DOF
 * TODO color changing in reflections
 */

public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(Settings.SCREEN_X, Settings.SCREEN_Y);
		frame.getContentPane().add(panel);
		frame.setVisible(true);

		Camera camera = new Camera(new Vector3f(5, 5, 5), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 45);
		//PointLight light1 = new PointLight(new Vector3f(-20, 10, 0));
		PointLight light1 = new PointLight(new Vector3f(10, 10, 0));
		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);

		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		scene.addLightSource(aLight);

		Renderer renderer = new Renderer(scene, panel);

		// Load object from file
		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);
		Material green = new DiffuseMaterial(new Color3f(0, 1, 0));
		Surface teapot = new Model("data/objects/teapot.obj", glass);
		Surface sphere = new Sphere(new Vector3f(0, 0, 0), 1, green);

		scene.addSurface(teapot);
		//scene.addSurface(sphere);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		renderer.render(1);

	}
}
