package demo;

import gui.PolarisMainWindow;
import gui.Renderer;
import scene.BasicScene;
import scene.Camera;
import scene.GridAcceleratedScene;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.lighting.AreaLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 02/12/12
 * Time: 00:49
 */
public class RunSoftShadows {
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();

		Scene scene = new GridAcceleratedScene(new BasicScene());
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), 1);
		mainWindow.setListener(renderer);

		renderer.loadScene(scene);

		scene.setBackground(new Color3f(0.3f, 0.3f, 0.3f));
		scene.setCamera(new Camera(new Point3f(0, 1, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), 60));
		//scene.addLightSource(new PointLight(new Vector3f(6, 4, -3)));
		scene.addLightSource(new AreaLight(new Point3f(0, 5, 0)));

		Surface sphere = new Sphere(new Vector3f(4, 1, -1), 0.5f, new DiffuseMaterial(new Color3f(0, 1, 0)));
		scene.addSurface(sphere);

		Surface plane = new Model("data/objects/plane.obj");
		plane.applyTransformation(AffineTransformation.scale(20));
		plane.setMaterial(new DiffuseMaterial(new Color3f(1, 1, 1)));
		scene.addSurface(plane);


		mainWindow.display();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		renderer.render();
	}
}
