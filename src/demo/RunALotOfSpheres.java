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
import scene.lighting.PointLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;
import scene.material.ReflectiveMaterial;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 01/12/12
 * Time: 18:07
 */
public class RunALotOfSpheres {

	public static void main(String[] args) {

		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();

		Scene scene = new GridAcceleratedScene(new BasicScene());
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), 1);
		mainWindow.setListener(renderer);

		renderer.loadScene(scene);

		scene.setCamera(new Camera(new Point3f(-5, 10f, 10f), new Vector3f(10, -10f, -10f), new Vector3f(0, 1, 0), 60));
		scene.setBackground(new Color3f(0.3f, 0.3f, 0.3f));
		scene.addLightSource(new PointLight(new Vector3f(-10, 3, 3), 0.9f));
		scene.addLightSource(new PointLight(new Vector3f(2.5f, 10, 3f), 0.3f));
		scene.addLightSource(new PointLight(new Vector3f(3, 1, -10), 0.3f));
		scene.addLightSource(new PointLight(new Vector3f(3, 1, 10), 0.3f));

		Surface plane = new Model("data/objects/plane.obj");
		plane.applyTransformation(AffineTransformation.scale(20));
		plane.setMaterial(new ReflectiveMaterial(0.3f));
		//scene.addSurface(plane);

		DiffuseMaterial randomColor;

		Random rand = new Random();
		Surface sphere;
		float x, y, z;

		for (int i = 0; i < 1000000; i++) {
			randomColor = new DiffuseMaterial(new Color3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));

			// Calculate next position (between 0 and 5
			x = 5 * rand.nextFloat();
			y = 5 * rand.nextFloat();
			z = 5 * rand.nextFloat();

			sphere = new Sphere(new Vector3f(x, y, z), 0.01f, randomColor);
			scene.addSurface(sphere);
		}

		mainWindow.display();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		renderer.render();
	}

}
